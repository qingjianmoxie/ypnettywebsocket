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
@Slf4j
public class KafkaConsumerImpl implements KafkaConsumer {

    @Autowired
    private NoticePublishCenter noticePublishCenter;

    /**
     * 监听topic
     *
     * <p>
     * 如果有值，就执行通知，使用观察者模式。
     * <p>
     * 可以同时订阅多主题，只需按数组格式即可，也就是用“,”隔开。
     *
     * 注意：集群模式和单机模式是不一样的，集群模式需要一个监听工厂（containerFactory = "kafkaListenerContainerFactory"）。
     *
     * @param record
     */
    @KafkaListener(topics = {"kafka-topic-notice"}, containerFactory = "kafkaListenerContainerFactory")
    @Override
    public void receive(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("监听到名称为kafka-topic-notice的topic有消息！{}", record);
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
