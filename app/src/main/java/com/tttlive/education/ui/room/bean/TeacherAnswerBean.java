package com.tttlive.education.ui.room.bean;

/**
 * Created by mr.li on 2018/6/11.
 */

public class TeacherAnswerBean{


    /**
     * data : {"correct":"BC","options":"ABCD","status":"ended"}
     * messageType : selector_status_req
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
         * correct : BC
         * options : ABCD
         * status : ended
         */

        private String correct;
        private String options;
        private String status;

        public String getCorrect() {
            return correct;
        }

        public void setCorrect(String correct) {
            this.correct = correct;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
