package com.lu.httpdemo;

import com.lu.httpdemo.api.PhpService;
import com.lu.httpdemo.api.PhpServiceImpl;

/**
 * @Author: luqihua
 * @Time: 2018/6/26
 * @Description: HttpClientApt
 */
public class HttpClientApt {
    private static PhpService sPhpService;

    public static PhpService getApiService() {
        if (sPhpService == null) {
            sPhpService = new PhpServiceImpl("http://119.23.237.24:8080/apidemo/api/");
        }
        return sPhpService;
    }
}
