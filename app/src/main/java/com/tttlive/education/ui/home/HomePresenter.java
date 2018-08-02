package com.tttlive.education.ui.home;


import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.CourseListBean;
import com.tttlive.education.net.NetManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Iverson on 2017/12/14 上午10:10
 * 此类用于：
 */

public class HomePresenter extends BasePresenter<HomePageUIinterface> {

    protected HomePresenter(HomePageUIinterface uiInterface) {
        super(uiInterface);
    }

    /**
     * 主页课程列表
     */
    public void getCourseList(String userId, String page, String size) {
        Subscription subscription = NetManager.getInstance().create(HomeApi.class)
                .getCourseList(userId, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<CourseListBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<CourseListBean> response) {
                        getUiInterface().getCourseListSuccess(response.getData());
                    }

                    @Override
                    public void onDataCode(BaseResponse<CourseListBean> response) {

                    }
                });
        addSubscription(subscription);
    }

    //删除课程
    public void delCourse(String userId, String id, final int layoutPosition) {
        Subscription subscription = NetManager.getInstance().create(HomeApi.class)
                .delCourse(userId, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        if (0 == response.getCode()) {
                            getUiInterface().delSucess(response.getMessage(), layoutPosition);
                        }
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {

                    }
                });
        addSubscription(subscription);

    }

}
