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
import okhttp3.RequestBody;
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

    public DownLoadRequest get(String url) {
        this.mUrl = url;
        this.mMethod = Const.GET;
        return this;
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
    protected RequestBody createRequestBody() {
        /*添加请求参数*/
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (mParams.size() > 0) {
            for (String key : mParams.keySet()) {
                formBuilder.add(key, mParams.get(key));
            }
        }
        return formBuilder.build();
    }


    /**
     * @return target File path
     */
    public Observable<String> observerString() {
        return observerResponseBody()
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(@NonNull ResponseBody responseBody) throws Exception {
                        InputStream in = null;
                        OutputStream out = null;
                        try {
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
