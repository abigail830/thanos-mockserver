package com.thanos.contract.mockserver.infrastructure.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;

public class JsonUtil {
    private final static Gson DEFAULT_GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private final static Gson PRETTY_PRINT_GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create();

    public static <T> T toObject(String json, Type obj) {
        return DEFAULT_GSON.fromJson(json, obj);
    }

    public static <T> T toObject(String json, Class<T> obj) {
        return DEFAULT_GSON.fromJson(json, obj);
    }

    public static String toJson(Object obj) {
        return DEFAULT_GSON.toJson(obj);
    }

    public static String toPrettyPrintedJson(Object obj) {
        return PRETTY_PRINT_GSON.toJson(obj);
    }

    public static <T> Type makeParameterizedType(Class<T> containerType, Type... subType) {
        return ParameterizedTypeImpl.make(containerType, subType, null);
    }
}
