#### [博客地址](https://www.jianshu.com/p/af33ac192540) 
#### gradle方式引入

```java
   implementation 'com.lu.lib:lhttp:1.0'
   annotationProcessor 'com.lu.lib:lhttp-processor:1.0'
```

#### 简单使用

1. 初始化`okhttpclient`

```java

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //在这里可以自定义OkHttpClient的实现，OkHttpClient的配置可自行百度
        OkClient.getInstance().init(new OkHttpClient());
    }
}
```

2. 定义接口

- 每个网络请求都要用`@ApiService`注解标注
- 每个方法都需要使用`@LRequest("login")`标注

```java
@ApiService
public interface UserService {

    @LRequest("login")
    Observable<HttpResult<UserInfo>> login(
    					@Param("name") String name, 
    					@Param("password") String password);
    					
    
    //get请求
    @LRequest(value = "login",method = LMethod.GET)
    Observable<HttpResult<User>> getLogin(@Param("username") String username
            , @Param("password") String password);
					
}
```

3. build工程会在目录`/build/generated/source/apt`下生成实现类`UserServiceImpl`,实现类的构造方法需要一个`String`类型参数`baseurl`，即接口的**基础地址**，实现类的具体代码不贴出，可以自行查看

> 代码中使用

```java

//实例化一个对象，通常这个可以设计为单例模式
 UserService service =  new UserServiceImpl("basr-url");//入参填写域名

//默认的post方式
service.login("luqihua","123456")
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Consumer<HttpResult<UserInfo>>() {
                   @Override
                   public void accept(HttpResult<UserInfo> userInfoHttpResult) throws Exception {
                       
                   }
               });
               
//get方式请求
service.getLogin("luqihua", "hello")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult<UserInfo>>() {
                   @Override
                   public void accept(HttpResult<UserInfo> userInfoHttpResult) throws Exception {
                       
                   }
               });           
               
```   

