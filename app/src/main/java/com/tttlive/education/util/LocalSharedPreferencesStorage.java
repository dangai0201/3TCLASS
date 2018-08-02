package com.tttlive.education.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Created by Administrator on 2018/3/19/0019.
 */

public class LocalSharedPreferencesStorage {
    private static final String CONFIG_NAME = "config";

    public LocalSharedPreferencesStorage() {
    }

    public static String getConfigStrValue(Context context, String key) {
        if (context == null) {
            throw new NullPointerException(" context is null");
        }
        SharedPreferences sf = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        return sf.getString(key, null);
    }

    public static String getConfigStrValue(Context context, String key, String defaultStr) {
        if (context == null) {
            throw new NullPointerException(" context is null");
        }
        SharedPreferences sf = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        return sf.getString(key, defaultStr);
    }

    public static boolean putConfigIntValue(Context context, String key,
                                            int value) {
        SharedPreferences sf = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        e.putInt(key, value);
        return e.commit();
    }

    public static int getConfigIntValue(Context context, String key,
                                        int defaultVal) {
        SharedPreferences sf = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        return sf.getInt(key, defaultVal);
    }

    public static boolean getConfigBooleanValue(Context context, String key,
                                                boolean defaultVal) {
        SharedPreferences sf = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        return sf.getBoolean(key, defaultVal);
    }

    public static boolean putConfigStrValue(Context context, String key,
                                            String value) {
        SharedPreferences sf = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        e.putString(key, value);
        return e.commit();
    }

    public static boolean putIntValue(Context context, String key, int value) {
        SharedPreferences sf = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        e.putInt(key, value);
        return e.commit();
    }

    public static boolean putBooleanValue(Context context, String key, boolean value) {
        SharedPreferences sf = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        e.putBoolean(key, value);
        return e.commit();
    }

    public static boolean putStrValue(Context context, String[] keys,
                                      String[] values) {
        if (keys == null || values == null) {
            return false;
        }
        if (keys.length != values.length) {
            throw new RuntimeException(
                    " keys's length is different with values's length");
        }
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        for (int index = 0; index < keys.length; index++) {
            e.putString(keys[index], values[index]);
        }
        return e.commit();
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};

            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection,
                        null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

}
