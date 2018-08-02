package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/3/15/0015.
 * 发送文字消息类
 */
public class SendTextMessageBean {

    /**
     * messageType : barrage_req
     * data : {"roomId":"124","userId":"321","nickName":"nickName","avatar":"avatar","message":"message"}
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
         */

        private String roomId;
        private String userId;
        private String nickName;
        private String avatar;
        private String message;
        private int type;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
