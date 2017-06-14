package com.lu.obj;

/**
 * Author: luqihua
 * Time: 2017/6/7
 * Description: HttpException
 */

public class HttpException extends Exception {
    int code;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
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
