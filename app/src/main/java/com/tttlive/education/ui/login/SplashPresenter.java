package com.tttlive.education.ui.login;

import android.util.Log;

import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.net.NetManager;
import com.tttlive.education.ui.room.bean.LocationBean;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/9/0009.
 */

public class SplashPresenter extends BasePresenter<SplashInterface> {

    private static final String TAG_CLASS = SplashPresenter.class.getSimpleName();

    protected SplashPresenter(SplashInterface uiInterface) {
        super(uiInterface);
    }


    /**
     * 请求服务资源
     */
    public void locationReqService(){

        Subscription subscription = NetManager.getInstance().create(SplashApi.class)
                .locationReq()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<LocationBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<LocationBean> response) {
                        getUiInterface().locationSucess(response);

                    }

                    @Override
                    public void onDataCode(BaseResponse<LocationBean> response) {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.e(TAG_CLASS , " locationReqService   error");
                        getUiInterface().getError();
                    }
                });

        addSubscription(subscription);

    }

}
