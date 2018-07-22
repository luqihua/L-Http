package com.lu.httpdemo.api;


import com.lu.http.Interface.IProgressListener;
import com.lu.http.annotation.Body;
import com.lu.http.annotation.FileParam;
import com.lu.http.annotation.LMethod;
import com.lu.http.annotation.LRequest;
import com.lu.http.annotation.Param;
import com.lu.http.annotation.ProgressListener;
import com.lu.httpdemo.bean.HttpResult;
import com.lu.httpdemo.bean.User;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: ApiService
 */
public interface PhpService {

    //简单的form请求
    @LRequest("login")
    Observable<HttpResult<User>> login(@Param("username") String username
            , @Param("password") String password);

    //get请求
    @LRequest(value = "login",method = LMethod.GET)
    Observable<HttpResult<User>> getLogin(@Param("username") String username
            , @Param("password") String password);

    //json实体请求
    @LRequest("loginJson")
    Observable<HttpResult<User>> loginJson(@Body User user);


    //文件上传
    @LRequest("upload")
    Observable<HttpResult<Map<String, String>>> upload(@Param("username") String username,
                                                       @Param("password") String password,
                                                       @FileParam("avatar") File image,
                                                       @ProgressListener IProgressListener callBack);
}
