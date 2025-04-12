package com.example.demo.utils;

public class MapUtils {

    public static <T> T getObjects(Object item, Class<T> tClass) {
        if (item != null) {
            if (tClass.getTypeName().equals("java.lang.Long")) {
                item = item != "" ? Long.valueOf(item.toString()) : null;
            } else if (tClass.getTypeName().equals("java.lang.Integer")) {
                item = item != "" ? Integer.valueOf(item.toString()) : null;
            } else if (tClass.getTypeName().equals("java.lang.String")) {
                item = item.toString();
            }
            return tClass.cast(item);
        }
        return null;
    }

}


