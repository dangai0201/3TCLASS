package com.wushuangtech.room.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.wushuangtech.bean.LocalAudioStats;
import com.wushuangtech.bean.LocalVideoStats;
import com.wushuangtech.bean.RemoteAudioStats;
import com.wushuangtech.bean.RemoteVideoStats;

/**
 * Created by wangzhiguo on 17/10/13.
 */

public class JniObjs implements Parcelable {

    public int mJniType;
    public long mUid;
    public int mIdentity;
    public int mReason;
    public boolean mIsEnableVideo;
    public int mAudioLevel;
    public String mChannelName;
    public String mSEI;
    public int mErrorType;
    public String sCustom;
    public long mRoomId;
    public String mIP;
    public int mPort;

    public String msSeqID;
    //OnChatMessageSent
    public int error;
    //OnChatMessageRecived
    public long nSrcUserID;
    public int type;
    public String strData;


    public RemoteVideoStats mRemoteVideoStats;
    public RemoteAudioStats mRemoteAudioStats;
    public LocalVideoStats mLocalVideoStats;
    public LocalAudioStats mLocalAudioStats;

    public JniObjs() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mJniType);
        dest.writeLong(this.mUid);
        dest.writeInt(this.mIdentity);
        dest.writeInt(this.mReason);
        dest.writeByte(this.mIsEnableVideo ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mAudioLevel);
        dest.writeString(this.mChannelName);
        dest.writeString(this.mSEI);
        dest.writeInt(this.mErrorType);
        dest.writeLong(this.mRoomId);
        dest.writeString(this.sCustom);
        dest.writeString(this.msSeqID);
        dest.writeInt(this.error);
        dest.writeLong(this.nSrcUserID);
        dest.writeInt(this.type);
        dest.writeString(this.strData);
        dest.writeString(this.mIP);
        dest.writeInt(this.mPort);
        dest.writeParcelable(this.mRemoteVideoStats, flags);
        dest.writeParcelable(this.mRemoteAudioStats, flags);
        dest.writeParcelable(this.mLocalVideoStats, flags);
        dest.writeParcelable(this.mLocalAudioStats, flags);

    }

    protected JniObjs(Parcel in) {
        this.mJniType = in.readInt();
        this.mUid = in.readLong();
        this.mIdentity = in.readInt();
        this.mReason = in.readInt();
        this.mIsEnableVideo = in.readByte() != 0;
        this.mAudioLevel = in.readInt();
        this.mChannelName = in.readString();
        this.mSEI = in.readString();
        this.mErrorType = in.readInt();
        this.mRoomId = in.readLong();
        this.sCustom = in.readString();
        this.msSeqID = in.readString();
        this.error = in.readInt();
        this.nSrcUserID = in.readLong();
        this.type = in.readInt();
        this.strData = in.readString();
        this.mIP = in.readString();
        this.mPort = in.readInt();
        this.mRemoteVideoStats = in.readParcelable(Thread.currentThread().getContextClassLoader());
        this.mRemoteAudioStats = in.readParcelable(Thread.currentThread().getContextClassLoader());
        this.mLocalVideoStats = in.readParcelable(Thread.currentThread().getContextClassLoader());
        this.mLocalAudioStats = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public static final Creator<JniObjs> CREATOR = new Creator<JniObjs>() {
        @Override
        public JniObjs createFromParcel(Parcel source) {
            return new JniObjs(source);
        }

        @Override
        public JniObjs[] newArray(int size) {
            return new JniObjs[size];
        }
    };
}
