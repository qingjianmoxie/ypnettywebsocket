package com.lzhpo.ypnettywebsocket.service.socket.impl;

import com.lzhpo.ypnettywebsocket.constant.MyConstant;
import com.lzhpo.ypnettywebsocket.entity.NoticePackage;
import com.lzhpo.ypnettywebsocket.service.socket.ChannelGroupTypeEnum;
import com.lzhpo.ypnettywebsocket.service.socket.ChannelIdPool;
import com.lzhpo.ypnettywebsocket.service.socket.PushToClientService;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lzhpo
 */
@Service
@Slf4j
public class PushToClientServiceImpl implements PushToClientService {

    /**
     * 往不同的通道发送数据
     *
     * @param noticePackage
     */
    @Override
    public void publishMsg(NoticePackage noticePackage, String receiver) {
        if (noticePackage.getGroupType() == ChannelGroupTypeEnum.CHANNEL_GROUP_SMS.getValue()) {
            send(receiver, MyConstant.SMS_CHANNELS, noticePackage);
        } else if (noticePackage.getGroupType() == ChannelGroupTypeEnum.CHANNEL_GROUP_AI_TRASH_SAN.getValue()) {
            send(receiver, MyConstant.AI_CHANNELS, noticePackage);
        } else if (noticePackage.getGroupType() == ChannelGroupTypeEnum.CHANNEL_GROUP_GLOBAL.getValue()) {
            send(receiver, MyConstant.GLOBAL_CHANNELS, noticePackage);
        }
    }

    /**
     * 推送数据到客户端
     *
     * @param channelId
     * @param channels
     * @param noticePackage
     */
    private void send(String channelId, ChannelGroup channels, NoticePackage noticePackage) {
        TextWebSocketFrame frame = new TextWebSocketFrame(noticePackage.getMessage());
        final Channel[] channel = {null};
        ChannelIdPool.get(channelId).ifPresent(channelId2 -> {
            channel[0] = channels.find(channelId2);
            channel[0].writeAndFlush(frame).addListener(future ->
                    log.info("消息主题为 [{}] 通知已向客户端 [{}] 推送完成！", noticePackage.getNoticeLabel(), channel[0].id()));
        });
    }
}
