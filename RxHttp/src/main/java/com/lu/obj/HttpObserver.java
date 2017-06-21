package com.lu.obj;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Author: luqihua
 * Time: 2017/6/21
 * Description: HttpObserver
 */

public abstract class HttpObserver implements Observer<Result> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(@NonNull Result result) {
        onSuccess(result.data, result.msg);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof HttpException) {
            //http exception like 400 404 500
            onHttpError((HttpException) e);
        } else if (e instanceof CustomException) {
            //custom exception like -1
            CustomException customException = (CustomException) e;
            onCustomError(customException.getCode(), customException.getMessage());
        } else {
            throw new RuntimeException(e);
        }
    }


    protected void onHttpError(HttpException e) {
        e.printStackTrace();
    }

    protected void onCustomError(int code, String msg) {
    }

    protected abstract void onSuccess(String data, String msg);
}
