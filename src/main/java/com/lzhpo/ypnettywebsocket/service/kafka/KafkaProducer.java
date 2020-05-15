package com.lzhpo.ypnettywebsocket.service.kafka;


import com.lzhpo.ypnettywebsocket.entity.KafkaPackage;
import com.lzhpo.ypnettywebsocket.vo.ResultData;

/**
 * @author lzhpo
 */
public interface KafkaProducer {

    ResultData producerMsg(KafkaPackage kafkaPackage);

}
