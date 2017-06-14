package com.lu.util;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: luqihua
 * Time: 2017/6/9
 * Description: Const
 */

public class Const {
    public static final int MAX_CACHE_SIZE = 10 * 1024 * 1024;

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String HEAD = "HEAD";
    public static final String PUT = "PUT";

    @StringDef({GET, POST, HEAD, PUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HttpMethod {
    }



}
