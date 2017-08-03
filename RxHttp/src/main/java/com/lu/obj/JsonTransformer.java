package com.lu.obj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Author: luqihua
 * Time: 2017/8/3
 * Description: JsonTransformer
 */

public class JsonTransformer implements ObservableTransformer<String,String[]>{

    private String[] jsonKeys;

    public JsonTransformer(String[] jsonKeys) {
        this.jsonKeys = jsonKeys;
    }

    @Override
    public ObservableSource<String[]> apply(@NonNull Observable<String> upstream) {
        return upstream.flatMap(new Function<String, ObservableSource<String[]>>() {
            @Override
            public ObservableSource<String[]> apply(@NonNull String s) throws Exception {
                JSONObject object = JSON.parseObject(s);
                int length = jsonKeys.length;
                String[] results = new String[length];
                for (int i = 0; i < length; i++) {
                    results[i] = object.getString(jsonKeys[i]);
                }
                return Observable.just(results);
            }
        });
    }
}
