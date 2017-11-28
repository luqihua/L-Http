package com.lu.rxhttp.request;

import com.lu.rxhttp.Interface.ProgressCallBack;
import com.lu.rxhttp.util.HttpUtil;
import com.lu.rxhttp.util.UploadBody;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: multi file upload by once
 */

public class MultiPartRequest extends AbstractRequest<MultiPartRequest> {

    private Map<String, File> mFileData = new LinkedHashMap<>();
    private ProgressCallBack mProgressCallback;
    private MultipartBody multipartBody;


    public MultiPartRequest() {
        this.obj = this;
    }

    public MultiPartRequest multipartBody(MultipartBody multipartBody) {
        this.multipartBody = multipartBody;
        return this;
    }

    public MultiPartRequest files(Map<String, File> fileData) {
        this.mFileData.putAll(fileData);
        return this;
    }

    public MultiPartRequest progress(ProgressCallBack callback) {
        this.mProgressCallback = callback;
        return this;
    }

    @Override
    protected Request createRequest() {
        Request.Builder builder = newRequestBuilder();

        /*添加请求参数*/

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (this.multipartBody != null && multipartBody.size() > 0) {
            for (MultipartBody.Part part : multipartBody.parts()) {
                multipartBuilder.addPart(part);
            }
        }

        if (mParams.size() > 0) {
            for (String key : mParams.keySet()) {
                multipartBuilder.addFormDataPart(key, mParams.get(key));
            }
        }

        if (mFileData.size() > 0) {
            for (String key : mFileData.keySet()) {
                File file = mFileData.get(key);//文件
                String fileName = file.getName();//文件名  这个参数默认是文件的名称

                RequestBody body = RequestBody
                        .create(MediaType.parse(HttpUtil.getMimeTypeFromFile(file)), file);

                multipartBuilder.addFormDataPart(key, fileName, body);
            }
        }

        RequestBody body = multipartBuilder.build();
        //根据ProgressCallBack的值是否为空判断是否需要包装requestBody检测进度
        if (mProgressCallback != null) {
            body = new UploadBody(body, mProgressCallback);
        }

        builder.post(body);

        return builder.build();
    }
}
