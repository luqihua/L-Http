package lu.httpdemo.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import lu.httpdemo.bean.HttpResult;

/**
 * Author: luqihua
 * Time: 2017/6/19
 * Description: HttpCompress
 */

public class HttpCompress<T> implements ObservableTransformer<String, T> {
    @Override
    public ObservableSource<T> apply(@NonNull Observable<String> upstream) {
        return upstream
                .map(new Function<String, HttpResult<T>>() {
                    @Override
                    public HttpResult<T> apply(@NonNull String s) throws Exception {
                        return (HttpResult<T>) new Gson().fromJson(s, new TypeToken<HttpResult<T>>() {
                        }.getRawType());
                    }
                })
                .map(new Function<HttpResult<T>, T>() {
                    @Override
                    public T apply(@NonNull HttpResult<T> tHttpResult) throws Exception {
                        return tHttpResult.getData();
                    }
                });
    }
}
