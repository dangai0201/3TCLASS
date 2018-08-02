package com.tttlive.education.ui.login;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.base.BaseUiInterface;
import com.tttlive.education.ui.room.bean.LocationBean;

/**
 * Created by Administrator on 2018/7/9/0009.
 */

public interface SplashInterface extends BaseUiInterface {

    /**
     * 请求资源
     * @param locationBean
     */
    void locationSucess(BaseResponse<LocationBean> locationBean);

    /**
     * 请求失败
     */
    void getError();
}
