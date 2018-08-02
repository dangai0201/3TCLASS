package com.tttlive.education.ui.room.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/12/0012.
 */

public class WebSocketResBean {

    /**
     * eType : 1
     * mProtocolMsg : {"sIDentify":"7B4FCDBC0914E7182B8EC357F07498F1","sSecret":"c052cd055acfabc5","sVersion":"Test_4.2.0.777","sConnectID":"1530864681000","bLoadBalance":true,"mIpAddrMsg":{"sIp":"47.93.36.144","nPort":5000,"eIpType":1,"sServerID":"6804015","mLocationMsg":{"sIP":"47.93.36.144","sCountry":"中国","sProvincialcapital":"北京","sCity":"北京","sOperator":"阿里云/电信/联通/移动/铁通/教育网"},"sDomain":"edu.service.3ttech.cn"},"nUserID":1127948,"sVerifyCode":"abcd","sAppID":"2d740137500f2d8eac0f3ea742498af3","mLocationMsg":{"sIP":"61.148.198.102","sCountry":"中国","sProvincialcapital":"北京","sCity":"北京","sOperator":"联通"},"sClientVer":"web_1.0","bReConnect":false,"bVipUser":false,"mGWIpAddrMsgList":[{"sIp":"47.93.36.144","nPort":5000,"eIpType":1,"sServerID":"6804015","mLocationMsg":{"sIP":"47.93.36.144","sCountry":"中国","sProvincialcapital":"北京","sCity":"北京","sOperator":"阿里云/电信/联通/移动/铁通/教育网"},"sDomain":"edu.service.3ttech.cn"}],"eUEType":6,"sWebIP":"61.148.198.102"}
     * eResult : 1
     * sSEQID : 111111111
     */

    private int eType;
    private MProtocolMsgBean mProtocolMsg;
    private int eResult;
    private String sSEQID;

    public int getEType() {
        return eType;
    }

    public void setEType(int eType) {
        this.eType = eType;
    }

    public MProtocolMsgBean getMProtocolMsg() {
        return mProtocolMsg;
    }

    public void setMProtocolMsg(MProtocolMsgBean mProtocolMsg) {
        this.mProtocolMsg = mProtocolMsg;
    }

    public int getEResult() {
        return eResult;
    }

    public void setEResult(int eResult) {
        this.eResult = eResult;
    }

    public String getSSEQID() {
        return sSEQID;
    }

    public void setSSEQID(String sSEQID) {
        this.sSEQID = sSEQID;
    }

    public static class MProtocolMsgBean {
        /**
         * sIDentify : 7B4FCDBC0914E7182B8EC357F07498F1
         * sSecret : c052cd055acfabc5
         * sVersion : Test_4.2.0.777
         * sConnectID : 1530864681000
         * bLoadBalance : true
         * mIpAddrMsg : {"sIp":"47.93.36.144","nPort":5000,"eIpType":1,"sServerID":"6804015","mLocationMsg":{"sIP":"47.93.36.144","sCountry":"中国","sProvincialcapital":"北京","sCity":"北京","sOperator":"阿里云/电信/联通/移动/铁通/教育网"},"sDomain":"edu.service.3ttech.cn"}
         * nUserID : 1127948
         * sVerifyCode : abcd
         * sAppID : 2d740137500f2d8eac0f3ea742498af3
         * mLocationMsg : {"sIP":"61.148.198.102","sCountry":"中国","sProvincialcapital":"北京","sCity":"北京","sOperator":"联通"}
         * sClientVer : web_1.0
         * bReConnect : false
         * bVipUser : false
         * mGWIpAddrMsgList : [{"sIp":"47.93.36.144","nPort":5000,"eIpType":1,"sServerID":"6804015","mLocationMsg":{"sIP":"47.93.36.144","sCountry":"中国","sProvincialcapital":"北京","sCity":"北京","sOperator":"阿里云/电信/联通/移动/铁通/教育网"},"sDomain":"edu.service.3ttech.cn"}]
         * eUEType : 6
         * sWebIP : 61.148.198.102
         */

