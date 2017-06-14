package com.lu;

import com.lu.intercept.GzipRequestIntercept;
import com.lu.intercept.GzipResponseIntercept;
import com.lu.request.DownLoadRequest;
import com.lu.request.FormRequest;
import com.lu.request.FileUpRequest;
import com.lu.request.MultiFileUpRequest;
import com.lu.request.MultiPartRequest;
import com.lu.util.Const;
import com.lu.util.FileStorageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/6/1
 * Description: RxHttp
 */

public class RxHttp {

    public static boolean debug = false;
    private static OkHttpClient sClient;

    private static ConcurrentHashMap<Object, List<Call>> sWorkList = new ConcurrentHashMap<>();

    public static void init(OkHttpClient client) {
        sClient = client == null ? getDefault() : client;
    }


    public static void setDebug(boolean debug) {
        RxHttp.debug = debug;
    }

    private static OkHttpClient getDefault() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .cache(new Cache(FileStorageUtil.getInstance().getOkHttpCacheFile(), Const.MAX_CACHE_SIZE))
                .addInterceptor(new GzipRequestIntercept())
                .addInterceptor(new GzipResponseIntercept());

        return builder.build();
    }

    public static OkHttpClient getClient() {
        if (sClient == null) {
            sClient = getDefault();
        }
        return sClient;
    }

    /**
     * 表单请求
     *
     * @return
     */
    public static FormRequest FormRequest() {
        return new FormRequest();
    }

    /**
     * 单文件上传请求
     *
     * @return
     */
    public static FileUpRequest UpFileRequest() {
        return new FileUpRequest();
    }


    /**
     * 多个文件依次上传请求
     *
     * @return
     */
    public static MultiFileUpRequest MultiFileRequest() {
        return new MultiFileUpRequest();
    }


    /**
     * 多个文件同时上传请求
     *
     * @return
     */
    public static MultiPartRequest MultiPartRequest() {
        return new MultiPartRequest();
    }


    /**
     * 文件下载
     *
     * @return
     */
    public static DownLoadRequest DownFileRequest() {
        return new DownLoadRequest();
    }


    /**
     * 加入一个请求
     *
     * @param tag
     * @param call
     */
    public static void addCall(Object tag, Call call) {
        if (sWorkList.containsKey(tag)) {
            sWorkList.get(tag).add(call);
        } else {
            List<Call> calls = new ArrayList<>();
            calls.add(call);
            sWorkList.put(tag, calls);
        }
    }


    /**
     * 取消一个请求
     *
     * @param tag
     */
    public static void cancelRequest(Object tag) {
        if (sWorkList.containsKey(tag)) {
            List<Call> calls = sWorkList.get(tag);
            for (Call call : calls) {
                if (call.request().tag().equals(tag)) {
                    call.cancel();
                }
            }
            sWorkList.remove(tag);

        }
    }

}
