package com.lu.request;

import com.lu.util.Const;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: 表单上传数据
 */

public class FormRequest extends AbstractRequest<FormRequest> {


    protected Map<String, String> mParams;

    public FormRequest() {
        this.obj = this;
    }


    public FormRequest addParam(String key, String value) {
        if (mParams == null) {
            mParams = new HashMap<>();
        }
        mParams.put(key, value);
        return this;
    }


    public FormRequest params(@NonNull Map<String, String> params) {
        this.mParams = params;
        return this;
    }


    @Override
    protected Request createRequest() {
        Request.Builder builder = new Request.Builder()
                .url(mUrl);
        /*添加请求参数*/
        if (mParams != null && mParams.size() > 0) {
            if (mMethod.equals(Const.GET)) {
                HttpUrl.Builder urlBuilder = HttpUrl
                        .parse(mUrl)
                        .newBuilder();
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }

                builder.url(urlBuilder.build().toString());

            } else {
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    formBuilder.add(entry.getKey(), entry.getValue());
                }

                builder.post(formBuilder.build());
            }
        }
        return builder.build();
    }
}
