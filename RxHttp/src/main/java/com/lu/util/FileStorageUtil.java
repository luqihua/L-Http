package com.lu.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Author: luqihua
 * Time: 2017/5/26
 * Description: FileStorageUtil
 */

public class FileStorageUtil {

    private Context mContext;
    private String rootDir = "tmp";


    private FileStorageUtil() {
    }

    private static class Holder {
        private static FileStorageUtil sInstance = new FileStorageUtil();
    }

    public static FileStorageUtil getInstance() {
        return Holder.sInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.rootDir = mContext.getPackageName();
    }

    /**
     * app external adCard root file path
     *
     * @return
     */
    public File getAppRootFile() {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile(), rootDir);
        } else {
            if (mContext == null) {
                throw new RuntimeException("please init FileStorageUtil first");
            } else {
                file = new File(mContext.getCacheDir().getAbsoluteFile(), rootDir);
            }
        }

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getFileByName(String name) {
        return new File(getAppRootFile(), name);
    }

    public File getFileCacheByUrl(String url) {

        String fileName = HttpUtil.digest(url);

        File file = new File(getAppRootFile(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
