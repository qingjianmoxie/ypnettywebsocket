package com.lzhpo.ypnettywebsocket.service.socket;

import com.lzhpo.ypnettywebsocket.service.socket.impl.NoticeConsumerCenterImpl;
import io.netty.channel.ChannelId;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义的管理容器 - 将clientId与channelId关联起来
 *
 * @author lzhpo
 */
public class ChannelIdPool {

    private static ConcurrentHashMap<String, ChannelId> channelIdMap;

    static {
        channelIdMap = new ConcurrentHashMap<>();
    }

    public ChannelIdPool() {}

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
        Iterator<Map.Entry<String, ChannelId>> iterator = channelIdMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ChannelId> entry = iterator.next();
            if (entry.getValue().compareTo(channelId) == 0) {
                iterator.remove();
                NoticeConsumerCenterImpl.removeReceiver(entry.getKey());
            }
        }
    }

    public static ConcurrentHashMap<String, ChannelId> getAll() {
        return channelIdMap;
    }

}
