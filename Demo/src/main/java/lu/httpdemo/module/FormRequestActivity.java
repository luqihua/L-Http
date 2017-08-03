package lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lu.obj.JsonTransformer;
import com.lu.request.FormRequest;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lu.httpdemo.R;
import lu.httpdemo.util.BindView;
import lu.httpdemo.util.InjectUtil;

public class FormRequestActivity extends AppCompatActivity {

    @BindView(R.id.result)
    TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_request);
        InjectUtil.bind(this);
    }


    public void get(View view) {
        new FormRequest()
//                .url("http://119.23.237.24:8080/demo/login")
                .url("http://192.168.70.56:8080/Test/login")
                .log(false)
                .addParam("username", "luqihua")
                .addParam("password", "helloworld")
//                .observerResult()
//                .subscribe(new HttpObserver() {
//                    @Override
//                    protected void onSuccess(String data, String msg) {
//                        mResultView.setText("onSuccess:\n" + "data: " + data + "\nmsg: " + msg);
//                    }
//                    @Override
//                    public void onAfter() {
//                        Log.d("FormRequestActivity", "onAfter");
//                    }
//                });
                .observerString()
                .compose(new JsonTransformer(new String[]{"code", "data", "msg"}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String[]>() {
                    @Override
                    public void accept(@NonNull String[] strings) throws Exception {
                        mResultView.setText(Arrays.toString(strings));
                    }
                });
    }


    public void post(View view) {
        new FormRequest()
                .post("http://119.23.237.24:8080/demo/login")
                .log(true)
                .addParam("username", "luqihua")
                .addParam("password", "helloworld")
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                    }
                });
    }
}
