package lu.okhttpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import lu.okhttpdemo.model.FileDownLoadActivity;
import lu.okhttpdemo.model.FileUploadActivity;
import lu.okhttpdemo.model.HttpsActivity;
import lu.okhttpdemo.model.FormActivity;
import lu.okhttpdemo.model.MultiPartActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void formRequest(View view) {
        startActivity(new Intent(this, FormActivity.class));
    }

    public void fileUpload(View view) {

        startActivity(new Intent(this, FileUploadActivity.class));
    }

    public void fileDownload(View view) {
        startActivity(new Intent(this, FileDownLoadActivity.class));
    }

    public void multiPart(View view) {
        startActivity(new Intent(this, MultiPartActivity.class));
    }

    public void toHttps(View view) {
        startActivity(new Intent(this, HttpsActivity.class));
    }

}
