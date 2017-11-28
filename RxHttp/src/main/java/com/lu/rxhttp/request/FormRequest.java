package com.lu.rxhttp.request;

import com.lu.rxhttp.util.Const;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: form post
 */

public class FormRequest extends AbstractRequest<FormRequest> {

    protected Map<String, String> mParams;

    public FormRequest() {
        this.obj = this;
    }

    public FormRequest params(@NonNull Map<String, String> params) {
        this.mParams = params;
        return this;
    }

    @Override
    protected Request createRequest() {
        Request.Builder builder = newRequestBuilder();
        /*添加请求参数*/
        if (mParams.size() > 0) {
            if (mMethod.equals(Const.GET)) {
                HttpUrl httpUrl = HttpUrl.parse(mUrl);
                if (httpUrl == null) {
                    throw new RuntimeException("incorrect url");
                }
                HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
                for (String key : mParams.keySet()) {
                    urlBuilder.addQueryParameter(key, mParams.get(key));
                }
                builder.url(urlBuilder.build().toString());
            } else {
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (String key : mParams.keySet()) {
                    formBuilder.add(key, mParams.get(key));
                }
                builder.post(formBuilder.build());
            }
        }
        return builder.build();
    }
}
