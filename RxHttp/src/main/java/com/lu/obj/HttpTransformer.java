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
 * Time: 2017/6/21
 * Description: HttpTransfom
 */

public class HttpTransformer implements ObservableTransformer<String, Result> {

    public static final HttpTransformer DEFAULT_TRANSFORMER = new HttpTransformer("code", "msg", "data", 0);

    private int sSuccessCode = 0;
    private String sCodeKey = "code";
    private String sMsgKey = "msg";
    private String sDataKey = "data";


    public HttpTransformer(String codeKey, String msgKey, String dataKey, int successCode) {
        sCodeKey = codeKey;
        sMsgKey = msgKey;
        sDataKey = dataKey;
        sSuccessCode = successCode;
    }

    @Override
    public ObservableSource<Result> apply(@NonNull Observable<String> upstream) {
        return upstream.map(new Function<String, Result>() {
            @Override
            public Result apply(@NonNull String s) throws Exception {
                Result result = new Result();
                JSONObject object = JSON.parseObject(s);

                result.code = object.getIntValue(sCodeKey);
                result.msg = object.getString(sMsgKey);
                result.data = object.getString(sDataKey);

                //the success code define by API server
                if (result.code == sSuccessCode) {
                    return result;
                }
                //if code != the success code , deal it as a custom exception onError();
                throw new CustomException(result.code, result.msg);
            }
        });
    }
}
