package com.lu.rxhttp.work;

import com.lu.rxhttp.annotation.Body;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.request.JsonRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
        if (len != 1) {
            throw new RuntimeException("@Json method must has one parameter with annotation @Body");
        }

        Annotation[] annotations = annotationSS[0];
        Object params = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Body) {
                params = args[0];
                break;
            }
        }

        if (params == null) {
            throw new RuntimeException("@Json method can't has null parameter");
        }

        return new JsonRequest()
                .url(url)
                .client(client)
                .log(true)
                .addJsonBody(params)
                .observerString()
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) throws Exception {
                        return parseResult(s, method);
                    }
                });
    }
}
