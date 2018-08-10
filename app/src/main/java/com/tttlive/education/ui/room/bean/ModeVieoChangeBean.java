package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/8/10/0010.
 */

public class ModeVieoChangeBean {

    /**
     * data : {"mode":"video"}
     * messageType : mode_change_req
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
         * mode : video
         */

        private String mode;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
