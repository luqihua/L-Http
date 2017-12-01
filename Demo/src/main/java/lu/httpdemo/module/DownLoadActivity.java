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

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lu.httpdemo.R;
import lu.httpdemo.util.BindView;
import lu.httpdemo.util.InjectUtil;

public class DownLoadActivity extends AppCompatActivity {

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
                //钉钉的安装程序下载地址   文件比较大可能会断
                .get("http://sw.bos.baidu.com/sw-search-sp/software/2d47084bcbd4d/dd_3.4.8.exe")
                //可选择是否订阅进度
                .progress(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("DownLoadActivity", "progress:" + progress);
                    }
                })
                .targetFile(file)
                .observerString()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("DownLoadActivity", s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("DownLoadActivity", "e:" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void baseTest(View view) {

    }

}
