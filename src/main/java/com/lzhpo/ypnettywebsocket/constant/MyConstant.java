package com.lzhpo.ypnettywebsocket.constant;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 公共常量
 *
 * @author lzhpo
 */
public class MyConstant {
    /** 仅仅通知SMS */
    public static final ChannelGroup SMS_CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /** 仅仅通知AI垃圾桶 */
    public static final ChannelGroup AI_CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /** 全局通知所有 */
    public static final ChannelGroup GLOBAL_CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /** 客户端发送心跳包定死的内容 */
    public static final String HEART_BEAT = "heart_beat";
    /** 服务端间隔多少秒检查是否有心跳 */
    public static final int HEART_BEAT_INTERVAL = 6;
    /** 服务端轮询多少次没有心跳包就将其删除 */
    public static final int HEART_BEAT_DISCONNECT_POLL_NUM = 10;

    /** 刚开始与websocket建立连接，开始鉴权的时候的分割符号，也就是发送心跳包的分割符号 */
    public static final String SPLIT_CLIENT_AND_HEART_BEAT = "@#@";

    /** 记录连接失败的客户端信息到Redis的前缀 */
    public static final String FAIL_CLIENT = "failClient-";
}
