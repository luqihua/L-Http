package com.lu.httpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lu.httpdemo.module.DownLoadActivity;
import com.lu.httpdemo.module.FormRequestActivity;
import com.lu.httpdemo.module.JsonActivity;

import lu.httpdemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}
