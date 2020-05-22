package com.lzhpo.common.loadbalance;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 负载均衡算法
 *
 * TODO：涉及权重的轮询算法，如果服务器配置不一，需要自定义分配权重的话，还要记录Netty所在的服务器的权重，然后再写加权轮询算法、加权随机算法...
 *
 * @author lzhpo
 */
public class MyZkLoadBalanceUtil {

    /** 负载均衡算法计数器 */
    private static Integer roundRobinCountNum = 0;

    /**
     * 轮询算法
     *
     * @param serverList Zookeeper中的Netty服务器列表
     * @return 返回服务器IP和端口
     */
    public static String roundRobin(List<String> serverList) {
        System.out.println("roundRobinCountNum: " +roundRobinCountNum);
        String server = null;
        if (roundRobinCountNum >= serverList.size()) {
            // 计数器大于服务器列表个数，计数器就重新归零
            roundRobinCountNum = 0;
            server = serverList.get(0);
        } else {
            server = serverList.get(roundRobinCountNum);
        }
        roundRobinCountNum++;
        return server;
    }

    /**
     * 随机算法
     *
     * @param serverList Zookeeper中的Netty服务器列表
     * @return 返回服务器IP和端口
     */
    public static String random(List<String> serverList) {
        int randomNum = (int) (Math.random() * serverList.size() - 1);
        return serverList.get(randomNum);
    }

    /**
     * 源地址哈希算法
     *
     * @param serverList serverList Zookeeper中的Netty服务器列表
     * @param request request请求
     * @return
     */
    public static String hash(List<String> serverList, HttpServletRequest request) {
        // 获取真实IP和端口号
//        String remoteIp = IpUtil.getRealIp(request) + ":" +nettyPort;
        String remoteIp = "127.0.0.1:8003";
        int hashCode = remoteIp.hashCode();
        int serverListSize = serverList.size();
        // 取模得到一个值
        int serverPos = hashCode % serverListSize;
        return serverList.get(serverPos);
    }
}
