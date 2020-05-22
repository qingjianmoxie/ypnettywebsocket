package com.lzhpo.gateway.controller;

import com.lzhpo.common.constant.MyConstant;
import com.lzhpo.common.loadbalance.MyZkLoadBalanceUtil;
import com.lzhpo.common.vo.ResultData;
import com.lzhpo.gateway.zk.ZookeeperUtil;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lzhpo
 */
@RestController
@RequestMapping("/api/v1/connect")
public class ConnectNettyController {

    @Autowired
    private ZookeeperUtil zookeeperUtil;

    /**
     * 负载均衡算法 -> 获取Zookeeper中的Netty一个地址
     *
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    @GetMapping("getNettyServer")
    public ResultData<String> getServer() throws KeeperException, InterruptedException {
        List<String> nettyServerList = zookeeperUtil.getChildrenArrayData(MyConstant.NETTY_SERVER, null);
        System.out.println("nettyServerList：" +nettyServerList);
        String nettyServer = MyZkLoadBalanceUtil.roundRobin(nettyServerList);
        System.out.println("nettyServer：" +nettyServer);
        return new ResultData<>(nettyServer);
    }
}
