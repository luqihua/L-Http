package com.lu.rxhttp.Interface;

import com.lu.rxhttp.util.Const;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Author: luqihua
 * Time: 2017/6/8
 * Description: IRequest
 */

public interface IRequest<T> {

    T url(String url);

    T tag(String tag);

    T method(@Const.HttpMethod String method);

    T header(String key, String value);

    T log(boolean log);

    T cacheControl(CacheControl control);

    T forceCache(String forceCache);

    Request getRequest();

    OkHttpClient getClient();

}
