package com.lu.request;

import android.text.TextUtils;

import com.lu.Interface.ProgressCallBack;
import com.lu.obj.HttpMethod;
import com.lu.util.DownloadBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/6/7
 * Description: DownLoadRequest
 */

public class DownLoadRequest extends AbstractRequest<DownLoadRequest> {

    private File file;
    protected Map<String, String> mParams;
    private ProgressCallBack mProgressCallback;

    public DownLoadRequest() {
        this.obj = this;
    }

    public DownLoadRequest addParam(String key, String value) {
        if (mParams == null) {
            mParams = new HashMap<>();
        }
        mParams.put(key, value);
        return this;
    }


    public DownLoadRequest params(@NonNull Map<String, String> params) {
        this.mParams = params;
        return this;
    }

    public DownLoadRequest progress(ProgressCallBack callback) {
        this.mProgressCallback = callback;
        return this;
    }

    public DownLoadRequest targetFilePath(String path) {
        if (!TextUtils.isEmpty(path)) {
            this.file = new File(path);
        }
        return this;
    }

    public DownLoadRequest targetFile(final File file) {
        this.file = file;
        return this;
    }

    @Override
    protected Request createRequest() {

        Request.Builder builder = new Request.Builder()
                .url(mUrl);
        /*添加请求参数*/
        if (mParams != null && mParams.size() > 0) {
            //get请求
            if (mMethod.equals(HttpMethod.GET)) {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(mUrl)
                        .newBuilder();
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }

                builder.url(urlBuilder.build().toString());

            } else {//post请求
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    formBuilder.add(entry.getKey(), entry.getValue());
                }
                builder.post(formBuilder.build());
            }
        }

        return builder.build();
    }


    /**
     * @return target File path
     */
    public Observable<String> observerString() {
        return observerResponse()
                .map(new Function<Response, String>() {
                    @Override
                    public String apply(@NonNull Response response) throws Exception {
                        InputStream in = null;
                        OutputStream out = null;
                        try {
                            ResponseBody responseBody = response.body();

                            //packing ResponseBody
                            if (mProgressCallback != null) {
                                responseBody = new DownloadBody(responseBody, mProgressCallback);
                            }

                            in = responseBody.byteStream();
                            out = new FileOutputStream(file);

                            byte[] buffer = new byte[10 * 1024];
                            int len;
                            while ((len = in.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                            }
                            out.flush();
                        } finally {
                            if (out != null) {
                                in.close();
                            }
                            if (out != null) {
                                out.close();
                            }
                        }
                        return file.getAbsolutePath();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
