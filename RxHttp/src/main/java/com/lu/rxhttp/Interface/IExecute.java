package com.lu.rxhttp.Interface;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/6/13
 * Description: IExecute
 */

public interface IExecute {

    Observable<ResponseBody> observerResponseBody();

    Observable<String> observerString();
}
