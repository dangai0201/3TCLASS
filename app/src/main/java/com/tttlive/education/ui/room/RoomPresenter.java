package com.tttlive.education.ui.room;

import android.annotation.TargetApi;
import android.util.Log;

import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.VideoProfileBean;
import com.tttlive.education.net.NetManager;
import com.tttlive.education.ui.room.api.RoomApi;
import com.tttlive.education.ui.room.bean.UploadImageBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mr.li on 2018/4/18.
 */

public class RoomPresenter extends BasePresenter<RoomUIinterface> {

    private static final String TAG_CLASS = RoomPresenter.class.getSimpleName();
    public RoomPresenter(RoomUIinterface uiInterface) {
        super(uiInterface);
    }

    /**
     * 请求分辨率
     */
    public void getVideoProfile(){
        Subscription subscription = NetManager.getInstance().create(RoomApi.class)
                .getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<VideoProfileBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<VideoProfileBean> response) {

                        if (response.getData()!=null){
                            getUiInterface().getProfileSuccess(response.getData().getValue());
                        }
                    }

                    @Override
                    public void onDataCode(BaseResponse<VideoProfileBean> response) {
                        Log.e(TAG_CLASS,"  onDataCode    "+response);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        getUiInterface().getError();
                        Log.e(TAG_CLASS,"  err  "+throwable);
                    }
                });
        addSubscription(subscription);
    }


    public void getImageUpload(RequestBody Path , MultipartBody.Part part){
        Subscription subscription = NetManager.getInstance().create(TeacherApi.class)
                .getImageUpload(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<UploadImageBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<UploadImageBean> response) {
                        getUiInterface().imageUploadSucess(response);

                    }

                    @Override
                    public void onDataCode(BaseResponse<UploadImageBean> response) {

                    }
                });
        addSubscription(subscription);
    }
}
