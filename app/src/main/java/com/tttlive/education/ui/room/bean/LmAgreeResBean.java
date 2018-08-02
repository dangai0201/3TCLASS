package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/3/21/0021.
 */

public class LmAgreeResBean {

    private String messageType;
    private LmDataBean data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public LmDataBean getData() {
        return data;
    }

    public void setData(LmDataBean data) {
        this.data = data;
    }

    public static class LmDataBean{
        private String roomId;
        private String userId;
        private String type;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
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
    }
}
