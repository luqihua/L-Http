package com.lu.rxhttp.request;

import com.lu.rxhttp.Interface.IExecute;
import com.lu.rxhttp.Interface.IRequest;
import com.lu.rxhttp.OkClient;
import com.lu.rxhttp.obj.HttpException;
import com.lu.rxhttp.obj.HttpHeaderMap;
import com.lu.rxhttp.util.Const;
import com.lu.rxhttp.util.HttpTool;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/6/11
 * Description: AbstractRequest
 */

public abstract class AbstractRequest<T extends AbstractRequest> implements IRequest<T>, IExecute {

    T thisInstance;//代表当前类对象

    private String mUrl;
    private String mMethod = Const.POST;
    private Object mTag;

    private HttpHeaderMap mHeaders = new HttpHeaderMap();
    private Map<String, String> mParams = new HashMap<>();

    @Override
    public T url(String url) {
        this.mUrl = url;
        return thisInstance;
    }

    @Override
    public T method(String method) {
        this.mMethod = method;
        return thisInstance;
    }

    @Override
    public T tag(Object tag) {
        this.mTag = tag;
        return thisInstance;
    }

    @Override
    public T params(Map<String, String> params) {
        this.mParams.putAll(params);
        return thisInstance;
    }

    @Override
    public T param(String key, String value) {
        this.mParams.put(key, value);
        return thisInstance;
    }

    @Override
    public T headers(Map<String, String> headers) {
        this.mHeaders.putAll(headers);
        return thisInstance;
    }

    @Override
    public Call newCall() {

        Request.Builder builder = new Request.Builder().url(mUrl);
        /*add tag  default tag is the url*/
        mTag = mTag == null ? mUrl : mTag;
        builder.tag(HttpTool.formatRequestTag(mTag));

        /*add headers*/
        for (String key : mHeaders.keySet()) {
            builder.addHeader(key, mHeaders.get(key));
        }

        //get请求
        if (mMethod.equalsIgnoreCase(Const.GET)) {
            HttpUrl httpUrl = HttpUrl.parse(mUrl);
            if (httpUrl == null) {
                throw new RuntimeException("AbstractRequest: incorrect url");
            }
            HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
            for (String key : mParams.keySet()) {
                urlBuilder.addQueryParameter(key, mParams.get(key));
            }
            builder.url(urlBuilder.build()).get();
        } else {  //post请求
            builder.post(createRequestBody(mParams));
        }
        return getClient().newCall(builder.build());
    }

    @Override
    public OkHttpClient getClient() {
        return OkClient.getInstance().mClient;
    }

    @Override
    public Observable<ResponseBody> observerResponseBody() {

        return Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ResponseBody> emitter) throws Exception {
                try {
                    Call call = newCall();
                    //execute
                    Response response = call.execute();

                    if (response.isSuccessful()) {
                        emitter.onNext(response.body());
                    } else {
                        emitter.onError(HttpException.newInstance(response.code()));
                    }
                } catch (Exception e) {
                    emitter.onError(new HttpException(-1, e.getMessage()));
                } finally {
                    emitter.onComplete();
                }
            }
        });
    }

    protected abstract RequestBody createRequestBody(Map<String, String> params);

}
