package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/3/22/0022.
 */

public class LmDisconnectBean {
    private String messageType;
    private DisData data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public DisData getData() {
        return data;
    }

    public void setData(DisData data) {
        this.data = data;
    }

    public static class DisData{
        private String roomId;
        private String adminUserId;
        private String userId;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(String adminUserId) {
            this.adminUserId = adminUserId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
