package com.lu.obj;

import android.util.SparseArray;

/**
 * Author: luqihua
 * Time: 2017/6/1
 * Description: HttpStatus
 */

public class HttpStatus {

    private static SparseArray<String> sStatusArray = new SparseArray<>();

    static {
        sStatusArray.put(100, "Continue");
        sStatusArray.put(100, "Continue");
        sStatusArray.put(101, "Switching Protocols");
        /**
         * 2**
         */
        sStatusArray.put(200, "OK");
        sStatusArray.put(201, "Created");
        sStatusArray.put(202, "Accepted");
        sStatusArray.put(203, "Non-Authoritative Information");
        sStatusArray.put(204, "No Content");
        sStatusArray.put(205, "Reset Content");
        sStatusArray.put(206, "Partial Content");


        /**
         * 3**
         */
        sStatusArray.put(300, "Multiple Choices");
        sStatusArray.put(301, "Moved Permanently");
        sStatusArray.put(302, "Found");
        sStatusArray.put(303, "See Other");
        sStatusArray.put(304, "Not Modified");
        sStatusArray.put(305, "Use Proxy");
        sStatusArray.put(306, "Unused");
        sStatusArray.put(307, "Temporary Redirect");

        /**
         * 4**
         */
        sStatusArray.put(400, "Bad Request");
        sStatusArray.put(401, "Unauthorized");
        sStatusArray.put(402, "Payment Required");
        sStatusArray.put(403, "Forbidden");
        sStatusArray.put(404, "Not Found");
        sStatusArray.put(405, "Method Not Allowed");
        sStatusArray.put(406, "Not Acceptable");
        sStatusArray.put(407, "Proxy Authentication Required");
        sStatusArray.put(408, "Request Time-out");
        sStatusArray.put(409, "Conflict");
        sStatusArray.put(410, "Gone");
        sStatusArray.put(411, "Length Required");
        sStatusArray.put(412, "Precondition Failed");
        sStatusArray.put(413, "Request Entity Too Large");
        sStatusArray.put(414, "Request-URI Too Large");
        sStatusArray.put(415, "Unsupported Media Type");
        sStatusArray.put(416, "Requested range not satisfiable");
        sStatusArray.put(417, "Expectation Failed");
        /**
         * 5**
         */
        sStatusArray.put(500, "Internal Server Error");
        sStatusArray.put(501, "Not Implemented");
        sStatusArray.put(502, "Bad Gateway");
        sStatusArray.put(503, "Service Unavailable");
        sStatusArray.put(504, "Gateway Time-out");
        sStatusArray.put(505, "HTTP Version not supported");
    }

    public int code;
    public String description;

    private HttpStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static HttpStatus creatErrorEntity(int code) {
        return new HttpStatus(code, getHttpErrorMessage(code));
    }

    public static String getHttpErrorMessage(int code) {
        String message = sStatusArray.get(code);
        if (message == null) {
            message = "unknown error";
        }
        return message;
    }
}
