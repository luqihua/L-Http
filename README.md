# RxHttp
okHttp+RxJava的简单封装


# 引入方式

> maven 引入
```
<dependency>
  <groupId>com.lu.lib</groupId>
  <artifactId>RxHttp</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

> gradle引入
```
compile 'com.lu.lib:RxHttp:1.0.1'
```

# 使用说明


> 初始化
```
//在项目的application中调用，使用默认的OkHttpClient
 RxHttp.init(this);
//或者OkHttpClient 
RxHttp.init(this,new OkHttpClient());

```

> 所有的请求都提供了三个方法 
```
# 该方法内部网络请求在IO线程，订阅回调切换到了主线程
observerString()

# 该方法默认是在主线程运行，可以自己通过RxJava的 subscribeOn()和observeOn() 变换线程
observerResponse()  

# 运行线程同observerResponse()
observerStream()

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

