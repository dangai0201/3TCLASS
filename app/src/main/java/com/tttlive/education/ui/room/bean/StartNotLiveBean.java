package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/5/16/0016.
 */

public class StartNotLiveBean {

    /**
     * data : {"roomId":"85","userId":"800056","type":"1"}
     * messageType : course_start_req
     */

    private DataBean data;
    private String messageType;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public static class DataBean {
        /**
         * roomId : 85
         * userId : 800056
         * type : 1
         */

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
