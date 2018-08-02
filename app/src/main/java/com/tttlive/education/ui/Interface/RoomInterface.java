package com.tttlive.education.ui.Interface;

import com.tttlive.education.base.BaseUiInterface;

/**
 * Created by Administrator on 2018/7/5/0005.
 */

public interface RoomInterface extends BaseUiInterface {


    /**
     * 本地ping服务器
     * 丢包率
     * @param lineLost
     */
    void pingLineLost(String lineLost);

    /**
     * 本地ping服务器
     * 网络延迟
     * @param delay
     */
    void pingLineDelay(String delay);

    /**
     * 本地ping服务器
     * 网络断开
     */
    void pingHttpDis();
}
