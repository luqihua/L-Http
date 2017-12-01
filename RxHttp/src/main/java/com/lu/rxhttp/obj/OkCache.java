package com.lu.rxhttp.obj;

import android.content.Context;
import android.os.Environment;

import com.lu.rxhttp.util.Const;

import java.io.File;

import okhttp3.Cache;

/**
 * Author: luqihua
 * Time: 2017/8/3
 * Description: OkCache
 */

public class OkCache {

    private Context mContext;
    private String mCacheDirs;
    private long mCacheSize = Const.MAX_CACHE_SIZE;

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

        if (mCacheDirs != null) {
            file = new File(mCacheDirs);
            if (file != null) {
                return new Cache(file, mCacheSize);
            }
        }

        if (mContext != null) {
            file = new File(mContext.getExternalCacheDir(), Const.HTTP_CACHE_DIR);
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

}
