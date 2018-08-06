package com.lu.http.obj;

import java.util.HashMap;

/**
 * Author: luqihua
 * Time: 2017/6/1
 * Description: Header
 */

public class HttpHeaderMap extends HashMap<String, String> {
    /*public header*/
    private final String CACHE_CONTROL = "Cache-Control";
    private final String PRAGMA = "Pragma";
    private final String CONTENT_LENGTH = "Content-Length";
    private final String CONTENT_TYPE = "Content-Type";
    /*request header*/
    private final String ACCEPT = "Accept";
    private final String USER_AGENT = "User-Agent";
    private final String RANGE = "Range";
    private final String ACCEPT_ENCODING = "Accept-Encoding";
    private final String CONNECTION = "Connection";
    private final String HOST = "Host";
    private final String ACCEPT_RANGES = "Accept-Ranges";

    /*response header*/
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

    public String getContent_Length() {
        return get(CONTENT_LENGTH);
    }

    public void setContent_Length(String value) {
        put(CONTENT_LENGTH, value);
    }

    public String getContent_Type() {
        return get(CONTENT_TYPE);
    }

    public void setContent_Type(String value) {
        put(CONTENT_TYPE, value);
    }

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

    public String getConnection() {
        return get(CONNECTION);
    }

    public void setConnection(String value) {
        put(CONNECTION, value);
    }

    public String getHost() {
        return get(HOST);
    }

    public void setHost(String value) {
        put(HOST, value);
    }

    public String getAcceptRanges() {
        return get(ACCEPT_RANGES);
    }

    public void setAcceptRanges(String value) {
        put(ACCEPT_RANGES, value);
    }

    public String getContentEncoding() {
        return get(CONTENT_ENCODING);
    }

    public void getContentEncoding(String value) {
        put(CONTENT_ENCODING, value);
    }
}
