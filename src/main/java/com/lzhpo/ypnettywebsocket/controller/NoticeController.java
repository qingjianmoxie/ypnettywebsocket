package com.lzhpo.ypnettywebsocket.controller;

import com.lzhpo.ypnettywebsocket.entity.NoticePackage;
import com.lzhpo.ypnettywebsocket.service.notice.NoticeService;
import com.lzhpo.ypnettywebsocket.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzhpo
 */
@Controller
@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/publish")
    public ResultData publish(@RequestBody NoticePackage noticePackage) {
        return noticeService.publish(noticePackage);
    }
}
