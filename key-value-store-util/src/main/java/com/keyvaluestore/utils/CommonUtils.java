package com.keyvaluestore.utils;

public class CommonUtils {

    public static final long TIME_TO_LIVE = 600000l;

    public static boolean hasExpired(long createdAt){
        if(System.currentTimeMillis() - createdAt > TIME_TO_LIVE){
            return true;
        }
        return false;
    }


    public static boolean isEmpty(String value){
        if(value == null || value.isEmpty())
            return true;
        return false;
    }
}
