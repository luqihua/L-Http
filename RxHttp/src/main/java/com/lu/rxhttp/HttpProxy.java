package com.lu.rxhttp;

import android.support.annotation.Nullable;

import com.lu.rxhttp.Interface.IResponseBodyConvert;
import com.lu.rxhttp.annotation.Form;
import com.lu.rxhttp.annotation.Json;
import com.lu.rxhttp.annotation.MultiPart;
import com.lu.rxhttp.work.FormWork;
import com.lu.rxhttp.work.JsonWork;
import com.lu.rxhttp.work.MultiPartWork;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/11/15
 * Description: HttpProxy
 */

public class HttpProxy {

    private String baseUrl;
    private OkHttpClient client;
    private IResponseBodyConvert responseBodyConvert;

    private HttpProxy(Builder builder) {
        this.baseUrl = builder.baseUrl;
        if (baseUrl == null || !baseUrl.startsWith("http")) {
            throw new RuntimeException("incorrect base_url");
        }
        this.client = builder.client == null ? new OkHttpClient() : builder.client;
        this.responseBodyConvert = builder.responseBodyConvert;
    }

    public <T> T create(final Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader()
                , new Class[]{service}
                , new InvocationHandler() {
                    public Object invoke(Object proxy, final Method method, @Nullable Object[] args) throws Throwable {
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        return dispatcher(method, args);
                    }
                });
    }


    private Object dispatcher(final Method method, @Nullable Object[] args) {
        //请求类型
        if (method.isAnnotationPresent(Form.class)) {
            return new FormWork(baseUrl, client,responseBodyConvert).invoke(method, args);
        } else if (method.isAnnotationPresent(MultiPart.class)) {
            return new MultiPartWork(baseUrl, client,responseBodyConvert).invoke(method, args);
        } else if (method.isAnnotationPresent(Json.class)) {
            return new JsonWork(baseUrl, client,responseBodyConvert).invoke(method, args);
        } else {
            throw new RuntimeException("you need to add annotation ( @Form || @MultiPart || @Json ) to declare the quest Type");
        }
    }


    public static class Builder {
        private String baseUrl = "";
        private OkHttpClient client;
        private IResponseBodyConvert responseBodyConvert;

        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder baseUrl(String url) {
            this.baseUrl = url;
            return this;
        }

        public Builder responseBodyConvert(IResponseBodyConvert convert) {
            this.responseBodyConvert = convert;
            return this;
        }

        public HttpProxy build() {
            return new HttpProxy(this);
        }
    }


}
