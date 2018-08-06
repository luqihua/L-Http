package com.lu.httpdemo.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Author: luqihua
 * Time: 2017/5/26
 * Description: FileStorageUtil
 */

public class FileStorageUtil {

    private String mAppCacheDir = "";
    private boolean hasInitialize = false;

    private FileStorageUtil() {
    }

    private static class Holder {
        private static FileStorageUtil sInstance = new FileStorageUtil();
    }

    public static FileStorageUtil getInstance() {
        return Holder.sInstance;
    }

    public void init(Context context) {
        if (!hasInitialize) {
            if (context == null) {
                throw new RuntimeException("FileStorageUtil: context must not be null");
            }
            hasInitialize = true;
            final String rootDir = context.getPackageName();
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                this.mAppCacheDir = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" + rootDir;
            } else {
                this.mAppCacheDir = context.getCacheDir().getAbsolutePath();
            }
        }
    }

    public File getFileByName(String name) {
        return new File(mAppCacheDir, name);
    }
}
