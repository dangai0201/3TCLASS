package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/4/26/0026.
 */

public class StartLiveBean {

    private String messageType;
    private Livebean data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Livebean getData() {
        return data;
    }

    public void setData(Livebean data) {
        this.data = data;
    }

    public static class Livebean{
        private String roomId;
        private String userId;

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
    }
}
