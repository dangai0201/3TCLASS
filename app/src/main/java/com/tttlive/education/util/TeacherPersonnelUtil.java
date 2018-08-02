package com.tttlive.education.util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.tttlive.education.adapter.BasePersonnelAdapter;
import com.tttlive.education.constant.Constant;
import com.tttlive.basic.education.R;
import com.tttlive.education.ui.room.MessageEvent;
import com.tttlive.education.ui.room.bean.CustomBean;
import com.tttlive.education.ui.room.bean.JoinQunRoomBean;
import com.tttlive.education.ui.room.bean.LmAgreeResBean;
import com.tttlive.education.ui.room.messageEvent.PersonnelEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/26/0026.
 */

public class TeacherPersonnelUtil{

    private static final String TAG_CLASS = TeacherPersonnelUtil.class.getSimpleName();

    private Context mContext;
    private RecyclerView mRecyclerView;
    private BasePersonnelAdapter basePerAdapter;
    private String personnelKey;
    private CustomBean personnelValue;
    private Gson tpGson = new Gson();
    private List<String> nowLmListUserid = new ArrayList<>();
    private PersonnelMessageUtil personnelMessage = new PersonnelMessageUtil();

    //当前房间人数
    private List<CustomBean> adapterList = new ArrayList<>();
    //学生房间人数
    private List<CustomBean> studentCustomList = new ArrayList<>();
    private AcceptOnClickListener acceptOnClickListener = new AcceptOnClickListener();
    private RefusedOnClickListener refusedOnClickListener = new RefusedOnClickListener();

    public TeacherPersonnelUtil(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;

    }


