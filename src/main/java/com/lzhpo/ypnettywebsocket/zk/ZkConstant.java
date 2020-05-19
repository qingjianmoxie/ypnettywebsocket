package com.lzhpo.ypnettywebsocket.zk;

/**
 * Zookeeper公用常量
 *
 * @author lzhpo
 */
public class ZkConstant {

    /** 在ZK中创建的节点目录(不能递归创建)，也就是注册中心 */
    public static final String YP_QUEUE = "/yp-queue";

    /** Netty在ZK中的 /yp-queue 注册的节点 */
    public static final String REG_NETTY_SERVICE = "/reg-netty-service";

}
