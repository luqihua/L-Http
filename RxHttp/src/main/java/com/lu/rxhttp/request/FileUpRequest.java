package com.lu.rxhttp.request;

import com.lu.rxhttp.Interface.ProgressCallBack;
import com.lu.rxhttp.util.Const;
import com.lu.rxhttp.util.HttpUtil;
import com.lu.rxhttp.util.UploadBody;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: single file upload
 */

public class FileUpRequest extends AbstractRequest<FileUpRequest> {

    private String mKey;

    private String mFileName;

    private File mFile;

    private ProgressCallBack mListener;

    public FileUpRequest() {
        this.obj = this;
        this.mMethod = Const.POST;
    }

    public FileUpRequest addFile(String key, File file) {
        if (file == null) {
            throw new RuntimeException("the file to key: " + key + " is null");
        }
        return addFile(key, file.getName(), file);
    }

    public FileUpRequest addFile(String key, String fileName, File file) {
        if (file == null) {
            throw new RuntimeException("the file to key: " + key + " is null");
        }
        this.mKey = key;
        this.mFile = file;
        this.mFileName = fileName;
        return this;
    }

    public FileUpRequest progress(ProgressCallBack listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    protected Request createRequest() {
        Request.Builder builder = new Request.Builder().url(mUrl);

        RequestBody requestBody = RequestBody
                .create(MediaType.parse(HttpUtil.getMimeTypeFromFile(mFile)), mFile);

        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart(mKey, mFileName, requestBody)
                .build();

        //packing the requestBody the get upload progress
        if (mListener != null) {
            body = new UploadBody(body, mListener);
        }

        builder.post(body);

        return builder.build();
    }

}
