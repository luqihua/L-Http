package com.lu.httpdemo.app;

import android.app.Application;

import com.lu.http.OkClient;
import com.lu.httpdemo.util.FileStorageUtil;

import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/6/19
 * Description: BaseApp
 */

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileStorageUtil.getInstance().init(this);
        OkClient.getInstance().init(new OkHttpClient());
    }
}
