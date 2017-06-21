package lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lu.request.JsonRequest;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import lu.httpdemo.R;
import lu.httpdemo.bean.User;
import lu.httpdemo.util.BindView;
import lu.httpdemo.util.InjectUtil;


public class JsonActivity extends AppCompatActivity {

    @BindView(R.id.result)
    TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        InjectUtil.bind(this);
    }

    public void jsonRequest(View view) {
        User user = new User();
        user.setName("wengzhiqi");
        user.setAge(28);
        user.setPassword("helloworld");

        new JsonRequest()
                .url("http://119.23.237.24:8080/demo/json")
                .addJsonBody(user)
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                    }
                });
    }
}
