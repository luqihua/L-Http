package com.lu.rxhttp;

import com.lu.rxhttp.obj.OkCache;
import com.lu.rxhttp.util.HttpsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;

/**
 * Author: luqihua
 * Time: 2017/8/3
 * Description: Optons
 */

public class HttpOptions {
    private int mConnectTimeOut = 10000;
    private int mReadTimeOut = 10000;
    private OkCache mCache;
    private List<Interceptor> mNetworkInterceptors;
    private List<Interceptor> mInterceptors;
    private CookieJar mCookieJar;
    private HttpsFactory mHttpsFactory;
    private Executor mWorkingThreadPool;//工作线程池


    public HttpOptions connectTimeOut(int connectTimeOut) {
        this.mConnectTimeOut = connectTimeOut;
        return this;
    }

    public HttpOptions readTimeOut(int readTimeOut) {
        this.mReadTimeOut = readTimeOut;
        return this;
    }

    public HttpOptions cache(OkCache cache) {
        this.mCache = cache;
        return this;
    }

    public HttpOptions networkInterceptors(List<Interceptor> networkInterceptors) {
        this.mNetworkInterceptors = networkInterceptors;
        return this;
    }

    public HttpOptions interceptors(List<Interceptor> interceptors) {
        this.mInterceptors = interceptors;
        return this;
    }

    public HttpOptions cookieJar(CookieJar cookieJar) {
        mCookieJar = cookieJar;
        return this;
    }

    public HttpOptions httpsFactory(HttpsFactory factory) {
        this.mHttpsFactory = factory;
        return this;
    }

    public HttpOptions workingThreadPool(Executor threadPool) {
        this.mWorkingThreadPool = threadPool;
        return this;
    }

    /**/

    public int getConnectTimeOut() {
        return mConnectTimeOut;
    }

    public int getReadTimeOut() {
        return mReadTimeOut;
    }

    public Cache getCache() {
        return mCache.createCache();
    }

    public List<Interceptor> getNetworkInterceptors() {
        if (mNetworkInterceptors == null) {
            mNetworkInterceptors = new ArrayList<>();
        }
        return mNetworkInterceptors;
    }

    public List<Interceptor> getInterceptors() {
        if (mInterceptors == null) {
            mInterceptors = new ArrayList<>();
        }
        return mInterceptors;
    }

    public CookieJar getCookieJar() {
        return mCookieJar;
    }

    public HttpsFactory getHttpsFactory() {
        return mHttpsFactory;
    }

    public Executor getWorkingThreadPool() {
        return mWorkingThreadPool;
    }
}
