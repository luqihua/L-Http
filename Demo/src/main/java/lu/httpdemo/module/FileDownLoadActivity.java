package lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.lu.rxhttp.Interface.ProgressCallBack;
import com.lu.rxhttp.request.DownLoadRequest;
import com.lu.rxhttp.util.FileStorageUtil;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import lu.httpdemo.R;
import lu.httpdemo.util.BindView;
import lu.httpdemo.util.InjectUtil;

public class FileDownLoadActivity extends AppCompatActivity {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_down_load);
        InjectUtil.bind(this);
    }

    public void singleFile(View view) {

        final File file = FileStorageUtil.getInstance().getFileByName("dingding.exe");

        new DownLoadRequest()
                //钉钉的安装程序下载地址
                .url("http://sw.bos.baidu.com/sw-search-sp/software/2d47084bcbd4d/dd_3.4.8.exe")
                //可选择是否订阅进度
                .progress(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("FileDownLoadActivity", "progress:" + progress);
                    }
                })
                .targetFile(file)
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d("FileDownLoadActivity", s);
                    }
                });

    }

    public void baseTest(View view) {

    }

}
