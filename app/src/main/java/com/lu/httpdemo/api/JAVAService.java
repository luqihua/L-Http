package com.lu.httpdemo.api;

import com.lu.http.Interface.IProgressListener;
import com.lu.http.annotation.ApiService;
import com.lu.http.annotation.Body;
import com.lu.http.annotation.ContextType;
import com.lu.http.annotation.FileParam;
import com.lu.http.annotation.Header;
import com.lu.http.annotation.Param;
import com.lu.http.annotation.ParamMap;
import com.lu.http.annotation.ProgressListener;
import com.lu.http.annotation.LRequest;
import com.lu.httpdemo.bean.HttpResult;
import com.lu.httpdemo.bean.User;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @Author: luqihua
 * @Time: 2018/6/6
 * @Description: JavaService
 */
@ApiService
public interface JAVAService {

    @LRequest("http://120.79.233.108:8080/api/loginForm")
    Observable<HttpResult<User>> login(
            @Header("max-age") String hello,
            @Param("name") String name,
            @Param("password") String password);

    @LRequest("http://120.79.233.108:8080/api/loginForm")
    Observable<HttpResult<User>> register(@Param("name") String name,
                                          @Param("password") String password,
                                          @ParamMap Map<String, String> params);

    @LRequest(value = "http://120.79.233.108:8080/api/loginForm", type = ContextType.MULTIPART)
    Observable<HttpResult<User>> postFile(@Param("name") String name,
                                          @Param("password") String password,
                                          @FileParam("avatar") File avatar,
                                          @ProgressListener IProgressListener listener);

    @LRequest(value = "http://120.79.233.108:8080/api/loginForm", type = ContextType.JSON)
    Observable<HttpResult<User>> postJson(@Body Object body);
}

