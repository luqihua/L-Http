package com.lu.rxhttp.work;

import com.lu.rxhttp.annotation.Body;
import com.lu.rxhttp.annotation.Header;
import com.lu.rxhttp.annotation.HeaderMap;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.obj.HttpHeader;
import com.lu.rxhttp.request.JsonRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: JsonWork
 */

public class JsonWork extends AWork {

    public JsonWork(String baseUrl, OkHttpClient client) {
        super(baseUrl, client);
    }

    @Override
    Object invoke(final Method method, Object[] args) {
        if (!method.isAnnotationPresent(POST.class)) {
            throw new RuntimeException("method need add  @POST(\"url\") ");
        }
        //请求地址
        String url = method.getAnnotation(POST.class).value();
        //请求方式
        if (url == null) {
            throw new RuntimeException("url is null");
        }

        if (!url.startsWith("http")) {
            url = baseUrl + url;
        }

        //解析参数
        Annotation[][] annotationSS = checkoutParameter(method, args);
        int len = annotationSS.length;

        Object requestBody = null;
        HttpHeader headers = new HttpHeader();

        for (int i = 0; i < len; i++) {
            if (annotationSS[i].length != 1) {
                //每个参数只能有一个注解，为了保证参数类型的正确性
                throw new RuntimeException("evey parameter need one annotation");
            }

            Annotation annotation = annotationSS[i][0];

            if (annotation instanceof Header) {
                headers.put(((Header) annotation).value(), (String) args[i]);
            } else if (annotation instanceof HeaderMap) {
                headers.putAll((Map<? extends String, ? extends String>) args[i]);
            } else if (annotation instanceof Body) {
                if (requestBody == null) {
                    requestBody = args[i];
                } else {
                    throw new RuntimeException("@JsonRequest can only declare one @Body");
                }
            }
        }

        return new JsonRequest()
                .url(url)
                .client(client)
                .log(true)
                .headers(headers)
                .addJsonBody(requestBody)
                .observerString()
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) throws Exception {
                        return parseResult(s, method);
                    }
                });
    }
}
