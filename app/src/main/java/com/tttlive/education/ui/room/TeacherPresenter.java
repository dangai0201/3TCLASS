package com.tttlive.education.ui.room;

import android.util.Log;

import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.net.NetManager;
import com.tttlive.education.ui.room.bean.UploadImageBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/12/0012.
 */

public class TeacherPresenter extends BasePresenter<TeacherUIinterface> {

    private static final String TAG_CLASS = TeacherPresenter.class.getSimpleName();

    public TeacherPresenter(TeacherUIinterface uiInterface) {
        super(uiInterface);
    }

    /**
     * 请求课程列表
     * @param userId
     * @param page
     * @param size
     */
    public void getDocsLists(String userId  , String page , String size , String originalName){
        Subscription subscription = NetManager.getInstance().create(TeacherApi.class)
                .getTeacherDocsList(userId , page , size,originalName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<DocsListbean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<DocsListbean> response) {
                        getUiInterface().docSucess(response.getData());

                    }

                    @Override
                    public void onDataCode(BaseResponse<DocsListbean> response) {

                    }

                });
        addSubscription(subscription);

    }


    /**
     * 请求课程列表详情
     * @param docId
     */
    public void getDocsListDetails(String docId){
        Subscription subDetails = NetManager.getInstance().create(TeacherApi.class)
                .getDocsListDetail(docId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<DocsListDetailsBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<DocsListDetailsBean> response) {
                        getUiInterface().docDetailsSucess(response.getData());
                    }

                    @Override
                    public void onDataCode(BaseResponse<DocsListDetailsBean> response) {

                    }
                });

        addSubscription(subDetails);
    }

    /**
     * 上传图片
     * 
     * @param Path
     * @param part
     */
    public void getImageUpload(RequestBody Path , MultipartBody.Part part){

        Subscription subImageUpload = NetManager.getInstance().create(TeacherApi.class)
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

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.e(TAG_CLASS  , "onerror");
                    }
                });
        addSubscription(subImageUpload);
    }

}
