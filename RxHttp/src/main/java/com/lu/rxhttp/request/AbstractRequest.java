package com.lu.rxhttp.request;

import android.text.TextUtils;

import com.lu.rxhttp.Interface.IExecute;
import com.lu.rxhttp.Interface.IRequest;
import com.lu.rxhttp.intercept.CacheIntercept;
import com.lu.rxhttp.intercept.LogInterceptor;
import com.lu.rxhttp.obj.HttpException;
import com.lu.rxhttp.obj.HttpHeaderMap;
import com.lu.rxhttp.util.Const;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.CacheControl;
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

    protected OkHttpClient mClient;
    protected String mUrl;
    protected String mMethod = Const.POST;//default post request
    protected HttpHeaderMap mHeaders = new HttpHeaderMap();
    protected Map<String, String> mParams = new HashMap<>();
    protected String mTag;
    protected boolean isLog;//print log or not
    protected CacheControl mCacheControl;//local cache rule
    protected String mForceCache;//change the header:Cache-Control of response

    protected T obj;

    public T client(@NonNull OkHttpClient client) {
        this.mClient = client;
        return obj;
    }

    @Override
    public T url(String url) {
        this.mUrl = url;
        return obj;
    }

    @Override
    public T method(@Const.HttpMethod String method) {
        this.mMethod = method;
        return obj;
    }

    @Override
    public T tag(String tag) {
        this.mTag = tag;
        return obj;
    }

    @Override
    public T params(Map<String, String> params) {
        this.mParams.putAll(params);
        return obj;
    }

    @Override
    public T headers(HttpHeaderMap headers) {
        this.mHeaders.putAll(headers);
        return obj;
    }

    @Override
    public T log(boolean log) {
        this.isLog = log;
        return obj;
    }

    @Override
    public T cacheControl(CacheControl control) {
        this.mCacheControl = control;
        return obj;
    }

    @Override
    public T forceCache(String forceCache) {
        this.mForceCache = forceCache;
        return obj;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public Call newCall() {

        Request.Builder builder = new Request.Builder().url(mUrl);
         /*add headers*/
        for (String key : mHeaders.keySet()) {
            builder.addHeader(key, mHeaders.get(key));
        }
        /*add tag  default tag is the url*/
        mTag = mTag == null ? mUrl : mTag;
        builder.tag(mTag);

        /*add Cache-Control*/
        if (mCacheControl != null) {
            builder.cacheControl(mCacheControl);
        }

        //get请求
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
        } else {  //post请求
            builder.post(createRequestBody());
        }
        return getClient().newCall(builder.build());
    }

    @Override
    public OkHttpClient getClient() {
        OkHttpClient.Builder builder;
        if (mClient != null) {
            builder = mClient.newBuilder();
        } else {
            builder = new OkHttpClient.Builder();
        }
        if (isLog) {
            builder.addInterceptor(new LogInterceptor());
        }

        if (!TextUtils.isEmpty(mForceCache)) {
            builder.addNetworkInterceptor(new CacheIntercept(mForceCache));
        }

        return builder.build();
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

    protected abstract RequestBody createRequestBody();

}
