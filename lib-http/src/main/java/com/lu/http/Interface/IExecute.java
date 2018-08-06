package com.lu.http.Interface;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/6/13
 * Description: IExecute
 */

public interface IExecute {

    Observable<ResponseBody> observerResponseBody();

    Call newCall();

    OkHttpClient getClient();
}
