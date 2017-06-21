package lu.httpdemo.app;

import android.app.Application;

import com.lu.RxHttp;
import com.lu.obj.HttpTransformer;
import com.lu.util.FileStorageUtil;
import com.lu.util.HttpsFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

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
        initHttp();

    }

    private void initHttp() {
        FileStorageUtil.getInstance().init(this);
        OkHttpClient.Builder builder = null;
        try {
            builder = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS);

            //https证书添加 以12306为例子
            SSLSocketFactory sslSocketFactory = HttpsFactory.getSSLSocketFactory(getAssets().open("srca12306.cer"));
            if (sslSocketFactory != null) {
                builder.sslSocketFactory(sslSocketFactory, HttpsFactory.sTrustManager);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        RxHttp.init(this, builder.build());
        RxHttp.setHttpTransformer(new HttpTransformer("code", "msg", "data", 1));
    }
}
