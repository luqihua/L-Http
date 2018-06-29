package com.lu.http.work;

import com.lu.http.Interface.IProgressListener;
import com.lu.http.Interface.IResponseBodyConvert;
import com.lu.http.annotation.FileMap;
import com.lu.http.annotation.FileParam;
import com.lu.http.annotation.LMethod;
import com.lu.http.annotation.ProgressListener;
import com.lu.http.request.MultiPartRequest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: MultiPartWork
 */

public class MultiPartWork extends AbstractWork {

    private Map<String, File> mFileMap = new HashMap<>();
    private IProgressListener mProgressListener;

    public MultiPartWork(String baseUrl, IResponseBodyConvert convert) {
        super(baseUrl, convert);
    }

    @Override
    void parseParameter(Annotation annotation, Object arg) {
        if (annotation instanceof FileParam) {
            if (arg instanceof File) {
                mFileMap.put(((FileParam) annotation).value(), (File) arg);
            } else {
                throw new RuntimeException(TAG + ": @FileParam can be only used in File Parameter");
            }
        } else if (annotation instanceof FileMap) {
            if (!(arg instanceof Map)) {
                throw new RuntimeException(TAG + ": @FileMap can be only used in  Map<? extends String, File> Parameter");
            }
            mFileMap.putAll((Map<? extends String, ? extends File>) arg);
        } else if (annotation instanceof ProgressListener) {
            mProgressListener = (IProgressListener) arg;
        }
    }

    @Override
    Object work(String url, Enum<LMethod> method, Map<String, String> headers, Map<String, String> params, final Type returnType) {
        return new MultiPartRequest()
                .url(url)
                .headers(headers)
                .params(params)
                .files(mFileMap)
                .progress(mProgressListener)
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
