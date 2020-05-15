package com.lzhpo.ypnettywebsocket.service.kafka.impl;

import com.lzhpo.ypnettywebsocket.entity.KafkaMsg;
import com.lzhpo.ypnettywebsocket.entity.NoticePackage;
import com.lzhpo.ypnettywebsocket.service.kafka.KafkaConsumer;
import com.lzhpo.ypnettywebsocket.service.socket.NoticePublishCenter;
import com.lzhpo.ypnettywebsocket.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Kafka消费者
 *
 * @author lzhpo
 */
@Service
public class KafkaConsumerImpl implements KafkaConsumer {

    @Autowired
    private NoticePublishCenter noticePublishCenter;

    /**
     * 监听topic
     *
     * <p>
     * 如果有值，就执行通知，使用观察者模式。
     *
     * @param record
     */
    @KafkaListener(topics = {"kafka-topic-notice"})
    @Override
    public void receive(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional<String> kafkaMsg = Optional.ofNullable(record.value());
        kafkaMsg.ifPresent(s -> {
            KafkaMsg msg = (KafkaMsg) JsonUtils.toObj(s, KafkaMsg.class);
            NoticePackage noticePackage = (NoticePackage) JsonUtils.toObj(msg.getBody(), NoticePackage.class);
            // 创建通知，使用了观察者模式
            noticePublishCenter.createNoticeGroup(noticePackage);
            // 手动提交offset
            ack.acknowledge();
        });
    }
}
