package com.tttlive.education.ui.home;


import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.CourseListBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Iverson on 2017/12/14 上午10:11
 * 此类用于：关于主页的api
 */

public interface HomeApi {

    //主页获取所有课程列表api
    @GET("/course/live-list")
    Observable<BaseResponse<CourseListBean>> getCourseList(@Query("userId")String userId,@Query("page")String page,@Query("size")String size);

    //删除课程
    @FormUrlEncoded
    @POST("/course/delete-course")
    Observable<BaseResponse<Object>> delCourse(@Field("userId")String userId, @Field("id")String id);
}
