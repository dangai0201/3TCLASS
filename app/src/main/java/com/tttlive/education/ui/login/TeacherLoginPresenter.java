package com.tttlive.education.ui.login;

import com.google.gson.Gson;
import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.net.NetManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mrliu on 2018/3/12.
 * 此类用于:账号密码登录的presenter
 */

public class TeacherLoginPresenter extends BasePresenter <TeacherLoginInterface>{

    protected TeacherLoginPresenter(TeacherLoginInterface uiInterface) {
        super(uiInterface);
    }

    /**
     * 账号密码登录
     * @param userName 用户名
     * @param password 密码
     */
    public void accountLogin(String userName,String password){

        Subscription subscription = NetManager.getInstance().create(LoginApi.class)
                .accountLogin(userName,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        String json = new Gson().toJson(response.getData());
                        AccountLoginBean accountLoginBean =  new Gson().fromJson(json,AccountLoginBean.class);

                        getUiInterface().loginSucess(accountLoginBean);
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getUiInterface().loginFailure();
                    }
                });
        addSubscription(subscription);
    }
}
