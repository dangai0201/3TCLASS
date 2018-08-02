package com.wushuangtech.room.core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wushuangtech.bean.ChatInfo;
import com.wushuangtech.bean.LocalAudioStats;
import com.wushuangtech.bean.LocalVideoStats;
import com.wushuangtech.bean.RemoteAudioStats;
import com.wushuangtech.bean.RemoteVideoStats;
import com.wushuangtech.wstechapi.TTTRtcEngineEventHandler;

import java.util.ArrayList;
import java.util.List;

import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_AUDIO_PLAY_COMPLATION;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_AUDIO_RECOGNIZED;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_AUDIO_VOLUME_INDICATION;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_CONNECT_SUCCESS;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_CHAT_MESSAGE_RECIVED;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_CHAT_MESSAGE_SENT;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_ENTER_ROOM;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_ERROR;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_FIRST_VIDEO_DECODER;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_FIRST_VIDEO_FRAME;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_LOCAL_AUDIO_STATE;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_LOCAL_VIDEO_STATE;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_REMOTE_AUDIO_STATE;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_REMOTE_VIDEO_STATE;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_SEI;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_UPDATE_USER_INFO;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_USER_JOIN;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_USER_MUTE_VIDEO;
import static com.wushuangtech.room.core.LocalConstans.CALL_BACK_ON_USER_OFFLINE;


/**
 * Created by wangzhiguo on 17/10/24.
 */

public class MyTTTRtcEngineEventHandler extends TTTRtcEngineEventHandler {

    public static final String TAG = "MyTTTRtcEngineEventHandler";
    public static final String MSG_TAG = "MyTTTRtcEngineEventHandlerMSG";
    private boolean mIsSaveCallBack;
    private List<JniObjs> mSaveCallBack;
    private Context mContext;

    public MyTTTRtcEngineEventHandler(Context mContext) {
        this.mContext = mContext;
        mSaveCallBack = new ArrayList<>();
    }

    @Override
    public void onJoinChannelSuccess(String channel, long uid) {
        Log.i("wzg", "onJoinChannelSuccess.... channel ： " + channel + " | uid : " + uid);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_ENTER_ROOM;
        mJniObjs.mChannelName = channel;
        mJniObjs.mUid = uid;
        sendMessage(mJniObjs);
        mIsSaveCallBack = false;
    }

    @Override
    public void onError(final int errorType) {
        Log.i("wzg", "onError.... errorType ： " + errorType + "mIsSaveCallBack : " + mIsSaveCallBack);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_ERROR;
        mJniObjs.mErrorType = errorType;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }


