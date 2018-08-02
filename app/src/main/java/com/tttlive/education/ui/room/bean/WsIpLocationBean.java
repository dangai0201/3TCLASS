package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/7/12/0012.
 */

public class WsIpLocationBean {


    /**
     * eType : 1
     * sSEQID : 111111111
     * mProtocolMsg : {"sVersion":"Test_4.2.0.777","bLoadBalance":false,"bReConnect":false,"bVipUser":false,"sIDentify":"7B4FCDBC0914E7182B8EC357F07498F1","sSecret":"c052cd055acfabc5","sVerifyCode":"abcd","sAppID":"2d740137500f2d8eac0f3ea742498af3","nUserID":1127948,"sClientVer":"web_1.0","sConnectID":"1530864681000","mIpAddrMsg":{"bFec":false,"sIp":"dev1.api.demo.3ttech.cn","nPort":0}}
     */

    private int eType;
    private String sSEQID;
    private MProtocolMsgBean mProtocolMsg;

    public int getEType() {
        return eType;
    }

    public void setEType(int eType) {
        this.eType = eType;
    }

    public String getSSEQID() {
        return sSEQID;
    }

    public void setSSEQID(String sSEQID) {
        this.sSEQID = sSEQID;
    }

    public MProtocolMsgBean getMProtocolMsg() {
        return mProtocolMsg;
    }

    public void setMProtocolMsg(MProtocolMsgBean mProtocolMsg) {
        this.mProtocolMsg = mProtocolMsg;
    }

    public static class MProtocolMsgBean {
        /**
         * sVersion : Test_4.2.0.777
         * bLoadBalance : false
         * bReConnect : false
         * bVipUser : false
         * sIDentify : 7B4FCDBC0914E7182B8EC357F07498F1
         * sSecret : c052cd055acfabc5
         * sVerifyCode : abcd
         * sAppID : 2d740137500f2d8eac0f3ea742498af3
         * nUserID : 1127948
         * sClientVer : web_1.0
         * sConnectID : 1530864681000
         * mIpAddrMsg : {"bFec":false,"sIp":"dev1.api.demo.3ttech.cn","nPort":0}
         */

        private String sVersion;
        private boolean bLoadBalance;
        private boolean bReConnect;
        private boolean bVipUser;
        private String sIDentify;
        private String sSecret;
        private String sVerifyCode;
        private String sAppID;
        private int nUserID;
        private String sClientVer;
        private String sConnectID;
        private MIpAddrMsgBean mIpAddrMsg;

        public String getSVersion() {
            return sVersion;
        }

        public void setSVersion(String sVersion) {
            this.sVersion = sVersion;
        }

        public boolean isBLoadBalance() {
            return bLoadBalance;
        }

        public void setBLoadBalance(boolean bLoadBalance) {
            this.bLoadBalance = bLoadBalance;
        }

        public boolean isBReConnect() {
            return bReConnect;
        }

        public void setBReConnect(boolean bReConnect) {
            this.bReConnect = bReConnect;
        }

        public boolean isBVipUser() {
            return bVipUser;
        }

        public void setBVipUser(boolean bVipUser) {
            this.bVipUser = bVipUser;
        }

        public String getSIDentify() {
            return sIDentify;
        }

        public void setSIDentify(String sIDentify) {
            this.sIDentify = sIDentify;
        }

        public String getSSecret() {
            return sSecret;
        }

        public void setSSecret(String sSecret) {
            this.sSecret = sSecret;
        }

        public String getSVerifyCode() {
            return sVerifyCode;
        }

        public void setSVerifyCode(String sVerifyCode) {
            this.sVerifyCode = sVerifyCode;
        }

        public String getSAppID() {
            return sAppID;
        }

        public void setSAppID(String sAppID) {
            this.sAppID = sAppID;
        }

        public int getNUserID() {
            return nUserID;
        }

        public void setNUserID(int nUserID) {
            this.nUserID = nUserID;
        }

        public String getSClientVer() {
            return sClientVer;
        }

        public void setSClientVer(String sClientVer) {
            this.sClientVer = sClientVer;
        }

        public String getSConnectID() {
            return sConnectID;
        }

        public void setSConnectID(String sConnectID) {
            this.sConnectID = sConnectID;
        }

        public MIpAddrMsgBean getMIpAddrMsg() {
            return mIpAddrMsg;
        }

        public void setMIpAddrMsg(MIpAddrMsgBean mIpAddrMsg) {
            this.mIpAddrMsg = mIpAddrMsg;
        }

        public static class MIpAddrMsgBean {
            /**
             * bFec : false
             * sIp : dev1.api.demo.3ttech.cn
             * nPort : 0
             */

            private boolean bFec;
            private String sIp;
            private int nPort;

            public boolean isBFec() {
                return bFec;
            }

            public void setBFec(boolean bFec) {
                this.bFec = bFec;
            }

            public String getSIp() {
                return sIp;
            }

            public void setSIp(String sIp) {
                this.sIp = sIp;
            }

            public int getNPort() {
                return nPort;
            }

            public void setNPort(int nPort) {
                this.nPort = nPort;
            }
        }
    }
}
