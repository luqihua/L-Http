package com.lu.rxhttp.work;

import android.support.annotation.Nullable;

import com.lu.rxhttp.Interface.IResponseBodyConvert;
import com.lu.rxhttp.annotation.Form;
import com.lu.rxhttp.annotation.Json;
import com.lu.rxhttp.annotation.MultiPart;

import java.lang.reflect.Method;

import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: PlatForm
 */

public class HttpDispatcher {

    private String baseUrl;
    private OkHttpClient client;
    private IResponseBodyConvert responseBodyConvert;

    public HttpDispatcher(String url, OkHttpClient client,IResponseBodyConvert convert) {
        if (url == null || !url.startsWith("http")) {
            throw new RuntimeException("incorrect base_url");
        }
        this.baseUrl = url;
        this.client = client == null ? new OkHttpClient() : client;
        this.responseBodyConvert = convert;
    }

    public Object dispatch(final Method method, @Nullable Object[] args) {
        //请求类型
        if (method.isAnnotationPresent(Form.class)) {
            return new FormWork(baseUrl, client).invoke(method, args);
        } else if (method.isAnnotationPresent(MultiPart.class)) {
            return new MultiPartWork(baseUrl, client).invoke(method, args);
        } else if (method.isAnnotationPresent(Json.class)) {
            return new JsonWork(baseUrl, client).invoke(method, args);
        } else {
            throw new RuntimeException("you need to add annotation ( @Form || @MultiPart || @Json ) to declare the quest Type");
        }
    }
}
