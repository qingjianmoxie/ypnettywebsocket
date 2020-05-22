package com.lzhpo.common.websocket;

import io.netty.channel.ChannelId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 关联ClientId和ChannelId。
 *
 * @author lzhpo
 */
public class ClientChannelPool {

    /** ConcurrentHashMap的最大容量是2的30次方，大概就是 10亿多。 */
    private static ConcurrentHashMap<String, ChannelId> channelIdMap;

    static {
        channelIdMap = new ConcurrentHashMap<>();
    }

    public ClientChannelPool() {}

    /**
     * 根据clientId查询ChannelIdPool中的channelId
     *
     * @param key
     * @return
     */
    public static Optional<ChannelId> get(String key) {
        return Optional.ofNullable(channelIdMap.get(key));
    }

    /**
     * 根据channelId查询ChannelIdPool中的clientId
     *
     * @param channelId
     * @return
     */
    public static String get(ChannelId channelId) {
        for (Map.Entry<String, ChannelId> entry : channelIdMap.entrySet()) {
            if (entry.getValue().compareTo(channelId) == 0) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void add(String clientId, ChannelId channelId) {
        channelIdMap.put(clientId, channelId);
    }

    /**
     * 根据clientId删除ChannelIdPool中的数据
     *
     * @param clientId
     */
    public static void remove(String clientId) {
        channelIdMap.remove(clientId);
    }

    /**
     * 根据channelId删除ChannelIdPool中的数据
     *
     * @param channelId
     */
    public static void remove(ChannelId channelId) {
        channelIdMap.entrySet().removeIf(entry -> entry.getValue().compareTo(channelId) == 0);
    }

    public static ConcurrentHashMap<String, ChannelId> getAll() {
        return channelIdMap;
    }

}
