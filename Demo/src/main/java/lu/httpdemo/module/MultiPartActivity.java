package lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.rxhttp.Interface.ProgressCallBack;
import com.lu.rxhttp.util.FileStorageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lu.httpdemo.HttpClient;
import lu.httpdemo.R;
import lu.httpdemo.bean.HttpResult;
import lu.httpdemo.util.BindView;
import lu.httpdemo.util.InjectUtil;

public class MultiPartActivity extends AppCompatActivity {


    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.result)
    TextView mResultView;


    private File imageFile1;
    private File imageFile2;

    private File txtFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_part);
        InjectUtil.bind(this);
        imageFile1 = FileStorageUtil.getInstance().getFileByName("image1.png");
        imageFile2 = FileStorageUtil.getInstance().getFileByName("image2.png");
        txtFile = FileStorageUtil.getInstance().getFileByName("test.txt");
    }

    public void saveFile(View view) {
        FileOutputStream out = null;
        InputStream in = null;
        try {
            /*第一张*/
            in = getAssets().open("zongjie.png");
            out = new FileOutputStream(imageFile1);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();

            /*第二张*/
            in = getAssets().open("zongjie.png");
            out = new FileOutputStream(imageFile2);
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();

            out = new FileOutputStream(txtFile);
            out.write("text uploadBody".getBytes());
            out.flush();
            out.close();
            in.close();
            Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void multiPartUpload(View view) {

        if (imageFile1 == null || !imageFile1.exists()) {
            Toast.makeText(this, "先保存文件到本地", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpClient.getApiService()
                .upload("luqihua", "123456", imageFile1,
                new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<String> stringHttpResult) {
                        mResultView.setText(stringHttpResult.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mResultView.setText(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


//        service.uploadPHP("carbase", "1", imageFile1
//                , new ProgressCallBack() {
//                    @Override
//                    public void onProgressChange(int progress) {
//                        Log.d("MultiPartActivity", "progress:" + progress);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HttpResult<Map<String, String>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(HttpResult<Map<String, String>> mapHttpResult) {
//                        Log.d("MultiPartActivity", "mapHttpResult:" + mapHttpResult);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(MultiPartActivity.this, "e:" + e, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        new MultiPartRequest()
//                .url("http://119.23.237.24:8080/demo/upload")
//                .addParam("key", "value")
//                .addFile("image", imageFile)
//                .addFile("image1", imageFile1)
//                .addFile("txt_file", txtFile)
//                .progress(new ProgressCallBack() {
//                    @Override
//                    public void onProgressChange(int progress) {
//                        mProgressBar.setProgress(progress);
//                        Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
//                    }
//                })
//                .observerString()
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(@NonNull String s) throws Exception {
//                        mResultView.setText(s);
//                    }
//                });
    }
}
