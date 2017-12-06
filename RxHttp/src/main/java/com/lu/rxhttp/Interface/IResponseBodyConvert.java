package com.lu.rxhttp.Interface;

import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/12/6
 * Description: IRequestBodyConvert
 */

public interface IResponseBodyConvert {
    <T> T convert(ResponseBody responseBody);
}