        private String sIDentify;
        private String sSecret;
        private String sVersion;
        private String sConnectID;
        private boolean bLoadBalance;
        private MIpAddrMsgBean mIpAddrMsg;
        private int nUserID;
        private String sVerifyCode;
        private String sAppID;
        private MLocationMsgBeanX mLocationMsg;
        private String sClientVer;
        private boolean bReConnect;
        private boolean bVipUser;
        private int eUEType;
        private String sWebIP;
        private List<MGWIpAddrMsgListBean> mGWIpAddrMsgList;

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

        public String getSVersion() {
            return sVersion;
        }

        public void setSVersion(String sVersion) {
            this.sVersion = sVersion;
        }

        public String getSConnectID() {
            return sConnectID;
        }

        public void setSConnectID(String sConnectID) {
            this.sConnectID = sConnectID;
        }

        public boolean isBLoadBalance() {
            return bLoadBalance;
        }

        public void setBLoadBalance(boolean bLoadBalance) {
            this.bLoadBalance = bLoadBalance;
        }

        public MIpAddrMsgBean getMIpAddrMsg() {
            return mIpAddrMsg;
        }

        public void setMIpAddrMsg(MIpAddrMsgBean mIpAddrMsg) {
            this.mIpAddrMsg = mIpAddrMsg;
        }

        public int getNUserID() {
            return nUserID;
        }

