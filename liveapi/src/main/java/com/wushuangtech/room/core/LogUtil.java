package com.wushuangtech.room.core;

import android.util.Log;

/**
 * Created by Iverson on 2017/10/10 上午11:27
 * 此类用于：打印log日志
 */

public class LogUtil {

    private static final boolean isOpen = true;

    public static void v(String tag, String msg) {
        if(!isOpen) return;
        Log.v(tag, msg);
    }

    public static void i(String tag,String msg) {
        if(!isOpen) return;
        Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if(!isOpen) return;
        Log.d(tag, msg);
    }

    public static void w(String tag,String msg) {
        if(!isOpen) return;
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if(!isOpen) return;
        Log.e(tag, msg);
    }

}
