package com.lzhpo.ypnettywebsocket.service.kafka.impl;

import com.lzhpo.ypnettywebsocket.entity.KafkaMsg;
import com.lzhpo.ypnettywebsocket.entity.KafkaPackage;
import com.lzhpo.ypnettywebsocket.service.kafka.KafkaProducer;
import com.lzhpo.ypnettywebsocket.util.JsonUtils;
import com.lzhpo.ypnettywebsocket.vo.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Kafka生产者
 * <p>
 * 去除了ChannelIdPool容器，直接采用了netty-channel的AttributeKey记住client-id信息。
 * 消息发送不再循环调用NoticeChannelHandler.publishMsg方法，在Stream流中统一记录待接受者，
 * 生成receivers集合，将集合传给NoticeChannelHandler.publishMsg进行统一发送，然后统计发送失败的client-id，
 * 回传至任务线程，进行相应的处理。
 *
 * @author lzhpo
 */
@Slf4j
@Component
@Service(value = "KafkaProducer")
public class KafkaProducerImpl implements KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final int MAX_RETRY_TIMES = 3;

    @Override
    public ResultData producerMsg(KafkaPackage kafkaPackage) {
        publish(kafkaPackage.getTopic(), kafkaPackage.getKafkaMsg());
        return ResultData.builder().builded();
    }

    /**
     * 推送到kafka
     *
     * @param topic
     * @param kafkaMsg
     */
    protected void publish(String topic, KafkaMsg kafkaMsg) {
        AtomicInteger retry = new AtomicInteger(0);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, JsonUtils.toJson(kafkaMsg));
        // ListenableFutureTask的异步回调
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            // 失败回调
            @Override
            public void onFailure(Throwable ex) {
                while (retry.get() != MAX_RETRY_TIMES) {
                    retry.incrementAndGet();
                }
                log.error("Kafka send failure!", ex);
            }
            // 成功回调
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Kafka send success!{}", result);
            }
        });
    }

}
