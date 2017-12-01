package com.lu.http;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 82172 on 2017/11/30.
 */
public class HttpProxy {
    public <T> T create(Class<T> c) {
        return (T) Proxy.newProxyInstance(
                c.getClassLoader()
                , new Class[]{c}
                , new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return parseRequest(proxy, method, args);
                    }
                }
        );
    }

    public Object parseRequest(Object proxy, Method method, Object[] args) {

        //该操作用于返回方法参数注解，由于方法参数可以有多个，而每个参数又可以有多个注解，所以返回的是一个二维数组。
        Annotation[][] annotationSS = method.getParameterAnnotations();

        //定义一个map用于接收入参key=value
        Map<String, String> params = new HashMap<>();
        int len = annotationSS.length;
        for (int i = 0; i < len; i++) {
            Annotation[] annotations = annotationSS[i];
            if (annotations.length != 1) {
                //这里我们限制每个参数只能有一个注解，这是为了保证key和value的一一对应
                throw new RuntimeException("每个参数只能有一个注解");
            }
            Annotation att = annotations[0];
            if (att instanceof com.lu.http.Field) {
                //将第i个参数的注解值作为key  参数值作为value放入map中
                params.put(((Field) att).value(), (String) args[i]);
            }
        }
        //解析返回值,
        Type resultType = method.getGenericReturnType();
        /**
         * 1.如果方法定义的参数返回值不带泛型参数，则这个type是返回值类型的Class对象
         *  例: String get();这个方法返回的Type是 String.class类
         *
         *  2.如果方法返回值带泛型参数，则返回Type属于 ParameterizedType 如果希望json解析的对象是泛型类型，需要进一步拿到泛型的Type
         *  例: Observable<T> get(); 这个方法需要进一步拿到T类型进行json转换
         */
        if (resultType instanceof ParameterizedType) {
            /**
             * 1.如果定义返回结果是Observable<T>类型，只有一个泛型，因此这个type[]长度是1，
             *2.如果定义的是形如Map<String,String>类型，有2个泛型， 则返回的Type[]数组长度为2
             */
            Type[] types = ((ParameterizedType) resultType).getActualTypeArguments();
            //得到Observable<T>中T的类型
            resultType = types[0];
        }


        //解析请求url和方法
        String url = "";//请求url
        String md = "POST";//请求方法，默认给POST

        if (method.isAnnotationPresent(POST.class)) {
            url = method.getAnnotation(POST.class).value();
            //打印看下
            System.out.println("请求地址: " + url);
            md = "POST";
        }

        //添加请求参数体
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            bodyBuilder.add(key, params.get(key));
        }
        //构建请求
        final Request request = new Request.Builder()
                .url(url)
                .method(md, bodyBuilder.build())
                .build();

        OkHttpClient client = new OkHttpClient();


        final Call call = client.newCall(request);
        final Type type = resultType;//返回结果定义成final类型才能在rxjava中使用


        //parseRequest的返回值从这一步开始变成我们需要的Observable<T>
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    //解析结果
                    emitter.onNext(parseResult(response.body(), type));
                } else {
                    // TODO: 异常处理 先简单处理
                    emitter.onError(new Exception("http error"));
                }
                emitter.onComplete();
            }
        });
    }


    /**
     * 将结果解析成方法定义的返回值类型
     *
     * @param responseBody
     * @param resultType
     * @return
     * @throws IOException
     */
    public Object parseResult(ResponseBody responseBody, Type resultType) throws IOException {
        String data = responseBody.string();
        //将服务器返回的字符串解析成我们需要的bean对象
        return new Gson().fromJson(data, resultType);
    }
}
