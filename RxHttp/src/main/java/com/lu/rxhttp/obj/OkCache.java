package com.lu.rxhttp.obj;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import okhttp3.Cache;

/**
 * Author: luqihua
 * Time: 2017/8/3
 * Description: OkCache
 */

public class OkCache {

    private static final long DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;
    private static final String DEFAULT_CACHE_DIR = "okcache";


    private Context mContext;
    private String mCacheDirs;
    private long mCacheSize;


    public OkCache() {
    }

    public OkCache(Context context) {
        this.mContext = context;
    }

    public OkCache(Context context, String cacheDirs) {
        this.mContext = context;
        this.mCacheDirs = cacheDirs;
    }

    public OkCache(Context context, String cacheDirs, long cacheSize) {
        this.mContext = context;
        this.mCacheDirs = cacheDirs;
        this.mCacheSize = cacheSize;
    }

    /**
     * default cache config
     *
     * @return
     */
    public Cache createCache() {
        File file = null;

        if (mCacheDirs != null && mCacheDirs.length() > 0) {
            file = new File(mCacheDirs);
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), DEFAULT_CACHE_DIR);
        } else if (mContext != null) {
            file = new File(mContext.getExternalCacheDir(), DEFAULT_CACHE_DIR);
        }

        if (file == null)
            return null;

        if (!file.exists()) {
            file.mkdirs();
        }

        return new Cache(file, DEFAULT_CACHE_SIZE);
    }

}
