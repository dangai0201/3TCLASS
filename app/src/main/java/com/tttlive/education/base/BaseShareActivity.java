package com.tttlive.education.base;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.PopupWindow;

import com.tttlive.basic.education.R;
import com.tttlive.education.ui.SharePopu;
import com.tttlive.education.util.Tools;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;



public abstract class BaseShareActivity extends BaseActivity implements UMShareListener {

    private static final String TAG_CLASS = BaseShareActivity.class.getSimpleName();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,requestCode);
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        Log.e(TAG_CLASS + "Shart Listener  ", "开始了 ");

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Log.e(TAG_CLASS + "Shart Listener  ", "成功了 ");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        Log.e(TAG_CLASS + "Shart Listener  ", "失败了 ");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        Log.e(TAG_CLASS + "Shart Listener  ", "取消了 ");

    }

    /**
     * @param share_media 分享类型  空的时候是复制
     * @param install     检测本地是否安装应用程序
     */
    public abstract void share(SHARE_MEDIA share_media, boolean install);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
