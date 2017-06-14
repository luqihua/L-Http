package com.lu.request;

import com.lu.Interface.ProgressCallBack;
import com.lu.util.HttpUtil;
import com.lu.util.UploadBody;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: 单个文件上传
 */

public class FileUpRequest extends AbstractRequest<FileUpRequest> {

    private String mKey;

    private String mFileName;

    private File mFile;

    private ProgressCallBack mListener;

    public FileUpRequest() {
        this.obj = this;
    }

    public FileUpRequest addFile(String key, File file) {
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

    public FileUpRequest addProgressListener(ProgressCallBack listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * 构建单个文件上传的请求体
     *
     * @return
     */
    @Override
    protected Request createRequest() {
        Request.Builder builder = new Request.Builder().url(mUrl);

        /*添加请求头*/
        if (mHeaders != null) {
            for (Map.Entry<String, String> entry : mHeaders.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //添加上传的文件体

        RequestBody requestBody = RequestBody
                .create(MediaType.parse(HttpUtil.getMimeTypeFromFile(mFile)), mFile);

        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart(mKey, mFileName, requestBody)
                .build();

        //根据ProgressCallBack的值是否为空判断是否需要包装requestBody检测进度
        if (mListener != null) {
            body = new UploadBody(body, mListener);
        }

        builder.post(body);

        return builder.build();
    }

}
