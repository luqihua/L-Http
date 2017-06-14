package com.lu.obj;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: luqihua
 * Time: 2017/6/1
 * Description: Header
 */

public class HttpHeader implements Map<String, String> {

    private Map<String, String> mValues = new HashMap<>();

    /*公共*/
    private final String CACHE_CONTROL = "Cache-Control";
    private final String PRAGMA = "Pragma";
    private final String CONTENT_LENGTH = "Content-Length";
    private final String CONTENT_TYPE = "Content-Type";
    /*请求头*/
    private final String ACCEPT = "Accept";
    private final String USER_AGENT = "User-Agent";
    private final String RANGE = "Range";
    private final String ACCEPT_ENCODING = "Accept-Encoding";
    private final String CONNECTION = "Connection";
    private final String HOST = "Host";
    private final String ACCEPT_RANGES = "Accept-Ranges";

    /*响应头*/
    private final String CONTENT_ENCODING = "Content-Encoding";

    /**/
    public String getPragma() {
        return get(PRAGMA);
    }

    public void setPragma(String value) {
        put(PRAGMA, value);
    }

    /**/
    public String getCacheControl() {
        return get(CACHE_CONTROL);
    }

    public void setCacheControl(String value) {
        put(CACHE_CONTROL, value);
    }

    /*内容长度*/
    public String getContent_Length() {
        return get(CONTENT_LENGTH);
    }

    public void setContent_Length(String value) {
        put(CONTENT_LENGTH, value);
    }

    /*返回内容的MIME类型*/
    public String getContent_Type() {
        return get(CONTENT_TYPE);
    }

    public void setContent_Type(String value) {
        put(CONTENT_TYPE, value);
    }

    /***请求头***/

    public String getAccept() {
        return get(ACCEPT);
    }

    public void setAccept(String value) {
        put(ACCEPT, value);
    }

    /**/
    public String getUserAgent() {
        return get(USER_AGENT);
    }

    public void setUserAgent(String value) {
        put(USER_AGENT, value);
    }

    /*请求内容范围*/
    public String getRange() {
        return get(RANGE);
    }

    public void setRange(String value) {
        put(RANGE, value);
    }

    /**/
    public String getAcceptEncoding() {
        return get(ACCEPT_ENCODING);
    }

    public void setAcceptEncoding(String value) {
        put(ACCEPT_ENCODING, value);
    }

    /*是否需要持久链接*/
    public String getConnection() {
        return get(CONNECTION);
    }

    public void setConnection(String value) {
        put(CONNECTION, value);
    }

    /*指定请求服务器的域名*/
    public String getHost() {
        return get(HOST);
    }

    public void setHost(String value) {
        put(HOST, value);
    }

    /*请求网页实体的一个或者多个子范围字段*/
    public String getAcceptRanges() {
        return get(ACCEPT_RANGES);
    }

    public void setAcceptRanges(String value) {
        put(ACCEPT_RANGES, value);
    }

    /***响应头***/
    public String getContentEncoding() {
        return get(CONTENT_ENCODING);
    }

    public void getContentEncoding(String value) {
        put(CONTENT_ENCODING, value);
    }

    /**
     * ------------------
     **/
    @Override
    public int size() {
        return mValues.size();
    }

    @Override
    public boolean isEmpty() {
        return mValues.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return mValues.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return mValues.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return mValues.get(key);
    }

    @Override
    public String put(String key, String value) {
        return mValues.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return mValues.remove(key);
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ? extends String> m) {
        mValues.putAll(m);
    }

    @Override
    public void clear() {
        mValues.clear();
    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return mValues.keySet();
    }

    @NonNull
    @Override
    public Collection<String> values() {
        return mValues.values();
    }

    @NonNull
    @Override
    public Set<Entry<String, String>> entrySet() {
        return mValues.entrySet();
    }
}
