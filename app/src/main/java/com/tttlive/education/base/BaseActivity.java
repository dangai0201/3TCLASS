package com.tttlive.education.base;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tttlive.basic.education.R;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.PermissionBean;
import com.tttlive.education.util.CustomBackToast;
import com.tttlive.education.util.CustomToast;
import com.tttlive.education.util.PermissionUtils;
import com.tttlive.education.util.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Iverson on 2017/10/10 上午11:21
 * 此类用于：所有的activity的基类
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseUiInterface {

    protected Context mContext;
    protected ProgressDialog mProgressDialog;
    protected TextView mTvTitleLeft;
    protected ImageButton mBtTitleLeft;
    protected TextView mTvTitleRight;
    protected TextView mTvTitleName;
    protected ImageButton mBtTitleRight;
    protected Unbinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        MyApplication.getInstance().addActivity(this);
        setContentView(getLayoutId(savedInstanceState));
        mBinder = ButterKnife.bind(this);
        Tools.fullScreen(this);
        findView();
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void showDataException(String msg) {
        toastShort(msg);
        dismissLoadingDialog();
    }

    @Override
    public void showNetworkException() {
        toastShort(R.string.msg_network_error);
        dismissLoadingDialog();
    }

    @Override
    public void showUnknownException() {
        toastShort(R.string.msg_unknown_error);
        dismissLoadingDialog();
    }

    //填充布局
    protected abstract int getLayoutId(Bundle savedInstanceState);

    protected abstract void findView();

    protected abstract void initView();

    /**
     * 左边的布局ImageButton
     * @param t
     * @param <T>
     */
    protected <T> void setBtGlobalLeft(T t){
        mBtTitleLeft = (ImageButton) findViewById(R.id.bt_global_title_left);
        if(t instanceof Integer){
            mBtTitleLeft.setImageResource((Integer) t);
        }else if(t instanceof Drawable){
            mBtTitleLeft.setImageDrawable((Drawable) t);
        }
        mBtTitleLeft.setVisibility(View.VISIBLE);
    }

    /**
     * 右边的布局ImageButton
     * @param t
     * @param <T>
     */
    protected <T> void setBtGlobalRight(T t){
        mBtTitleRight = (ImageButton) findViewById(R.id.bt_global_title_right);
        if(t instanceof Integer){
            mBtTitleRight.setImageResource((Integer) t);
        }else if(t instanceof Drawable){
            mBtTitleRight.setImageDrawable((Drawable) t);
        }
        mBtTitleRight.setVisibility(View.VISIBLE);
    }

    /**
     * 左边的布局TextView
     * @param t
     * @param <T>
     */
    protected <T> void setTvGlobalLeft(T t){
        mTvTitleLeft = (TextView) findViewById(R.id.tv_global_title_left);
        if(t instanceof Integer){
            mTvTitleLeft.setText((Integer) t);
        }else if(t instanceof String){
            mTvTitleLeft.setText((String)t);
        }
        mTvTitleLeft.setVisibility(View.VISIBLE);
    }

    /**
     * 右边的布局TextView
     * @param t
     * @param <T>
     */
    protected <T> void setTvGlobalRight(T t){
        mTvTitleRight = (TextView) findViewById(R.id.tv_global_title_right);
        if(t instanceof Integer){
            mTvTitleRight.setText((Integer) t);
        }else if(t instanceof String){
            mTvTitleRight.setText((String)t);
        }
        mTvTitleRight.setVisibility(View.VISIBLE);
    }


    /**
     * 中间的布局title的TextView
     * @param t
     * @param <T>
     */
    protected <T> void setTvGlobalTitleName(T t){
        mTvTitleName = (TextView) findViewById(R.id.tv_global_title_name);
        if(t instanceof Integer){
            mTvTitleName.setText((Integer) t);
        }else if(t instanceof String){
            mTvTitleName.setText((String)t);
        }
        mTvTitleName.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBinder!=null){
            mBinder.unbind();
        }
        MyApplication.getInstance().deleActvitity(this);
    }


    @Override
    public void showLoadingComplete() {
        //Empty implementation
        dismissLoadingDialog();
    }

    @Override
    public Dialog showLoadingDialog() {
        if (mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(this, null, "请稍候", true, false);
        return mProgressDialog;
    }

    @Override
    public void dismissLoadingDialog() {
        if (mProgressDialog==null || (!mProgressDialog.isShowing())){
            return ;
        }
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    protected void toastShort(@StringRes int msg){
        CustomToast.makeCustomText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toastShort(@NonNull String msg){
        CustomToast.makeCustomText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void toastShortLong(@NonNull String msg){
        CustomToast.makeCustomText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void toastBackShort(String msg){
        CustomBackToast.makeCustomBackText(this , msg , Toast.LENGTH_SHORT).show();
    }


    /**
     * 事件点击前检查
     * @param view 点击的view
     * @param action1 检查通过处理
     * @param func1 检查条件
     * @param windowDuration 时长
     * @param unit 时间标准
     */
    protected void filterClick(View view, Func1<Void, Boolean> func1,Action1<Void> action1, long windowDuration, TimeUnit unit){
        RxView.clicks(view).throttleFirst(windowDuration, unit)
                .filter(func1)
                .subscribe(action1);
    }

    /**
     * 快速点击
     * @param view
     * @param action1
     */
    protected void onClick(Object view,Action1<Void> action1){
        View clickView = null;
        if(view instanceof View){
            clickView = (View) view;
        }else {
            clickView = findViewById((Integer) view);
        }
       subscribeClick(clickView,action1, Constant.VIEW_THROTTLE_SHORT_TIME,TimeUnit.MILLISECONDS);
    }

    /**
     * 短时间延迟
     */
    protected void onShortClick(Object view, Action1<Void> action1){
        View clickView = null;
        if(view instanceof View){
            clickView = (View) view;
        }else {
            clickView = findViewById((Integer) view);
        }
        subscribeClick(clickView,action1, Constant.VIEW_THROTTLE_MIDDLING_TIME,TimeUnit.SECONDS);
    }

    /**
     * 长时间延迟
     */
    protected void onLongClick(Object view, Action1<Void> action1){
        View clickView = null;
        if(view instanceof View){
            clickView = (View) view;
        }else {
            clickView = findViewById((Integer) view);
        }
        subscribeClick(clickView,action1, Constant.VIEW_THROTTLE_LONG_TIME,TimeUnit.SECONDS);
    }


    /**
     * 点击事件处理
     * @param view
     * @param action1
     */
    private void subscribeClick(View view, Action1<Void> action1,long windowDuration, TimeUnit unit){
        RxView.clicks(view)
                .throttleFirst(windowDuration,unit)
                .subscribe(action1);
    }


    //-----------------------点击空白区域隐藏键盘-------------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //获取权限
    public boolean permissionInit() {
        final ArrayList<PermissionBean> mPermissionList = new ArrayList<>();
        mPermissionList.add(new PermissionBean(Manifest.permission.WRITE_EXTERNAL_STORAGE, mContext.getResources().getString(R.string.permission_write_external_storage)));
        mPermissionList.add(new PermissionBean(Manifest.permission.CAMERA, mContext.getResources().getString(R.string.permission_camera)));
        mPermissionList.add(new PermissionBean(Manifest.permission.RECORD_AUDIO, mContext.getResources().getString(R.string.permission_record_audio)));
        mPermissionList.add(new PermissionBean(Manifest.permission.READ_PHONE_STATE, mContext.getResources().getString(R.string.permission_read_phone_state)));

        boolean isOk = PermissionUtils.checkPermission(mContext, new PermissionUtils.PermissionUtilsInter() {
            @Override
            public List<PermissionBean> getApplyPermissions() {
                return mPermissionList;
            }

            @Override
            public AlertDialog.Builder getTipAlertDialog() {
                return null;
            }

            @Override
            public Dialog getTipDialog() {
                return null;
            }

            @Override
            public AlertDialog.Builder getTipAppSettingAlertDialog() {
                return null;
            }

            @Override
            public Dialog getTipAppSettingDialog() {
                return null;
            }
        });

        return isOk;

    }

}
