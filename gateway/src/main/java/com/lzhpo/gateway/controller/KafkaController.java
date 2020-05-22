package com.lzhpo.gateway.controller;

import com.lzhpo.common.constant.MyConstant;
import com.lzhpo.common.entity.NoticePackage;
import com.lzhpo.common.enums.MyHttpCode;
import com.lzhpo.common.vo.ResultData;
import com.lzhpo.gateway.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lzhpo
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    /**
     * 推送消息至Kafka集群
     *
     * @param noticePackage 消息内容
     * @return 返回成功或失败以及信息
     */
    @PostMapping("pushMsgToMq")
    public ResultData<String> pushMsgToMq(@RequestBody @Valid NoticePackage noticePackage) {
        try {
            System.out.println("noticePackage：" +noticePackage);
            if (noticePackage.getChannelGroup().equals(MyConstant.YP_SMS_TOPIC)) {
                System.out.println("YP_SMS_TOPIC");
                kafkaProducerService.publish(MyConstant.YP_SMS_TOPIC, noticePackage);
            }
            if (noticePackage.getChannelGroup().equals(MyConstant.YP_TRASH_TOPIC)) {
                System.out.println("YP_TRASH_TOPIC");
                kafkaProducerService.publish(MyConstant.YP_TRASH_TOPIC, noticePackage);
            }
            if (noticePackage.getChannelGroup().equals(MyConstant.YP_GLOBAL_TOPIC)) {
                System.out.println("YP_GLOBAL_TOPIC");
                kafkaProducerService.publish(MyConstant.YP_GLOBAL_TOPIC, noticePackage);
            }
            return new ResultData<>(MyHttpCode.OK.getCode(), MyHttpCode.OK.getMsg());
        } catch (Exception e) {
            log.error("推送消息到Kafka集群失败！", e);
            return new ResultData<>(MyHttpCode.ERROR.getCode(), "推送消息到Kafka集群失败！" + e.getMessage());
        }
    }

}
