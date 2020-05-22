package com.lzhpo.nettyserver01.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lzhpo
 */
public class Md5Util {

    /**
     * 转换为 md5 32位小写
     *
     * @param str
     * @return
     */
    public static String to32LowerCase(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();
        for (byte b : byteArray) {
            if (Integer.toHexString(0xFF & b).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & b));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & b));
            }
        }
        return md5StrBuff.toString();
    }

    public static void main(String[] args) {
        System.out.println(to32LowerCase("123456"));;
    }
}
