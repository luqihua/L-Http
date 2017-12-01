package com.lu.rxhttp.util;

import android.webkit.MimeTypeMap;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author: luqihua
 * Time: 2017/6/6
 * Description: HttpUtil
 */

public class HttpUtil {
    /**
     * get mimetype by file Extension
     *
     * @param file
     * @return
     */
    public static String getMimeTypeFromFile(File file) {
        /*如果文件没有后缀名，默认以二进制流上传*/
        String mime = "application/octet-stream";
        String[] data = file.getName().split("\\.");

        if (data.length > 1) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(data[data.length - 1]);
        }
        return mime;
    }

    /**
     * digest the context by md5
     *
     * @param content
     * @return
     */
    public static String digest(String content) {

        StringBuffer buffer = new StringBuffer("");
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");

            byte[] bytes = md5.digest(content.getBytes());

            int len = bytes.length;

            for (int i = 0; i < len; i++) {
                int value = bytes[i] & 0xff;
                if (value < 16) {
                    buffer.append("0");
                }
                buffer.append(Integer.toHexString(value));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

}
