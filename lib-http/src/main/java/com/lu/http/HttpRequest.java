package com.lu.http;

import com.google.gson.Gson;
import com.lu.http.Interface.IProgressListener;
import com.lu.http.Interface.IRequestWrapper;
import com.lu.http.annotation.LMethod;
import com.lu.http.annotation.RequestWrapper;
import com.lu.http.request.FormRequest;
import com.lu.http.request.JsonRequest;
import com.lu.http.request.MultiPartRequest;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @Author: luqihua
 * @Time: 2018/6/5
 * @Description: HttpRequest
 */
@RequestWrapper
public class HttpRequest implements IRequestWrapper<Observable<?>> {
    private static Gson sGson = new Gson();

    @Override
    public Observable<?> form(final String url,
                              final Enum<LMethod> method,
                              Map<String, String> headers,
                              Map<String, String> params,
                              final Type type) {
        return new FormRequest()
                .url(url)
                .params(params)
                .headers(headers)
                .method(method)
                .observerResponseBody()
                .map(new Function<ResponseBody, Object>() {
                    @Override
                    public Object apply(ResponseBody responseBody) throws Exception {
                        Object o = sGson.fromJson(responseBody.string(), type);
                        return o;
                    }
                });
    }

    @Override
    public Observable<?> json(final String url,
                              Map<String, String> headers,
                              Map<String, String> params,
                              Object jsonBody,
                              final Type type) {
        return new JsonRequest()
                .url(url)
                .headers(headers)
                .params(params)
                .addJsonBody(jsonBody)
                .observerResponseBody()
                .map(new Function<ResponseBody, Object>() {
                    @Override
                    public Object apply(ResponseBody responseBody) throws Exception {
                        Object o = sGson.fromJson(responseBody.string(), type);
                        return o;
                    }
                });

    }

    @Override
    public Observable<?> multipart(final String url,
                                   Map<String, String> headers,
                                   Map<String, String> params,
                                   Map<String, File> files,
                                   IProgressListener listener,
                                   final Type type) {
        return new MultiPartRequest()
                .url(url)
                .headers(headers)
                .params(params)
                .files(files)
                .progress(listener)
                .observerResponseBody()
                .map(new Function<ResponseBody, Object>() {
                    @Override
                    public Object apply(ResponseBody responseBody) throws Exception {
                        Object o = sGson.fromJson(responseBody.string(), type);
                        return o;
                    }
                });

    }
}
