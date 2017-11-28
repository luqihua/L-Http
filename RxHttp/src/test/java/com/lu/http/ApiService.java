package com.lu.http;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Author: luqihua
 * Time: 2017/11/15
 * Description: ApiService
 */

public interface ApiService {
    Observable<Result<Map<String, String>>> login(@Param("name") String name, @Param("password") String passwors);
}
