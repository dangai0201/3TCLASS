package com.tttlive.education.ui.course.playback;

import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.PlayBackListBean;
import com.tttlive.education.net.NetManager;

import java.util.ArrayList;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mrliu on 2018/3/12.
 * 此类用于:回放列表的presenter
 */

public class PlayBackListPresenter extends BasePresenter<PlayBackListInterface> {

    protected PlayBackListPresenter(PlayBackListInterface uiInterface) {
        super(uiInterface);
    }

    /**
     * 获取回放列表
     * @param id
     * @param page
     * @param size
     */
    public void getPlayBackList(String id,String page,String size){
        Subscription subscription = NetManager.getInstance().create(PlayBackListApi.class)
                .getPlayBackList(id,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<PlayBackListBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<PlayBackListBean> response) {
                        if(response.getData()!=null&&response.getData().getItemList().size()>0){
                            getUiInterface().getPlayBackList(response.getData().getItemList());
                            getUiInterface().getDataIsSucess(true);
                        }else {
                            getUiInterface().getPlayBackList(new ArrayList<PlayBackListBean.ItemListBean>());
                            getUiInterface().getDataIsSucess(false);
                        }
                    }

                    @Override
                    public void onDataCode(BaseResponse<PlayBackListBean> response) {

                    }
                });
        addSubscription(subscription);
    }

    /**
     * 删除回放列表
     */

    public void delPlayBack(String id, final int position){
        Subscription subscription = NetManager.getInstance().create(PlayBackListApi.class)
                .delPlayBack(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getUiInterface().delSucess(response.getMessage(),position);
                    }

                    @Override
                    public void onDataCode(BaseResponse<Object> response) {

                    }
                });
        addSubscription(subscription);
    }
}
