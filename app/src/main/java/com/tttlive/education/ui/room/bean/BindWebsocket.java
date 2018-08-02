package com.tttlive.education.ui.room.bean;

import java.util.List;

/**
 * Created by mr.li on 2018/4/3.
 */

public class BindWebsocket {


    /**
     * eType : 17
     * mChatTransMsg : {"mRouterTableMsg":{"eRouterType":2,"nUserID":949,"eUEType":1,"sMediaIDList":["24_testtest_1"],"bServer":false,"nGroupID":24,"sAppID":"testtest","sConnectID":"524be852-ac34-4c0a-938d-2da6ccff933c"},"eBusinessType":1,"sSeqID":"524be852-ac34-4c0a-938d-2da6ccff933c"}
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
         * mRouterTableMsg : {"eRouterType":2,"nUserID":949,"eUEType":1,"sMediaIDList":["24_testtest_1"],"bServer":false,"nGroupID":24,"sAppID":"testtest","sConnectID":"524be852-ac34-4c0a-938d-2da6ccff933c"}
         * eBusinessType : 1
         * sSeqID : 524be852-ac34-4c0a-938d-2da6ccff933c
         */

        private MRouterTableMsgBean mRouterTableMsg;
        private int eBusinessType;
        private String sSeqID;

        public MRouterTableMsgBean getMRouterTableMsg() {
            return mRouterTableMsg;
        }

        public void setMRouterTableMsg(MRouterTableMsgBean mRouterTableMsg) {
            this.mRouterTableMsg = mRouterTableMsg;
        }

        public int getEBusinessType() {
            return eBusinessType;
        }

        public void setEBusinessType(int eBusinessType) {
            this.eBusinessType = eBusinessType;
        }

        public String getSSeqID() {
            return sSeqID;
        }

        public void setSSeqID(String sSeqID) {
            this.sSeqID = sSeqID;
        }

        public static class MRouterTableMsgBean {
            /**
             * eRouterType : 2
             * nUserID : 949
             * eUEType : 1
             * sMediaIDList : ["24_testtest_1"]
             * bServer : false
             * nGroupID : 24
             * sAppID : testtest
             * sConnectID : 524be852-ac34-4c0a-938d-2da6ccff933c
             */

            private int eRouterType;
            private int nUserID;
            private int eUEType;
            private boolean bServer;
            private int nGroupID;
            private String sAppID;
            private String sConnectID;
            private List<String> sMediaIDList;

            public int getERouterType() {
                return eRouterType;
            }

            public void setERouterType(int eRouterType) {
                this.eRouterType = eRouterType;
            }

            public int getNUserID() {
                return nUserID;
            }

            public void setNUserID(int nUserID) {
                this.nUserID = nUserID;
            }

            public int getEUEType() {
                return eUEType;
            }

            public void setEUEType(int eUEType) {
                this.eUEType = eUEType;
            }

            public boolean isBServer() {
                return bServer;
            }

            public void setBServer(boolean bServer) {
                this.bServer = bServer;
            }

            public int getNGroupID() {
                return nGroupID;
            }

            public void setNGroupID(int nGroupID) {
                this.nGroupID = nGroupID;
            }

            public String getSAppID() {
                return sAppID;
            }

            public void setSAppID(String sAppID) {
                this.sAppID = sAppID;
            }

            public String getSConnectID() {
                return sConnectID;
            }

            public void setSConnectID(String sConnectID) {
                this.sConnectID = sConnectID;
            }

            public List<String> getSMediaIDList() {
                return sMediaIDList;
            }

            public void setSMediaIDList(List<String> sMediaIDList) {
                this.sMediaIDList = sMediaIDList;
            }
        }
    }
}
