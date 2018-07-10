package com.lu.http.processor;

import com.squareup.javapoet.ClassName;

/**
 * @Author: luqihua
 * @Time: 2018/6/8
 * @Description: Constants
 */

public class Constants {
    public static final ClassName HTTP_REQUEST = ClassName.get("com.lu.http", "HttpRequest");
    public static final String IMPL_CLASS_SUFFIX = "Impl";//生成接口实现类的后缀

    public static final String REQUEST_METHOD_GET = "get";
    public static final String REQUEST_METHOD_POST = "post";
    public static final String REQUEST_METHOD_JSON = "postJson";
    public static final String REQUEST_METHOD_POST_MULTIPART = "postMultipart";
}
