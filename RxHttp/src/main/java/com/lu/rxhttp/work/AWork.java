package com.lu.rxhttp.work;


import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: AWork
 */

public abstract class AWork {

    String baseUrl;
    OkHttpClient client;
    Gson gson = new Gson();

    public AWork(String baseUrl, OkHttpClient client) {
        this.baseUrl = baseUrl;
        this.client = client;
    }

    //解析方法的返回值，去除 Observable<>层，取里面的泛型
    private Type parseReturnType(Method method) {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (types.length != 1) {
                throw new RuntimeException("return type error");
            }
        } else {
            throw new RuntimeException("return type error");
        }

        return ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    /**
     * 检验参数是否合法（每个形参必须带有注解标记）
     *
     * @param method
     * @param args
     * @return
     */
    public Annotation[][] checkoutParameter(Method method, Object[] args) {
        Annotation[][] annotationSS = method.getParameterAnnotations();
        int len = annotationSS.length;
        if (len != args.length) {
            throw new RuntimeException("some parameter do not have annotations");
        }
        return annotationSS;
    }

    /**
     * 解析返回结果，如果用户想要得到string  则不进行json转换
     *
     * @param method
     * @return
     */
    public Object parseResult(String jsonStr, Method method) {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType().equals(Observable.class)) {
                Type[] types = pType.getActualTypeArguments();
                return gson.fromJson(jsonStr, types[0]);
            } else {
                throw new RuntimeException("return type must an io.reactivex.Observable<Bean>");
            }
        } else {
            throw new RuntimeException("return type error");
        }
    }

    abstract Object invoke(final Method method, final Object[] args);
}
