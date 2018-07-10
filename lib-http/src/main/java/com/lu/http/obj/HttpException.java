package com.lu.http.obj;

import android.util.SparseArray;

/**
 * Author: luqihua
 * Time: 2017/6/7
 * Description: HttpException
 */

public class HttpException extends Exception {

    private final static SparseArray<String> sErrorArray;

    static {
        sErrorArray = new SparseArray<>();
        sErrorArray.put(100, "Continue");
        sErrorArray.put(100, "Continue");
        sErrorArray.put(101, "Switching Protocols");
        /**
         * 2**
         */
        sErrorArray.put(200, "OK");
        sErrorArray.put(201, "Created");
        sErrorArray.put(202, "Accepted");
        sErrorArray.put(203, "Non-Authoritative Information");
        sErrorArray.put(204, "No Content");
        sErrorArray.put(205, "Reset Content");
        sErrorArray.put(206, "Partial Content");


        /**
         * 3**
         */
        sErrorArray.put(300, "Multiple Choices");
        sErrorArray.put(301, "Moved Permanently");
        sErrorArray.put(302, "Found");
        sErrorArray.put(303, "See Other");
        sErrorArray.put(304, "Not Modified");
        sErrorArray.put(305, "Use Proxy");
        sErrorArray.put(306, "Unused");
        sErrorArray.put(307, "Temporary Redirect");

        /**
         * 4**
         */
        sErrorArray.put(400, "Bad LRequest");
        sErrorArray.put(401, "Unauthorized");
        sErrorArray.put(402, "Payment Required");
        sErrorArray.put(403, "Forbidden");
        sErrorArray.put(404, "Not Found");
        sErrorArray.put(405, "HttpMethod Not Allowed");
        sErrorArray.put(406, "Not Acceptable");
        sErrorArray.put(407, "Proxy Authentication Required");
        sErrorArray.put(408, "LRequest Time-out");
        sErrorArray.put(409, "Conflict");
        sErrorArray.put(410, "Gone");
        sErrorArray.put(411, "Length Required");
        sErrorArray.put(412, "Precondition Failed");
        sErrorArray.put(413, "LRequest Entity Too Large");
        sErrorArray.put(414, "LRequest-URI Too Large");
        sErrorArray.put(415, "Unsupported Media Type");
        sErrorArray.put(416, "Requested range not satisfiable");
        sErrorArray.put(417, "Expectation Failed");
        /**
         * 5**
         */
        sErrorArray.put(500, "Internal Server Error");
        sErrorArray.put(501, "Not Implemented");
        sErrorArray.put(502, "Bad Gateway");
        sErrorArray.put(503, "Service Unavailable");
        sErrorArray.put(504, "Gateway Time-out");
        sErrorArray.put(505, "HTTP Version not supported");
    }

    int code;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public static HttpException newInstance(int code) {
        String message = sErrorArray.get(code);
        return new HttpException(code, message == null ? "" : message);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "错误码: " + code + "\n"
                + "错误信息：" + getMessage();
    }
}
