package com.tttlive.education.ui.home;


import com.tttlive.education.base.BaseUiInterface;
import com.tttlive.education.data.CourseListBean;

/**
 * Created by Iverson on 2017/12/14 上午10:09
 * 此类用于：
 */

public interface HomePageUIinterface extends BaseUiInterface {

    void getCourseListSuccess(CourseListBean dataBean);
    //删除成功
    void delSucess(String msg, int layoutPosition);

}
