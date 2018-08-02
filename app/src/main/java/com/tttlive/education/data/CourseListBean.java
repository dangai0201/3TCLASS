package com.tttlive.education.data;

import java.util.List;

/**
 * Created by Iverson on 2018/1/31 下午3:41
 * 此类用于：
 */

public class CourseListBean {

    /**
     * page : 1
     * size : 10
     * totalPage : 16
     * totalCount : 151
     * itemList : [{"id":"358","userId":"900075","appId":"2d740137500f2d8eac0f3ea742498af3","teacherName":"user900075","coverSrc":"","title":"共发生地方","timeStart":"2018.07.07 10:55","timeEnd":"永久","videoSrc":"","docId":"0","isLive":"0","isLiveP2P":"0","desc":"","created":"2018.07.06 11:57","type":"1","joinRule":"1","capacity":"10","duration":"0","durationType":"1","toplimit":"0","identifyYellow":[],"isVideoSrc":false,"isBackStream":0,"pushRtmp":"rtmp://push.xinshenglan.cdn.3ttech.cn/sdk/358","status":"initial","pullRtmp":"rtmp://pull.xinshenglan.cdn.3ttech.cn/sdk/358","unionId":"","publicCode":"6zpsq","teacherInviteCode":"9ap0o","capacityLianmai":"6"}]
     */

    private int page;
    private int size;
    private int totalPage;
    private int totalCount;
    private List<ItemListBean> itemList;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean> itemList) {
        this.itemList = itemList;
    }

    public static class ItemListBean {
        /**
         * id : 358
         * userId : 900075
         * appId : 2d740137500f2d8eac0f3ea742498af3
         * teacherName : user900075
         * coverSrc :
         * title : 共发生地方
         * timeStart : 2018.07.07 10:55
         * timeEnd : 永久
         * videoSrc :
         * docId : 0
         * isLive : 0
         * isLiveP2P : 0
         * desc :
         * created : 2018.07.06 11:57
         * type : 1
         * joinRule : 1
         * capacity : 10
         * duration : 0
         * durationType : 1
         * toplimit : 0
         * identifyYellow : []
         * isVideoSrc : false
         * isBackStream : 0
         * pushRtmp : rtmp://push.xinshenglan.cdn.3ttech.cn/sdk/358
         * status : initial
         * pullRtmp : rtmp://pull.xinshenglan.cdn.3ttech.cn/sdk/358
         * unionId :
         * publicCode : 6zpsq
         * teacherInviteCode : 9ap0o
         * capacityLianmai : 6
         */

        private String id;
        private String userId;
        private String appId;
        private String teacherName;
        private String coverSrc;
        private String title;
        private String timeStart;
        private String timeEnd;
        private String videoSrc;
        private String docId;
        private String isLive;
        private String isLiveP2P;
        private String desc;
        private String created;
        private String type;
        private String joinRule;
        private String capacity;
        private String duration;
        private String durationType;
        private String toplimit;
        private boolean isVideoSrc;
        private int isBackStream;
        private String pushRtmp;
        private String status;
        private String pullRtmp;
        private String unionId;
        private String publicCode;
        private String teacherInviteCode;
        private String capacityLianmai;
        private List<?> identifyYellow;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getCoverSrc() {
            return coverSrc;
        }

        public void setCoverSrc(String coverSrc) {
            this.coverSrc = coverSrc;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }

        public String getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(String timeEnd) {
            this.timeEnd = timeEnd;
        }

        public String getVideoSrc() {
            return videoSrc;
        }

        public void setVideoSrc(String videoSrc) {
            this.videoSrc = videoSrc;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getIsLive() {
            return isLive;
        }

        public void setIsLive(String isLive) {
            this.isLive = isLive;
        }

        public String getIsLiveP2P() {
            return isLiveP2P;
        }

        public void setIsLiveP2P(String isLiveP2P) {
            this.isLiveP2P = isLiveP2P;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getJoinRule() {
            return joinRule;
        }

        public void setJoinRule(String joinRule) {
            this.joinRule = joinRule;
        }

        public String getCapacity() {
            return capacity;
        }

        public void setCapacity(String capacity) {
            this.capacity = capacity;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getDurationType() {
            return durationType;
        }

        public void setDurationType(String durationType) {
            this.durationType = durationType;
        }

        public String getToplimit() {
            return toplimit;
        }

        public void setToplimit(String toplimit) {
            this.toplimit = toplimit;
        }

        public boolean isIsVideoSrc() {
            return isVideoSrc;
        }

        public void setIsVideoSrc(boolean isVideoSrc) {
            this.isVideoSrc = isVideoSrc;
        }

        public int getIsBackStream() {
            return isBackStream;
        }

        public void setIsBackStream(int isBackStream) {
            this.isBackStream = isBackStream;
        }

        public String getPushRtmp() {
            return pushRtmp;
        }

        public void setPushRtmp(String pushRtmp) {
            this.pushRtmp = pushRtmp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPullRtmp() {
            return pullRtmp;
        }

        public void setPullRtmp(String pullRtmp) {
            this.pullRtmp = pullRtmp;
        }

        public String getUnionId() {
            return unionId;
        }

        public void setUnionId(String unionId) {
            this.unionId = unionId;
        }

        public String getPublicCode() {
            return publicCode;
        }

        public void setPublicCode(String publicCode) {
            this.publicCode = publicCode;
        }

        public String getTeacherInviteCode() {
            return teacherInviteCode;
        }

        public void setTeacherInviteCode(String teacherInviteCode) {
            this.teacherInviteCode = teacherInviteCode;
        }

        public String getCapacityLianmai() {
            return capacityLianmai;
        }

        public void setCapacityLianmai(String capacityLianmai) {
            this.capacityLianmai = capacityLianmai;
        }

        public List<?> getIdentifyYellow() {
            return identifyYellow;
        }

        public void setIdentifyYellow(List<?> identifyYellow) {
            this.identifyYellow = identifyYellow;
        }
    }
}
