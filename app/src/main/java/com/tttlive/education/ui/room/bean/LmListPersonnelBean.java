package com.tttlive.education.ui.room.bean;

import com.wushuangtech.library.screenrecorder.gles.Drawable2d;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21/0021.
 */

public class LmListPersonnelBean {

    private String messageType;
    private ListDataBean data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public ListDataBean getData() {
        return data;
    }

    public void setData(ListDataBean data) {
        this.data = data;
    }

    public static class ListDataBean{
        private List<UserListBean> userList;

        public List<UserListBean> getUserList() {
            return userList;
        }

        public void setUserList(List<UserListBean> userList) {
            this.userList = userList;
        }

        public static class UserListBean{
            private String userId;
            private String nickName;
            private String avatar;
            private String introduction;
            private String type;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getIntroduction() {
                return introduction;
            }

            public void setIntroduction(String introduction) {
                this.introduction = introduction;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

    }


}
