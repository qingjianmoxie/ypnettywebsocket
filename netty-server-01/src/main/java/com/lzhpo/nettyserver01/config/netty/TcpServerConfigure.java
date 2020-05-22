package com.lzhpo.nettyserver01.config.netty;

import com.lzhpo.common.constant.MyConstant;
import com.lzhpo.common.util.IpUtil;
import com.lzhpo.nettyserver01.zk.ZookeeperUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

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
    private ZookeeperUtil zookeeperUtil;

    @Value("${netty.server.tcp.port}")
    private String nettyServerTcpPort;

    /**
     * 启动Netty
     *
     * @throws Exception
     */
    public void start() throws Exception {
        // 获取本主机IP地址
        String hostIp = IpUtil.getHostIp();
        // 0.0.0.0/0.0.0.0:8003
        System.out.println("tcpPort: " +tcpPort.toString());
        String nettyServerPort = hostIp +":" +nettyServerTcpPort;
        boolean node = zookeeperUtil.createTemporaryNode(MyConstant.NETTY_SERVER + "/" + nettyServerPort, nettyServerPort);
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
