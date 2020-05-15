package com.lzhpo.ypnettywebsocket.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

/**
 * @author lzhpo
 */
public interface KafkaConsumer {

    void receive(ConsumerRecord<String, String> record, Acknowledgment ack);

}
