package com.lu.http;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Author: luqihua
 * Time: 2017/11/15
 * Description: ProxyUtil
 */

public class ProxyUtil {
    public static <T> T create(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, final Method method, Object[] args) throws Throwable {
                Annotation[][] annotationSS = method.getParameterAnnotations();
                int len = annotationSS.length;
                if (len != args.length) {
                    throw new RuntimeException("error");
                }

                Map<String, String> params = new HashMap<>();
                for (int i = 0; i < len; i++) {
                    for (Annotation annotation : annotationSS[i]) {
                        if (annotation instanceof Param) {
                            params.put(((Param) annotation).value(), (String) args[i]);
                        }
                    }
                }
                String response = WorkStation.invoke(params);

                return Observable.just(response.toString())
                        .map(new Function<String, Object>() {
                            @Override
                            public Object apply(String s) throws Exception {
                                ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
                                Type type1 = type.getActualTypeArguments()[0];
                                Result<Map<String, String>> mapResult = new Gson().fromJson(s, type1);
                                return mapResult;
                            }
                        });
            }
        });
    }
}
