package lu.okhttpdemo.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.RxHttp;
import com.lu.Interface.ProgressCallBack;
import com.lu.util.FileStorageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lu.okhttpdemo.R;

public class MultiPartActivity extends AppCompatActivity {

    private File imageFile;
    private File imageFile1;
    private File txtFile;

    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_part);
    }


    private void initView() {
        mResultView = (TextView) findViewById(R.id.result);
    }

    public void saveFile(View view) {
        imageFile = FileStorageUtil.getInstance().getFileByName("image.png");
        imageFile1 = FileStorageUtil.getInstance().getFileByName("image1.png");
        txtFile = FileStorageUtil.getInstance().getFileByName("test.txt");
        try {
            InputStream in = getAssets().open("dota3.png");
            FileOutputStream out = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();

            /*第二张*/
            in = getAssets().open("dota3.png");
            out = new FileOutputStream(imageFile1);
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

        RxHttp.MultiPartRequest()
                .url("http://192.168.70.56:8080/Test/UpLoadServlet")
                .addFile("image", imageFile)
                .addFile("image1", imageFile1)
                .addFile("txt_file", txtFile)
                .addProgressListener(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                    }
                })
                .observerString()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("FileUploadActivity", "Observer: 开始");
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        mResultView.setText(s);
                        Log.d("FileUploadActivity", "Observer: "+s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("FileUploadActivity", "e:" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("FileUploadActivity", "Observer结束");
                    }
                });
    }


}
