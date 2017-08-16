# RxHttp
okHttp+RxJava的简单封装


# 引入方式

> maven 引入
```
<dependency>
  <groupId>com.lu.lib</groupId>
  <artifactId>RxHttp</artifactId>
  <version>1.1.0</version>
  <type>pom</type>
</dependency>
```

> gradle引入
```
compile 'com.lu.lib:RxHttp:1.1.0'
```

# 使用说明


> 初始化
```
//在项目的application中初始化
  RxHttp.init(new HttpOptions()
                    .connectTimeOut(10000)
                    .readTimeOut(10000)
                    //拦截器
                    .networkInterceptors(new ArrayList<Interceptor>())
                    .interceptors(new ArrayList<Interceptor>())
                    //所有请求的公共头
                    .publicHeaders(new HttpHeader())
                    //添加缓存
                    .cache(new OkCache(this))
                    //解析http返回
                    .httpTransformer(new HttpTransformer("code", "msg", "data", 1))
                    //管理cookie持久化
                    .cookieJar(new CookieJarImp(this))
                    //自定义工作线程池
                    .WorkingThreadPool(new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>()))
                    //添加证书
                    .httpsFactory(new HttpsFactory(getAssets().open("srca12306.cer"))));

```

> 所有的请求都提供了三个方法 
```
# 该方法默认是在主线程运行
observerString()

# 该方法默认是在主线程运行，可以自己通过RxJava的 subscribeOn()和observeOn() 变换线程
observerResponse()  

# 运行线程同observerResponse()
observerStream()

# 网络访问在线程池，回调在主线程
observerData()

# 该方法用于监控进度  在主线程
progress(new ProgressCallBack(){})

```

> 简单的表单请求 

```
//log方法可以控制是否打印log
new FormRequest()
                .url("")
                .log(false)
                .addParam("key1", "value1")
                .addParam("key2", "value2")
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                        Log.d("FormRequestActivity", s);
                    }
                });
```



> 请求体包装成json请求

```
User user = new User();
user.setName("zhangsan");
user.setAge(28);
user.setPassword("helloworld");

new JsonRequest()
        .url("")
        .addJsonBody(user)
        .observerString()
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                mResultView.setText(s);
            }
        });
```



> 单个文件上传请求

```
//当添加了progress()回调时会回传上传进度   如果不调用该方法则不计算上传进度  
 new FileUpRequest()
                .url("")
                .addFile("image", imageFile)
                .progress(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                    }
                })
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                    }
                });
```



> multipart请求

```
 new MultiPartRequest()
                .url("")
                .addParam("key", "value")
                .addFile("file1", file1)
                .addFile("file2", file2)
                .addFile("file3", file3)
                .progress(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                    }
                })
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                    }
                });
```


> 多个文件依次上传请求(一般多张图片上传到同一个地址时使用)

```
//该处进度回调时每个文件上传的进度
new MultiFileUpRequest()
                .url("")
                .addFile("file1", file1)
                .addFile("file2", file2)
                .addFile("file2", file3)
                .progress(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("FileUploadActivity", "progress:" + progress + "===" + Thread.currentThread().getName());
                    }
                })
                .observerString()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        mResultView.setText(s);
                    }
                });
```


> 文件下载请求

```
  new DownLoadRequest()
                //钉钉的安装程序下载地址
                .url("http://sw.bos.baidu.com/sw-search-sp/software/2d47084bcbd4d/dd_3.4.8.exe")
                //可选择是否订阅进度
                .progress(new ProgressCallBack() {
                    @Override
                    public void onProgressChange(int progress) {
                        mProgressBar.setProgress(progress);
                        Log.d("FileDownLoadActivity", "progress:" + progress);
                    }
                })
                .targetFile(file)
                .observerString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                       //回调下载后存储的文件路径
                        Log.d("FileDownLoadActivity", s);
                    }
                });

```
# 封装json响应数据

```
//假设服务器返回的数据格式如下
{
"code":1,//正确的响应码
"msg":"success",//提示信息
"data":{"id":"1","name":"zhangsan"}//实体数据
}


//在application中初始化
/**
 * 
 * @param codeKey 对应json字符串中"code"
 * @param msgKey  对应json字符串中"msg"
 * @param dataKey 对应json字符串中"data"
 * @param successCode 成功结果的code值
 */
  RxHttp.init(new HttpOptions()
                    .httpTransformer(new HttpTransformer("code", "msg", "data", 1)));


//使用 如果希望对所有错误统一处理  直接重写observer的onError()方法即可
//如果希望根据错误类型分开处理  如下所示
 new FormRequest()
                .url("")
                .log(false)
                .addParam("key", "value")
                .observerData()
                .subscribe(new HttpObserver() {
                    #请求成功的回到   data代表数据实体的json字符串 例如上面所示{"id":"1","name":"zhangsan"} msg是服务端响应的消息 例如“请求成功” 
                    @Override
                    protected void onSuccess(String data, String msg) {
                        mResultView.setText("onSuccess:\n" + "data: " + data + "\nmsg: " + msg);
                    }
            
                    # http出错  例如404 500 等情况的回调
                    @Override
                    protected void onHttpError(HttpException e) {
                        super.onHttpError(e);
                    }
                
                    # 服务器自定义错误码例如 -1 0 等情况
                    @Override
                    protected void onCustomError(int code, String msg) {
                        mResultView.setText("onCustomError:  \ncode: " + code + "\nmsg: " + msg);
                    }
                });

```
