package com.lu.rxhttp.Interface;

import com.lu.rxhttp.obj.HttpHeader;
import com.lu.rxhttp.util.Const;

import java.util.Map;

import okhttp3.CacheControl;

/**
 * Author: luqihua
 * Time: 2017/6/8
 * Description: IRequest
 */

public interface IRequest<T> {

    T url(String url);

    T method(@Const.HttpMethod String method);

    T tag(String tag);

    T headers(HttpHeader header);

    T params(Map<String, String> params);

    T log(boolean log);

    T cacheControl(CacheControl control);

    T forceCache(String forceCache);

}
