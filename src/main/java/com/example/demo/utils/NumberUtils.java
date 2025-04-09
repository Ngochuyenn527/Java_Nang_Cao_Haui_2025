package com.example.demo.utils;

public class NumberUtils {
//	public static boolean isLong(String value) {
//		if(value == null)return false;
//		try {
//			Long numBer = Long.parseLong(value);
//		}
//		catch(NumberFormatException ex) {
//			return false;
//		}
//		return true;
//	}

    public static boolean isNumber(String value) {
        try {
            Long number = Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static boolean checkNumber(Double number) {
        if (number != null) {
            return true;
        }
        return false;
    }
}
