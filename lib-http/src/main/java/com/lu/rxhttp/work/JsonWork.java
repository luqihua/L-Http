package com.lu.rxhttp.work;

import com.lu.rxhttp.Interface.IResponseBodyConvert;
import com.lu.rxhttp.annotation.Body;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.obj.HttpHeaderMap;
import com.lu.rxhttp.request.JsonRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: JsonWork
 */

public class JsonWork extends AWork {


    public JsonWork(String baseUrl, IResponseBodyConvert convert) {
        super(baseUrl, convert);
    }

    @Override
    public Object invoke(final Method method, Object[] args) {
        if (!method.isAnnotationPresent(POST.class)) {
            throw new RuntimeException("@JsonRequest method need add  @POST(\"url\") ");
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

        HttpHeaderMap headers = getHttpHeaderMap(annotations, args);

        Object requestBody = null;

        for (int i = 0; i < len; i++) {
            Annotation annotation = annotations[i];
            if (annotation instanceof Body) {
                requestBody = args[i];
                break;
            } else {
                throw new RuntimeException("@JsonRequest can only declare one @Body");
            }
        }

        final Type returnType = getReturnType(method);

        return new JsonRequest()
                .url(url)
                .headers(headers)
                .addJsonBody(requestBody)
                .observerResponseBody()
                .map(new Function<ResponseBody, Object>() {
                    @Override
                    public Object apply(ResponseBody responseBody) throws Exception {
                        if (responseBodyConvert != null) {
                            return responseBodyConvert.convert(responseBody, returnType);
                        } else {
                            return gson.fromJson(responseBody.string(), returnType);
                        }
                    }
                });

    }
}
