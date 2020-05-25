package com.lzhpo.common.config.netty;

import com.lzhpo.common.config.netty.NettyChannelHandler;
import com.lzhpo.common.constant.MyConstant;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author lzhpo
 */
@Component
@Qualifier("ChannelInitializer")
public class NettyWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());
        // websocket连接地址
        pipeline.addLast(new WebSocketServerProtocolHandler("/notice"));
        // 心跳监测每隔6秒监测是否有心跳，没有心跳就将其从redis中删除并且断开连接
        //入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式【这里的心跳监测，需要在NoticeChannelHandler之前，不然无效】
        pipeline.addLast(new IdleStateHandler(MyConstant.HEART_BEAT_INTERVAL, 0, 0, TimeUnit.SECONDS));
        // 处理websocket协议
        pipeline.addLast(new NettyChannelHandler());
    }

}
