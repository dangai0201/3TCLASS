package com.tttlive.education.ui.room.bean;

/**
 * Created by mr.li on 2018/3/19.
 *
 * 申请连麦
 */
public class LmBean {

    /**
     * data : {"adminUserId":"12345","nickName":"nickName","roomId":"124","userId":"321"}
     * messageType : lm_req
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
         * adminUserId : 12345
         * nickName : nickName
         * roomId : 124
         * userId : 321
         */

        private String adminUserId;
        private String nickName;
        private String roomId;
        private String userId;

        public String getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(String adminUserId) {
            this.adminUserId = adminUserId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
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
    }
}
