package com.lzhpo.ypnettywebsocket.service.socket;

/**
 * @author lzhpo
 */

public enum ChannelGroupTypeEnum {

    /**
     * SMS消息通道分组
     */
    CHANNEL_GROUP_SMS(1, "channel_sms"),

    /**
     * AI垃圾桶消息通道分组
     */
    CHANNEL_GROUP_AI_TRASH_SAN(2, "channel_ai_trash_san"),

    /**
     * 全部通道分组
     */
    CHANNEL_GROUP_GLOBAL(3, "channel_global")
    ;

    private int value;
    private String type;

    ChannelGroupTypeEnum(int value, String type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
