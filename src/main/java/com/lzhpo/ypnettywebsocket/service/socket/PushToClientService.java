package com.lzhpo.ypnettywebsocket.service.socket;

import com.lzhpo.ypnettywebsocket.entity.NoticePackage;

/**
 * @author lzhpo
 */
public interface PushToClientService {

    void publishMsg(NoticePackage noticePackage, String receiver);

}
