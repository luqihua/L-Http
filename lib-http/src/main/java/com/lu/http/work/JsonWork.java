package com.lu.http.work;

import com.lu.http.Interface.IResponseBodyConvert;
import com.lu.http.annotation.Body;
import com.lu.http.annotation.LMethod;
import com.lu.http.request.JsonRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: JsonWork
 */

public class JsonWork extends AbstractWork {

    private Object requestBody;

    public JsonWork(String baseUrl, IResponseBodyConvert convert) {
        super(baseUrl, convert);
    }

    @Override
    void parseParameter(Annotation annotation, Object arg) {
        if (annotation instanceof Body) {
            requestBody = arg;
        }
    }

    @Override
    Object work(String url, Enum<LMethod> method, Map<String, String> headers, Map<String, String> params, final Type returnType) {
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
