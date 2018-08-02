package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/7/16/0016.
 * 白板信令
 */
public class WhiteBoardBean {

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

    public static class DataBean{
        private boolean access;

        public boolean isAccess() {
            return access;
        }

        public void setAccess(boolean access) {
            this.access = access;
        }
    }

}
