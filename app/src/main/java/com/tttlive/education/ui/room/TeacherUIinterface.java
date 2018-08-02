package com.tttlive.education.ui.room;

import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.base.BaseUiInterface;
import com.tttlive.education.ui.room.bean.UploadImageBean;

/**
 * Created by Administrator on 2018/3/12/0012.
 */

public interface TeacherUIinterface extends BaseUiInterface{


    /**
     * 加载课件列表
     * @param docsLists
     */
    void docSucess(DocsListbean docsLists);


    /**
     * 加载课件列表详情
     * @param docDetail
     */
    void docDetailsSucess(DocsListDetailsBean docDetail);

    /**
     *
     * 上传图片成功
     * @param uploadBean
     */
    void imageUploadSucess(BaseResponse<UploadImageBean> uploadBean);

}
