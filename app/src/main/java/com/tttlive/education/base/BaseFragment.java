package com.tttlive.education.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tttlive.basic.education.R;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.util.CustomBackToast;
import com.tttlive.education.util.CustomToast;
import com.jakewharton.rxbinding.view.RxView;
import com.tttlive.education.util.Tools;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Iverson on 2017/10/14 下午6:27
 * 此类用于：
 */

public abstract class BaseFragment extends Fragment implements BaseUiInterface {


    protected FragmentActivity mContext;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    protected TextView mTvTitleLeft;
    protected ImageButton mBtTitleLeft;
    protected TextView mTvTitleRight;
    protected TextView mTvTitleName;
    protected ImageButton mBtTitleRight;
    protected View mContentView;
//    private RequestResult mRequestResult;

    protected Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mContentView = inflater.inflate(getLayoutId(),null);
        mUnbinder = ButterKnife.bind(this,mContentView);
        initViews(mContentView);
        return mContentView;
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(View view);

    @Override
    public void showLoadingComplete() {
        //Empty implementation
        dismissLoadingDialog();
    }

    @Override
    public Dialog showLoadingDialog() {
        return showLoadingDialog(getResources().getString(R.string.defult_load_content));
    }


    protected Dialog showLoadingDialog(String content) {
        if (mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(mContext, null, content, true, false);
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
    /**
     * 左边的布局ImageButton
     * @param t
     * @param <T>
     */
    protected <T> void setBtGlobalLeft(T t){
        mBtTitleLeft = (ImageButton) mContentView.findViewById(R.id.bt_global_title_left);
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
        mBtTitleRight = (ImageButton) mContentView.findViewById(R.id.bt_global_title_right);
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
        mTvTitleLeft = (TextView) mContentView.findViewById(R.id.tv_global_title_left);
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
        mTvTitleRight = (TextView) mContentView.findViewById(R.id.tv_global_title_right);
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
        mTvTitleName = (TextView) mContentView.findViewById(R.id.tv_global_title_name);
        if(t instanceof Integer){
            mTvTitleName.setText((Integer) t);
        }else if(t instanceof String){
            mTvTitleName.setText((String)t);
        }
        mTvTitleName.setVisibility(View.VISIBLE);
    }
    @Override
    public void showDataException(String msg) {
        toastShort(msg);
    }

    @Override
    public void showNetworkException() {
//        toastShort(R.string.msg_network_error);
    }

    @Override
    public void showUnknownException() {
//        toastShort(R.string.msg_unknown_error);
    }

    protected void toastShort(@StringRes int msg){
        CustomToast.makeCustomText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    protected void toastShort(@NonNull String msg){
        CustomToast.makeCustomText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    public void toastBackShort(String msg){
        CustomBackToast.makeCustomBackText(mContext , msg , Toast.LENGTH_SHORT).show();
    }



    /**
     * 事件点击前检查
     * @param view 点击的view
     * @param action1 检查通过处理
     * @param func1 检查条件
     * @param windowDuration 时长
     * @param unit 时间标准
     */
    protected void filterClick(View view, Func1<Void, Boolean> func1, Action1<Void> action1, long windowDuration, TimeUnit unit){
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
            clickView = mContext.findViewById((Integer) view);
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
            clickView = mContext.findViewById((Integer) view);
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
            clickView = mContext.findViewById((Integer) view);
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

//
//    /**
//     * 请求权限
//     * @param premissionList 权限列表
//     * @param requestResult 处理权限
//     */
//    protected void requestPremission(List<String> premissionList, RequestResult requestResult){
//        this.mRequestResult = requestResult;
//        PermissionGen permissionGen = PermissionGen.with(this).addRequestCode(100);
//        for (String premission: premissionList){
//            permissionGen.permissions(premission);
//        }
//        permissionGen.request();
//    }
//
//    @PermissionSuccess(requestCode = 100)
//    protected void requestContactsSuccess(){
//        LogUtil.e("permissionGen","requestContactsSuccess");
//        if(mRequestResult != null){
//            mRequestResult.successResult();
//        }
//    }
//
//    @PermissionFail(requestCode = 100)
//    protected void requestContactsFail(){
//        LogUtil.e("permissionGen","requestContactsFail");
//        BaseTools.goToSetting(getActivity());
//    }

    @Override
    public void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
        if(mUnbinder!= null){
            mUnbinder.unbind();
        }

    }
}
