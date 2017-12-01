package com.lu.rxhttp.Interface;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/11/30
 * Description: Convert
 */

public interface Convert<F, T> {
    F convert(T value);

    public static class ConvertFactory {
        public Convert<RequestBody, ?> requestConvert(Object o) {
            return null;
        }

        public Convert<?, ResponseBody> requestConvert(RequestBody o) {
            return null;
        }
    }

}
