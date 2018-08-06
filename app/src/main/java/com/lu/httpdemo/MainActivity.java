package com.lu.httpdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lu.httpdemo.module.DownLoadActivity;
import com.lu.httpdemo.module.FormRequestActivity;
import com.lu.httpdemo.module.JsonActivity;
import com.lu.httpdemo.module.MultiPartActivity;

import lu.httpdemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public void formRequest(View view) {
        startActivity(new Intent(this, FormRequestActivity.class));
    }

    public void jsonRequest(View view) {
        startActivity(new Intent(this, JsonActivity.class));
    }

    public void downloadFile(View view) {
        startActivity(new Intent(this, DownLoadActivity.class));
    }

    public void uploadFile(View v){
        startActivity(new Intent(this, MultiPartActivity.class));
    }
}
