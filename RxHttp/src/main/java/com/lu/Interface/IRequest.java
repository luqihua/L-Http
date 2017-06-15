package com.lu.Interface;

import com.lu.util.Const;

import okhttp3.CacheControl;

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


}
