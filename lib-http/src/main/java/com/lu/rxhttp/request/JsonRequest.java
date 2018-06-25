package com.lu.rxhttp.request;

import com.google.gson.Gson;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author: luqihua
 * Time: 2017/6/20
 * Description: JsonRequest
 */

public class JsonRequest extends AbstractRequest<JsonRequest> {

    private String mJsonBody;

    public JsonRequest() {
        this.thisInstance = this;
    }

    public JsonRequest addJsonBody(Object body) {
        this.mJsonBody = new Gson().toJson(body);
        return this;
    }

    @Override
    protected RequestBody createRequestBody(Map<String, String> params) {
        if (mJsonBody == null) {
            mJsonBody = new Gson().toJson(params);
        }
        return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), mJsonBody);
    }
}
