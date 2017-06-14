package com.lu.request;

import android.text.TextUtils;

import com.lu.RxHttp;
import com.lu.Interface.IExecute;
import com.lu.Interface.IRequest;
import com.lu.intercept.CacheIntercept;
import com.lu.intercept.LogInterceptor;
import com.lu.obj.HttpException;
import com.lu.obj.HttpHeader;
import com.lu.obj.HttpStatus;
import com.lu.util.Const;

import java.io.InputStream;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
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

public abstract class AbstractRequest<T> implements IRequest<T>, IExecute {

    protected String mUrl;

    protected String mMethod;

    protected HttpHeader mHeaders;

    protected Object mTag;

    protected boolean isLog = RxHttp.debug;//是否打印log

    protected CacheControl mCacheControl;//自定义缓存策略

    protected String mForceCache;//修改响应头的Cache-Control字段

    protected T obj;

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
    public T tag(Object tag) {
        this.mTag = tag;
        return obj;
    }

    @Override
    public T header(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new HttpHeader();
        }
        mHeaders.put(key, value);
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

    public T headers(HttpHeader header) {
        this.mHeaders = header;
        return obj;
    }


    @Override
    public Request getRequest() {

        Request.Builder builder = createRequest().newBuilder();

         /*添加请求头*/
        if (mHeaders != null) {
            for (Map.Entry<String, String> entry : mHeaders.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        /*添加tag*/
        if (mTag != null) {
            builder.tag(mTag);
        }

        /*添加缓存控制*/
        if (mCacheControl != null) {
            builder.cacheControl(mCacheControl);
        }
        return builder.build();
    }

    @Override
    public OkHttpClient getClient() {
        OkHttpClient.Builder builder = RxHttp.getClient().newBuilder();
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
                Request request = getRequest();
                Object tag = request.tag();
                try {
                    Call call = getClient().newCall(request);

                    if (tag != null) {
                        RxHttp.addCall(request.tag(), call);
                    }

                    Response response = call.execute();

                    if (response.isSuccessful()) {
                        emitter.onNext(response);
                        emitter.onComplete();
                    } else {
                        HttpStatus status = HttpStatus.creatErrorEntity(response.code());
                        HttpException exception = new HttpException(status.code, status.description);
                        emitter.onError(exception);
                    }

                } finally {
                    RxHttp.cancelRequest(tag);
                }
            }
        });
    }

    @Override
    public Observable<String> observerString() {
        return observerResponse().map(new Function<Response, String>() {
            @Override
            public String apply(@NonNull Response response) throws Exception {
                return response.body().string();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<InputStream> observerStream() {
        return observerResponse().map(new Function<Response, InputStream>() {
            @Override
            public InputStream apply(@NonNull Response response) throws Exception {
                return response.body().byteStream();
            }
        })
                .subscribeOn(Schedulers.io());
    }


    /*--抽象方法---*/


    protected abstract Request createRequest();

}
