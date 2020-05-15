package com.lzhpo.ypnettywebsocket.entity;

import com.lzhpo.ypnettywebsocket.service.socket.ChannelGroupTypeEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

/**
 * @author lzhpo
 */
public class NoticePackage implements Serializable {

    protected String noticeId;
    /** 消息主题 */
    protected String noticeLabel;
    /** 推送者 */
    protected String publisher;
    /** 接收者 */
    protected List<String> receiverChannelIds;
    /** 消息通道分组(Kafka)，{@link ChannelGroupTypeEnum} */
    protected int groupType;
    /** 消息内容 */
    protected String message;
    /** 总有多少个接收者 */
    protected long totalReceivers;

    public NoticePackage() {
        noticeId = UUID.randomUUID().toString();
    }

    public NoticePackage(String noticeId, String noticeLabel, String publisher, List<String> receiverChannelIds, int groupType, String message, long totalReceivers) {
        this.noticeId = noticeId;
        this.noticeLabel = noticeLabel;
        this.publisher = publisher;
        this.receiverChannelIds = receiverChannelIds;
        this.groupType = groupType;
        this.message = message;
        this.totalReceivers = totalReceivers;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeLabel() {
        return noticeLabel;
    }

    public void setNoticeLabel(String noticeLabel) {
        this.noticeLabel = noticeLabel;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<String> getReceiverChannelIds() {
        return receiverChannelIds;
    }

    public void setReceiverChannelIds(List<String> receiverChannelIds) {
        this.receiverChannelIds = receiverChannelIds;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTotalReceivers() {
        return totalReceivers;
    }

    public void setTotalReceivers(long totalReceivers) {
        this.totalReceivers = totalReceivers;
    }

    @Override
    public String toString() {
        return "NoticePackage{" +
                "noticeId='" + noticeId + '\'' +
                ", noticeLabel='" + noticeLabel + '\'' +
                ", publisher='" + publisher + '\'' +
                ", receiverChannelIds=" + receiverChannelIds +
                ", groupType=" + groupType +
                ", message='" + message + '\'' +
                ", totalReceivers=" + totalReceivers +
                '}';
    }
}
