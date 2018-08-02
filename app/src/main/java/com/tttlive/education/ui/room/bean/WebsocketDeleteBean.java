package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/5/15/0015.
 */

public class WebsocketDeleteBean {

    /**
     * eType : 17
     * mChatTransMsg : {"eChatDataType":7,"mWBMgrMsg":{"eWBMgrType":1,"sAppID":"2d740137500f2d8eac0f3ea742498af3","nGroupID":900009}}
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
         * eChatDataType : 7
         * mWBMgrMsg : {"eWBMgrType":1,"sAppID":"2d740137500f2d8eac0f3ea742498af3","nGroupID":900009}
         */

        private int eChatDataType;
        private MWBMgrMsgBean mWBMgrMsg;

        public int getEChatDataType() {
            return eChatDataType;
        }

        public void setEChatDataType(int eChatDataType) {
            this.eChatDataType = eChatDataType;
        }

        public MWBMgrMsgBean getMWBMgrMsg() {
            return mWBMgrMsg;
        }

        public void setMWBMgrMsg(MWBMgrMsgBean mWBMgrMsg) {
            this.mWBMgrMsg = mWBMgrMsg;
        }

        public static class MWBMgrMsgBean {
            /**
             * eWBMgrType : 1
             * sAppID : 2d740137500f2d8eac0f3ea742498af3
             * nGroupID : 900009
             */

            private int eWBMgrType;
            private String sAppID;
            private int nGroupID;

            public int getEWBMgrType() {
                return eWBMgrType;
            }

            public void setEWBMgrType(int eWBMgrType) {
                this.eWBMgrType = eWBMgrType;
            }

            public String getSAppID() {
                return sAppID;
            }

            public void setSAppID(String sAppID) {
                this.sAppID = sAppID;
            }

            public int getNGroupID() {
                return nGroupID;
            }

            public void setNGroupID(int nGroupID) {
                this.nGroupID = nGroupID;
            }
        }
    }
}
