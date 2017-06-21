package lu.httpdemo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import lu.httpdemo.bean.HttpResult;

/**
 * Author: luqihua
 * Time: 2017/6/19
 * Description: HttpFunc
 */

public class HttpFunc<T> implements Function<String, HttpResult<T>> {

    Class<T> tClass;

    public HttpFunc(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public HttpResult<T> apply(@NonNull String s) throws Exception {
        HttpResult<T> result = new HttpResult<>();

        JSONObject object = JSON.parseObject(s);

        int code = object.getIntValue("code");
        String msg = object.getString("msg");
        String data = object.getString("data");

        T t = JSON.parseObject(data, tClass);

        result.setCode(code);
        result.setMsg(msg);
        result.setData(t);

        return result;
    }
}
