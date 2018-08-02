package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/7/17/0017.
 * 授权关闭摄像头
 * 关闭麦克风
 */
public class VAClosedBean {

    /**
     * data : {"closed":true}
     * messageType : camera_closed_req
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
         * closed : true
         */

        private boolean closed;

        public boolean isClosed() {
            return closed;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }
    }
}
