package com.tttlive.education.ui.room.bean;

/**
 * Author: sunny
 * Time: 2018/8/30
 * description:
 */

public class CourseWareData {


    /**
     * slideId : 746
     * docTotalPage : 16
     * docCurrentPage : 3
     */

    private String slideId;
    private String docTotalPage;
    private String docCurrentPage;

    public String getSlideId() {
        return slideId;
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }

    public String getDocTotalPage() {
        return docTotalPage;
    }

    public void setDocTotalPage(String docTotalPage) {
        this.docTotalPage = docTotalPage;
    }

    public String getDocCurrentPage() {
        return docCurrentPage;
    }

    public void setDocCurrentPage(String docCurrentPage) {
        this.docCurrentPage = docCurrentPage;
    }

    @Override
    public String toString() {
        return "CourseWareData{" +
                "slideId='" + slideId + '\'' +
                ", docTotalPage='" + docTotalPage + '\'' +
                ", docCurrentPage='" + docCurrentPage + '\'' +
                '}';
    }
}
