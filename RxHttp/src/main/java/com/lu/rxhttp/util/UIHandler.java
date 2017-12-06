package com.lu.rxhttp.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lu.rxhttp.Interface.IProgressCallBack;

import java.lang.ref.WeakReference;

/**
 * Author: luqihua
 * Time: 2017/6/7
 * Description: UIHandler
 */

public class UIHandler extends Handler {
    private WeakReference<IProgressCallBack> weakReference;

    public UIHandler(Looper looper, IProgressCallBack callBack) {
        super(looper);
        this.weakReference = new WeakReference<>(callBack);
    }

    @Override
    public void handleMessage(Message msg) {
        IProgressCallBack callBack = weakReference.get();
        if (callBack != null) {
            callBack.onProgressChange(msg.what);
        }
    }
}
