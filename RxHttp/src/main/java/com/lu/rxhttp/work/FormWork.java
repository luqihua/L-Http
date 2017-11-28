package com.lu.rxhttp.work;

import com.lu.rxhttp.annotation.Field;
import com.lu.rxhttp.annotation.FieldMap;
import com.lu.rxhttp.annotation.GET;
import com.lu.rxhttp.annotation.POST;
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
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < len; i++) {
            for (Annotation annotation : annotationSS[i]) {
                if (annotation instanceof Field) {
                    params.put(((Field) annotation).value(), (String) args[i]);
                } else if (annotation instanceof FieldMap) {
                    params.putAll((Map<? extends String, ? extends String>) args[i]);
                }
            }
        }

        //构建请求执行访问操作
        return new FormRequest()
                .url(url)
                .method(md)
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
