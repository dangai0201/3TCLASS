package com.tttlive.education.ui.share;

import com.tttlive.education.base.BaseUiInterface;
import com.tttlive.education.ui.room.bean.ShareBean;

/**
 * Created by Administrator on 2018/5/18/0018.
 */
public interface ShareInterface extends BaseUiInterface{

    /**
     * 获取房间邀请码
     * @param shareBean
     */
    void shareRoomInvite(ShareBean shareBean);
}
