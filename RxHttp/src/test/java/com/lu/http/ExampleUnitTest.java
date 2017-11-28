package com.lu.http;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

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
        Hello hello = new Hello();

        Class cls = hello.getClass();

        Method method = cls.getDeclaredMethod("getObj");

        Type type = method.getGenericReturnType();

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType().equals(Call.class)) {

            }
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type type1 : types) {
                if (type1 instanceof WildcardType) {
                    System.out.println(((WildcardType) type1).getUpperBounds()[0]);
                } else {
                    System.out.println(type1.toString());
                }
            }
        } else {
            System.out.println(type);
        }


        List<String> list = new ArrayList<String>();

        Type type1 = list.getClass().getGenericSuperclass();
        if (type1 instanceof ParameterizedType) {
            System.out.println(((ParameterizedType) type1).getRawType());
        }

    }

    public static class Hello {
        public Observable<?> getObj() throws Exception {
            System.out.println("hello");
            return null;
        }
    }

}