package com.tttlive.education.ui.room.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/18/0018.
 */

public class ShareBean {

    private List<TeacherBean> teacher;
    private List<StudentBean> student;
    private List<AssistantBean> assistant;

    public List<TeacherBean> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<TeacherBean> teacher) {
        this.teacher = teacher;
    }

    public List<StudentBean> getStudent() {
        return student;
    }

    public void setStudent(List<StudentBean> student) {
        this.student = student;
    }

    public List<AssistantBean> getAssistant() {
        return assistant;
    }

    public void setAssistant(List<AssistantBean> assistant) {
        this.assistant = assistant;
    }

    public static class TeacherBean {
        /**
         * id : 260
         * courseId : 85
         * role : 3
         * inviteCode : sfem6
         * status : 0
         */

        private String id;
        private String courseId;
        private String role;
        private String inviteCode;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class StudentBean {
        /**
         * id : 259
         * courseId : 85
         * role : 1
         * inviteCode : 9xjmy
         * status : 1
         */

        private String id;
        private String courseId;
        private String role;
        private String inviteCode;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class AssistantBean {
        /**
         * id : 261
         * courseId : 85
         * role : 2
         * inviteCode : 1gce4
         * status : 0
         */

        private String id;
        private String courseId;
        private String role;
        private String inviteCode;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
