package com.lu.rxhttp.request;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: form post
 */

public class FormRequest extends AbstractRequest<FormRequest> {

    public FormRequest() {
        this.obj = this;
    }
    @Override
    protected RequestBody createRequestBody() {
        FormBody.Builder formBuilder = new FormBody.Builder();
        /*添加请求参数*/
        if (mParams.size() > 0) {
            for (String key : mParams.keySet()) {
                formBuilder.add(key, mParams.get(key));
            }
        }
        return formBuilder.build();
    }
}
