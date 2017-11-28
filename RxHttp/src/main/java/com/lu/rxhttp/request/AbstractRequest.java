package com.lu.rxhttp.request;

import android.text.TextUtils;

import com.lu.rxhttp.Interface.IExecute;
import com.lu.rxhttp.Interface.IRequest;
import com.lu.rxhttp.RxHttp;
import com.lu.rxhttp.intercept.CacheIntercept;
import com.lu.rxhttp.intercept.LogInterceptor;
import com.lu.rxhttp.obj.CustomResponse;
import com.lu.rxhttp.obj.HttpException;
import com.lu.rxhttp.obj.HttpHeader;
import com.lu.rxhttp.util.Const;

import java.io.InputStream;

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

    HttpHeader mHeaders;

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

        if (mUrl == null || mUrl.length() == 0 || !mUrl.startsWith("http")) {
            throw new RuntimeException("incorrect http url");
        }

        Request.Builder builder = createRequest().newBuilder();

         /*add headers*/
        if (mHeaders != null) {
            for (String key : mHeaders.keySet()) {
                builder.addHeader(key, mHeaders.get(key));
            }
        }
        /*add tag  default tag is the url*/
        mTag = mTag == null ? mUrl : mTag;
        builder.tag(mTag);

        /*add Cache-Control*/
        if (mCacheControl != null) {
            builder.cacheControl(mCacheControl);
        }
        return builder.build();
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
                CustomResponse customResponse = execute();
                if (customResponse.isSuccess) {
                    emitter.onNext(customResponse.response);
                } else {
                    emitter.onError(customResponse.exception);
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

    @Override
    public Observable<InputStream> observerStream() {
        return observerResponse().map(new Function<Response, InputStream>() {
            @Override
            public InputStream apply(@NonNull Response response) throws Exception {
                return response.body().byteStream();
            }
        });
    }

    @Override
    public CustomResponse execute() {
        CustomResponse customResponse = new CustomResponse();
        Request request = getRequest();
        String tag = (String) request.tag();
        try {
            Call call = getClient().newCall(request);

            if (tag != null) {
                RxHttp.addCall(tag, call);
            }
            //execute
            Response response = call.execute();

            if (response.isSuccessful()) {
                customResponse.isSuccess = true;
                customResponse.response = response;
            } else {
                customResponse.exception = HttpException.newInstance(response.code());
            }
        } catch (Exception e) {
            customResponse.exception = new HttpException(-1, e.toString());
        } finally {
            RxHttp.cancelCall(tag);
        }
        return customResponse;
    }

    protected abstract Request createRequest();

}
