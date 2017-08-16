package lu.httpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import lu.httpdemo.module.FileDownLoadActivity;
import lu.httpdemo.module.FileUploadActivity;
import lu.httpdemo.module.FormRequestActivity;
import lu.httpdemo.module.HttpsActivity;
import lu.httpdemo.module.JsonActivity;
import lu.httpdemo.module.MultiPartActivity;
import lu.httpdemo.util.InjectUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Runtime.getRuntime().availableProcessors():" + Runtime.getRuntime().availableProcessors());
        InjectUtil.bind(this);
    }

    public void formRequest(View view) {
        startActivity(new Intent(this, FormRequestActivity.class));
    }


    public void uploadFile(View view) {
        startActivity(new Intent(this, FileUploadActivity.class));
    }

    public void downloadFile(View view) {
        startActivity(new Intent(this, FileDownLoadActivity.class));
    }

    public void httpsRequest(View view) {
        startActivity(new Intent(this, HttpsActivity.class));
    }

    public void jsonRequest(View view) {
        startActivity(new Intent(this, JsonActivity.class));
    }


    public void multiPartUpload(View view) {
        startActivity(new Intent(this, MultiPartActivity.class));
    }

}
