package com.lzhpo.ypnettywebsocket.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lzhpo
 */
public class KafkaMsg implements Serializable {

    private String id;
    private String body;
    private Date sendTime;
    /** 接收者 */
    protected List<String> receiverChannelIds;

    public KafkaMsg() {
    }

    public KafkaMsg(String id, String body, Date sendTime) {
        this.id = id;
        this.body = body;
        this.sendTime = sendTime;
    }

    public KafkaMsg(String id, String body, Date sendTime, List<String> receiverChannelIds) {
        this.id = id;
        this.body = body;
        this.sendTime = sendTime;
        this.receiverChannelIds = receiverChannelIds;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public List<String> getReceiverChannelIds() {
        return receiverChannelIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private KafkaMsg kafkaMsg;

        public Builder() {
            kafkaMsg = new KafkaMsg();
        }

        public Builder id(String id) {
            kafkaMsg.id = id;
            return this;
        }

        public Builder body(String body) {
            kafkaMsg.body = body;
            return this;
        }

        public Builder sendTime(Date sendTime) {
            kafkaMsg.sendTime = sendTime;
            return this;
        }

        public Builder receiverChannelIds(List<String> receiverChannelIds) {
            kafkaMsg.receiverChannelIds = receiverChannelIds;
            return this;
        }

        public KafkaMsg builded() {
            return kafkaMsg;
        }
    }
}

