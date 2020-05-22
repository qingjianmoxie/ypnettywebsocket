package com.lzhpo.nettyserver01;

import com.lzhpo.nettyserver01.config.netty.TcpServerConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * NettyServer01Application
 *
 * @author lzhpo
 */
@SpringBootApplication
@EnableAsync
public class NettyServer01Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyServer01Application.class, args);
        TcpServerConfigure tcpServer = context.getBean(TcpServerConfigure.class);
        try {
            tcpServer.start();
        } catch (Exception e) {
            System.out.println("启动失败！" +e.getMessage());
        }
    }

}
