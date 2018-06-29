package com.lu.http.Interface;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/12/6
 * Description: IRequestBodyConvert
 */

public interface IResponseBodyConvert {
    <T> T convert(ResponseBody responseBody, Type type);
}
