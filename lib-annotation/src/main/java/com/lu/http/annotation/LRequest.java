package com.lu.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: luqihua
 * @Time: 2018/6/26
 * @Description: LRequest
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface LRequest {
    String value();

    LMethod method() default LMethod.POST;

    ContextType type() default ContextType.FORM;
}
