package com.lzhpo.ypnettywebsocket.service.notice;

import com.lzhpo.ypnettywebsocket.entity.KafkaMsg;
import com.lzhpo.ypnettywebsocket.entity.KafkaPackage;
import com.lzhpo.ypnettywebsocket.entity.NoticePackage;
import com.lzhpo.ypnettywebsocket.service.kafka.KafkaProducer;
import com.lzhpo.ypnettywebsocket.util.JsonUtils;
import com.lzhpo.ypnettywebsocket.vo.ResultData;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author lzhpo
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${kafka.consumer.topic.notice}")
    private String kafkaTopicNotice;

    @Autowired
    private RedisTemplate<String ,String> redisTemplate;

    @Override
    public ResultData<List<String>> publish(NoticePackage noticePackage) {
        // 记录推送失败的channelId
        ArrayList<String> failChennelIds = new ArrayList<>();
        // 传过来的需要推送的channelId
        List<String> receiverChannelIds = noticePackage.getReceiverChannelIds();
        receiverChannelIds.forEach(clientId -> {
            String clientIdFromRedis = redisTemplate.opsForValue().get(clientId);
            System.out.println("clientIdFromRedis：" +clientIdFromRedis);
            if (StringUtils.isEmpty(clientIdFromRedis)) {
                failChennelIds.add(clientId);
            }
        });
        // 推送
        kafkaProducer.producerMsg(KafkaPackage.builder()
                .topic(kafkaTopicNotice)
                .kafkaMsg(KafkaMsg.builder()
                        .id(UUID.randomUUID().toString())
                        .body(JsonUtils.toJson(noticePackage))
                        .sendTime(new Date())
                        .receiverChannelIds(noticePackage.getReceiverChannelIds())
                        .builded())
                .builded());
        // 全部推送成功
        if (failChennelIds.isEmpty()) {
            return new ResultData<>(
                    HttpResponseStatus.OK.code(),
                    "消息发布成功！已全部推送至客户端！");
        } else {
            // 有推送失败的
            return new ResultData<>(
                    HttpResponseStatus.BAD_REQUEST.code(),
                    "消息发布成功！\n推送至客户端失败数[" +failChennelIds.size() +"]个！",
                    failChennelIds);
        }
    }
}
