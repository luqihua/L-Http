package lu.okhttpdemo.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lu.RxHttp;
import com.lu.obj.HttpException;
import com.lu.util.Const;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lu.okhttpdemo.R;
import okhttp3.CacheControl;

public class FormActivity extends AppCompatActivity {

    private TextView mResultView;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_request);
        initView();
    }

    private void initView() {
        mResultView = (TextView) findViewById(R.id.result);
        compositeDisposable = new CompositeDisposable();
    }

    public void request(View view) {
        RxHttp.FormRequest()
                .url("http://192.168.70.56:8080/Test/loginServlet")
                .header("name","thisisaheader")
                .addParam("username", "luqihua")
                .addParam("password", "qihualu")
                .method(Const.GET)
                .tag(this)
                .log(true)
                .cacheControl(new CacheControl.Builder()
                        .maxAge(60, TimeUnit.SECONDS)
                        .build())
                .observerString()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("FormActivity", "开始" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d("FormActivity", "结果： " + Thread.currentThread().getName());
                        mResultView.setText(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            Log.d("FormActivity", e.toString());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("FormActivity", "结束" + Thread.currentThread().getName());
                    }
                });

    }


    public void test(View view) {
        RxHttp.cancelRequest(this);
    }

    public void cancel(View view) {
        compositeDisposable.clear();
    }

}
