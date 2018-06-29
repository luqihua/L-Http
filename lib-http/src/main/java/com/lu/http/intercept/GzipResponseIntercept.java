package com.lu.http.intercept;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Author: luqihua
 * Time: 2017/6/8
 * Description: GzipResponseIntercept
 */

public class GzipResponseIntercept implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());

        String encode = originalResponse.header("Content-Encoding");

        if (originalResponse.body() == null || encode == null || !encode.equals("gzip")) {
            return originalResponse;
        }

        Response gzipResponse = originalResponse.newBuilder()
                .body(new GzipBody(originalResponse.body()))
                .build();

        return gzipResponse;
    }


    private class GzipBody extends ResponseBody {

        private ResponseBody body;

        public GzipBody(ResponseBody body) {
            this.body = body;
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return body.contentType();
        }

        @Override
        public long contentLength() {
            return body.contentLength();
        }

        @Override
        public BufferedSource source() {
            BufferedSource gzipSource = Okio.buffer(new GzipSource(body.source()));
            return gzipSource;
        }
    }

}
