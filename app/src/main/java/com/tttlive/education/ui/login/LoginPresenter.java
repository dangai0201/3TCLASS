package com.tttlive.education.ui.login;


import android.util.Log;

import com.google.gson.Gson;
import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.data.InviteCodeLoginBean;
import com.tttlive.education.net.NetManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mrliu on 2018/3/12.
 * 此类用于:邀请码登录的presenter
 */

public class LoginPresenter extends BasePresenter<LoginInterface> {

    private static final String TAG_CLASS = LoginPresenter.class.getSimpleName();

    public LoginPresenter(LoginInterface uiInterface) {
        super(uiInterface);
    }

    /**
     * 邀请码登录
     *
     * @param inviteCode 邀请码
     * @param nickName   昵称
     */
    public void inviteCodeLogin(String inviteCode, String nickName) {

        Subscription subscription = NetManager.getInstance().create(LoginApi.class)
                .inviteCodeLogin(inviteCode, nickName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        String json = new Gson().toJson(response.getData());
                        InviteCodeLoginBean inviteCodeLoginBean = new Gson().fromJson(json, InviteCodeLoginBean.class);
                        getUiInterface().inviteCodeLoginSuccess(inviteCodeLoginBean);
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {
                        getUiInterface().inviteCodeLoginFailure(response);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.e(TAG_CLASS  , "  inviteCodeLogin  onerror");
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 账号密码登录
     *
     * @param userName 账号
     * @param password 密码
     */
    public void accountLogin(String userName, String password) {

        Subscription subscription = NetManager.getInstance().create(LoginApi.class)
                .accountLogin(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        String json = new Gson().toJson(response.getData());
                        AccountLoginBean accountLoginBean = new Gson().fromJson(json, AccountLoginBean.class);
                        getUiInterface().accountLoginSuccess(accountLoginBean);
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {
                        getUiInterface().accountLoginFailure(response);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.e(TAG_CLASS  , "  accountLogin  onerror");
                        getUiInterface().loginError();
                    }
                });
        addSubscription(subscription);
    }


    /**
     * 校验账号
     *
     * @param account
     */
    public void veridyAccount(String account) {
        Subscription subscription = NetManager.getInstance().create(LoginApi.class)
                .verifyAccount(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getUiInterface().verifyAccount(response);

                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {
                        getUiInterface().verifyAccount(response);
                    }

                });

        addSubscription(subscription);
    }

    /**
     * 发送短信验证码
     */
    public void sendSMSCode(String mobile) {
        Subscription subscription = NetManager.getInstance().create(LoginApi.class)
                .sendSMSCode(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getUiInterface().getSMSVerificationCode(response);


                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {
                        getUiInterface().getSMSVerificationCode(response);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 校验短信验证码
     */
    public void checkSMSCode(String mobile, String code) {
        Subscription subscription = NetManager.getInstance().create(LoginApi.class)
                .checkSMSCode(mobile, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getUiInterface().getCheckSMSCode(response);
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {
                        getUiInterface().getCheckSMSCode(response);
                    }
                });

        addSubscription(subscription);

    }

    /**
     * 忘记密码重置
     */
    public void forgetResetPassword(String mobile, String password, String code) {
        Subscription subscription = NetManager.getInstance().create(LoginApi.class)
                .resetPassword(mobile, password, code , "4")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getUiInterface().getForgetPassword(response);

                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {
                        getUiInterface().getForgetPassword(response);
                    }


                });
        addSubscription(subscription);
    }

    /**
     * 注册账号
     */
    public void registeredAccountNum(String mobile, String password, String code) {
        Subscription subscription = NetManager.getInstance().create(LoginApi.class)
                .registerAccount(mobile, password, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getUiInterface().getRegisterAccount(response);
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {
                        getUiInterface().getRegisterAccount(response);
                    }
                });
        addSubscription(subscription);

    }


}
