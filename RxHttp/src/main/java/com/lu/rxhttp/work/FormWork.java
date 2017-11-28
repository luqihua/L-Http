package com.lu.rxhttp.work;

import com.lu.rxhttp.annotation.Field;
import com.lu.rxhttp.annotation.FieldMap;
import com.lu.rxhttp.annotation.GET;
import com.lu.rxhttp.annotation.Header;
import com.lu.rxhttp.annotation.HeaderMap;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.obj.HttpHeader;
import com.lu.rxhttp.request.FormRequest;
import com.lu.rxhttp.util.Const;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: FormWork
 */

public class FormWork extends AWork {

    public FormWork(String baseUrl, OkHttpClient client) {
        super(baseUrl, client);
    }

    @Override
    Object invoke(final Method method, Object[] args) {
        //请求地址
        String url = null;
        //请求方式
        String md = null;
        if (method.isAnnotationPresent(GET.class)) {
            url = method.getAnnotation(GET.class).value();
            md = Const.GET;
        }

        if (url == null && method.isAnnotationPresent(POST.class)) {
            url = method.getAnnotation(POST.class).value();
            md = Const.POST;
        }

        if (url == null) {
            throw new RuntimeException("url is null");
        }

        if (!url.startsWith("http")) {
            url = baseUrl + url;
        }

        //解析参数
        Annotation[][] annotationSS = checkoutParameter(method, args);
        int len = annotationSS.length;
        //参数map
        Map<String, String> params = new HashMap<>();
        //请求头map
        HttpHeader headers = new HttpHeader();

        for (int i = 0; i < len; i++) {
            if (annotationSS[i].length != 1) {
                //每个参数只能有一个注解，为了保证参数类型的正确性
                throw new RuntimeException("evey parameter need one annotation");
            }

            Annotation annotation = annotationSS[i][0];

            if (annotation instanceof Field) {
                params.put(((Field) annotation).value(), (String) args[i]);
            } else if (annotation instanceof FieldMap) {
                params.putAll((Map<? extends String, ? extends String>) args[i]);
            } else if (annotation instanceof Header) {
                headers.put(((Header) annotation).value(), (String) args[i]);
            } else if (annotation instanceof HeaderMap) {
                headers.putAll((Map<? extends String, ? extends String>) args[i]);
            }
        }

        //构建请求执行访问操作
        return new FormRequest()
                .url(url)
                .method(md)
                .headers(headers)
                .client(client)
                .params(params)
                .observerString()
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) throws Exception {
                        return parseResult(s, method);
                    }
                });
    }
}
