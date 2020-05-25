package com.lzhpo.gateway.controller;

import com.lzhpo.common.constant.MyConstant;
import com.lzhpo.common.entity.ConnectNettyServer;
import com.lzhpo.common.loadbalance.MyZkLoadBalanceUtil;
import com.lzhpo.common.vo.ResultData;
import com.lzhpo.gateway.zk.ZookeeperUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public ResultData<ConnectNettyServer> getServer() throws KeeperException, InterruptedException {
        List<String> nettyServerList = zookeeperUtil.getChildrenArrayData(MyConstant.NETTY_SERVER, null);
        String nettyServer = MyZkLoadBalanceUtil.roundRobin(nettyServerList);
        ConnectNettyServer connectNettyServer = new ConnectNettyServer();
        connectNettyServer.setNettyServerUrl(nettyServer);
        log.info("客户端请求的Netty服务器地址 [{}]", connectNettyServer);
        return new ResultData<>(connectNettyServer);
    }
}
