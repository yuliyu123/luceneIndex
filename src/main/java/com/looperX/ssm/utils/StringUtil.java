package com.looperX.ssm.utils;

/**
 * Created by apple on 17/4/14.
 */
public class StringUtil {
    /**
     * 判断是否是空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(str==null||"".equals(str.trim())){
            return true;
        }else{
            return false;
        }
    }
}
