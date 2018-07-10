package com.lu.httpdemo.util;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Author: luqihua
 * Time: 2017/6/19
 * Description: InjectUtil
 */

public class InjectUtil {
    public static void bind(Activity activity) {
        Class<Activity> c = (Class<Activity>) activity.getClass();
        try {
            for (Field field : c.getDeclaredFields()) {

                if (field.isAnnotationPresent(BindView.class)) {
                    BindView bindView = field.getAnnotation(BindView.class);
                    int id = bindView.value();

                    field.setAccessible(true);

                    field.set(activity, activity.findViewById(id));

                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
