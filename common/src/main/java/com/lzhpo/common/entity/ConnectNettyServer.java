package com.lzhpo.common.entity;

import lombok.Data;

/**
 * @author lzhpo
 */
@Data
public class ConnectNettyServer {
    /** Netty连接地址和端口 */
    private String nettyServerUrl;
}
