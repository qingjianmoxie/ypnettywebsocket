package com.lzhpo.ypnettywebsocket.config.netty;

import com.lzhpo.ypnettywebsocket.zk.ZkApi;
import com.lzhpo.ypnettywebsocket.zk.ZkConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author lzhpo
 */
@Component
@Slf4j
public class TcpServerConfigure {

    @Autowired
    @Qualifier("ServerBootstrap")
    private ServerBootstrap serverBootstrap;

    /** 包含了Netty启动的时候一些主机和端口等信息 */
    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress tcpPort;

    private Channel serverChannel;

    /** Zookeeper Api操作 */
    @Autowired
    private ZkApi zkApi;

    /**
     * 启动Netty
     *
     * @throws Exception
     */
    public void start() throws Exception {
        System.out.println("tcpPort: " +tcpPort.toString());
        // 在Zookeeper中创建临时节点，注册的时候的格式是 0.0.0.0/0.0.0.0:8003 明显就是不符合我要的数据规范，我需要的格式是 0.0.0.0:8003
        String serverAndPortStr = tcpPort.toString();
        String[] serverAndPortArr = serverAndPortStr.split("/");
        // 我需要的是第二个0.0.0.0:8003，也就是serverAndPortArr[1]
        boolean node = zkApi.createTemporaryNumNode(ZkConstant.YP_QUEUE +ZkConstant.REG_NETTY_SERVICE, serverAndPortArr[1]);
        if (node) {
            log.info("服务 [{}] 注册成功！", tcpPort.toString());
        } else {
          log.error("服务 [{}] 注册失败！", tcpPort.toString());
        }
        // 在此之前做操作
        serverChannel =  serverBootstrap.bind(tcpPort).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannel.close();
        serverChannel.parent().close();
    }

    public ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public void setServerBootstrap(ServerBootstrap serverBootstrap) {
        this.serverBootstrap = serverBootstrap;
    }

    public InetSocketAddress getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(InetSocketAddress tcpPort) {
        this.tcpPort = tcpPort;
    }

}
