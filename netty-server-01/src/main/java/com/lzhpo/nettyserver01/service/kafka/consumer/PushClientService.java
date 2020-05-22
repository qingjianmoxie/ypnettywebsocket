package com.lzhpo.nettyserver01.service.kafka.consumer;

import com.lzhpo.common.websocket.ClientChannelPool;
import com.lzhpo.common.constant.MyConstant;
import com.lzhpo.common.entity.NoticePackage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 往客户端发送消息
 *
 * @author lzhpo
 */
@Service
@Slf4j
public class PushClientService {

    /**
     * 往不同的通道发送数据
     *
     * @param noticePackage
     * @return 返回推送成功的客户端ID集合
     */
    public List<String> publishMsg(NoticePackage noticePackage) {
        System.out.println("noticePackage.getReceiverChannelIds()1111111：" +noticePackage.getReceiverChannelIds());
        List<String> successReceivers = new ArrayList<>();
        if (noticePackage.getReceiverChannelIds() != null) {
            for (String receiver : noticePackage.getReceiverChannelIds()) {
                ClientChannelPool.get(receiver).ifPresent(receiverP -> {
                    System.out.println("noticePackage.getChannelGroup()：" +noticePackage.getChannelGroup());
                    if (noticePackage.getChannelGroup().equals(MyConstant.YP_SMS_TOPIC)
                            && MyConstant.SMS_CHANNEL_GROUP.find(receiverP) != null) {
                        System.out.println("YP_CHANNEL_SMS---------------");
                        send(receiver, MyConstant.SMS_CHANNEL_GROUP, noticePackage);
                    }
                    if (noticePackage.getChannelGroup().equals(MyConstant.YP_TRASH_TOPIC)
                            && MyConstant.SMS_CHANNEL_GROUP.find(receiverP) != null) {
                        System.out.println("YP_CHANNEL_TRASH-------------");
                        send(receiver, MyConstant.TRASH_CHANNELS_GROUP, noticePackage);
                    }
                    if (noticePackage.getChannelGroup().equals(MyConstant.YP_GLOBAL_TOPIC)
                            && MyConstant.SMS_CHANNEL_GROUP.find(receiverP) != null) {
                        System.out.println("YP_CHANNEL_GLOBAL--------------");
                        send(receiver, MyConstant.GLOBAL_CHANNELS_GROUP, noticePackage);
                    }
                    successReceivers.add(receiver);
                });
            }
        }
        System.out.println("noticePackage.getReceiverChannelIds()222222：" +successReceivers);
        return successReceivers;
    }

    /**
     * 推送数据到客户端
     *
     * @param receiver
     * @param channels
     * @param noticePackage
     */
    private void send(String receiver, ChannelGroup channels, NoticePackage noticePackage) {
        TextWebSocketFrame frame = new TextWebSocketFrame(noticePackage.getMessage());
        final Channel[] channel = {null};
        Optional<ChannelId> optionalChannelId = ClientChannelPool.get(receiver);
        optionalChannelId.ifPresent(channelId -> {
            channel[0] = channels.find(channelId);
            channel[0].writeAndFlush(frame);
        });
    }
}
