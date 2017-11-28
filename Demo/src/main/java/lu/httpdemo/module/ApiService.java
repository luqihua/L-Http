package lu.httpdemo.module;


import com.lu.rxhttp.Interface.ProgressCallBack;
import com.lu.rxhttp.annotation.Body;
import com.lu.rxhttp.annotation.Field;
import com.lu.rxhttp.annotation.FilePart;
import com.lu.rxhttp.annotation.Form;
import com.lu.rxhttp.annotation.GET;
import com.lu.rxhttp.annotation.Json;
import com.lu.rxhttp.annotation.MultiBody;
import com.lu.rxhttp.annotation.MultiPart;
import com.lu.rxhttp.annotation.POST;
import com.lu.rxhttp.annotation.ProgressListener;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import lu.httpdemo.bean.HttpResult;
import lu.httpdemo.bean.User;
import okhttp3.MultipartBody;

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

    @Form
    @GET("login")
    Observable<HttpResult<User>> getLogin(@Field("username") String username
            , @Field("password") String password);

    @Json
    @POST("loginJson")
    Observable<HttpResult<User>> loginJson(@Body User user);

    @MultiPart
    @POST("upload")
    Observable<HttpResult<Map<String, String>>> upload(@Field("username") String username,
                                                       @Field("password") String password,
                                                       @FilePart("avatar") File image,
                                                       @ProgressListener ProgressCallBack callBack);

    @MultiPart
    @POST("upload2")
    Observable<HttpResult<Map<String, String>>> multiUpload(
            @MultiBody MultipartBody body,
            @ProgressListener ProgressCallBack callBack);
}
