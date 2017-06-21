package com.lu.request;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author: luqihua
 * Time: 2017/6/20
 * Description: JsonRequest
 */

public class JsonRequest extends AbstractRequest<JsonRequest> {

    private Map<String, Object> mJsonBodyMap = new HashMap<>();
    private String mJsonBody;

    public JsonRequest() {
        this.obj = this;
    }

    public JsonRequest addParam(String key, Object value) {
        if (mJsonBodyMap == null) {
            mJsonBodyMap = new HashMap<>();
        }
        mJsonBodyMap.put(key, value);

        return this;
    }


    public JsonRequest addJsonBody(String body) {
        this.mJsonBody = body;
        return this;
    }


    public JsonRequest addJsonBody(Object body) {
        this.mJsonBody = JSON.toJSONString(body);
        return this;
    }

    @Override
    protected Request createRequest() {
        Request.Builder builder = new Request.Builder();
        builder.url(mUrl);
        builder.addHeader("Content-Type", "application/json");

        if (mJsonBody == null) {
            mJsonBody = JSON.toJSONString(mJsonBodyMap);
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), mJsonBody);

        builder.post(body);

        return builder.build();
    }
}
