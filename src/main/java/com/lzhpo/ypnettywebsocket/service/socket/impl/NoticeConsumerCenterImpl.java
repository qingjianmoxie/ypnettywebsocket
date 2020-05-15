package com.lzhpo.ypnettywebsocket.service.socket.impl;

import com.lzhpo.ypnettywebsocket.entity.NoticePackage;
import com.lzhpo.ypnettywebsocket.service.socket.NoticeConsumerCenter;
import com.lzhpo.ypnettywebsocket.service.socket.NoticePublishCenter;
import com.lzhpo.ypnettywebsocket.service.socket.PushToClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通知消费者中心，websocket channel从这里获取信息进行消费
 *
 * <p>
 * 观察者模式所特有的addObserver以及notifyObservers，
 * 在这里，我将消息发布中心与消费中心用了观察者模式分解；
 * 当消息发布中心接收到发布事件后，通知消息消费中心准备开始执行消息发布任务，
 * 通知后，消息发布中心就不需要阻塞等待消息消费中心的作业完成结果，而是继续做自己的事情；
 * 而消息消费中心同样也实现了观察者模式，当消息消费中心完了消息发布中心所通知的任务后，
 * 将任务完成结果通知消息发布中心，剩下的关于消息消费的结果的处理就完全扔给了消息发布中心，
 * 而消息消费中心则可以继续自己的其他消息消费任务。
 *
 * @author lzhpo
 */
@Slf4j
@Component
public class NoticeConsumerCenterImpl extends Observable implements NoticeConsumerCenter {

    /** 存接收者 */
    private static ConcurrentLinkedQueue<String> receivers = new ConcurrentLinkedQueue<>();

    @Autowired
    private PushToClientService pushToClientService;

    @Autowired
    private NoticePublishCenter publishCenter;

    private static ThreadPoolExecutor PublishThreadPool;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    private void init() {
        addObserver(publishCenter);
        PublishThreadPool = new ThreadPoolExecutor(4,
                12,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20),
                new NoticeThreadFactory(),
                new RejectHandler());
    }

    /**
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        NoticePackage noticePackage = (NoticePackage) arg;
        PublishThreadPool.execute(new NoticeConsumeTask(noticePackage));
    }

    public static void addReceiver(String receiver) {
        if (!receivers.contains(receiver)) {
            receivers.add(receiver);
        }
    }

    public static void removeReceiver(String receiver) {
        receivers.remove(receiver);
    }

    /**
     * 推送任务
     */
    private class NoticeConsumeTask implements Runnable {

        private NoticePackage noticePackage;

        public NoticeConsumeTask(NoticePackage noticePackage) {
            this.noticePackage = noticePackage;
        }

        /**
         * 执行推送任务
         */
        @Override
        public void run() {
            log.info("Push data to client start...");
            List<String> already = new ArrayList<>();
            noticePackage.getReceiverChannelIds()
                    .parallelStream()
                    .filter(receiver -> receivers.contains(receiver))
                    .peek(receiver -> {
                        already.add(receiver);
                        pushToClientService.publishMsg(noticePackage, receiver);
                    })
                    .count();
            noticePackage.getReceiverChannelIds().removeAll(already);
            noticePackage.getReceiverChannelIds().forEach(item -> {
                // 如果redis中没有此接收者，就直接接受者noticePackage.setTotalReceivers(0);设置为0
                if (StringUtils.isEmpty(redisTemplate.opsForValue().get(item))) {
                    noticePackage.getReceiverChannelIds().clear();
                }
            });
            noticePackage.setTotalReceivers(noticePackage.getReceiverChannelIds().size());
            setChanged();
            // JDK特有的观察者模式notifyObservers
            notifyObservers(noticePackage);
            log.info("Push data to client end...");
        }
    }

    /**
     * 线程池任务拒绝策略
     */
    private class RejectHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("{} 消息推送服务任务被拒绝。 {}", r.toString(), executor.toString());
        }
    }

    private class NoticeThreadFactory implements ThreadFactory {

        private final String namePrefix;
        private final AtomicInteger nextId = new AtomicInteger(1);

        NoticeThreadFactory() {
            namePrefix = "消息推送-工作线程-";
        }

        @Override
        public Thread newThread(Runnable r) {
            String name = namePrefix + nextId.getAndDecrement();
            return new Thread(r, name);
        }
    }

}
