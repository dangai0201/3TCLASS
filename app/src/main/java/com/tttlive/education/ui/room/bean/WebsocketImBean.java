package com.tttlive.education.ui.room.bean;

/**
 * Created by mr.li on 2018/4/3.
 */

public class WebsocketImBean{


    /**
     * eType : 17
     * mChatTransMsg : {"sMediaID":"24_testtest_1","eChatDataType":1,"nSrcUserID":949,"nDstUserID":0,"sData":"cccccccc","nDataLen":8,"sSeqID":"0","sAppID":"testtest","bPersistence":true,"eBusinessType":1,"nGroupID":24}
     */

    private int eType;
    private MChatTransMsgBean mChatTransMsg;

    public int getEType() {
        return eType;
    }

    public void setEType(int eType) {
        this.eType = eType;
    }

    public MChatTransMsgBean getMChatTransMsg() {
        return mChatTransMsg;
    }

    public void setMChatTransMsg(MChatTransMsgBean mChatTransMsg) {
        this.mChatTransMsg = mChatTransMsg;
    }

    public static class MChatTransMsgBean {
        /**
         * sMediaID : 24_testtest_1
         * eChatDataType : 1
         * nSrcUserID : 949
         * nDstUserID : 0
         * sData : cccccccc
         * nDataLen : 8
         * sSeqID : 0
         * sAppID : testtest
         * bPersistence : true
         * eBusinessType : 1
         * nGroupID : 24
         */

        private String sMediaID;
        private int eChatDataType;
        private int nSrcUserID;
        private int nDstUserID;
        private String sData;
        private int nDataLen;
        private String sSeqID;
        private String sAppID;
        private boolean bPersistence;
        private int eBusinessType;
        private int nGroupID;

        public String getSMediaID() {
            return sMediaID;
        }

        public void setSMediaID(String sMediaID) {
            this.sMediaID = sMediaID;
        }

        public int getEChatDataType() {
            return eChatDataType;
        }

        public void setEChatDataType(int eChatDataType) {
            this.eChatDataType = eChatDataType;
        }

        public int getNSrcUserID() {
            return nSrcUserID;
        }

        public void setNSrcUserID(int nSrcUserID) {
            this.nSrcUserID = nSrcUserID;
        }

        public int getNDstUserID() {
            return nDstUserID;
        }

        public void setNDstUserID(int nDstUserID) {
            this.nDstUserID = nDstUserID;
        }

        public String getSData() {
            return sData;
        }

        public void setSData(String sData) {
            this.sData = sData;
        }

        public int getNDataLen() {
            return nDataLen;
        }

        public void setNDataLen(int nDataLen) {
            this.nDataLen = nDataLen;
        }

        public String getSSeqID() {
            return sSeqID;
        }

        public void setSSeqID(String sSeqID) {
            this.sSeqID = sSeqID;
        }

        public String getSAppID() {
            return sAppID;
        }

        public void setSAppID(String sAppID) {
            this.sAppID = sAppID;
        }

        public boolean isBPersistence() {
            return bPersistence;
        }

        public void setBPersistence(boolean bPersistence) {
            this.bPersistence = bPersistence;
        }

        public int getEBusinessType() {
            return eBusinessType;
        }

        public void setEBusinessType(int eBusinessType) {
            this.eBusinessType = eBusinessType;
        }

        public int getNGroupID() {
            return nGroupID;
        }

        public void setNGroupID(int nGroupID) {
            this.nGroupID = nGroupID;
        }
    }
}
