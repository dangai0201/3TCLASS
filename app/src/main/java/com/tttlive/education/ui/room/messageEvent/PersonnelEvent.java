package com.tttlive.education.ui.room.messageEvent;

import com.tttlive.education.ui.room.bean.CustomBean;
import com.tttlive.education.ui.room.bean.JoinRoomBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/20/0020.
 */

public class PersonnelEvent {

    private String type ;
    private Map<String , CustomBean> customBeanMap;
    private String personnelId;


    public PersonnelEvent(String mType, String userId, Map<String , CustomBean> mCustomBean){
        this.type = mType;
        this.customBeanMap = mCustomBean;
        this.personnelId = userId;
    }

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, CustomBean> getCustomBeanMap() {
        return customBeanMap;
    }

    public void setCustomBeanMap(Map<String, CustomBean> customBeanMap) {
        this.customBeanMap = customBeanMap;
    }
}
