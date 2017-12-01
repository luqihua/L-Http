package com.lu.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: luqihua
 * Time: 2017/11/30
 * Description: POST
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)//作用在方法上
public @interface POST {
    String value() default "";
}
