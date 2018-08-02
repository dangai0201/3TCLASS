package com.tttlive.education.base;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.util.LogUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iverson on 2017/10/9 下午4:20
 * 此类用于：分类加载的application
 * <p>
 * 3T Class
 * 在线教育
 */
public class MyApplication extends MultiDexApplication {

    public static List<Activity> activities = new ArrayList<>();
    private static MyApplication instance;
    private static Context mContext;


    private void LibApplication() {
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        MultiDex.install(this);
        //初始化友盟分享SDK
        initUmeng();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), Constant.BuglyKey, true);
        Fresco.initialize(this);

    }

    /**
     * 初始化友盟
     */
    private void initUmeng() {

        UMConfigure.init(mContext, "5b34b5c5f29d98124a000067", "umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "7d709323eee65ca8c2980337609ea240");

        //开启 debug 方便定位错误
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wxf7fce729270ce5bb", "5171041582652607b61b5069a3cb6215");
        PlatformConfig.setQQZone("1106932641", "qxYb7ccNfRorAyOe");
        PlatformConfig.setSinaWeibo("3921700954", "e9eb12c24c825862fdc8c6c09a48309c", "http://sns.whalecloud.com");
    }

    public static Context getContext() {
        return mContext;
    }


    public void addActivity(Activity activity) {
        if (activity == null)
            return;
        activities.add(activity);
        LogUtil.e("activityName: ", "activityName add:" + activity.toString());
    }

    public void deleActvitity(Activity activity) {
        if (activity == null)
            return;
        activities.remove(activity);
        LogUtil.e("activityName: ", "activityName deleActvitity: " + activity.toString());
    }

    public <T> void finishActivity(Class<T> cls) {
        for (Activity activityName : activities) {
            if (activityName.getClass().getSimpleName().equals(cls.getSimpleName())) {
                activityName.finish();
            }
        }
    }

    public void finishAllActivity() {
        for (Activity activityName : activities) {
            activityName.finish();
        }
    }
}
