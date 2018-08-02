package com.tttlive.education.data;

import java.util.List;

/**
 * Created by mrliu on 2018/3/12.
 * 此类用于: 回放列表的bean
 */

public class PlayBackListBean {

    /**
     * page : 1
     * size : 10
     * totalPage : 1
     * totalCount : 2
     * itemList : [{"id":"200192","duration":"","videoSrc":"","title":"12"},{"id":"200064","duration":"","videoSrc":"","title":"12"}]
     */

    private int page;
    private int size;
    private int totalPage;
    private int totalCount;
    private List<ItemListBean> itemList;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean> itemList) {
        this.itemList = itemList;
    }

    public static class ItemListBean {
        /**
         * id : 200192
         * duration :
         * videoSrc :
         * title : 12
         */

        private String id;
        private String duration;
        private String videoSrc;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getVideoSrc() {
            return videoSrc;
        }

        public void setVideoSrc(String videoSrc) {
            this.videoSrc = videoSrc;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
