package com.example.demo.utils;

public final class StringUtils {
    //	public static boolean check(String data) {
//		if(data != null && !data.equals(""))return true;
//		else return false;
//	}
    public static boolean checkString(String s) {
        if (s != null && !s.equals("")) {
            return true;
        }
        return false;
    }
}
