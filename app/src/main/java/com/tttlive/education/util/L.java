package com.tttlive.education.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Application-based log class.
 * <p>
 * Usages of this class is same as android.util.Log, but you can modify the log
 * implementation of this class by just modify the methods, e.g. call a third-class log class.
 * </p>
 *
 * @author Muyangmin
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class L implements HttpLoggingInterceptor.Logger {
    //a non-strict singleton
    public static final L INSTANCE = new L();
    @Override
    public void log(String message) {
        d("OkHttp", message);
    }
    public static void d(@NonNull String tag, @NonNull String msg) {
        BuglyLog.d(tag, msg);
    }

    public static void v(@NonNull String tag, @NonNull String msg) {
        v(true, tag, msg);
    }
    /**
     * This method logs verbose information.
     *
     * @param shouldReport If set to true, this log would not be send to remote server, e.g. some
     *                     verbose "success" info.
     */
    public static void v(boolean shouldReport, @NonNull String tag, @NonNull String msg) {
        if (shouldReport){
            BuglyLog.v(tag, msg);
        }
        else{
            Log.v(tag, msg);
        }
    }
    public static void i(@NonNull String tag, @NonNull String msg) {
        BuglyLog.i(tag, msg);
    }
    public static void w(@NonNull String tag, @NonNull String msg) {
        BuglyLog.w(tag, msg);
    }
    public static void e(@NonNull String tag, @NonNull String msg) {
        BuglyLog.e(tag, msg);
    }
    public static void wtf(@NonNull String tag, @NonNull String msg) {
        Log.wtf(tag, msg);
    }
    /**
     * call {@link #e(boolean, String, String, Throwable)} with shouldReport set to false.
     */
    public static void e(@NonNull String tag, @NonNull String msg, Throwable tr) {
        e(false, tag, msg, tr);
    }

    /**
     * @param shouldReport If set to true, this Throwable is auto post to issue server.
     */
    public static void e(boolean shouldReport, @NonNull String tag, @NonNull String msg,
                         Throwable tr) {
        BuglyLog.e(tag, msg, tr);
        if (shouldReport){
            CrashReport.postCatchedException(tr);
        }
    }
    public static void v(@NonNull String tag, @NonNull String format, Object... args) {
        v(tag, String.format(format, args));
    }

    public static void v(boolean shouldReport, @NonNull String tag, @NonNull String format,
                         Object... args) {
        v(shouldReport, tag, String.format(format, args));
    }
    public static void d(@NonNull String tag, @NonNull String format, Object... args) {
        d(tag, String.format(format, args));
    }

    public static void i(@NonNull String tag, @NonNull String format, Object... args) {
        i(tag, String.format(format, args));
    }

    public static void w(@NonNull String tag, @NonNull String format, Object... args) {
        w(tag, String.format(format, args));
    }

    public static void e(@NonNull String tag, @NonNull String format, Object... args) {
        e(tag, String.format(format, args));
    }

}
