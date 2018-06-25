package com.lu.rxhttp.request;

import com.lu.rxhttp.Interface.IProgressListener;
import com.lu.rxhttp.util.HttpTool;
import com.lu.rxhttp.util.UploadBody;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: multi file upload by once
 */

public class MultiPartRequest extends AbstractRequest<MultiPartRequest> {

    private Map<String, File> mFileData = new LinkedHashMap<>();
    private IProgressListener mProgressListener;
    private MultipartBody mMultipartBody;

    public MultiPartRequest() {
        this.thisInstance = this;
    }

    public MultiPartRequest multipartBody(MultipartBody multipartBody) {
        this.mMultipartBody = multipartBody;
        return this;
    }

    public MultiPartRequest files(Map<String, File> fileData) {
        this.mFileData.putAll(fileData);
        return this;
    }

    public MultiPartRequest progress(IProgressListener callback) {
        this.mProgressListener = callback;
        return this;
    }

    @Override
    protected RequestBody createRequestBody(Map<String, String> params) {
        /*添加请求参数*/

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (mMultipartBody != null) {
            for (MultipartBody.Part part : mMultipartBody.parts()) {
                multipartBuilder.addPart(part);
            }
        }

        for (String key : params.keySet()) {
            multipartBuilder.addFormDataPart(key, params.get(key));
        }

        for (String key : mFileData.keySet()) {
            File file = mFileData.get(key);//文件
            String fileName = file.getName();//文件名  这个参数默认是文件的名称

            RequestBody body = RequestBody
                    .create(MediaType.parse(HttpTool.getMimeTypeFromFile(file)), file);

            multipartBuilder.addFormDataPart(key, fileName, body);
        }

        RequestBody body = multipartBuilder.build();

        //根据ProgressCallBack的值是否为空判断是否需要包装requestBody检测进度
        if (mProgressListener != null) {
            body = new UploadBody(body, mProgressListener);
        }

        return body;
    }
}
