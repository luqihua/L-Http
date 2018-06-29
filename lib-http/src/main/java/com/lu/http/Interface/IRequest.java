package com.lu.http.Interface;

import com.lu.http.annotation.LMethod;

import java.util.Map;

/**
 * Author: luqihua
 * Time: 2017/6/8
 * Description: IRequest
 */

public interface IRequest<T> {

    T url(String url);

    T method(Enum<LMethod> method);

    T tag(Object tag);

    T headers(Map<String, String> header);

    T params(Map<String, String> params);

    T param(String key, String value);

}
