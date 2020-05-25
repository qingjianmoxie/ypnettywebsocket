package com.lzhpo.nettyserver03;

import com.lzhpo.common.config.netty.TcpServerConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * nettyserver03Application
 *
 * @author lzhpo
 */
@SpringBootApplication(scanBasePackages = "com.lzhpo")
@EnableAsync
public class NettyServer03Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyServer03Application.class, args);
        TcpServerConfigure tcpServer = context.getBean(TcpServerConfigure.class);
        try {
            tcpServer.start();
        } catch (Exception e) {
            System.out.println("启动失败！" +e.getMessage());
        }
    }

}
