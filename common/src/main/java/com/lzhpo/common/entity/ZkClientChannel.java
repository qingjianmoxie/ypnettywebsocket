package com.lzhpo.common.entity;

import lombok.Data;

/**
 * @author lzhpo
 */
@Data
public class ZkClientChannel extends BaseEntity {
    /** 客户端的clientId */
    private String clientId;
    /** 客户端的channelId */
    private String channelId;
}
