package com.lu.http.util;

import android.os.Looper;

import com.lu.http.Interface.IProgressListener;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Author: luqihua
 * Time: 2017/5/31
 * Description: UploadBody
 */

public class UploadBody extends RequestBody {

    private final RequestBody requestBody;
    private BufferedSink bufferedSink;
    public UIHandler mHandler;

    public UploadBody(RequestBody requestBody, IProgressListener listener) {
        this.requestBody = requestBody;
        mHandler = new UIHandler(Looper.getMainLooper(), listener);
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(getSink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }


    private Sink getSink(Sink sink) {
        return new ForwardingSink(sink) {

            private long byteWritten = 0L;
            private long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }

                //add written byte length
                byteWritten += byteCount;
                int progress = (int) (byteWritten * 100f / contentLength);
                mHandler.sendEmptyMessage(progress);

                //reset byteWritten when complete
                if (byteWritten == contentLength) {
                    byteWritten = 0;
                }
            }
        };
    }

}
