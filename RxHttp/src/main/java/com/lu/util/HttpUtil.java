package com.lu.util;

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
     * 获取文件的mimetype
     *
     * @param file
     * @return
     */
    public static String getMimeTypeFromFile(File file) {
        /*默认是txt文件*/
        String mime = "text/plain";
        String[] data = file.getName().split("\\.");

        if (data.length > 1) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(data[data.length - 1]);
        }
        return mime;
    }

    /**
     * 对内容进行md5加密
     * @param content
     * @return
     */
    public static String digest(String content) {

        StringBuffer buffer = new StringBuffer("");
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");

            byte[] bytes = md5.digest(content.getBytes());

            for (byte b : bytes) {
                int value = b & 0xff;
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
