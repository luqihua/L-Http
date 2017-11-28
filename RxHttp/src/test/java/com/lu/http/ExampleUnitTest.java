package com.lu.http;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test1() throws Exception {
        assertEquals(4, 2 + 2);
        ApiService service = ProxyUtil.create(ApiService.class);
        service.login("luqihua", "123456")
                .subscribe(new Observer<Result<Map<String, String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<Map<String, String>> mapResult) {
                        System.out.println(mapResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Test
    public void test2() throws Exception {
        Class cls = Hello.class;
        Method method = cls.getDeclaredMethod("getObj", String.class);

        Annotation[][] annotations = method.getParameterAnnotations();
        int len = annotations.length;
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            for (Annotation annotation : annotations[i]) {
                System.out.println(annotation.toString());
            }
        }
    }

    public static class Hello {
        public Observable<?> getObj(@Param("key") @Header("header") String value) throws Exception {
            System.out.println("hello");
            return null;
        }
    }

}