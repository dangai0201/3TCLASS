package com.tttlive.education.ui.login;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.PermissionBean;
import com.tttlive.education.ui.room.bean.LocationBean;
import com.tttlive.education.util.PermissionUtils;
import com.wushuangtech.library.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2018/7/9/0009.
 *
 * 启动页
 */
public class SplashActivity extends BaseActivity implements SplashInterface {
    private static final String TAG_CLASS = SplashActivity.class.getSimpleName();
    private Context mContext;

    private SplashPresenter splashPresenter;
    private TextView app_version_num;

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.splace_activity;
    }

    @Override
    protected void findView() {
        splashPresenter = new SplashPresenter(this);
        mContext = this;
        app_version_num = findViewById(R.id.app_version_num);

    }

    @Override
    protected void initView() {
        splashPresenter.locationReqService();
        getVersionNumber();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this , LoginActivity.class));
                finish();

            }
        }, 2500);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashPresenter.unsubscribeTasks();
    }

    @Override
    public void locationSucess(BaseResponse<LocationBean> locationBean) {
        //请求服务器资源
        Log.e(TAG_CLASS , " locationBean  " + new Gson().toJson(locationBean));
        if (locationBean != null){
            Constant.DocUrl = locationBean.getData().getDocsWbUrl();
            Constants.SDKVideo_url = locationBean.getData().getSdk().getHost();
            Constants.SDKVideo_port = locationBean.getData().getSdk().getPort();
            Constant.Websocket_port = locationBean.getData().getIpLocation().getPortBusiness();
            Constant.SERVER_CLAUSE_URL = locationBean.getData().getServerClause();
            String default_url = locationBean.getData().getIpLocation().getHost();
            Constant.SOCKET_URL_PORT = locationBean.getData().getIpLocation().getPort();
            Constant.SOCKET_URL = "ws://" + default_url + ":" + Constant.SOCKET_URL_PORT;
            Constant.SOCKET_URL_DEFAULT = "ws://" + default_url + ":" + Constant.SOCKET_URL_PORT;
            Constant.SHARE_CLASS_ROOM_URL = locationBean.getData().getClassroomUrl() + "/?code=";

        }
    }

    @Override
    public void getError() {
        Log.e(TAG_CLASS , " getError ");
//        splashPresenter.locationReqService();
    }

    /**
     * 获取版本号
     * @return
     */
    public void getVersionNumber() {

        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName() , 0);
            String versionName = packageInfo.versionName;
            Constant.APP_VERSION_CODE = versionName;
            app_version_num.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}
