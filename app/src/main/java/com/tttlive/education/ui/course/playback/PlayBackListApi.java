package com.tttlive.education.ui.course.playback;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.PlayBackListBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mrliu on 2018/3/12.
 * 此类用于:回放列表的api
 */

public interface PlayBackListApi {

    //获取回放列表
    @GET("/course/back-stream")
    Observable<BaseResponse<PlayBackListBean>> getPlayBackList(@Query("id")String id, @Query("page")String page, @Query("size")String size);
    //删除回放
    @FormUrlEncoded
    @POST("/course/delete-back-stream")
    Observable<BaseResponse<Object>> delPlayBack(@Field("id")String id);
}
