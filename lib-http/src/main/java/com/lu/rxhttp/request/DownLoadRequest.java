package com.lu.rxhttp.request;

import com.lu.rxhttp.Interface.IProgressListener;
import com.lu.rxhttp.util.DownloadBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Author: luqihua
 * Time: 2017/6/7
 * Description: DownLoadRequest
 */

public class DownLoadRequest extends AbstractRequest<DownLoadRequest> {

    private File mFile;
    private IProgressListener mProgressListener;

    public DownLoadRequest() {
        this.thisInstance = this;
    }

    public DownLoadRequest progress(IProgressListener callback) {
        this.mProgressListener = callback;
        return this;
    }

    public DownLoadRequest targetFile(final File file) {
        this.mFile = file;
        return this;
    }

    public DownLoadRequest targetFile(final String filePath) {
        this.mFile = new File(filePath);
        return this;
    }

    @Override
    protected RequestBody createRequestBody(Map<String, String> params) {
        /*添加请求参数*/
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBuilder.add(key, params.get(key));
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
                            if (mProgressListener != null) {
                                responseBody = new DownloadBody(responseBody, mProgressListener);
                            }

                            in = responseBody.byteStream();
                            out = new FileOutputStream(mFile);

                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = in.read(buffer)) > 0) {
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
                        return mFile.getAbsolutePath();
                    }
                });
    }

}
