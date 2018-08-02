package com.tttlive.education.ui.room.bean;

/**
 * Created by mr.li on 2018/3/19.
 *
 * 离开房间
 */

public class LeaveMassageBean {


    /**
     * messageType : leave_req
     * data : {"roomId":124,"userId":321,"avatar":"avatar","nickName":"nickName","level":"level","isMaster":0,"message":""}
     */

    private String messageType;
    private DataBean data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * roomId : 124
         * userId : 321
         * avatar : avatar
         * nickName : nickName
         * level : level
         * isMaster : 0
         * message :
         */

        private int roomId;
        private String userId;
        private String avatar;
        private String nickName;
        private String level;
        private int isMaster;
        private String message;

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getIsMaster() {
            return isMaster;
        }

        public void setIsMaster(int isMaster) {
            this.isMaster = isMaster;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
