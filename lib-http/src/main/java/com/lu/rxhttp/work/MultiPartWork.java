package com.lu.rxhttp.work;

import com.lu.rxhttp.Interface.IProgressListener;
import com.lu.rxhttp.Interface.IResponseBodyConvert;
import com.lu.rxhttp.annotation.Field;
import com.lu.rxhttp.annotation.FieldMap;
import com.lu.rxhttp.annotation.FilePart;
import com.lu.rxhttp.annotation.FilePartMap;
import com.lu.rxhttp.annotation.MultiBody;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.annotation.ProgressListener;
import com.lu.rxhttp.obj.HttpHeaderMap;
import com.lu.rxhttp.request.MultiPartRequest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: MultiPartWork
 */

public class MultiPartWork extends AWork {


    public MultiPartWork(String baseUrl, IResponseBodyConvert convert) {
        super(baseUrl, convert);
    }

    @Override
    public Object invoke(final Method method, Object[] args) {
        if (!method.isAnnotationPresent(POST.class)) {
            throw new RuntimeException("method need add  @POST(\"url\") ");
        }
        //请求地址
        String url = method.getAnnotation(POST.class).value();
        //请求方式
        if (!url.startsWith("http")) {
            url = baseUrl + url;
        }

        Annotation[] annotations = checkoutParameter(method);
        int len = annotations.length;

        Map<String, String> params = new LinkedHashMap<>();
        Map<String, File> fileData = new LinkedHashMap<>();
        MultipartBody body = null;
        IProgressListener callBack = null;

        for (int i = 0; i < len; i++) {
            Annotation annotation = annotations[i];
            if (annotation instanceof Field) {
                params.put(((Field) annotation).value(), (String) args[i]);
            } else if (annotation instanceof FieldMap) {
                params.putAll((Map<? extends String, ? extends String>) args[i]);
            } else if (annotation instanceof FilePart) {
                fileData.put(((FilePart) annotation).value(), (File) args[i]);
            } else if (annotation instanceof FilePartMap) {
                fileData.putAll((Map<? extends String, ? extends File>) args[i]);
            } else if (annotation instanceof ProgressListener) {
                callBack = (IProgressListener) args[i];
            } else if (annotation instanceof MultiBody) {
                if (args[i] instanceof MultipartBody) {
                    body = (MultipartBody) args[i];
                } else {
                    throw new RuntimeException("@MultiPart just can be annotation at MultipartBody parameter");
                }
            }
        }

        final HttpHeaderMap headerMap = getHttpHeaderMap(annotations, args);
        final Type returnType = getReturnType(method);

        return new MultiPartRequest()
                .url(url)
                .headers(headerMap)
                .params(params)
                .multipartBody(body)
                .files(fileData)
                .progress(callBack)
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
