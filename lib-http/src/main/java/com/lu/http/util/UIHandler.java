package com.lu.http.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lu.http.Interface.IProgressListener;

import java.lang.ref.WeakReference;

/**
 * Author: luqihua
 * Time: 2017/6/7
 * Description: UIHandler
 */

public class UIHandler extends Handler {
    private WeakReference<IProgressListener> weakReference;

    public UIHandler(Looper looper, IProgressListener callBack) {
        super(looper);
        this.weakReference = new WeakReference<>(callBack);
    }

    @Override
    public void handleMessage(Message msg) {
        IProgressListener callBack = weakReference.get();
        if (callBack != null) {
            callBack.onProgressChange(msg.what);
        }
    }
}
