package com.lu.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lu.Interface.ProgressCallBack;

import java.lang.ref.WeakReference;

/**
 * Author: luqihua
 * Time: 2017/6/7
 * Description: UIHandler
 */

public class UIHandler extends Handler {
    private WeakReference<ProgressCallBack> weakReference;

    public UIHandler(Looper looper, ProgressCallBack callBack) {
        super(looper);
        this.weakReference = new WeakReference<>(callBack);
    }

    @Override
    public void handleMessage(Message msg) {
        ProgressCallBack callBack = weakReference.get();
        if (callBack != null) {
            callBack.onProgressChange(msg.what);
        }
    }
}
