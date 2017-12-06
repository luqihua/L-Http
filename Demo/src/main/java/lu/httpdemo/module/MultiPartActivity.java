package lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.rxhttp.Interface.IProgressCallBack;
import com.lu.rxhttp.util.HttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lu.httpdemo.HttpClient;
import lu.httpdemo.R;
import lu.httpdemo.bean.HttpResult;
import lu.httpdemo.util.BindView;
import lu.httpdemo.util.FileStorageUtil;
import lu.httpdemo.util.InjectUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public void simpleFileUpload(View view) {

        if (imageFile1 == null || !imageFile1.exists()) {
            Toast.makeText(this, "先保存文件到本地", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpClient.getApiService()
                .upload("luqihua", "123456", imageFile1,
                        new IProgressCallBack() {
                            @Override
                            public void onProgressChange(int progress) {
                                mProgressBar.setProgress(progress);
                                Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<Map<String, String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<Map<String, String>> stringHttpResult) {
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
    }

    public void multiFileUpload(View view) {

        if (imageFile1 == null || !imageFile1.exists()) {
            Toast.makeText(this, "先保存文件到本地", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("imageFile1", imageFile1);
        fileMap.put("imageFile2", imageFile2);
        fileMap.put("txtfile", txtFile);


        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (String key : fileMap.keySet()) {
            File file = fileMap.get(key);
            String name = file.getName();
            RequestBody body = RequestBody.create(MediaType.parse(HttpUtil.getMimeTypeFromFile(file)), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, body);
            builder.addPart(part);
        }


        HttpClient.getApiService()
                .multiUpload(builder.build(),
                        new IProgressCallBack() {
                            @Override
                            public void onProgressChange(int progress) {
                                mProgressBar.setProgress(progress);
                                Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<Map<String, String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<Map<String, String>> stringHttpResult) {
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
    }
}
