package com.lzhpo.nettyserver01.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * 实现Watcher监听
 *
 * @author lzhpo
 */
@Slf4j
public class WatcherApi implements Watcher {

    /**
     * 监听处理事件
     *
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {
        log.info("【Watcher监听事件】={}",event.getState());
        log.info("【监听路径为】={}",event.getPath());
        //  三种监听类型： 创建，删除，更新
        log.info("【监听的类型为】={}",event.getType());
    }
}