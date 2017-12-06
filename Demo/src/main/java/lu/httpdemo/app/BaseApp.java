package lu.httpdemo.app;

import android.app.Application;

import lu.httpdemo.util.FileStorageUtil;

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
    }
}
