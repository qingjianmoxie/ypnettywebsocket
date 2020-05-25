package com.lzhpo.common.config.kafka;

import com.lzhpo.common.constant.MyConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * 程序启动的时候创建topic
 *
 * TODO：从数据库中加载出需要创建的topic
 *
 * @author lzhpo
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;

    /**
     * KafkaAdmin
     *
     * @param properties
     * @return
     */
    @Bean
    public KafkaAdmin admin(KafkaProperties properties){
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }

    /**
     * 云澎SMS的topic
     *
     * @return
     */
    @Bean
    public NewTopic ypSmsTopic() {
        String[] kafkaServersStr = this.kafkaServers.split(",");
        // 队列名称、分区数、副本个数
        return new NewTopic(MyConstant.YP_SMS_TOPIC, kafkaServersStr.length * 2, (short) kafkaServersStr.length);
    }

    /**
     * 云澎AI垃圾桶topic
     *
     * @return
     */
    @Bean
    public NewTopic ypTrashTopic() {
        String[] kafkaServersStr = this.kafkaServers.split(",");
        return new NewTopic(MyConstant.YP_TRASH_TOPIC, kafkaServersStr.length * 2, (short) kafkaServersStr.length);
    }

    /**
     * 云澎全局topic
     *
     * @return
     */
    @Bean
    public NewTopic ypGlobalTopic() {
        String[] kafkaServersStr = this.kafkaServers.split(",");
        return new NewTopic(MyConstant.YP_GLOBAL_TOPIC, kafkaServersStr.length * 2, (short) kafkaServersStr.length);
    }
}
