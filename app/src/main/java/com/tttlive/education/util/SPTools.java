package com.tttlive.education.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tttlive.education.data.AccountBean;
import com.tttlive.education.data.AccountLoginBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: sunny
 * Time: 2018/7/17
 * description: SP存储工具类
 */

public class SPTools {

    //缓存登录账号密码历史记录数量
    public static final int MAX_CACHE_HISTORY_SIZE = 3;
    public static final String KEY_LOGIN_INFO = "key_login_info";//登录信息
    public static final String KEY_ACCOUNT = "key_account";//账号登录历史列表
    public static final String KEY_LOGIN_INVITE_CODE = "key_login_invite_code";//登录的邀请码
    public static final String KEY_NICKNAME = "nick_name";
    private static SPTools instance = new SPTools();
    private static SharedPreferences mSp = null;

    public static SPTools getInstance(Context context) {

        if (mSp == null) {
            mSp = context.getApplicationContext().getSharedPreferences("live", Context.MODE_PRIVATE);
        }

        return instance;
    }

    // 保存
    public void save(String key, Object value) {

        if (value instanceof String) {
            mSp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            mSp.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Long) {
            mSp.edit().putLong(key, (Long) value).commit();
        }
    }

    // 读取
    // 读取String类型数据
    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    // 读取boolean类型数据
    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    // 读取int类型数据
    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    // 读取long类型数据
    public long getLong(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }


    /**
     * 存储登录信息
     *
     * @param loginInfo
     */
    public void saveLoginInfo(@NonNull AccountLoginBean loginInfo) {
        save(SPTools.KEY_LOGIN_INFO, new Gson().toJson(loginInfo));
    }



    /**
     * 获取登录信息
     */
    public AccountLoginBean getLoginInfo() {
        String loginInfoJson = getString(SPTools.KEY_LOGIN_INFO, "");
        if (loginInfoJson.length() == 0) {
            return null;
        }
        AccountLoginBean loginInfo = new Gson().fromJson(loginInfoJson, AccountLoginBean.class);
        return loginInfo;
    }

    /**
     * 保存登录历史
     *
     * @param list
     */
    public  void saveAccountList(List<AccountBean> list) {

        save(KEY_ACCOUNT, new Gson().toJson(list));
    }

    /**
     * 获取登录历史
     *
     * @return
     */
    public  List<AccountBean> getAccountList() {
        String accountJson = getString(KEY_ACCOUNT, "");

        List<AccountBean> list = new ArrayList<>();
        if (accountJson.length() != 0) {
            Gson gson = new Gson();
            list = gson.fromJson(accountJson, new TypeToken<List<AccountBean>>() {
            }.getType());
        }

        if (list.size() > MAX_CACHE_HISTORY_SIZE) {
            list = list.subList(0, MAX_CACHE_HISTORY_SIZE);

        }
        return list;
    }


}
