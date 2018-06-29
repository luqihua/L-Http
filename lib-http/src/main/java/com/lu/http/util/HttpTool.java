package com.lu.http.util;

import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Author: luqihua
 * Time: 2017/6/6
 * Description: HttpTool
 */

public class HttpTool {
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
            mime = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(data[data.length - 1].toLowerCase());
        }
        return mime;
    }


    public static Object formatRequestTag(Object obj) {
        return obj.hashCode();
    }
}
