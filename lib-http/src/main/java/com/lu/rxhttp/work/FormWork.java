package com.lu.rxhttp.work;

import com.lu.http.annotation.Param;
import com.lu.http.annotation.ParamMap;
import com.lu.rxhttp.Interface.IResponseBodyConvert;
import com.lu.rxhttp.annotation.GET;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.obj.HttpHeaderMap;
import com.lu.rxhttp.request.FormRequest;
import com.lu.rxhttp.util.Const;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: FormWork
 */

public class FormWork extends AWork {

    public FormWork(String baseUrl, IResponseBodyConvert convert) {
        super(baseUrl, convert);
    }

    @Override
    public Object invoke(final Method method, Object[] args) {
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
        Annotation[] annotations = checkoutParameter(method);
        int len = annotations.length;
        //参数map
        Map<String, String> params = new HashMap<>();
        //请求头map
        HttpHeaderMap headers = getHttpHeaderMap(annotations, args);

        for (int i = 0; i < len; i++) {
            Annotation annotation = annotations[0];
            if (annotation instanceof Param) {
                params.put(((Param) annotation).value(), (String) args[i]);
            } else if (annotation instanceof ParamMap) {
                params.putAll((Map<? extends String, ? extends String>) args[i]);
            }
        }

        //返回值类型
        final Type returnType = getReturnType(method);

        //构建请求执行访问操作
        return new FormRequest()
                .url(url)
                .method(md)
                .headers(headers)
                .params(params)
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
