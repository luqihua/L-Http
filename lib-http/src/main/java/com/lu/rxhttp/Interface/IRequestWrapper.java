package com.lu.rxhttp.Interface;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @Author: luqihua
 * @Time: 2018/6/22
 * @Description: IRequestWrapper
 */
public interface IRequestWrapper<T> {
    T post(final String url,
           Map<String, String> headers,
           Map<String, String> params,
           final Type type);


    T get(final String url,
          Map<String, String> headers,
          Map<String, String> params,
          final Type type);

    T postJson(final String url,
               Map<String, String> headers,
               Map<String, String> params,
               final Type type);

    T postMultipart(final String url,
                    Map<String, String> headers,
                    Map<String, String> params,
                    Map<String, File> fileMap,
                    IProgressListener listener,
                    final Type type);

}
