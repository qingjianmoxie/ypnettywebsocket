package com.lzhpo.ypnettywebsocket;

import com.lzhpo.ypnettywebsocket.config.netty.TcpServerConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动类
 *
 * @author lzhpo
 */
@SpringBootApplication
@Slf4j
public class YpnettywebsocketApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YpnettywebsocketApplication.class, args);
        TcpServerConfigure tcpServer = context.getBean(TcpServerConfigure.class);
        try {
            tcpServer.start();
//            System.out.println("启动成功！");
        } catch (Exception e) {
            System.out.println("启动失败！");
            log.error(e.getMessage());
        }
    }

}
