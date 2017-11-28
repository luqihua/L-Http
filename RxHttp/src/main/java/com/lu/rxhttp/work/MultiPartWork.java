package com.lu.rxhttp.work;

import com.lu.rxhttp.Interface.ProgressCallBack;
import com.lu.rxhttp.annotation.Field;
import com.lu.rxhttp.annotation.FilePart;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.annotation.ProgressListener;
import com.lu.rxhttp.request.MultiPartRequest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: MultiPartWork
 */

public class MultiPartWork extends AWork {

    public MultiPartWork(String baseUrl, OkHttpClient client) {
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
        if (!url.startsWith("http")) {
            url = baseUrl + url;
        }

        Annotation[][] annotationSS = checkoutParameter(method, args);

        Map<String, String> params = new LinkedHashMap<>();
        Map<String, File> fileData = new LinkedHashMap<>();
        ProgressCallBack callBack = null;


        int len = annotationSS.length;

        for (int i = 0; i < len; i++) {
            for (Annotation annotation : annotationSS[i]) {
                if (annotation instanceof Field) {
                    params.put(((Field) annotation).value(), (String) args[i]);
                } else if (annotation instanceof FilePart) {
                    fileData.put(((FilePart) annotation).value(), (File) args[i]);
                } else if (annotation instanceof ProgressListener) {
                    callBack = (ProgressCallBack) args[i];
                }
            }
        }


        return new MultiPartRequest()
                .url(url)
                .client(client)
                .params(params)
                .files(fileData)
                .progress(callBack)
                .observerString()
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) throws Exception {
                        return parseResult(s, method);
                    }
                });
    }
}
