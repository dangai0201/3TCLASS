package com.tttlive.education.ui.room.bean;

/**
 * Created by mr.li on 2018/3/17.
 *
 * 加人房间
 */

public class JoinRoomBean {

    /**
     * messageType : join_reqes
     * data : {"roomId":"124","userId":"321","nickName":"nickName","avatar":"avatar","message":"message","role":0,"level":5,"masterUserId":111,"masterNickName":"masterNickName","masterAvatar":"masterAvatar","masterLevel":9}
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
         * nickName : nickName
         * avatar : avatar
         * message : message
         * role : 0
         * level : 5
         * masterUserId : 111
         * masterNickName : masterNickName
         * masterAvatar : masterAvatar
         * masterLevel : 9
         */

        private String roomId;
        private String userId;
        private String nickName;
        private String avatar;
        private String message;
        private int role;
        private int level;
        private int masterUserId;
        private int masterLevel;
        private String masterNickName;
        private String masterAvatar;
        private String balance;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getMasterUserId() {
            return masterUserId;
        }

        public void setMasterUserId(int masterUserId) {
            this.masterUserId = masterUserId;
        }

        public String getMasterNickName() {
            return masterNickName;
        }

        public void setMasterNickName(String masterNickName) {
            this.masterNickName = masterNickName;
        }

        public String getMasterAvatar() {
            return masterAvatar;
        }

        public void setMasterAvatar(String masterAvatar) {
            this.masterAvatar = masterAvatar;
        }

        public int getMasterLevel() {
            return masterLevel;
        }

        public void setMasterLevel(int masterLevel) {
            this.masterLevel = masterLevel;
        }
    }
}
