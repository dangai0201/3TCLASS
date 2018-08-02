package com.tttlive.education.data;

import java.io.Serializable;

/**
 * Author: sunny
 * Time: 2018/7/12
 * description:登录账号实体类
 */

public class AccountBean implements Serializable {

    private String mobile;
    private String password;

    public AccountBean(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccountBean{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
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
        if (obj instanceof AccountBean) {
            AccountBean accountBean = (AccountBean) obj;
           return accountBean.mobile.equals(this.mobile);
        }
        return false;
    }
}
