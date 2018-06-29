package com.lu.http.work;


import com.google.gson.Gson;
import com.lu.http.Interface.IResponseBodyConvert;
import com.lu.http.annotation.LMethod;
import com.lu.http.annotation.Header;
import com.lu.http.annotation.Param;
import com.lu.http.annotation.ParamMap;
import com.lu.http.annotation.LRequest;
import com.lu.http.obj.HttpHeaderMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: AWork
 */

public abstract class AbstractWork {
    final String TAG;
    private String baseUrl;
    IResponseBodyConvert responseBodyConvert;
    Gson gson = new Gson();

    AbstractWork(String baseUrl, IResponseBodyConvert convert) {
        this.TAG = this.getClass().getSimpleName();
        this.baseUrl = baseUrl;
        this.responseBodyConvert = convert;
    }

    /**
     * 检验参数是否合法（每个形参必须带有注解标记）
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

    /**
     * 处理方法生成http请求所需要的参数
     */
    public Object invoke(LRequest LRequest, final Method method, final Object[] args) {
        //请求地址
        String url = LRequest.value();
        //请求方式
        final Enum<LMethod> md = LRequest.method();

        if (url == null || url.length() == 0) {
            throw new RuntimeException("url is null");
        }

        //不是http开头说明需要拼接baseUrl
        if (!url.startsWith("http")) {
            url = baseUrl + url;
        }

        final Map<String, String> headers = new HttpHeaderMap();
        final Map<String, String> params = new HashMap<>();
        final Type returnType = getReturnType(method);


        //解析参数
        Annotation[] annotations = checkoutParameter(method);
        int len = annotations.length;
        for (int i = 0; i < len; i++) {
            Annotation annotation = annotations[i];
            if (annotation instanceof Param) {
                params.put(((Param) annotation).value(), (String) args[i]);
            } else if (annotation instanceof ParamMap) {
                params.putAll((Map<? extends String, ? extends String>) args[i]);
            } else if (annotation instanceof Header) {
                headers.put(((Header) annotation).value(), (String) args[i]);
            } else {
                parseParameter(annotation, args[i]);
            }
        }

        return work(url, md, headers, params, returnType);
    }

    void parseParameter(Annotation annotation, Object arg) {

    }

    abstract Object work(final String url,
                         final Enum<LMethod> method,
                         final Map<String, String> headers,
                         final Map<String, String> params,
                         final Type returnType);
}
