package com.lu;

import android.content.Context;
import android.os.Environment;

import com.lu.obj.HttpHeader;
import com.lu.obj.HttpTransformer;
import com.lu.util.Const;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.annotations.NonNull;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/6/1
 * Description: RxHttp
 */

public class RxHttp {

    private static OkHttpClient sClient;
    private static HttpHeader sHeader = new HttpHeader();
    private static ConcurrentHashMap<String, List<Call>> sWorkList = new ConcurrentHashMap<>();
    private static HttpTransformer sHttpTransformer;

    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, OkHttpClient client) {
        init(context, client, null);
    }

    public static void init(Context context, OkHttpClient client, HttpHeader header) {
        if (context == null) {
            throw new RuntimeException("RxHttp.init() context must not be null");
        }

        sHeader = header;

        sClient = client;

        if (sClient == null) {
            sClient = new OkHttpClient();
        }

        if (sClient.cache() == null) {
            Cache cache = createCache(context);
            if (cache != null) {
                sClient = sClient.newBuilder().cache(cache).build();
            }
        }
    }


    public static void setHttpTransformer(HttpTransformer sHttpTransformer) {
        RxHttp.sHttpTransformer = sHttpTransformer;
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


    public static HttpTransformer getTransformer() {
        return sHttpTransformer;
    }

    /**
     * default cache config
     *
     * @param context
     * @return
     */
    private static Cache createCache(@NonNull Context context) {
        File file = null;
        if (context != null) {
            file = new File(context.getExternalCacheDir(), Const.HTTP_CACHE_DIR);
        } else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory(), Const.HTTP_CACHE_DIR);
            }
        }
        if (file != null) {
            return new Cache(file, Const.MAX_CACHE_SIZE);
        }
        return null;
    }

    /**
     * add a call
     *
     * @param tag
     * @param call
     */
    public static void addCall(String tag, Call call) {
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
            for (Call call : calls) {
                if (call.request().tag().equals(tag)) {
                    call.cancel();
                }
            }
            sWorkList.remove(tag);
        }
    }
}
