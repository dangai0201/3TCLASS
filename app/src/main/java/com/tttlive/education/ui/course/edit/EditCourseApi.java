package com.tttlive.education.ui.course.edit;

import com.tttlive.education.base.BaseResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by mrliu on 2018/3/10.
 * 此类用于:
 */

public interface EditCourseApi {

    //编辑课程
    @FormUrlEncoded
    @POST("/course/edit-course")
    Observable<BaseResponse<Object>> editCourse(@Field("id")String id,@Field("type")String type,@Field("title")String title
            ,@Field("startTime")String startTime,@Field("duration")String duration,@Field("durationType")String durationType
            ,@Field("capacity")String capacity,@Field("coverSrc")String coverSrc,@Field("joinRule")String joinRule,@Field("desc")String desc
            ,@Field("userId")String userId,@Field("timeEnd")String timeEnd) ;

}
