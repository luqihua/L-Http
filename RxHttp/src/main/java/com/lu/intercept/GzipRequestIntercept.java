package com.lu.intercept;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * Author: luqihua
 * Time: 2017/6/8
 * Description: 压缩请求实体的拦截器
 */

public class GzipRequestIntercept implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();

        String encode = originalRequest.header("Content-Encoding");

        if (originalRequest.body() == null || encode == null || !encode.equals("gzip")) {
            return chain.proceed(originalRequest);
        }

        Request gzipRequest = originalRequest.newBuilder()
                .method(originalRequest.method(), new GzipBody(originalRequest.body()))
                .build();

        return chain.proceed(gzipRequest);
    }

    private class GzipBody extends RequestBody {

        private RequestBody body;

        public GzipBody(RequestBody body) {
            this.body = body;
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return body.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return body.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
            body.writeTo(gzipSink);
            gzipSink.close();
        }
    }

}
