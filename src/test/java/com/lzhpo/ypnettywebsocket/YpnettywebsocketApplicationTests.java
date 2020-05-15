package com.lzhpo.ypnettywebsocket;

import com.lzhpo.ypnettywebsocket.service.socket.ChannelIdPool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YpnettywebsocketApplicationTests {


    @Test
    void testChannelIdPoolGetAll() {
        System.out.println("testChannelIdPoolGetAll：" +ChannelIdPool.getAll().toString());
    }
}
