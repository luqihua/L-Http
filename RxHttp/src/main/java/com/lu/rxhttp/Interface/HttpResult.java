package com.lu.rxhttp.Interface;

/**
 * Author: luqihua
 * Time: 2017/9/21
 * Description: HttpResult
 */

public interface HttpResult<T> {
    int getCode();

    String getMesseage();

    String getData();
}
