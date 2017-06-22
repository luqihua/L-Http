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
        onAfter();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onAfter();
        if (e instanceof HttpException) {
            onHttpError((HttpException) e);
        } else if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;
            onCustomError(customException.getCode(), customException.getMessage());
        } else {
            throw new RuntimeException(e);
        }
    }

    /**
     * http exception like 400 404 500
     *
     * @param e
     */
    protected void onHttpError(HttpException e) {
        e.printStackTrace();
    }

    /**
     * custom exception like -1
     *
     * @param code
     * @param msg
     */
    protected void onCustomError(int code, String msg) {
    }

    /**
     * after request done
     */
    protected void onAfter() {

    }

    /**
     * response success
     * @param data
     * @param msg
     */
    protected abstract void onSuccess(String data, String msg);
}
