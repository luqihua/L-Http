package com.lu;

import android.support.annotation.NonNull;

import com.lu.obj.HttpHeader;
import com.lu.obj.HttpTransformer;
import com.lu.util.Const;
import com.lu.util.HttpsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/6/1
 * Description: RxHttp
 */

public class RxHttp {

    private static OkHttpClient sClient;
    private static HttpHeader sHeader;
    private static HttpTransformer sHttpTransformer;
    private static ConcurrentHashMap<String, List<Call>> sWorkList = new ConcurrentHashMap<>();
    private static Executor sWorkingThreadPool;

    public static void init(OkHttpClient client, HttpHeader header, HttpTransformer transformer) {
        sClient = client;
        sHeader = header;
        sHttpTransformer = transformer;
    }

    public static void init(@NonNull HttpOptions options) {
        if (options == null) {
            options = new HttpOptions();
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(options.getConnectTimeOut(), TimeUnit.MILLISECONDS);
        builder.readTimeout(options.getReadTimeOut(), TimeUnit.MILLISECONDS);
        //设置缓存目录
        if (options.getCache() != null) {
            builder.cache(options.getCache());
        }
        builder.interceptors().addAll(options.getInterceptors());
        builder.networkInterceptors().addAll(options.getNetworkInterceptors());
        //持久化cookie
        if (options.getCookieJar() != null) {
            builder.cookieJar(options.getCookieJar());
        }
        //加入https证书
        if (options.getHttpsFactory() != null) {
            HttpsFactory factory = options.getHttpsFactory();
            builder.sslSocketFactory(factory.getSslSocketFactory(), factory.getX509TrustManager());
        }
        sClient = builder.build();
        sHeader = options.getPublicHeaders();
        sHttpTransformer = options.getHttpTransformer();
        sWorkingThreadPool = options.getWorkingThreadPool();
    }

    /**
     * @return HttpHeader
     */
    public static HttpHeader getPublicHeader() {
        if (sHeader == null) {
            sHeader = new HttpHeader();
        }
        return sHeader;
    }

    /**
     * @return OkHttpClient
     */
    public static OkHttpClient getClient() {
        if (sClient == null) {
            sClient = new OkHttpClient();
        }
        return sClient;
    }

    /**
     * @return HttpTransformer
     */
    public static HttpTransformer getTransformer() {
        if (sHttpTransformer == null) {
            sHttpTransformer = HttpTransformer.DEFAULT_TRANSFORMER;
        }
        return sHttpTransformer;
    }


    public static Executor getWorkingThreadPool() {
        if (sWorkingThreadPool == null) {
            sWorkingThreadPool = new ThreadPoolExecutor(
                    Const.MAX_CORE_THREAD_NUM,
                    Const.MAX_THREAD_NUM,
                    10, TimeUnit.SECONDS,
                    new LinkedBlockingDeque<Runnable>(),
                    new ThreadFactory() {
                        private AtomicInteger mCount = new AtomicInteger(1);

                        @Override
                        public Thread newThread(@NonNull Runnable r) {
                            return new Thread(r, "RxHttp-thread#" + mCount.getAndIncrement());
                        }
                    });
        }
        return sWorkingThreadPool;
    }

    /**
     * add a call
     *
     * @param tag
     * @param call
     */
    public static void addCall(String tag, Call call) {
        if (tag == null || call == null) return;
        if (sWorkList.containsKey(tag)) {
            sWorkList.get(tag).add(call);
        } else {
            List<Call> calls = new ArrayList<>();
            calls.add(call);
            sWorkList.put(tag, calls);
        }
    }

    /**
     * cancel a call
     *
     * @param tag
     */
    public static void cancelCall(String tag) {
        if (sWorkList.containsKey(tag)) {
            List<Call> calls = sWorkList.get(tag);
            if (calls != null) {
                for (Call call : calls) {
                    if (call.request().tag().equals(tag)) {
                        call.cancel();
                    }
                }
                sWorkList.remove(tag);
            }
        }
    }


    public static void cancelAllCall() {
        for (Map.Entry<String, List<Call>> entry : sWorkList.entrySet()) {
            List<Call> calls = entry.getValue();
            if (calls != null) {
                for (Call call : calls) {
                    call.cancel();
                }
            }
        }
        sWorkList.clear();
    }
}
