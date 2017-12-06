package com.lu.rxhttp;

import android.support.annotation.Nullable;

import com.lu.rxhttp.Interface.IResponseBodyConvert;
import com.lu.rxhttp.work.HttpDispatcher;

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

    private HttpDispatcher mDispatcher;

    private HttpProxy(Builder builder) {
        this.mDispatcher = new HttpDispatcher(builder.baseUrl, builder.client, builder.responseBodyConvert);
    }

    public <T> T create(final Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader()
                , new Class[]{service}
                , new InvocationHandler() {
                    public Object invoke(Object proxy, final Method method, @Nullable Object[] args) throws Throwable {
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        return mDispatcher.dispatch(method, args);
                    }
                });
    }


    public static class Builder {
        private OkHttpClient client;
        private String baseUrl = "";
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
