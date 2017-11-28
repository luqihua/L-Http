package com.lu.rxhttp.intercept;

import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/6/9
 * Description: LogInterceptor
 */

public class LogInterceptor implements Interceptor {

    private static final String TAG = "OkHttp";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        StringBuilder builder = new StringBuilder();

        builder.append("==========request==========\n")
                .append(request.method() + "-->" + request.url() + "\n")
                .append("header: \n")
                .append("[\n" + request.headers() + "]\n");

        if (request.body() != null) {
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();

                int size = body.size();

                for (int i = 0; i < size; i++) {
                    builder.append(body.encodedName(i) + "=" + body.encodedValue(i) + "\n");
                }
            }
        }

        long t1 = System.currentTimeMillis();

        Response response = chain.proceed(request);

        long t2 = System.currentTimeMillis();

        builder.append("==========response{" + (t2 - t1) + "ms}==========\n")
                .append("CODE: " + response.code() + "\n")
                .append("header:\n")
                .append("[\n" + response.headers() + "]\n")
                .append("\n==========body==========\n");

        MediaType type = response.body().contentType();

        String content = null;

        //just print text and json
        if (type == null || type.type().contains("text") || type.type().contains("json")) {

            content = response.body().string();

            builder.append(content);
        }

        builder.append("\n===========END===========\n");

        Log.d(TAG, builder.toString());

        if (content == null) {
            return response;
        } else {
            return response.newBuilder()
                    .body(ResponseBody.create(type, content.toString()))
                    .build();
        }
    }
}
