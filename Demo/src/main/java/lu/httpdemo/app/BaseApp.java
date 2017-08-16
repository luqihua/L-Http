package lu.httpdemo.app;

import android.app.Application;

import com.lu.HttpOptions;
import com.lu.RxHttp;
import com.lu.obj.CookieJarImp;
import com.lu.obj.HttpHeader;
import com.lu.obj.HttpTransformer;
import com.lu.obj.OkCache;
import com.lu.util.FileStorageUtil;
import com.lu.util.HttpsFactory;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;

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
                    //拦截器
                    .networkInterceptors(new ArrayList<Interceptor>())
                    .interceptors(new ArrayList<Interceptor>())
                    //所有请求的公共头
                    .publicHeaders(new HttpHeader())
                    //添加缓存
                    .cache(new OkCache(this))
                    //解析http返回
                    .httpTransformer(new HttpTransformer("code", "msg", "data", 1))
                    //管理cookie持久化
                    .cookieJar(new CookieJarImp(this))
                    //自定义工作线程池
                    .workingThreadPool(new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>()))
                    //添加证书
                    .httpsFactory(new HttpsFactory(getAssets().open("srca12306.cer"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
