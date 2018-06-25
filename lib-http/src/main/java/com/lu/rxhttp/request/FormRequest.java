package com.lu.rxhttp.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: form post
 */

public class FormRequest extends AbstractRequest<FormRequest> {

    public FormRequest() {
        this.thisInstance = this;
    }

    @Override
    protected RequestBody createRequestBody(Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        /*添加请求参数*/
        for (String key : params.keySet()) {
            formBuilder.add(key, params.get(key));
        }
        return formBuilder.build();
    }
}
