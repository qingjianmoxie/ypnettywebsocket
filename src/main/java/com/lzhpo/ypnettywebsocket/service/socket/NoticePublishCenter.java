package com.lzhpo.ypnettywebsocket.service.socket;

import com.lzhpo.ypnettywebsocket.entity.NoticePackage;

import java.util.Observer;

/**
 * @author lzhpo
 */
public interface NoticePublishCenter extends Observer {

    /**
     * 创建消息通知主题并放入消息信息
     *
     * @param noticePackage
     * @return
     */
    boolean createNoticeGroup(NoticePackage noticePackage);

}
