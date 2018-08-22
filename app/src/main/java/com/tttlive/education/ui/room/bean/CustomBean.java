package com.tttlive.education.ui.room.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/9/0009.
 */

public class CustomBean implements Serializable {

    /**
     * avatar : https://www.qqtouxiang.com/d/file/tupian/mx/20170911/jikbmmbo3jncm.jpg
     * level : 0
     * nickName : 匿名-2041843
     * role : 1          //角色 1:学生 3:老师
     * userId : 2041843     //用户ID
     * lm: 0  //连麦状态   0:没有连麦 1:正在连麦 2:正在连麦
     * stop : 0         //是否禁言 0:未禁言 1:禁言
     * trophyCount : 0      //奖杯数量
     * whiteBoardAccess : true  // 被授权白板的状态
     * micClosed : true //关闭摄像头
     * cameraClosed: true   //打开麦克风
     */

    private String avatar;
    private int level;
    private String nickName;
    private int role;
    private int userId;
    private int lm;
    private int stop;
    private int trophyCount;
    private boolean whiteBoardAccess;
    private boolean micClosed;
    private boolean cameraClosed;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLm() {
        return lm;
    }

    public void setLm(int lm) {
        this.lm = lm;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public int getTrophyCount() {
        return trophyCount;
    }

    public void setTrophyCount(int trophyCount) {
        this.trophyCount = trophyCount;
    }

    public boolean isWhiteBoardAccess() {
        return whiteBoardAccess;
    }

    public void setWhiteBoardAccess(boolean whiteBoardAccess) {
        this.whiteBoardAccess = whiteBoardAccess;
    }

    public boolean isMicClosed() {
        return micClosed;
    }

    public void setMicClosed(boolean micClosed) {
        this.micClosed = micClosed;
    }

    public boolean isCameraClosed() {
        return cameraClosed;
    }

    public void setCameraClosed(boolean cameraClosed) {
        this.cameraClosed = cameraClosed;
    }


    @Override
    public String toString() {
        return "CustomBean{" +
                "avatar='" + avatar + '\'' +
                ", level=" + level +
                ", nickName='" + nickName + '\'' +
                ", role=" + role +
                ", userId=" + userId +
                ", lm=" + lm +
                ", stop=" + stop +
                ", trophyCount=" + trophyCount +
                ", whiteBoardAccess=" + whiteBoardAccess +
                ", micClosed=" + micClosed +
                ", cameraClosed=" + cameraClosed +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof CustomBean) {
            CustomBean customBean = (CustomBean) obj;
            if (customBean.userId == this.userId) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
