package com.lu.obj;

/**
 * Author: luqihua
 * Time: 2017/6/1
 * Description: HttpMethod
 */

public enum HttpMethod {

    POST("POST"), GET("GET"), TRACE("TRACE");

    private String name;

    HttpMethod(String name) {
        this.name = name;
    }
}
