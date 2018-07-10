package com.lu.http.obj;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;

/**
 * @Author: luqihua
 * @Time: 2018/6/21
 * @Description: OkConfigure
 */
public class OkConfigure {
    public long connectTimeOut;
    public boolean isLog;
    public Cache cache;
    public List<Interceptor> interceptors;
    public List<Interceptor> networkInterceptors = new ArrayList<>();

    private OkConfigure(Builder builder) {
        this.connectTimeOut = builder.connectTimeOut;
        this.isLog = builder.isLog;
        this.interceptors = builder.interceptors;
        this.cache = builder.okCache.createCache();
    }


    public static class Builder {
        long connectTimeOut;
        boolean isLog;
        OkCache okCache;
        List<Interceptor> interceptors = new ArrayList<>();
        List<Interceptor> networkInterceptors = new ArrayList<>();

        public Builder() {
        }

        public Builder connectTimeOut(long connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        public Builder isLog(boolean isLog) {
            this.isLog = isLog;
            return this;
        }

        public Builder cache(OkCache okCache) {
            this.okCache = okCache;
            return this;
        }

        public Builder interceptor(Interceptor interceptor) {
            this.interceptors.add(interceptor);
            return this;
        }

        public Builder networkInterceptor(Interceptor interceptor) {
            this.networkInterceptors.add(interceptor);
            return this;
        }

        public OkConfigure build() {
            return new OkConfigure(this);
        }
    }
}