    /**
     * 更新人员列表
     *
     * @param context
     * @param personnelEvent
     */
    public void initViewPersonnel(Context context, PersonnelEvent personnelEvent , Map<Integer , Integer> cupNumList) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,1));

        if (Constant.SEND_EVENT_PERSONNEL.equals(personnelEvent.getType())) {
            Map<String, CustomBean> roomPersonnelMap = personnelEvent.getCustomBeanMap();
            Set<Map.Entry<String, CustomBean>> entrySet = roomPersonnelMap.entrySet();
            Iterator<Map.Entry<String, CustomBean>> mapIt = entrySet.iterator();
            adapterList.clear();
            while (mapIt.hasNext()) {
                Map.Entry<String, CustomBean> me = mapIt.next();
                personnelKey = me.getKey();
                personnelValue = me.getValue();
                adapterList.add(personnelValue);
            }
            personnelMessage.setType(Constant.SEND_EVENT_PERSONNEL);
            personnelMessage.setmCupNumMap(cupNumList);
            setPersonnelAdapter(mContext, adapterList, personnelMessage );
        }

    }

    /**
     * 学生端更新人员列表
     * @param context
     */
    public void initViewStudentPersonnel(Context context , Map<Integer , CustomBean> mCustomBeanMap , String roomType ){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        Map<Integer, CustomBean> cupMap = mCustomBeanMap;
        Set<Map.Entry<Integer, CustomBean>> entrySet = cupMap.entrySet();
        Iterator<Map.Entry<Integer, CustomBean>> mapIt = entrySet.iterator();
        studentCustomList.clear();
        while (mapIt.hasNext()) {
            Map.Entry<Integer, CustomBean> me = mapIt.next();
            studentCustomList.add(me.getValue());

        }
        personnelMessage.setType(Constant.SEND_EVENT_PERSONNEL_STUDENT);
        personnelMessage.setmCustomBeanList(studentCustomList);
        personnelMessage.setRoomType(roomType);
        setPersonnelAdapter(mContext, null, personnelMessage );

    }

    /**
     * 新人进房更新整体人员列表各种状态
     */
    public void setPersonnelAll(Context context , Map<String, CustomBean> mjoinRoom ,
                                List<String> nowLmUserid ,List<String> LmUserId,Map<Integer , Integer> cupNumList){
        mapListPersonnel(mjoinRoom);
        personnelMessage.setType(Constant.SEND_EVENT_OVERALL_PERSONNEL);
        personnelMessage.setNowLmUserid(nowLmUserid);
        personnelMessage.setLmUserId(LmUserId);
        personnelMessage.setmCupNumMap(cupNumList);
        setPersonnelAdapter(mContext ,adapterList , personnelMessage);

    }

    /**
     * 接收到连麦信息
     *
     * @param context
     * @param mjoinRoom
     * @param LmUserId
     */
    public void setPersonnelLm(Context context, Map<String, CustomBean> mjoinRoom,
                               List<String> LmUserId , Map<Integer , Integer> cupNumList) {
        mapListPersonnel(mjoinRoom);
        personnelMessage.setType(Constant.SEND_EVENT_LMREQ);
        personnelMessage.setLmUserId(LmUserId);
        personnelMessage.setmCupNumMap(cupNumList);
        setPersonnelAdapter(mContext, adapterList, personnelMessage);
    }

    /**
     * 接收同意连麦人员
     */
    public void setNowPersonnelLm(Context context ,Map<String, CustomBean> mjoinRoom ,
                                  List<String> nowLmUserid ,List<String> LmUserId,Map<Integer , Integer> cupNumList ){
        mapListPersonnel(mjoinRoom);
        personnelMessage.setType(Constant.SEND_EVENT_NOW_MESSAGE_LM);
        personnelMessage.setNowLmUserid(nowLmUserid);
        personnelMessage.setLmUserId(LmUserId);
        personnelMessage.setmCupNumMap(cupNumList);
        setPersonnelAdapter(mContext ,adapterList , personnelMessage);
    }


    /**
     * 设置adapter
     */
    public void setPersonnelAdapter(Context context, List joinRoomBean, PersonnelMessageUtil message) {
        if (message.getType().equals(Constant.SEND_EVENT_PERSONNEL)) {
            basePerAdapter = new BasePersonnelAdapter(mContext, joinRoomBean , message.getmCupNumMap());
        } else if (message.getType().equals(Constant.SEND_EVENT_LMREQ)) {
            basePerAdapter = new BasePersonnelAdapter(mContext, joinRoomBean, message.getLmUserId() , message.getmCupNumMap());
        }else if (message.getType().equals(Constant.SEND_EVENT_NOW_MESSAGE_LM)){
            basePerAdapter =new BasePersonnelAdapter(mContext,joinRoomBean , message.getLmUserId() ,
                    message.getNowLmUserid(),message.getmCupNumMap());
        } else if (message.getType().equals(Constant.SEND_EVENT_OVERALL_PERSONNEL)) {
            basePerAdapter =new BasePersonnelAdapter(mContext,joinRoomBean , message.getLmUserId() ,
                    message.getNowLmUserid(),message.getmCupNumMap());
        }else if (message.getType().equals(Constant.SEND_EVENT_PERSONNEL_STUDENT)){
            basePerAdapter = new BasePersonnelAdapter(mContext , "1" , message.getmCustomBeanList() , message.getRoomType());
        }
        mRecyclerView.setAdapter(basePerAdapter);
        basePerAdapter.setAcceptOnClickListener(acceptOnClickListener);
        basePerAdapter.setRefusedOnClickListener(refusedOnClickListener);
    }

    /**
     * 加载当前人员列表
     * @param mjoinRoom
     */
    private void mapListPersonnel(Map<String, CustomBean> mjoinRoom) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Set<Map.Entry<String, CustomBean>> entrySet = mjoinRoom.entrySet();
        Iterator<Map.Entry<String, CustomBean>> mapIt = entrySet.iterator();
        adapterList.clear();
        while (mapIt.hasNext()) {
            Map.Entry<String, CustomBean> me = mapIt.next();
            personnelKey = me.getKey();
            personnelValue = me.getValue();
            adapterList.add(personnelValue);
        }
    }

    /**
     * 同意连麦
     */
    private class AcceptOnClickListener implements BasePersonnelAdapter.tvAcceptSetOnClick{

        @Override
        public void onTvAcceptClick(View view, int position) {
            Log.e(TAG_CLASS  , "接受连麦 " );
            LmAgreeResBean lmAgreeResBean = new LmAgreeResBean();
            LmAgreeResBean.LmDataBean  lmDataBean = new LmAgreeResBean.LmDataBean();
            for (int i = 0; i < adapterList.size(); i++) {
                if (position == i){
                    lmAgreeResBean.setMessageType(Constant.LM_RES);
                    lmDataBean.setRoomId(Constant.LIVE_ROOM_ID);
                    lmDataBean.setUserId(String.valueOf(adapterList.get(position).getUserId()));
                    lmDataBean.setType("1");
                    lmAgreeResBean.setData(lmDataBean);
                }
            }
            nowLmListUserid.add(lmAgreeResBean.getData().getUserId());
            EventBus.getDefault().post(new MessageEvent(Constant.ACCEPT_RED_LM , tpGson.toJson(lmAgreeResBean)));
            view.findViewById(R.id.ll_personnel_two).setVisibility(View.GONE);
        }
    }

    /**
     * 拒绝连麦
     */
    private class RefusedOnClickListener implements BasePersonnelAdapter.tvRefusedSetOnClick{

        @Override
        public void onTvRefusedOnClick(View view, int position) {
            Log.e(TAG_CLASS , "拒绝连麦");
            LmAgreeResBean lmAgreeResBean = new LmAgreeResBean();
            LmAgreeResBean.LmDataBean  lmDataBean = new LmAgreeResBean.LmDataBean();
            for (int i = 0; i < adapterList.size(); i++) {
                if (position == i){
                    lmAgreeResBean.setMessageType(Constant.LM_RES);
                    lmDataBean.setRoomId(Constant.LIVE_ROOM_ID);
                    lmDataBean.setUserId(String.valueOf(adapterList.get(position).getUserId()));
                    lmDataBean.setType("0");
                    lmAgreeResBean.setData(lmDataBean);
                }
            }
            EventBus.getDefault().post(new MessageEvent(Constant.REFUSED_RED_LM , tpGson.toJson(lmAgreeResBean)));
            view.findViewById(R.id.ll_personnel_two).setVisibility(View.GONE);
        }
    }

    /**
     * 移除正在连麦人员
     * @param userid
     */
    public void removeNowListUserId(String userid){
        for (int i = 0; i < nowLmListUserid.size(); i++) {
            if (nowLmListUserid.get(i).equals(userid)) {
                nowLmListUserid.remove(userid);
            }
        }
    }


}
