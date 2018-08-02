package com.tttlive.education.ui.login;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.ui.room.bean.LocationBean;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2018/7/9/0009.
 */

public interface SplashApi {

    //获取服务配置资源
    @POST("/server/location")
    Observable<BaseResponse<LocationBean>> locationReq();

}
