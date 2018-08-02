package com.tttlive.education.ui.login;

import com.tttlive.education.base.BaseUiInterface;
import com.tttlive.education.data.AccountLoginBean;

/**
 * Created by mrliu on 2018/3/12.
 * 此类用于:
 */

public interface TeacherLoginInterface extends BaseUiInterface {

    void loginSucess(AccountLoginBean accountLoginBean); //账号密码登录成功

    void loginFailure();
}
