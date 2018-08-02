package com.tttlive.education.data;

/**
 * Created by Iverson on 2018/3/7 上午11:38
 * 此类用于：
 */

public class AccountLoginBean {


    /**
     * userId : 145
     */

    private int userId;
    private String appId = "";
    private int appType;
    private String userName = "";
    private String nickName = "";

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "AccountLoginBean{" +
                "userId=" + userId +
                ", appId='" + appId + '\'' +
                ", appType=" + appType +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
