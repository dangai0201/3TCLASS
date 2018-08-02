package com.tttlive.education.ui.course.playback;

import com.tttlive.education.base.BaseUiInterface;
import com.tttlive.education.data.PlayBackListBean;

import java.util.List;

/**
 * Created by mrliu on 2018/3/12.
 * 此类用于:回放列表接口回调
 */

public interface PlayBackListInterface extends BaseUiInterface{

    void getPlayBackList(List<PlayBackListBean.ItemListBean> data);//获取回放列表

    void getDataIsSucess(boolean flag);//获取回放列表成功

    void delSucess(String msg,int position);

}
