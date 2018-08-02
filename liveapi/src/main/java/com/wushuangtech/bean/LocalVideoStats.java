package com.wushuangtech.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 本地视频相关的统计信息类
 */
public class LocalVideoStats implements Parcelable{
    /**
     * （上次统计后）发送的码率(kbps)
     */
    private int mSentBitrate;
    /**
     * (上次统计后）发送的帧率(fps)
     */
    private int mSentFrameRate;
    /**
     * 本地视频上行丢包率
     */
    private float mVideoLossRate;
    /**
     * 本地视频上行缓冲区大小
     */
    private int mVideoBuffer;

    public LocalVideoStats(int mSentBitrate, int mSentFrameRate, float mVideoLossRate, int mVideoBuffer) {
        this.mSentBitrate = mSentBitrate;
        this.mSentFrameRate = mSentFrameRate;
        this.mVideoLossRate = mVideoLossRate;
        this.mVideoBuffer = mVideoBuffer;
    }

    public int getSentBitrate() {
        return mSentBitrate;
    }

    public int getSentFrameRate() {
        return mSentFrameRate;
    }

    public float getVideoLossRate() {
        return mVideoLossRate;
    }

    public int getVideoBuffer() {
        return mVideoBuffer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSentBitrate);
        dest.writeInt(this.mSentFrameRate);
        dest.writeFloat(this.mVideoLossRate);
        dest.writeInt(this.mVideoBuffer);
    }

    private LocalVideoStats(Parcel in) {
        this.mSentBitrate = in.readInt();
        this.mSentFrameRate = in.readInt();
        this.mVideoLossRate = in.readFloat();
        this.mVideoBuffer = in.readInt();
    }

    public static final Creator<LocalVideoStats> CREATOR = new Creator<LocalVideoStats>() {
        @Override
        public LocalVideoStats createFromParcel(Parcel source) {
            return new LocalVideoStats(source);
        }

        @Override
        public LocalVideoStats[] newArray(int size) {
            return new LocalVideoStats[size];
        }
    };
}
