package com.tttlive.education.ui.room.api;

import com.tttlive.education.base.BaseResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Author: sunny
 * Time: 2018/8/27
 * description:
 */

public interface HelpApi {

    @FormUrlEncoded
    @POST("user")
    Observable<BaseResponse<Object>> sendQuestion(@FieldMap Map<String, String> params);
}
