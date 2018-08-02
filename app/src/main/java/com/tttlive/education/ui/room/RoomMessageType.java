package com.tttlive.education.ui.room;

import android.util.Log;


import com.tttlive.education.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mr.li on 2018/3/16.
 */

public class RoomMessageType {
    private RoomMsg mRoomInstance;
    public RoomMessageType(RoomMsg liveInterface){
        this.mRoomInstance = liveInterface;
    }
    public void appendData(String data){
        String type = null;
        try {
            JSONObject json = new JSONObject(data);
            type = json.optString(Constant.FIELD_TYPE);
            Log.e("roommessagetype","---"+type);

            switch (type){
                case Constant.BARRAGE_REQ:
                    mRoomInstance.receiveTextMessage(data);
                    break;
                case Constant.JOINREQ:
                    mRoomInstance.joinRoomSuccess(data);
                    break;
                case Constant.LEAVEREQ:
                    mRoomInstance.leaveMessage(data);
                    break;
                case Constant.LMREQ:
                    mRoomInstance.dealApplyMicMessage(data);

                    break;
                case Constant.ROOM_JOIN_QUN_RES:
                    mRoomInstance.roomPersonnelList(data);
                    break;
                case Constant.LM_RES:
                    mRoomInstance.dealApplyAgreeMessage(data);
                    break;
                case Constant.DOC_CONTENT_REQ:
                    mRoomInstance.roomDocConect(data);
                    break;
                case Constant.LM_CLOSE_CALL:
                    mRoomInstance.closeLmCall(data);
                    break;

                case Constant.OUT_ROOM:
                    mRoomInstance.outRoomClose(data);
                    break;
                case Constant.LM_LIST_PERSONNEL:
                    mRoomInstance.lmListPersonnel(data);
                    break;
                case Constant.GAG_REG:
                    //禁言
                    mRoomInstance.gagReqPersonnel(data);
                    break;
                case Constant.GAG_RES_REMOVE:
                    mRoomInstance.gagRerRemovePersonnel(data);
                    //解除禁言
                    break;
                case Constant.COURSE_START_REQ:
                    //上课
                    mRoomInstance.courseStart(data);
                    break;
                case Constant.COURSE_LEAVE_REQ:
                    //下课
                    mRoomInstance.courseLeave(data);
                    break;
                case Constant.COURSE_START_NOT_REQ:
                    //是否已经开课
                    mRoomInstance.courseTeacherNotStart(data);
                    break;
                case Constant.TROPHY_AWARD_REQ:
                    mRoomInstance.trophyAward(data);
                    break;
                case Constant.SELECTOR_STATUS_REQ:
                    mRoomInstance.statrAnswer(data);
                   break;
                case Constant.SELECTOR_REPLY_REQ:
                    mRoomInstance.statisicsAnswer(data);
                    break;
                case Constant.WHITEBOARD_ACCESS_REQ:
                    mRoomInstance.whiteboardAccess(data);
                    break;
                case Constant.VIDEO_CLOSE_OPEN:
                    mRoomInstance.liveVideoClose(data);
                    break;
                case Constant.AUDIO_CLOSE_OPEN:
                    mRoomInstance.liveAudioClose(data);
                    break;
            }
        } catch (JSONException e) {

        }

    }
}
