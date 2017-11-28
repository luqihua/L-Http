package lu.httpdemo;

import com.lu.rxhttp.HttpProxy;

import lu.httpdemo.module.ApiService;
import okhttp3.OkHttpClient;

/**
 * Author: luqihua
 * Time: 2017/11/24
 * Description: HttpClient
 */

public class HttpClient {
    private static ApiService sApiService;

    public static ApiService getApiService() {
        if (sApiService == null) {
            synchronized (HttpClient.class) {
                if (sApiService == null) {
                    HttpProxy proxy = new HttpProxy.Builder()
                            .baseUrl("http://119.23.237.24:8080/apidemo/api/")
                            .client(new OkHttpClient())
                            .build();
                    sApiService = proxy.create(ApiService.class);
                }
            }
        }
        return sApiService;
    }
}
