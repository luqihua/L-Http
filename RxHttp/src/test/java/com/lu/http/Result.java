package com.lu.http;

/**
 * Author: luqihua
 * Time: 2017/11/15
 * Description: Result
 */

public class Result<T> {
    int code;
    String msg;
    T data;

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
