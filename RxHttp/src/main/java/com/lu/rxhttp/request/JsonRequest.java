package com.lu.rxhttp.request;

import com.google.gson.Gson;
import com.lu.rxhttp.util.Const;

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
        this.mMethod = Const.POST;
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
        this.mJsonBody = new Gson().toJson(body);
        return this;
    }

    @Override
    protected Request createRequest() {
        Request.Builder builder = new Request.Builder();
        builder.url(mUrl);
        builder.addHeader("Content-Type", "application/json");

        if (mJsonBody == null) {
            mJsonBody =  new Gson().toJson(mJsonBodyMap);
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), mJsonBody);

        builder.post(body);

        return builder.build();
    }
}
