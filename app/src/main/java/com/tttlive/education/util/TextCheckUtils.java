package com.tttlive.education.util;

/**
 * Author: sunny
 * Time: 2018/7/20
 * description:文本校验工具类
 */

public class TextCheckUtils {


    /**
     * 校验文本长度 (包含最小和最大边界)
     * @param text 校验的文本
     * @param minLength 最小文本长度
     * @param maxLength 最大文本长度
     * @return
     */
    public static boolean checkTextLength(String text, int minLength, int maxLength) {
        int length = text.length();
        if(length < minLength || length > maxLength) {
            return false;
        }
        return true;
    }

    public static boolean isMobileNo(String text){
        return MobileCheckUtil.isMobileNO(text);
    }


}
