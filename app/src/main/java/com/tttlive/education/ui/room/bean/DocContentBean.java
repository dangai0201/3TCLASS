package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/3/21/0021.
 */

public class DocContentBean {

    private String messageType;
    private DocDataBean data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public DocDataBean getData() {
        return data;
    }

    public void setData(DocDataBean data) {
        this.data = data;
    }

    public static class DocDataBean{
        private String roomId;
        private String imgSrc;
        private String docId;
        private String page;
        private String current;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }
    }

}
