package com.tttlive.education.ui.room;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12/0012.
 */

public class DocsListDetailsBean {


    /**
     * id : 1000005
     * applicationId : 123321123
     * userId : 700009
     * type : 2
     * page : 12
     * originalName : 国士无双--游戏行业解决方案.pptx
     * originalSrc : http://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0659_4386.pptx
     * imgSrcData : ["http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-0.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-1.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-2.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-3.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-4.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-5.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-6.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-7.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-8.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-9.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-10.jpg","http:\/\/3tdoc.oss-cn-beijing.aliyuncs.com\/docs\/2018\/03\/08\/17\/0728_3559-11.jpg"]
     * htmlUrl: https://pfiles.ebh.net/222/222.html
     * convert : 1
     * created : 1520500048
     * updated : 1520500048
     * imgSrc : ["https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-0.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-1.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-2.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-3.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-4.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-5.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-6.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-7.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-8.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-9.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-10.jpg","https://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/03/08/17/0728_3559-11.jpg"]
     */

    private String id;
    private String applicationId;
    private String userId;
    private String type;
    private String page;
    private String originalName;
    private String originalSrc;
    private String imgSrcData;
    private String htmlUrl;
    private String convert;
    private String created;
    private String updated;
    private List<String> imgSrc;

    @Override
    public String toString() {
        return "DocsListDetailsBean{" +
                "id='" + id + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", page='" + page + '\'' +
                ", originalName='" + originalName + '\'' +
                ", originalSrc='" + originalSrc + '\'' +
                ", imgSrcData='" + imgSrcData + '\'' +
                ", convert='" + convert + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", imgSrc=" + imgSrc +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOriginalSrc() {
        return originalSrc;
    }

    public void setOriginalSrc(String originalSrc) {
        this.originalSrc = originalSrc;
    }

    public String getImgSrcData() {
        return imgSrcData;
    }

    public void setImgSrcData(String imgSrcData) {
        this.imgSrcData = imgSrcData;
    }

    public String getConvert() {
        return convert;
    }

    public void setConvert(String convert) {
        this.convert = convert;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<String> getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(List<String> imgSrc) {
        this.imgSrc = imgSrc;
    }
}
