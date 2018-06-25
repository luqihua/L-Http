package com.lu.rxhttp;

import com.google.gson.Gson;
import com.lu.http.annotation.RequestWrapper;
import com.lu.rxhttp.Interface.IProgressListener;
import com.lu.rxhttp.Interface.IRequestWrapper;
import com.lu.rxhttp.request.FormRequest;
import com.lu.rxhttp.request.JsonRequest;
import com.lu.rxhttp.request.MultiPartRequest;
import com.lu.rxhttp.util.Const;

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
    public Observable<?> post(final String url,
                              Map<String, String> headers,
                              Map<String, String> params,
                              final Type type) {
        return new FormRequest()
                .url(url)
                .params(params)
                .headers(headers)
                .method(Const.POST)
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
    public Observable<?> get(final String url,
                             Map<String, String> headers,
                             Map<String, String> params,
                             final Type type) {
        return new FormRequest()
                .url(url)
                .headers(headers)
                .method(Const.GET)
                .params(params)
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
    public Observable<?> postMultipart(final String url,
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

    @Override
    public Observable<?> postJson(final String url,
                                  Map<String, String> headers,
                                  Map<String, String> params,
                                  final Type type) {
        return new JsonRequest()
                .url(url)
                .headers(headers)
                .params(params)
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
