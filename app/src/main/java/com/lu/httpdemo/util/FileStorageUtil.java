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
    private String rootDir = "tmp";
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
            hasInitialize = true;
            this.rootDir = context.getPackageName();
            this.mAppCacheDir = context.getCacheDir().getAbsolutePath();
        }
    }

    /**
     * app external adCard root file path
     *
     * @return
     */
    public File getAppRootFile() {

        if (!hasInitialize) {
            throw new RuntimeException("please invoke FileStorageUtil.getInstance().init(context) first");
        }
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), rootDir);
        } else {
            if (!TextUtils.isEmpty(mAppCacheDir)) {
                file = new File(mAppCacheDir, rootDir);
            }
        }

        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getFileByName(String name) {
        return new File(getAppRootFile(), name);
    }

}
