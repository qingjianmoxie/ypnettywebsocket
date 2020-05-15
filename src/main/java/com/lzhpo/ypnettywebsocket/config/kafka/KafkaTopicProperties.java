package com.lzhpo.ypnettywebsocket.config.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author lzhpo
 */
@Data
@ConfigurationProperties("kafka.topic")
public class KafkaTopicProperties implements Serializable {

    private String groupId;
    private String[] topicName;

}
