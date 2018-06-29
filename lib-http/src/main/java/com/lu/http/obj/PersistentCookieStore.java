package com.lu.http.obj;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieStore {
    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS_HOST = "Cookies_host";
    private static final String COOKIE_PREFS_OBJECT = "Cookies_object";

    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;
    private final SharedPreferences mHostPrefs;//host-cookieName的文件
    private final SharedPreferences mObjectPrefs;//cookieName-cookie的文件


    public PersistentCookieStore(Context context) {
        if (context == null) {
            throw new RuntimeException("the context must not be null for PersistentCookieStore");
        }
        mHostPrefs = context.getSharedPreferences(COOKIE_PREFS_HOST, Context.MODE_PRIVATE);
        mObjectPrefs = context.getSharedPreferences(COOKIE_PREFS_OBJECT, Context.MODE_PRIVATE);
        cookies = new HashMap<>();
        //取出所有host对应的cookieName字符串
        Map<String, ?> prefsMap = mHostPrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            final String key = entry.getKey();
            final String value = (String) entry.getValue();
            //解析当前host所有的cookieName
            String[] cookieNames = TextUtils.split(value, ",");

            for (String name : cookieNames) {
                //通过cookieName找到对应的cookie对象字符串
                String cookieStr = mObjectPrefs.getString(name, null);
                if (cookieStr != null) {
                    //反序列化加载cookie到内存中并添加到cookies中
                    Cookie cookie = decodeCookie(cookieStr);
                    if (cookie != null) {
                        if (!cookies.containsKey(key)) {
                            cookies.put(key, new ConcurrentHashMap<String, Cookie>());
                        }
                        cookies.get(key).put(name, cookie);
                    }
                }
            }
        }
    }

    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    //添加单个cookie
    public void add(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);
        final String host = url.host();
        //将cookies缓存到内存中 如果缓存过期 就重置此cookie
        if (!cookie.persistent()) {
            if (!cookies.containsKey(host)) {
                cookies.put(host, new ConcurrentHashMap<String, Cookie>());
            }
            cookies.get(host).put(name, cookie);
        } else {
            if (cookies.containsKey(host)) {
                cookies.get(host).remove(name);
            }
        }

        //讲cookies持久化到本地
        if (cookies.get(host) != null) {
            //将host对应的所有cookie名称用","拼接保存起来 保存关系为  hots-cookieName
            mHostPrefs.edit().putString(host, TextUtils.join(",", cookies.get(host).keySet())).apply();
            //将cookie序列化成字符串,保存关系为 cookieName-cookie
            mObjectPrefs.edit().putString(name, encodeCookie(new SerializableCookies(cookie))).apply();
        }
    }

    public List<Cookie> get(HttpUrl url) {
        ArrayList<Cookie> ret = new ArrayList<>();
        if (cookies.containsKey(url.host())) {
            ret.addAll(cookies.get(url.host()).values());
        }
        return ret;
    }

    public boolean removeAll() {
        mHostPrefs.edit().clear().apply();
        mObjectPrefs.edit().clear().apply();
        cookies.clear();
        return true;
    }

    public boolean remove(HttpUrl url, Cookie cookie) {
        final String host = url.host();
        final String name = getCookieToken(cookie);

        Map<String, Cookie> cookieMap = cookies.get(host);

        if (cookieMap != null && cookieMap.containsKey(name)) {
            //移除cookieName
            cookieMap.remove(name);
            //重新拼接所有的cookieName
            final String cookieNames = TextUtils.join(",", cookies.get(url.host()).keySet());

            mHostPrefs.edit().putString(host, cookieNames).apply();

            if (mObjectPrefs.contains(name)) {
                mObjectPrefs.edit().remove(name).apply();
            }

            return true;
        } else {
            return false;
        }
    }

    public List<Cookie> getCookies() {
        ArrayList<Cookie> ret = new ArrayList<>();
        for (String key : cookies.keySet())
            ret.addAll(cookies.get(key).values());

        return ret;
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    protected String encodeCookie(SerializableCookies cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e);
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableCookies) objectInputStream.readObject()).getCookies();
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
        }

        return cookie;
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}