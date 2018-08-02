package com.tttlive.education.ui.course;

import com.tttlive.education.base.BaseResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Iverson on 2018/3/7 上午11:39
 * 此类用于：
 */

public interface CourseApi {

    //创建课程
    @FormUrlEncoded
    @POST("/course/create-course")
    Observable<BaseResponse<Object>> createCourse(@Field("userId") String userId, @Field("title") String title,
                                                  @Field("startTime") String startTime,@Field("type") String type,
                                                  @Field("duration") String duration,@Field("durationType") String durationType,
                                                  @Field("capacity") String capacity,@Field("applicationId")String applicationId,
                                                  @Field("userName")String userName,@Field("endTime")String endTime);
}
