package com.tttlive.education.ui.login;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.data.InviteCodeLoginBean;
import com.tttlive.education.ui.room.bean.LocationBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Iverson on 2018/3/7 上午11:23
 * 此类用于：
 */

public interface LoginApi {

    //邀请码登录
    @FormUrlEncoded
    @POST("/login/invite-code")
    Observable<BaseResponse<Object>> inviteCodeLogin(@Field("inviteCode") String inviteCode,
                                                                  @Field("nickName") String nickName);

    //账号登录
    @FormUrlEncoded
    @POST("/login/login")
    Observable<BaseResponse<Object>> accountLogin(@Field("userName") String inviteCode,
                                                            @Field("password") String nickName);
    //校验账号
    @FormUrlEncoded
    @POST("/user/verify-account")
    Observable<BaseResponse<Object>> verifyAccount(@Field("userName") String account);

    //发送短信验证码
    @FormUrlEncoded
    @POST("/user/get-mobile-captcha")
    Observable<BaseResponse<Object>> sendSMSCode(@Field("mobile") String mobile);

    //校验短信验证码
    @FormUrlEncoded
    @POST("/user/verify-mobile-captcha")
    Observable<BaseResponse<Object>> checkSMSCode(@Field("mobile") String mobile , @Field("code") String code);

    //忘记密码重置
    @FormUrlEncoded
    @POST("/user/reset-pwd")
    Observable<BaseResponse<Object>> resetPassword(@Field("mobile") String mobile , @Field("password") String password , @Field("code") String code , @Field("type") String type);

    //注册账号
    @FormUrlEncoded
    @POST("/user/register")
    Observable<BaseResponse<Object>> registerAccount(@Field("telephone")String telephone , @Field("password") String password , @Field("code") String code);



}
