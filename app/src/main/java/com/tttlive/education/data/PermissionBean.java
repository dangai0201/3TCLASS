package com.tttlive.education.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/6/8/0008.
 */

public class PermissionBean implements Parcelable {

    public String mPermissionName;
    public String mPermissionReason;

    public PermissionBean(String mPermissionName, String mPermissionReason) {
        this.mPermissionName = mPermissionName;
        this.mPermissionReason = mPermissionReason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPermissionName);
        dest.writeString(this.mPermissionReason);
    }

    public PermissionBean() {
    }

    protected PermissionBean(Parcel in) {
        this.mPermissionName = in.readString();
        this.mPermissionReason = in.readString();
    }

    public static final Creator<PermissionBean> CREATOR = new Creator<PermissionBean>() {
        @Override
        public PermissionBean createFromParcel(Parcel source) {
            return new PermissionBean(source);
        }

        @Override
        public PermissionBean[] newArray(int size) {
            return new PermissionBean[size];
        }
    };
}
