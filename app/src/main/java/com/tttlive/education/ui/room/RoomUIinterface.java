package com.tttlive.education.ui.room;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.base.BaseUiInterface;
import com.tttlive.education.ui.room.bean.UploadImageBean;

/**
 * Created by mr.li on 2018/4/18.
 */

public interface RoomUIinterface extends BaseUiInterface {

    void getProfileSuccess(int i);

    /**
     *
     * 上传图片成功
     * @param uploadBean
     */
    void imageUploadSucess(BaseResponse<UploadImageBean> uploadBean);


    /**
     * 请求分辨率失败
     */
    void getError();


}
