package lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.Interface.ProgressCallBack;
import com.lu.request.FileUpRequest;
import com.lu.request.MultiFileUpRequest;
import com.lu.util.FileStorageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lu.httpdemo.R;
import lu.httpdemo.util.BindView;
import lu.httpdemo.util.InjectUtil;

public class FileUploadActivity extends AppCompatActivity {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.result)
    TextView mResultView;

    private File imageFile;
    private File imageFile1;
    private File txtFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        InjectUtil.bind(this);
    }

    /**
     * 先保存文件
     *
     * @param view
     */
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

    /**
     * 多个文件依次上传
     *
     * @param view
     */
    public void multiUpload(View view) {

        if (imageFile == null || !imageFile.exists()) {
            Toast.makeText(this, "先保存文件到本地", Toast.LENGTH_SHORT).show();
            return;
        }

        new MultiFileUpRequest()
                .url("http://119.23.237.24:8080/demo/upload")
                .addFile("image", imageFile)
                .addFile("image1", imageFile1)
                .addFile("txt_file", txtFile)
                .progress(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                    }
                })
                .observerString()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                    }
                });
    }

    /**
     * 单个文件上传
     *
     * @param view
     */
    public void singleUpload(View view) {

        if (imageFile == null || !imageFile.exists()) {
            Toast.makeText(this, "先保存文件到本地", Toast.LENGTH_SHORT).show();
            return;
        }

        new FileUpRequest()
                .url("http://119.23.237.24:8080/Test/upload")
                .addFile("image", imageFile)
                .progress(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                    }
                })
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                    }
                });
    }


}
