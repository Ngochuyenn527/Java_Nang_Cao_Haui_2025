package com.example.demo.utils;

public final class StringUtils {

    public static boolean checkString(String s) {
        if (s != null && !s.equals("")) {
            return true;
        }
        return false;
    }
}
