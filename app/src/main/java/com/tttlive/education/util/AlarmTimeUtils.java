package com.tttlive.education.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by Administrator on 2018/7/13/0013.
 * 定时器 循环ping
 */
public class AlarmTimeUtils {
    
    private Context mContext;
    private static AlarmTimeUtils alarmTime;
    private static PendingIntent mPendingIntent;
    private String mActionName;
    private String cycleValue;

    public static synchronized AlarmTimeUtils getInstance(){
        if (alarmTime ==null){
            synchronized (AlarmTimeUtils.class){
                if (alarmTime == null){
                    alarmTime = new AlarmTimeUtils();
                }

            }
        }
        return alarmTime;
    }


    public AlarmTimeUtils(){};

    /**
     * 加载定时任务
     * @param context
     * @param actionName
     * @param time
     */
    public void configureAlarmManagers(Context context ,String actionName, String value, long time){
        this.mActionName = actionName;
        this.mContext = context;
        this.cycleValue = value;
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntents();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time , pendingIntent);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            manager.setExact(AlarmManager.RTC_WAKEUP , time ,pendingIntent);
        }else {
            manager.set(AlarmManager.RTC_WAKEUP , time , pendingIntent);
        }


    }

    public void cancelAlarmManager(){
        if(mContext == null) return;
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(getPendingIntents());
    }


    /**
     * 发送广播
     * @return
     */
    private PendingIntent getPendingIntents(){
        if (mPendingIntent == null){
            int requestCode = 0;
            Intent intent = new Intent();
            intent.setAction(mActionName);
            intent.putExtra("heardTime" , cycleValue);
            mPendingIntent = PendingIntent.getBroadcast(mContext , requestCode , intent , PendingIntent.FLAG_CANCEL_CURRENT);

        }
        return  mPendingIntent;
    }
}
