package com.lu.aptdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lu.aptdemo.api.HttpClient;
import com.lu.aptdemo.bean.HttpResult;
import com.lu.aptdemo.bean.UserInfo;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lu.aptdemo.R;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private TextView mDataV;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataV = findViewById(R.id.data);
    }

    public void formRequest(View v) {
        HttpClient.getJavaservice().login("luqihua", "1123344")
               .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<UserInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<UserInfo> userInfoHttpResult) {
                        mDataV.setText(userInfoHttpResult.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDataV.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void stopCall(View v) {
    }

}
