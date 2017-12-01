package com.lu.rxhttp.Interface;

import com.lu.rxhttp.obj.HttpHeader;

import java.io.InputStream;

/**
 * Author: luqihua
 * Time: 2017/7/6
 * Description: IResponse
 */

public interface IResponse {
    int getResponseCode();

    long contentLength();

    HttpHeader getHeaders();

    String string();

    InputStream stream();
}
