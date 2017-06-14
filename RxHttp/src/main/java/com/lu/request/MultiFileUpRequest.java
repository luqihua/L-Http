package com.lu.request;

import com.lu.Interface.ProgressCallBack;
import com.lu.util.Const;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: luqihua
 * Time: 2017/6/5
 * Description: 上传多个文件  每个文件建立一次HTTP链接
 */

public class MultiFileUpRequest extends AbstractRequest<MultiFileUpRequest> {

    private Map<String, File> mFileData = new LinkedHashMap<>();

    private ProgressCallBack listener;

    public MultiFileUpRequest() {
        this.obj = this;
    }

    public MultiFileUpRequest addFile(String key, File file) {
        if (file == null) {
            throw new RuntimeException("the file to key: " + key + " is null");
        }
        this.mFileData.put(key, file);
        return this;
    }

    public MultiFileUpRequest addFileMap(Map<String, File> fileMap) {
        this.mFileData = fileMap;
        return this;
    }

    public MultiFileUpRequest addProgressListener(ProgressCallBack listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected Request createRequest() {
        return null;
    }

    @Override
    public Observable<Response> observerResponse() {
        Set<Map.Entry<String, File>> set = mFileData.entrySet();
        return Observable.fromIterable(set)
                .flatMap(new Function<Map.Entry<String, File>, ObservableSource<Response>>() {
                    @Override
                    public ObservableSource<Response> apply(@NonNull Map.Entry<String, File> entry) throws Exception {
                        return new FileUpRequest()
                                .url(mUrl)
                                .method(Const.POST)
                                .headers(mHeaders)
                                .tag(mTag)
                                .log(isLog)
                                .addFile(entry.getKey(), entry.getValue())
                                .addProgressListener(listener)
                                .observerResponse();
                    }
                });
    }
}
