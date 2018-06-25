package lu.aptdemo;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void main() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();


        System.out.println(client.dispatcher());


        client.newBuilder().connectTimeout(10000, TimeUnit.MILLISECONDS).build();


        System.out.println(client.dispatcher());
    }
}