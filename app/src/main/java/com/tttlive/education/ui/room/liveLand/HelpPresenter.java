package com.tttlive.education.ui.room.liveLand;

import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.net.NetManager;
import com.tttlive.education.ui.room.api.HelpApi;

import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author: sunny
 * Time: 2018/8/27
 * description:
 */

public class HelpPresenter extends BasePresenter<HelpInterface> {

    protected HelpPresenter(HelpInterface uiInterface) {
        super(uiInterface);
    }

    public void sendQuestion(Map<String, String> params){
        Subscription subscription = NetManager
                .getInstance()
                .create(HelpApi.class)
                .sendQuestion(params)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                         getUiInterface().onSendSuccess(response);
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {
                          getUiInterface().onSendFailed(response);
                    }
                });

        addSubscription(subscription);

    }
}
