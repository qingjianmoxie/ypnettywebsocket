package com.lzhpo.nettyserver01;

import com.lzhpo.nettyserver01.zk.ZookeeperUtil;
import org.apache.zookeeper.KeeperException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lzhpo
 */
@SpringBootTest
public class ZkTest {

    @Autowired
    private ZookeeperUtil zookeeperUtil;

    @Test
    public void getChannel() throws KeeperException, InterruptedException {
    }

}
