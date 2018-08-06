package com.lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.httpdemo.HttpClientApt;
import com.lu.httpdemo.bean.HttpResult;
import com.lu.httpdemo.bean.User;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lu.httpdemo.R;


public class JsonActivity extends AppCompatActivity {

    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        initView();
    }

    private void initView() {
        mResultView = findViewById(R.id.result);
    }

    public void jsonRequest(View view) {
        User user = new User();
        user.setUsername("wengzhiqi");
        user.setAge(28);
        user.setPassword("helloworld");

        HttpClientApt.getApiService()
                .loginJson(user)
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
                        Toast.makeText(JsonActivity.this, "e:" + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
