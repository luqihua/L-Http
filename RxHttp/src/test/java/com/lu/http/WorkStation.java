package com.lu.http;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Author: luqihua
 * Time: 2017/11/15
 * Description: WorkStation
 */

public class WorkStation {
    public static String invoke(Map<String, String> params) {
        JsonObject object = new JsonObject();
        object.addProperty("code", 0);
        object.addProperty("msg", "ok");

        JsonObject map = new JsonObject();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            map.addProperty(entry.getKey(), entry.getValue());
        }
        object.add("data", map);
        //http请求
        return object.toString();
    }
}
