package com.tttlive.education.ui.room.bean;

/**
 * Created by mr.li on 2018/6/11.
 */

public class StudentAnswerBean {

    /**
     * data : {"nickName":"张三","reply":"BC","userId":"900074"}
     * messageType : selector_reply_req
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
         * nickName : 张三
         * reply : BC
         * userId : 900074
         */

        private String nickName;
        private String reply;
        private String userId;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
