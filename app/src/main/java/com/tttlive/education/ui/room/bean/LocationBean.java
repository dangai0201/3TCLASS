package com.tttlive.education.ui.room.bean;

/**
 * Created by Administrator on 2018/7/6/0006.
 */
public class LocationBean {


    /**
     * docsWbUrl : https://dev.3ttech.cn:8250/resources/jsw/docs-wb.html
     * serverClause : https://dev.api.edu.3ttech.cn/index/server-clause
     * player : https://dev.api.edu.3ttech.cn/index/player
     * classroomUrl : https://dev.3ttech.cn:8250
     * ipLocation : {"host":"education.3ttech.cn","port":10081,"portBusiness":10082}
     * sdk : {"host":"education.3ttech.cn","port":11000}
     */

    private String docsWbUrl;
    private String serverClause;
    private String player;
    private String classroomUrl;
    private IpLocationBean ipLocation;
    private SdkBean sdk;

    public String getDocsWbUrl() {
        return docsWbUrl;
    }

    public void setDocsWbUrl(String docsWbUrl) {
        this.docsWbUrl = docsWbUrl;
    }

    public String getServerClause() {
        return serverClause;
    }

    public void setServerClause(String serverClause) {
        this.serverClause = serverClause;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getClassroomUrl() {
        return classroomUrl;
    }

    public void setClassroomUrl(String classroomUrl) {
        this.classroomUrl = classroomUrl;
    }

    public IpLocationBean getIpLocation() {
        return ipLocation;
    }

    public void setIpLocation(IpLocationBean ipLocation) {
        this.ipLocation = ipLocation;
    }

    public SdkBean getSdk() {
        return sdk;
    }

    public void setSdk(SdkBean sdk) {
        this.sdk = sdk;
    }

    public static class IpLocationBean {
        /**
         * host : education.3ttech.cn
         * port : 10081
         * portBusiness : 10082
         */

        private String host;
        private int port;
        private int portBusiness;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getPortBusiness() {
            return portBusiness;
        }

        public void setPortBusiness(int portBusiness) {
            this.portBusiness = portBusiness;
        }
    }

    public static class SdkBean {
        /**
         * host : education.3ttech.cn
         * port : 11000
         */

        private String host;
        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
