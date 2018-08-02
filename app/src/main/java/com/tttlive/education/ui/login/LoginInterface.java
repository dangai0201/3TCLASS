package com.tttlive.education.ui.login;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.base.BaseUiInterface;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.data.InviteCodeLoginBean;

/**
 * Created by mrliu on 2018/3/12.
 * 此类用于:登录界面接口回调
 */

public interface LoginInterface extends BaseUiInterface {

    /**
     * 登录成功
     * @param inviteCodeLoginBean
     */
    void inviteCodeLoginSuccess(InviteCodeLoginBean inviteCodeLoginBean); //邀请码登录成功

    /**
     * 邀请码登录失败
     * @param response
     */
    void inviteCodeLoginFailure(BaseResponse<Object> response);

    /**
     * 账号登录成功
     * @param accountLoginBean
     */
    void accountLoginSuccess(AccountLoginBean accountLoginBean);

    /**
     * 账号登录失败
     * @param response
     */
    void accountLoginFailure(BaseResponse<Object> response);

    /**
     * 校验账号
     * @param baceBean
     */
    void verifyAccount(BaseResponse<Object> baceBean);


    /**
     * 发送短信验证码
     */
    void getSMSVerificationCode(BaseResponse<Object> VerificationCode);


    /**
     * 校验短信验证码
     */
    void getCheckSMSCode(BaseResponse<Object> checkCode);

    /**
     * 忘记密码重置
     */
    void getForgetPassword(BaseResponse<Object> frogetPassword);

    /**
     * 注册账号
     */
    void getRegisterAccount(BaseResponse<Object> accountBaen);

    //登录失败
    void loginError();

}
