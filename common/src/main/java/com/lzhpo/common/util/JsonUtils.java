package com.lzhpo.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * @author lzhpo
 */
public class JsonUtils<T> {

    private volatile static Gson gson;

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    private JsonUtils() {}

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    @SuppressWarnings("nochecked")
    public static <T> Object toObj(String s, T t) {
        return gson.fromJson(s, (Type) t);
    }

}
