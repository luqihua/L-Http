package lu.httpdemo.app;

import android.app.Application;

import com.lu.HttpOptions;
import com.lu.RxHttp;
import com.lu.obj.CookieJarImp;
import com.lu.obj.HttpTransformer;
import com.lu.obj.OkCache;
import com.lu.util.FileStorageUtil;
import com.lu.util.HttpsFactory;

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
        try {
            RxHttp.init(new HttpOptions()
                    .connectTimeOut(10000)
                    .readTimeOut(10000)
                    .cache(new OkCache(this))
                    .httpTransformer(new HttpTransformer("code", "msg", "data", 1))
                    .cookieJar(new CookieJarImp(this))
                    .httpsFactory(new HttpsFactory(getAssets().open("srca12306.cer")))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
