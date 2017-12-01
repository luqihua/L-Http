package com.lu.http;


import org.junit.Test;

import io.reactivex.functions.Consumer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test2() {

        HttpProxy proxy = new HttpProxy();

        ApiService service = proxy.create(ApiService.class);

        service.login("luqihua", "123456")
                .subscribe(new Consumer<HttpResult<UserInfo>>() {
                    @Override
                    public void accept(HttpResult<UserInfo> userInfoHttpResult) throws Exception {
                        System.out.println("accept: " + userInfoHttpResult);
                    }
                });


//        service.getUserList("1")
//                .subscribe(new Consumer<List<UserInfo>>() {
//                    @Override
//                    public void accept(List<UserInfo> userInfos) throws Exception {
//                        System.out.println(userInfos);
//                    }
//                });

    }

}