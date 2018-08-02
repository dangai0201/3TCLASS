package com.tttlive.education.ui.course.edit;

import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.net.NetManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mrliu on 2018/3/10.
 * 此类用于:编辑课程表presenter
 */

public class EditCoursePresenter extends BasePresenter<EditCourseInterface> {

    protected EditCoursePresenter(EditCourseInterface uiInterface) {
        super(uiInterface);
    }

    //编辑课程
    public void editCourse(String id,String type,String title,String startTime,String duration,String durationType
                            ,String capacity,String coverSrc,String joinRule,String desc,String userId,String endTime){

        Subscription subscription = NetManager.getInstance().create(EditCourseApi.class)
                .editCourse(id,type,title,startTime,duration,durationType,capacity,coverSrc,joinRule,desc,userId,endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        if(0==response.getCode()){
                            getUiInterface().editSucess();
                        }
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {

                    }
                });
        addSubscription(subscription);

    }

}
