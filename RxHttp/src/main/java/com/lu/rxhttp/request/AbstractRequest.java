package com.lu.rxhttp.request;

import android.text.TextUtils;

import com.lu.rxhttp.Interface.IExecute;
import com.lu.rxhttp.Interface.IRequest;
import com.lu.rxhttp.RxHttp;
import com.lu.rxhttp.intercept.CacheIntercept;
import com.lu.rxhttp.intercept.LogInterceptor;
import com.lu.rxhttp.obj.HttpException;
import com.lu.rxhttp.obj.HttpHeader;
import com.lu.rxhttp.util.Const;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: luqihua
 * Time: 2017/6/11
 * Description: AbstractRequest
 */

public abstract class AbstractRequest<T extends AbstractRequest> implements IRequest<T>, IExecute {

    OkHttpClient mClient;

    String mUrl;

    String mMethod = Const.POST;//default post request

    HttpHeader mHeaders = new HttpHeader();

    Map<String, String> mParams = new HashMap<>();

    String mTag;

    boolean isLog;//print log or not

    CacheControl mCacheControl;//local cache rule

    String mForceCache;//change the header:Cache-Control of response

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
    public T headers(HttpHeader headers) {
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

    @Override
    public Request.Builder newRequestBuilder() {

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
        return builder;
    }

    @Override
    public OkHttpClient getClient() {
        OkHttpClient.Builder builder = mClient.newBuilder();
        if (isLog) {
            builder.addInterceptor(new LogInterceptor());
        }

        if (!TextUtils.isEmpty(mForceCache)) {
            builder.addNetworkInterceptor(new CacheIntercept(mForceCache));
        }

        return builder.build();
    }

    @Override
    public Observable<Response> observerResponse() {

        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> emitter) throws Exception {
                Request request = createRequest();
                String tag = (String) request.tag();
                try {
                    Call call = getClient().newCall(request);

                    if (tag != null) {
                        RxHttp.addCall(tag, call);
                    }
                    //execute
                    Response response = call.execute();

                    if (response.isSuccessful()) {
                        emitter.onNext(response);
                    } else {
                        emitter.onError(HttpException.newInstance(response.code()));
                    }
                } catch (Exception e) {
                    emitter.onError(new HttpException(-1, e.toString()));
                } finally {
                    RxHttp.cancelCall(tag);
                    emitter.onComplete();
                }
            }
        });
    }

    @Override
    public Observable<String> observerString() {
        return observerResponse()
                .map(new Function<Response, String>() {
                    @Override
                    public String apply(@NonNull Response response) throws Exception {
                        return response.body().string();
                    }
                });
    }

    protected abstract Request createRequest();

}
