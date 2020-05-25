package com.lzhpo.common.config.kafka;

import com.lzhpo.common.constant.MyConstant;
import com.lzhpo.common.service.kafka.consumer.PushClientService;
import com.lzhpo.common.util.JsonUtils;
import com.lzhpo.common.entity.NoticePackage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * topic监听
 *
 * @author lzhpo
 */
@Component
@Slf4j
public class KafkaTopicListen {

    @Autowired
    private PushClientService pushClientService;

    /**
     * 监听的topic
     *
     * <p>
     * 1. 正则匹配：topicPattern = "yp.*"  表示监听以yp开头的所有topic的消息
     * 2. 手动指定：topics = {"tp.sms.topic"}  表示监听 tp.sms.topic 的topic
     *
     * @param record
     */
    @KafkaListener(topicPattern = "yp.*", containerFactory = "kafkaListenerContainerFactory")
    public void ypSmsTopicListener(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("监听到名称为 [{}] 的topic有消息：{}", MyConstant.YP_SMS_TOPIC,record);
        Optional<String> kafkaMsg = Optional.ofNullable(record.value());
        kafkaMsg.ifPresent(s -> {
            NoticePackage noticePackage = (NoticePackage) JsonUtils.toObj(s, NoticePackage.class);
            System.out.println("noticePackage：" +noticePackage.toString());
            // 推送消息(消费数据)
            List<String> successReceivers = pushClientService.publishMsg(noticePackage);
            List<String> failReceivers = noticePackage.getReceiverChannelIds();
            failReceivers.removeAll(successReceivers);
            if (failReceivers.size() > 0) {
                log.error("客户端消费失败的个数为 {} 个，消费失败的客户端： [{}]", failReceivers.size(), failReceivers);
            } else {
                log.info("客户端全部消费成功！");
            }
            // 手动提交offset
            ack.acknowledge();
        });
    }

}
