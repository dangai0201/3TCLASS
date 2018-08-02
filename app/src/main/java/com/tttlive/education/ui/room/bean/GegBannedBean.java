package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/4/8/0008.
 * 禁止.解除 发言
 */
public class GegBannedBean {

    /**
     * messageType : gag_req
     * data : {"roomId":"124","adminUserId":"321","userId":"123","avatar":"147","nickName":"456","level":"789","expiry":"256","message":""}
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
         * adminUserId : 321
         * userId : 123
         * avatar : 147
         * nickName : 456
         * level : 789
         * expiry : 256
         * message :
         */

        private String roomId;
        private String adminUserId;
        private String userId;
        private String avatar;
        private String nickName;
        private String level;
        private String expiry;
        private String message;

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

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
