package com.lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.httpdemo.HttpClient;
import com.lu.httpdemo.bean.HttpResult;
import com.lu.httpdemo.bean.User;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lu.httpdemo.R;

public class FormRequestActivity extends AppCompatActivity {

    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_request);
        initView();
    }

    private void initView() {
        mResultView = findViewById(R.id.result);
    }


    public void get(View view) {
        HttpClient.getApiService()
                .getLogin("luqihua", "hello")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<User> userHttpResult) {
                        mResultView.setText(userHttpResult.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FormRequestActivity.this, "e:" + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void post(View view) {
        HttpClient.getApiService()
                .login("luqihua", "hello")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<User> userHttpResult) {
                        mResultView.setText(userHttpResult.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FormRequestActivity.this, "e:" + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
