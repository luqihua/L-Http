package lu.httpdemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lu.request.JsonRequest;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import lu.httpdemo.R;
import lu.httpdemo.bean.DataBean;
import lu.httpdemo.bean.JsonBody;
import lu.httpdemo.util.BindView;
import lu.httpdemo.util.HMacUtil;
import lu.httpdemo.util.InjectUtil;

public class HMacActivity extends AppCompatActivity {

    @BindView(R.id.result)
    TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hmac);
        InjectUtil.bind(this);
    }


    public void javaApiTest(View view) {

        JsonBody<DataBean> jsonBody = new JsonBody<>();
        jsonBody.setSysName("postman");
        jsonBody.setRandom("25A4b1");
        jsonBody.setOperator("hulei");
        jsonBody.setTimestamp(System.currentTimeMillis() + "");
        jsonBody.setSign(HMacUtil.enCodeByKey(this, jsonBody.toString()));

        //请求数据
        DataBean dataBean = new DataBean("2567", "1");
        jsonBody.setData(dataBean);

        new JsonRequest()
                .url("http://192.168.30.33:8080/fecar/finance/biz/queryAccount")
                .addJsonBody(jsonBody)
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                    }
                });

    }

}
