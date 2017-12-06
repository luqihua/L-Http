package com.lu.rxhttp.work;

import com.lu.rxhttp.Interface.IResponseBodyConvert;
import com.lu.rxhttp.annotation.Body;
import com.lu.rxhttp.annotation.Header;
import com.lu.rxhttp.annotation.HeaderMap;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.obj.HttpHeader;
import com.lu.rxhttp.request.JsonRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: JsonWork
 */

public class JsonWork extends AWork {


    public JsonWork(String baseUrl, OkHttpClient client, IResponseBodyConvert convert) {
        super(baseUrl, client, convert);
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
        Annotation[] annotations = checkoutParameter(method);
        int len = annotations.length;

        Object requestBody = null;
        HttpHeader headers = new HttpHeader();

        for (int i = 0; i < len; i++) {
            Annotation annotation = annotations[i];

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

        final Type returnType = getReturnType(method);

        return new JsonRequest()
                .url(url)
                .client(client)
                .log(true)
                .headers(headers)
                .addJsonBody(requestBody)
                .observerResponseBody()
                .map(new Function<ResponseBody, Object>() {
                    @Override
                    public Object apply(ResponseBody responseBody) throws Exception {
                        if (responseBodyConvert != null) {
                            return responseBodyConvert.convert(responseBody);
                        } else {
                            return gson.fromJson(responseBody.string(), returnType);
                        }
                    }
                });

    }
}
