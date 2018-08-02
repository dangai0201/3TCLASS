package com.tttlive.education.ui.room.bean;

/**
 *
 * Created by Administrator on 2018/6/7/0007.
 *
 * 发送奖杯
 *
 */
public class TrophyAwardBean {

    /**
     * data : {"userId":"900086","nickName":"张三"}
     * messageType : trophy_award_req
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
         * userId : 900086
         * nickName : 张三
         */

        private String userId;
        private String nickName;

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
    }
}
