package com.lu.httpdemo;

import com.lu.httpdemo.api.UserService;
import com.lu.httpdemo.api.UserServiceImpl;

/**
 * @Author: luqihua
 * @Time: 2018/6/26
 * @Description: HttpClientApt
 */
public class HttpClient {
    private static UserService sUserService;

    public static UserService getApiService() {
        if (sUserService == null) {
            sUserService = new UserServiceImpl("http://www.luqihua.cn/libhttp/");
        }
        return sUserService;
    }
}
