package com.lu.http.util;

import android.os.Looper;

import com.lu.http.Interface.IProgressListener;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Author: luqihua
 * Time: 2017/5/31
 * Description: DownloadBody
 */

public class DownloadBody extends ResponseBody {

    private final ResponseBody responseBody;
    private UIHandler mHandler;
    private BufferedSource bufferedSource;

    public DownloadBody(ResponseBody responseBody, IProgressListener listener) {
        this.responseBody = responseBody;
        this.mHandler = new UIHandler(Looper.getMainLooper(), listener);
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(getSource(responseBody.source()));
        }
        return bufferedSource;
    }


    private Source getSource(Source source) {
        return new ForwardingSource(source) {
            long totalByteRead = 0L;
            long contentLength = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                long byteRead = super.read(sink, byteCount);

                totalByteRead += byteRead < 0 ? 0 : byteRead;

                int progress = (int) (totalByteRead * 100f / contentLength);
                mHandler.sendEmptyMessage(progress);

                return byteRead;
            }
        };
    }
}
