package com.lu.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: luqihua
 * @Time: 2018/6/7
 * @Description: UrlEncoded
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface UrlEncoded {
    EncodeType value() default EncodeType.FORM;

    enum EncodeType {
        FORM, MULTIPART,JSON
    }
}
