package com.tttlive.education.ui.room.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20/0020.
 * 群发主播房间用户列表
 */
public class JoinQunRoomBean {

    private String messageType;
    private String code;
    private String message;
    private DataBeans data;


    @Override
    public String toString() {
        return "JoinQunRoomBean{" +
                "messageType='" + messageType + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeans getData() {
        return data;
    }

    public void setData(DataBeans data) {
        this.data = data;
    }

    public static class DataBeans{
        private String roomId;
        private String userId;
        private String avatar;
        private String nickName;
        private String level;
        private String count;
        private List<UserListBeans> userList;

        @Override
        public String toString() {
            return "DataBeans{" +
                    "roomId='" + roomId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", level='" + level + '\'' +
                    ", count='" + count + '\'' +
                    ", userList=" + userList +
                    '}';
        }

        public List<UserListBeans> getUserList() {
            return userList;
        }

        public void setUserList(List<UserListBeans> userList) {
            this.userList = userList;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public static class UserListBeans{
        private String userId;
        private String nickName;
        private String avatar;
        private int level;
        private int role;
        private int trophyCount;


        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

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

        public int getTrophyCount() {
            return trophyCount;
        }

        public void setTrophyCount(int trophyCount) {
            this.trophyCount = trophyCount;
        }
    }

}
