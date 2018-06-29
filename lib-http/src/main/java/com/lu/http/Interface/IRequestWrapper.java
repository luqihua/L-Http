package com.lu.http.Interface;

import com.lu.http.annotation.LMethod;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @Author: luqihua
 * @Time: 2018/6/22
 * @Description: IRequestWrapper
 */
public interface IRequestWrapper<T> {
    T form(final String url,
           final Enum<LMethod> method,
           Map<String, String> headers,
           Map<String, String> params,
           final Type type);

    T json(final String url,
           Map<String, String> headers,
           Map<String, String> params,
           Object jsonBody,
           final Type type);

    T multipart(final String url,
                Map<String, String> headers,
                Map<String, String> params,
                Map<String, File> fileMap,
                IProgressListener listener,
                final Type type);
}
