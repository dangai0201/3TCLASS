package com.tttlive.education.ui.room.liveLand;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.base.BaseUiInterface;

/**
 * Author: sunny
 * Time: 2018/8/27
 * description:
 */

public interface HelpInterface extends BaseUiInterface{

    //发送成功
    void onSendSuccess(BaseResponse<Object> response);

    //发送失败
    void onSendFailed(BaseResponse<Object> response);
}
