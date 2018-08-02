package com.tttlive.education.ui.room;



import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.ui.room.bean.UploadImageBean;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2018/3/12/0012.
 */

public interface TeacherApi {

    /**
     * //获取老师课件列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GET("/docs/document-list")
    Observable<BaseResponse<DocsListbean>> getTeacherDocsList(@Query("userId")String userId , @Query("page")String page , @Query("size")String size , @Query("originalName")String originalName);


    /**
     * 获取老师课件详情
     * @param id
     * @return
     */
    @GET("/docs/document-details")
    Observable<BaseResponse<DocsListDetailsBean>> getDocsListDetail(@Query("id")String id);

    /**
     * 上传本地图片
     * , @Part MultipartBody.Part file
     * @Part("file") RequestBody requestBody
     * @param file
     * @return
     */
    @Multipart
    @POST("/chat/upload")
    Observable<BaseResponse<UploadImageBean>> getImageUpload(@Part MultipartBody.Part file);


}
