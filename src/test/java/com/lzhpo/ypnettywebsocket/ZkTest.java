package com.lzhpo.ypnettywebsocket;

import com.lzhpo.ypnettywebsocket.zk.ZkApi;
import com.lzhpo.ypnettywebsocket.zk.ZkConstant;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lzhpo
 */
@SpringBootTest
public class ZkTest {

    @Autowired
    private ZkApi zkApi;

    private static Stat stat = new Stat();

    @Test
    public void testGetNodes() throws KeeperException, InterruptedException {
        System.out.println(zkApi.getChildrenArrayData(ZkConstant.YP_QUEUE, null).toString());
    }

}
