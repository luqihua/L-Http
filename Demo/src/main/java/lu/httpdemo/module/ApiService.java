package lu.httpdemo.module;


import com.lu.rxhttp.Interface.ProgressCallBack;
import com.lu.rxhttp.annotation.Body;
import com.lu.rxhttp.annotation.Field;
import com.lu.rxhttp.annotation.FilePart;
import com.lu.rxhttp.annotation.Form;
import com.lu.rxhttp.annotation.Json;
import com.lu.rxhttp.annotation.MultiPart;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.annotation.ProgressListener;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import lu.httpdemo.bean.HttpResult;
import lu.httpdemo.bean.User;

/**
 * Author: luqihua
 * Time: 2017/11/17
 * Description: ApiService
 */

public interface ApiService {
    @Form
    @POST("login")
    Observable<HttpResult<User>> login(@Field("username") String username
            , @Field("password") String password);

    @Json
    @POST("loginJson")
    Observable<HttpResult<User>> loginJson(@Body User user);

    @MultiPart
    @POST("upload")
    Observable<HttpResult<String>> upload(@Field("username") String username,
                                          @Field("password") String password,
                                          @FilePart("avatar") File image,
                                          @ProgressListener ProgressCallBack callBack);


    @MultiPart
    @POST("http://test.check.fecar.com/index.php/Check/Image/upload")
    Observable<HttpResult<Map<String, String>>> uploadPHP(@Field("file_name") String carbase,
                                                          @Field("is_thumb") String is_thumb,
                                                          @FilePart("img") File image,
                                                          @ProgressListener ProgressCallBack callBack);
}
