package com.lu.rxhttp.work;


import com.google.gson.Gson;
import com.lu.rxhttp.Interface.IResponseBodyConvert;

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
    IResponseBodyConvert responseBodyConvert;
    Gson gson = new Gson();

    public AWork(String baseUrl, OkHttpClient client, IResponseBodyConvert convert) {
        this.baseUrl = baseUrl;
        this.client = client;
        this.responseBodyConvert = convert;
    }

    /**
     * 检验参数是否合法（每个形参必须带有注解标记）
     *
     * @param method
     * @return
     */
    public Annotation[] checkoutParameter(Method method) {
        Annotation[][] annSS = method.getParameterAnnotations();
        int len = annSS.length;

        Annotation[] annotations = new Annotation[len];

        for (int i = 0; i < len; i++) {
            if (annSS[i].length < 1) {
                throw new RuntimeException("every parameter need one annotation");
            }
            //每个参数只有第一个注解生效，为了保证参数类型的正确性
            annotations[i] = annSS[i][0];
        }

        return annotations;
    }


    /**
     * 解析返回值类型
     *
     * @param method
     * @return
     */
    public Type getReturnType(Method method) {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType().equals(Observable.class)) {
                type = pType.getActualTypeArguments()[0];
            }
        }
        return type;
    }

    abstract Object invoke(final Method method, final Object[] args);
}
