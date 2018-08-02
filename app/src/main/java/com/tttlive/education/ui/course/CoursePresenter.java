package com.tttlive.education.ui.course;

import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.net.NetManager;

import retrofit2.http.Field;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Iverson on 2018/3/7 上午11:47
 * 此类用于：
 */

public class CoursePresenter extends BasePresenter<CourseUI> {

    protected CoursePresenter(CourseUI uiInterface) {
        super(uiInterface);
    }

    public void createCourse(String userId, String title, String startTime, String type,
                             String duration, String durationType, String capacity,String applicationId,String userName,String endTime){

        Subscription subscription = NetManager.getInstance().create(CourseApi.class)
                .createCourse(userId, title, startTime, type, duration, durationType, capacity,applicationId,userName,endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getUiInterface().createSuccess();
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {

                    }
                });
        addSubscription(subscription);
    }



}
