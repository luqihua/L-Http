package com.lu.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 82172 on 2017/11/30.
 */
//作用时时运行时
@Retention(RetentionPolicy.RUNTIME)
//作用目标是方法参数
@Target(ElementType.PARAMETER)
public @interface Field {
    String value() default "";
}
