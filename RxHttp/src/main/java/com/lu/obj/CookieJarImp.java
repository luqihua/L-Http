package com.lu.obj;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Author: luqihua
 * Time: 2017/6/12
 * Description: CookieJarImp
 */

public class CookieJarImp implements CookieJar {

    private PersistentCookieStore cookieStore;

    public CookieJarImp(Context context) {
        this.cookieStore = new PersistentCookieStore(context.getApplicationContext());
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                cookieStore.add(url, cookie);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {

        List<Cookie> list = cookieStore.get(url);

        return list;
    }
}
