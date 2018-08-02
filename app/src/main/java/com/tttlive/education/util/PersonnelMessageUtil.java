package com.tttlive.education.util;

import com.tttlive.education.ui.room.bean.CustomBean;
import com.tttlive.education.ui.room.bean.JoinRoomBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/27/0027.
 */

public class PersonnelMessageUtil {

    private List<CustomBean> mCustomBeanList;
    private Map<Integer ,Integer> mCupNumMap;
    private List<String> LmUserId;
    private List<String> nowLmUserid;
    private String type;
    private String message;
    private String roomType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CustomBean> getmCustomBeanList() {
        return mCustomBeanList;
    }

    public void setmCustomBeanList(List<CustomBean> mCustomBeanList) {
        this.mCustomBeanList = mCustomBeanList;
    }

    public List<String> getLmUserId() {
        return LmUserId;
    }

    public void setLmUserId(List<String> lmUserId) {
        LmUserId = lmUserId;
    }

    public List<String> getNowLmUserid() {
        return nowLmUserid;
    }

    public void setNowLmUserid(List<String> nowLmUserid) {
        this.nowLmUserid = nowLmUserid;
    }

    public Map<Integer, Integer> getmCupNumMap() {
        return mCupNumMap;
    }

    public void setmCupNumMap(Map<Integer, Integer> mCupNumMap) {
        this.mCupNumMap = mCupNumMap;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}
