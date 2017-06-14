package lu.okhttpdemo.app;

import android.app.Application;

import com.lu.RxHttp;
import com.lu.obj.CookieJarImp;
import com.lu.util.Const;
import com.lu.util.FileStorageUtil;
import com.lu.util.HttpsFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;


/**
 * Author: luqihua
 * Time: 2017/5/27
 * Description: BaseApplication
 */

public class BaseApplication extends Application {
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
                    .readTimeout(10, TimeUnit.SECONDS)
                    .cache(new Cache(FileStorageUtil.getInstance().getOkHttpCacheFile(), Const.MAX_CACHE_SIZE))
                    .cookieJar(new CookieJarImp(this))
                    .sslSocketFactory(HttpsFactory.getSSLSocketFactory(getAssets().open("srca.cer")), HttpsFactory.sTrustManager)
//                    .addInterceptor(new GzipRequestIntercept())
//                    .addInterceptor(new GzipResponseIntercept())
            ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        RxHttp.init(builder.build());
    }
}
