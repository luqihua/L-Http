package com.lu.rxhttp.Interface;

import com.lu.rxhttp.obj.CustomResponse;

import java.io.InputStream;

import io.reactivex.Observable;
import okhttp3.Response;

/**
 * Author: luqihua
 * Time: 2017/6/13
 * Description: IExecute
 */

public interface IExecute {

    Observable<Response> observerResponse();

    Observable<String> observerString();

    Observable<InputStream> observerStream();

    CustomResponse execute();

}
