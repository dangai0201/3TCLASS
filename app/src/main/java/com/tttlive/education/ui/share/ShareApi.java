package com.tttlive.education.ui.share;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.ui.room.bean.ShareBean;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2018/5/18/0018.
 */

public interface ShareApi {

    @POST("/course/get-invite-code")
    Observable<BaseResponse<ShareBean>> getShareInviteCode(@Query("id")String id);

}
