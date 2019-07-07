package com.sunshine.springboot.redis.main.util;

public class StringUtil {
    public static String toString(Object object){
        String result = "" ;
        if (object != null){
            result = object.toString() ;
        }
        return result ;
    }
}
