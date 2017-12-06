package com.lu.http;


import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import io.reactivex.functions.Consumer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test2() {

        HttpProxy proxy = new HttpProxy();

        ApiService service = proxy.create(ApiService.class);

        service.login("luqihua", "123456")
                .subscribe(new Consumer<HttpResult<UserInfo>>() {
                    @Override
                    public void accept(HttpResult<UserInfo> userInfoHttpResult) throws Exception {
                        System.out.println("accept: " + userInfoHttpResult);
                    }
                });


//        service.getUserList("1")
//                .subscribe(new Consumer<List<UserInfo>>() {
//                    @Override
//                    public void accept(List<UserInfo> userInfos) throws Exception {
//                        System.out.println(userInfos);
//                    }
//                });

    }


    @Test
    public void test() {
        try {
            Hello hello = new Hello();

            Method method = hello.getClass().getDeclaredMethod("test", String.class, String.class);

            Annotation[][] annotations = method.getParameterAnnotations();
            System.out.println(annotations.length);

            for (Annotation[] annotation : annotations) {
                System.out.println("len:  "+annotation.length);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public static class Hello {
        public void test(@Field String hello, String name) {
        }
    }

}