        public void setNUserID(int nUserID) {
            this.nUserID = nUserID;
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

        public MLocationMsgBeanX getMLocationMsg() {
            return mLocationMsg;
        }

        public void setMLocationMsg(MLocationMsgBeanX mLocationMsg) {
            this.mLocationMsg = mLocationMsg;
        }

        public String getSClientVer() {
            return sClientVer;
        }

        public void setSClientVer(String sClientVer) {
            this.sClientVer = sClientVer;
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

        public int getEUEType() {
            return eUEType;
        }

        public void setEUEType(int eUEType) {
            this.eUEType = eUEType;
        }

        public String getSWebIP() {
            return sWebIP;
        }

        public void setSWebIP(String sWebIP) {
            this.sWebIP = sWebIP;
        }

        public List<MGWIpAddrMsgListBean> getMGWIpAddrMsgList() {
            return mGWIpAddrMsgList;
        }

        public void setMGWIpAddrMsgList(List<MGWIpAddrMsgListBean> mGWIpAddrMsgList) {
            this.mGWIpAddrMsgList = mGWIpAddrMsgList;
        }

        public static class MIpAddrMsgBean {
            /**
             * sIp : 47.93.36.144
             * nPort : 5000
             * eIpType : 1
             * sServerID : 6804015
             * mLocationMsg : {"sIP":"47.93.36.144","sCountry":"中国","sProvincialcapital":"北京","sCity":"北京","sOperator":"阿里云/电信/联通/移动/铁通/教育网"}
             * sDomain : edu.service.3ttech.cn
             */

            private String sIp;
            private int nPort;
            private int eIpType;
            private String sServerID;
            private MLocationMsgBean mLocationMsg;
            private String sDomain;

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

            public int getEIpType() {
                return eIpType;
            }

            public void setEIpType(int eIpType) {
                this.eIpType = eIpType;
            }

            public String getSServerID() {
                return sServerID;
            }

            public void setSServerID(String sServerID) {
                this.sServerID = sServerID;
            }

            public MLocationMsgBean getMLocationMsg() {
                return mLocationMsg;
            }

            public void setMLocationMsg(MLocationMsgBean mLocationMsg) {
                this.mLocationMsg = mLocationMsg;
            }

            public String getSDomain() {
                return sDomain;
            }

            public void setSDomain(String sDomain) {
                this.sDomain = sDomain;
            }

            public static class MLocationMsgBean {
                /**
                 * sIP : 47.93.36.144
                 * sCountry : 中国
                 * sProvincialcapital : 北京
                 * sCity : 北京
                 * sOperator : 阿里云/电信/联通/移动/铁通/教育网
                 */

                private String sIP;
                private String sCountry;
                private String sProvincialcapital;
                private String sCity;
                private String sOperator;

                public String getSIP() {
                    return sIP;
                }

                public void setSIP(String sIP) {
                    this.sIP = sIP;
                }

                public String getSCountry() {
                    return sCountry;
                }

                public void setSCountry(String sCountry) {
                    this.sCountry = sCountry;
                }

                public String getSProvincialcapital() {
                    return sProvincialcapital;
                }

                public void setSProvincialcapital(String sProvincialcapital) {
                    this.sProvincialcapital = sProvincialcapital;
                }

                public String getSCity() {
                    return sCity;
                }

                public void setSCity(String sCity) {
                    this.sCity = sCity;
                }

                public String getSOperator() {
                    return sOperator;
                }

                public void setSOperator(String sOperator) {
                    this.sOperator = sOperator;
                }
            }
        }

        public static class MLocationMsgBeanX {
            /**
             * sIP : 61.148.198.102
             * sCountry : 中国
             * sProvincialcapital : 北京
             * sCity : 北京
             * sOperator : 联通
             */

            private String sIP;
            private String sCountry;
            private String sProvincialcapital;
            private String sCity;
            private String sOperator;

            public String getSIP() {
                return sIP;
            }

            public void setSIP(String sIP) {
                this.sIP = sIP;
            }

            public String getSCountry() {
                return sCountry;
            }

            public void setSCountry(String sCountry) {
                this.sCountry = sCountry;
            }

            public String getSProvincialcapital() {
                return sProvincialcapital;
            }

            public void setSProvincialcapital(String sProvincialcapital) {
                this.sProvincialcapital = sProvincialcapital;
            }

            public String getSCity() {
                return sCity;
            }

            public void setSCity(String sCity) {
                this.sCity = sCity;
            }

            public String getSOperator() {
                return sOperator;
            }

            public void setSOperator(String sOperator) {
                this.sOperator = sOperator;
            }
        }

        public static class MGWIpAddrMsgListBean {
            /**
             * sIp : 47.93.36.144
             * nPort : 5000
             * eIpType : 1
             * sServerID : 6804015
             * mLocationMsg : {"sIP":"47.93.36.144","sCountry":"中国","sProvincialcapital":"北京","sCity":"北京","sOperator":"阿里云/电信/联通/移动/铁通/教育网"}
             * sDomain : edu.service.3ttech.cn
             */

            private String sIp;
            private int nPort;
            private int eIpType;
            private String sServerID;
            private MLocationMsgBeanXX mLocationMsg;
            private String sDomain;

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

            public int getEIpType() {
                return eIpType;
            }

            public void setEIpType(int eIpType) {
                this.eIpType = eIpType;
            }

            public String getSServerID() {
                return sServerID;
            }

            public void setSServerID(String sServerID) {
                this.sServerID = sServerID;
            }

            public MLocationMsgBeanXX getMLocationMsg() {
                return mLocationMsg;
            }

            public void setMLocationMsg(MLocationMsgBeanXX mLocationMsg) {
                this.mLocationMsg = mLocationMsg;
            }

            public String getSDomain() {
                return sDomain;
            }

            public void setSDomain(String sDomain) {
                this.sDomain = sDomain;
            }

            public static class MLocationMsgBeanXX {
                /**
                 * sIP : 47.93.36.144
                 * sCountry : 中国
                 * sProvincialcapital : 北京
                 * sCity : 北京
                 * sOperator : 阿里云/电信/联通/移动/铁通/教育网
                 */

                private String sIP;
                private String sCountry;
                private String sProvincialcapital;
                private String sCity;
                private String sOperator;

                public String getSIP() {
                    return sIP;
                }

                public void setSIP(String sIP) {
                    this.sIP = sIP;
                }

                public String getSCountry() {
                    return sCountry;
                }

                public void setSCountry(String sCountry) {
                    this.sCountry = sCountry;
                }

                public String getSProvincialcapital() {
                    return sProvincialcapital;
                }

                public void setSProvincialcapital(String sProvincialcapital) {
                    this.sProvincialcapital = sProvincialcapital;
                }

                public String getSCity() {
                    return sCity;
                }

                public void setSCity(String sCity) {
                    this.sCity = sCity;
                }

                public String getSOperator() {
                    return sOperator;
                }

                public void setSOperator(String sOperator) {
                    this.sOperator = sOperator;
                }
            }
        }
    }
}
