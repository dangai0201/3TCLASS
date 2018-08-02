package com.tttlive.education.ui.share;

import com.tttlive.education.base.BaseObserver;
import com.tttlive.education.base.BasePresenter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.net.NetManager;
import com.tttlive.education.ui.room.bean.ShareBean;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/18/0018.
 */

public class SharePresenter extends BasePresenter<ShareInterface> {

    public SharePresenter(ShareInterface uiInterface) {
        super(uiInterface);
    }


    /**
     * 获取房间邀请码
     * @param roomId
     */
    public void getShareInvite(String roomId){
        Subscription subscription = NetManager.getInstance().create(ShareApi.class)
                .getShareInviteCode(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<ShareBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<ShareBean> response) {
                        getUiInterface().shareRoomInvite(response.getData());
                    }

                    @Override
                    public void onDataCode(BaseResponse<ShareBean> response) {

                    }
                });
        addSubscription(subscription);


    }



}
