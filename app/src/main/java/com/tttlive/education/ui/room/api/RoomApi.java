package com.tttlive.education.ui.room.api;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.VideoProfileBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by mr.li on 2018/4/18.
 */

public interface RoomApi {

    //https://dev.api.edu.3ttech.cn:8443/video/video-profile

    /**
     * 获取分辨率
     * @return
     */
    @GET("/video/video-profile")
    Observable<BaseResponse<VideoProfileBean>> getProfile();

}
