package com.lu.http.work;

import com.lu.http.Interface.IResponseBodyConvert;
import com.lu.http.annotation.LMethod;
import com.lu.http.request.FormRequest;

import java.lang.reflect.Type;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: FormWork
 */

public class FormWork extends AbstractWork {

    public FormWork(String baseUrl, IResponseBodyConvert convert) {
        super(baseUrl, convert);
    }

    @Override
    Object work(String url, Enum<LMethod> method, Map<String, String> headers, Map<String, String> params, final Type returnType) {
        return new FormRequest()
                .url(url)
                .method(method)
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
