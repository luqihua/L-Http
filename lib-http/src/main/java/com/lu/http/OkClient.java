package com.lu.http;

import com.lu.http.intercept.LogInterceptor;
import com.lu.http.obj.OkConfigure;
import com.lu.http.util.HttpTool;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2018/6/7
 * Description: HttpCore
 */

public class OkClient {
    public OkHttpClient mClient = new OkHttpClient();

    private OkClient() {

    }

    private static class Holder {
        private static OkClient sInstance = new OkClient();
    }

    public static OkClient getInstance() {
        return Holder.sInstance;
    }

    public void init(OkHttpClient okHttpClient) {
        if (okHttpClient == null) return;
        this.mClient = okHttpClient;
    }

    public void init(OkConfigure configure) {
        if (configure == null) return;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(configure.connectTimeOut, TimeUnit.SECONDS);
        if (configure.cache != null) {
            builder.cache(configure.cache);
        }
        if (configure.isLog) {
            builder.addInterceptor(new LogInterceptor());
        }
        builder.interceptors().addAll(configure.interceptors);
        builder.networkInterceptors().addAll(configure.networkInterceptors);
        mClient = builder.build();
    }


    /**
     * 取消一个请求
     *
     * @param tag
     */
    public void cancelRequest(Object tag) {

      tag = HttpTool.formatRequestTag(tag);
      List<Call> queuedCalls = mClient.dispatcher().queuedCalls();
      for (Call call : queuedCalls) {
          if (call.request().tag().equals(tag)) {
              call.cancel();
          }
      }

      List<Call> runningCalls = mClient.dispatcher().runningCalls();
      for (Call call : runningCalls) {
          if (call.request().tag().equals(tag)) {
              call.cancel();
          }
        }
    }

    /**
     * 取消所有的请求
     */
    public void cancelAllRequest() {
        mClient.dispatcher().cancelAll();
    }
}
