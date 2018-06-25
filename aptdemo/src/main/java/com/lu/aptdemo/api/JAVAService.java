package com.lu.aptdemo.api;

import com.lu.aptdemo.bean.HttpResult;
import com.lu.aptdemo.bean.UserInfo;
import com.lu.http.annotation.Header;
import com.lu.http.annotation.POST;
import com.lu.http.annotation.Param;
import com.lu.http.annotation.ParamMap;
import com.lu.http.annotation.UrlEncoded;

import java.util.Map;

import io.reactivex.Observable;

/**
 * @Author: luqihua
 * @Time: 2018/6/6
 * @Description: JavaService
 */

public interface JAVAService {

    @UrlEncoded
    @POST("http://120.79.233.108:8080/api/loginForm")
    Observable<HttpResult<UserInfo>> login(@Param("name") String name,
                                           @Param("password") String password);

    @UrlEncoded
    @POST("http://120.79.233.108:8080/api/loginForm")
    Observable<HttpResult<UserInfo>> register(@Param("name") String name,
                                              @Param("password") String password,
                                              @ParamMap Map<String, String> params);


    @UrlEncoded(UrlEncoded.EncodeType.MULTIPART)
    @POST("http://120.79.233.108:8080/api/loginForm")
    Observable<HttpResult<UserInfo>> postFile(@Param("name") String name,
                                              @Param("password") String password);

    @UrlEncoded(UrlEncoded.EncodeType.JSON)
    @POST("http://120.79.233.108:8080/api/loginForm")
    Observable<HttpResult<UserInfo>> postJson(
            @Header("max-age") String hello,
            @Param("name") String name,
            @Param("password") String password);
}

