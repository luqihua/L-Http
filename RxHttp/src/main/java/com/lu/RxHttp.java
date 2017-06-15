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

    private static ConcurrentHashMap<String, List<Call>> sWorkList = new ConcurrentHashMap<>();

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
     * from post
     *
     * @return
     */
    public static FormRequest FormRequest() {
        return new FormRequest();
    }

    /**
     * single file upload
     *
     * @return
     */
    public static FileUpRequest UpFileRequest() {
        return new FileUpRequest();
    }


    /**
     * multi file upload
     *
     * @return
     */
    public static MultiFileUpRequest MultiFileRequest() {
        return new MultiFileUpRequest();
    }


    /**
     * multiPart post
     *
     * @return
     */
    public static MultiPartRequest MultiPartRequest() {
        return new MultiPartRequest();
    }


    /**
     * file download
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
    public static void addCall(String tag, Call call) {
        tag.getClass().getSimpleName();
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
    public static void cancelRequest(String tag) {
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
