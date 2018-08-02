package com.tttlive.education.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Iverson on 2017/12/14 上午11:18
 * 此类用于：
 */

public class DateUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    private static long lastClickTime;

    private SimpleDateFormat sf = null;

    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(String time) {
        //        Date d = new Date(time);
        //        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        //        return sf.format(d);
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        long lcc_time = Long.valueOf(time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /*时间戳转换成字符窜*/
    public static String getDateToStringLong(String time) {
        //        Date d = new Date(time);
        //        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        //        return sf.format(d);
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc_time = Long.valueOf(time);
        re_StrTime = sdf.format(new Date(lcc_time));
        return re_StrTime.substring(0, re_StrTime.length() - 3);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;//返回十位时间戳
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将时间戳转换为"距离现在多久"的字符串
     */
    public static String getDateToToday(String timeServer) {

        long timeStart = 0;
        try {
            timeStart = sdf.parse(timeServer).getTime();
            //获取当前时间与获取时间的差值
            long newTime = System.currentTimeMillis() - (timeStart);
            //获取天数
            long day = newTime / 24 / 60 / 60 / 1000;
            //获取小时值
            long hour = (newTime - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            //获取分值
            long minute = (newTime - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60);
            if (day >= 1) {
                return day + "天" + hour + "小时" + minute + "分钟前";
            } else {
                if (hour >= 1) {
                    return hour + "小时" + minute + "分钟前";
                } else {
                    if (minute >= 1) {
                        return minute + "分钟前";
                    } else {
                        return "刚刚";
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }


    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    /**
     * 正则校验手机号
     */
    public static boolean isChinaPhoneLegal(String str)
            throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
