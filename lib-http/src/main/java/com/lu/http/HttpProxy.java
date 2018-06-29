package com.lu.http;

import com.lu.http.Interface.IResponseBodyConvert;
import com.lu.http.annotation.ContextType;
import com.lu.http.annotation.LRequest;
import com.lu.http.work.FormWork;
import com.lu.http.work.JsonWork;
import com.lu.http.work.MultiPartWork;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author: luqihua
 * Time: 2017/11/15
 * Description: HttpProxy
 */

public class HttpProxy {

    private String baseUrl;
    private IResponseBodyConvert responseBodyConvert;

    private HttpProxy(Builder builder) {
        this.baseUrl = builder.baseUrl;
        if (baseUrl == null || !baseUrl.startsWith("http")) {
            throw new RuntimeException("incorrect base_url");
        }
        this.responseBodyConvert = builder.responseBodyConvert;
    }

    public <T> T create(final Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader()
                , new Class<?>[]{service}
                , new InvocationHandler() {
                    public Object invoke(Object proxy, final Method method, Object[] args) throws Throwable {
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        return dispatcher(method, args);
                    }
                });
    }


    private Object dispatcher(final Method method, Object[] args) {
        LRequest LRequest = method.getAnnotation(LRequest.class);
        if (LRequest == null) {
            throw new RuntimeException("you need to add annotation ( @LRequest  ) to the method");
        }
        //请求类型
        if (LRequest.type() == ContextType.JSON) {
            return new JsonWork(baseUrl, responseBodyConvert).invoke(LRequest, method, args);
        } else if (LRequest.type() == ContextType.MULTIPART) {
            return new MultiPartWork(baseUrl, responseBodyConvert).invoke(LRequest,method, args);
        } else {
            return new FormWork(baseUrl, responseBodyConvert).invoke(LRequest,method, args);
        }
    }


    public static class Builder {
        private String baseUrl = "";
        private IResponseBodyConvert responseBodyConvert;

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
