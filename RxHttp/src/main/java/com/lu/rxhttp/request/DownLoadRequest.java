package com.lu.rxhttp.request;

import android.text.TextUtils;

import com.lu.rxhttp.Interface.ProgressCallBack;
import com.lu.rxhttp.util.Const;
import com.lu.rxhttp.util.DownloadBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
    private ProgressCallBack mProgressCallback;

    public DownLoadRequest() {
        this.obj = this;
    }

    public DownLoadRequest addParam(String key, String value) {
        this.mParams.put(key, value);
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
        Request.Builder builder = newRequestBuilder();
        /*添加请求参数*/
        if (mParams.size() > 0) {
            //get请求
            if (mMethod.equals(Const.GET)) {
                HttpUrl httpUrl = HttpUrl.parse(mUrl);
                if (httpUrl == null) {
                    throw new RuntimeException("incorrect url");
                }
                HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
                for (String key : mParams.keySet()) {
                    urlBuilder.addQueryParameter(key, mParams.get(key));
                }

                builder.url(urlBuilder.build().toString());

            } else {//post请求
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (String key : mParams.keySet()) {
                    formBuilder.add(key, mParams.get(key));
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

                            byte[] buffer = new byte[1024];
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
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