    @Override
    public void onUserJoined(long nUserId, int identity, String sCustom) {
        Log.i("wzg", "onUserJoined.... nUserId ： " + nUserId + " | identity : " + identity
                + " | mIsSaveCallBack : " + mIsSaveCallBack + " | sCustom : " + sCustom);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_USER_JOIN;
        mJniObjs.mUid = nUserId;
        mJniObjs.mIdentity = identity;
        mJniObjs.sCustom = sCustom;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onUserOffline(long nUserId, int reason) {
        Log.i("wzg", "onUserOffline.... nUserId ： " + nUserId + " | reason : " + reason);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_USER_OFFLINE;
        mJniObjs.mUid = nUserId;
        mJniObjs.mReason = reason;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onConnectionLost() {
        Log.i("wzg", "onConnectionLost.... ");
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_ERROR;
        mJniObjs.mErrorType = 100;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onUserEnableVideo(long uid, boolean muted) {
        Log.i("wzg", "onUserEnableVideo.... uid : " + uid + " | mute : " + muted);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_USER_MUTE_VIDEO;
        mJniObjs.mUid = uid;
        mJniObjs.mIsEnableVideo = muted;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onAudioVolumeIndication(long nUserID, int audioLevel, int audioLevelFullRange) {
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_AUDIO_VOLUME_INDICATION;
        mJniObjs.mUid = nUserID;
        mJniObjs.mAudioLevel = audioLevel;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onFirstRemoteVideoFrame(long uid, int width, int height) {
        Log.i("wzg", "onFirstRemoteVideoFrame.... uid ： " + uid + " | width : " + width + " | height : " + height);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_FIRST_VIDEO_FRAME;
        mJniObjs.mUid = uid;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }


    @Override
    public void onFirstRemoteVideoDecoded(long uid, int width, int height) {
        Log.i("wzg", "onFirstRemoteVideoDecoded.... uid ： " + uid + " | width : " + width + " | height : " + height);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_FIRST_VIDEO_DECODER;
        mJniObjs.mUid = uid;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onSetSEI(String sei) {
        Log.i("wzg", "onSei.... sei : " + sei);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_SEI;
        mJniObjs.mSEI = sei;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void OnChatMessageSent(ChatInfo chatInfo, int error) {
        Log.i("wzg", "OnChatMessageSent.... sSeqID ： " + chatInfo.seqID + " | error : " + error);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_CHAT_MESSAGE_SENT;
        mJniObjs.error = error;
        mJniObjs.msSeqID = chatInfo.seqID;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void OnChatMessageRecived(long nSrcUserID, ChatInfo chatInfo) {
        Log.i("wzg", "OnChatMessageRecived.... nSrcUserID ： " + nSrcUserID + " | type : " + chatInfo.chatType + " | sSeqID : " + chatInfo.seqID + " | strData : " + chatInfo.chatData);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_CHAT_MESSAGE_RECIVED;
        mJniObjs.nSrcUserID = nSrcUserID;
        mJniObjs.type = chatInfo.chatType;
        mJniObjs.msSeqID = chatInfo.seqID;
        mJniObjs.strData = chatInfo.chatData;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }




    @Override
    public void onPlayChatAudioCompletion(String filePath) {
        Log.i("wzg", "onPlayChatAudioCompletion...." + filePath);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_AUDIO_PLAY_COMPLATION;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onSpeechRecognized(String str) {
        Log.i("wzg", "onSpeechRecognized...." + str);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.strData = str;
        mJniObjs.mJniType = CALL_BACK_ON_AUDIO_RECOGNIZED;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void OnUpdateUserBaseInfo(long roomID, long uid, String sCustom) {
        Log.i("wzg", "OnUpdateUserBaseInfo...." + sCustom);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mUid = uid;
        mJniObjs.mRoomId = roomID;
        mJniObjs.sCustom = sCustom;
        mJniObjs.mJniType = CALL_BACK_ON_UPDATE_USER_INFO;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    private synchronized void sendMessage(JniObjs mJniObjs) {
        Intent i = new Intent();
        i.setAction(TAG);
        i.putExtra(MSG_TAG, mJniObjs);
        mContext.sendBroadcast(i);
    }

    public synchronized void setIsSaveCallBack(boolean mIsSaveCallBack) {
        this.mIsSaveCallBack = mIsSaveCallBack;
        if (!mIsSaveCallBack) {
            for (int i = 0; i < mSaveCallBack.size(); i++) {
                sendMessage(mSaveCallBack.get(i));
            }
            mSaveCallBack.clear();
        }
    }

    private synchronized void saveCallBack(JniObjs mJniObjs) {
        if (mIsSaveCallBack) {
            mSaveCallBack.add(mJniObjs);
        }
    }


    @Override
    public void onLocalVideoStats(LocalVideoStats stats) {
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_LOCAL_VIDEO_STATE;
        mJniObjs.mLocalVideoStats = stats;
        if (mIsSaveCallBack){
            saveCallBack(mJniObjs);
        }else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onRemoteVideoStats(RemoteVideoStats stats) {
        Log.i("WJH", "onRemoteVideoStats.... uid : " + stats.getUid() + " | bitrate : " + stats.getReceivedBitrate()
                + " | framerate : " + stats.getReceivedFrameRate());
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_REMOTE_VIDEO_STATE;
        mJniObjs.mRemoteVideoStats = stats;
        if (mIsSaveCallBack){
            saveCallBack(mJniObjs);
        }else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onLocalAudioStats(LocalAudioStats stats) {
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_LOCAL_AUDIO_STATE;
        mJniObjs.mLocalAudioStats = stats;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }

    @Override
    public void onRemoteAudioStats(RemoteAudioStats stats) {
        Log.i("WJH", "RemoteAudioStats.... uid : " + stats.getUid() + " | bitrate : " + stats.getReceivedBitrate());
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_REMOTE_AUDIO_STATE;
        mJniObjs.mRemoteAudioStats = stats;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }
    }


    @Override
    public void onConnectSuccess(String ip, int port) {
        Log.i("WJH", "onConnectSuccess.... ip : " + ip + " | port : " + port);
        JniObjs mJniObjs = new JniObjs();
        mJniObjs.mJniType = CALL_BACK_ON_CONNECT_SUCCESS;
        mJniObjs.mIP = ip;
        mJniObjs.mPort = port;
        if (mIsSaveCallBack) {
            saveCallBack(mJniObjs);
        } else {
            sendMessage(mJniObjs);
        }


    }
}
