package com.lzhpo.ypnettywebsocket.service.notice;

import com.lzhpo.ypnettywebsocket.entity.NoticePackage;
import com.lzhpo.ypnettywebsocket.vo.ResultData;

/**
 * @author lzhpo
 */
public interface NoticeService {

    ResultData publish(NoticePackage noticePackage);

}
