package com.lzhpo.ypnettywebsocket.util;


import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端机型
 *
 * @author lzhpo
 */
public class MobileDevice {

    public static String getMobileDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        String deviceStr = "";
        try {
            if (!userAgent.isEmpty()) {
                int startIndex = userAgent.indexOf("(");
                int endIndex = userAgent.indexOf(")");
                deviceStr = userAgent.substring(startIndex + 1, endIndex);
                return deviceStr;
            }
        } catch (Exception e) {
            System.out.println("获取客户端机型异常：" +e);
        }
        return null;
    }

}
