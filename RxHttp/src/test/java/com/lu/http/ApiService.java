package com.lu.http;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by 82172 on 2017/11/30.
 */
public interface ApiService {
    @POST("http://119.23.237.24:8080/apidemo/api/login")
    Observable<HttpResult<UserInfo>> login(@Field("username") String username, @Field("password") String password);


    //添加一个方法用于根据groupId获取用户列表
    @POST("http://localhost:8080/api/getUserList")
    Observable<List<UserInfo>> getUserList(@Field("groupId") String groupId);

}
