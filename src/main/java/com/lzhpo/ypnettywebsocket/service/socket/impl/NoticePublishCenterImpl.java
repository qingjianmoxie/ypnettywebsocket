package com.lzhpo.ypnettywebsocket.service.socket.impl;

import com.lzhpo.ypnettywebsocket.entity.KafkaMsg;
import com.lzhpo.ypnettywebsocket.entity.KafkaPackage;
import com.lzhpo.ypnettywebsocket.entity.NoticePackage;
import com.lzhpo.ypnettywebsocket.service.kafka.KafkaProducer;
import com.lzhpo.ypnettywebsocket.service.socket.NoticeConsumerCenter;
import com.lzhpo.ypnettywebsocket.service.socket.NoticePublishCenter;
import com.lzhpo.ypnettywebsocket.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Observable;
import java.util.UUID;

/**
 * 通知发布者中心，发布的消息进入此处准备发送
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
public class NoticePublishCenterImpl extends Observable implements NoticePublishCenter {

    @Value("${kafka.consumer.topic.notice}")
    private String kafkaTopicNotice;

    @Autowired
    private NoticeConsumerCenter noticeConsumerCenter;
    @Qualifier("KafkaProducer")
    @Autowired
    private KafkaProducer kafkaProducer;

    @PostConstruct
    public void init() {
        addObserver(noticeConsumerCenter);
    }

    /**
     * 通知
     *
     * <p>
     * 当被观察事件发生时，执行：
     * setChanged();
     * notifyObservers();
     *
     * setChange()方法用来设置一个内部标志位注明数据发生了变化；
     * notifyObservers()方法会去调用观察者对象列表中所有的Observer的update()方法，通知它们数据发生了变化。
     * 只有在setChange()被调用后，notifyObservers()才会去调用update()。
     *
     * @param noticePackage
     * @return
     */
    @Override
    public boolean createNoticeGroup(NoticePackage noticePackage) {
        setChanged();
        // JDK特有的观察者模式notifyObservers
        notifyObservers(noticePackage);
        return true;
    }

    /**
     * 由消息消费者中心通知回调告知通知发布中心该消息消费结果
     * 如果消息通知任务未完成，则消息发布中心将消息回压至kafka消息队列
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        NoticePackage noticePackage = (NoticePackage) arg;
        if (noticePackage.getTotalReceivers() == 0) {
            System.out.println("消息推送成功！");
        } else {
            System.out.println("消息通知任务未完成！");
            KafkaMsg kafkaMsg = KafkaMsg.builder()
                    .id(UUID.randomUUID().toString())
                    .body(JsonUtils.toJson(noticePackage))
                    .receiverChannelIds(noticePackage.getReceiverChannelIds())
                    .sendTime(new Date())
                    .builded();
            kafkaProducer.producerMsg(KafkaPackage.builder().topic(kafkaTopicNotice).kafkaMsg(kafkaMsg).builded());
        }
    }

}
