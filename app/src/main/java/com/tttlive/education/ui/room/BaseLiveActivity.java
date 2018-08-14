package com.tttlive.education.ui.room;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.ui.room.bean.BindWebsocket;
import com.tttlive.education.ui.room.bean.CustomBean;
import com.tttlive.education.ui.room.bean.DocContentBean;
import com.tttlive.education.ui.room.bean.JoinQunRoomBean;
import com.tttlive.education.ui.room.bean.JoinRoomBean;
import com.tttlive.education.ui.room.bean.LeaveMassageBean;
import com.tttlive.education.ui.room.bean.LmAgreeResBean;
import com.tttlive.education.ui.room.bean.LmBean;
import com.tttlive.education.ui.room.bean.LmListPersonnelBean;
import com.tttlive.education.ui.room.bean.StartLiveBean;
import com.tttlive.education.ui.room.bean.StartNotLiveBean;
import com.tttlive.education.ui.room.bean.StudentAnswerBean;
import com.tttlive.education.ui.room.bean.TeacherAnswerBean;
import com.tttlive.education.ui.room.bean.TrophyAwardBean;
import com.tttlive.education.ui.room.bean.WebsocketDeleteBean;
import com.tttlive.education.ui.room.bean.WebsocketImBean;
import com.tttlive.education.ui.room.bean.WsIpLocationBean;
import com.wushuangtech.room.core.RoomLiveHelp;
import com.wushuangtech.room.core.RoomLiveInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Iverson on 2018/3/6 上午10:44
 * 此类用于：这是老师和学生公共东西
 */

public abstract class BaseLiveActivity extends BaseActivity implements RoomLiveInterface {

    private static String TAG = BaseLiveActivity.class.getSimpleName();

    protected RoomLiveHelp mRoomLiveHelp;
    private Context mContext;
    private int currentPage;

    public static String persionQun;
    private String versionName;

    public RadioButton[] mRadioButtons = new RadioButton[2];
    public Fragment[] mFragments = new Fragment[2];

    public static List<LmListPersonnelBean.ListDataBean.UserListBean> luList = new ArrayList<>();
    public static String lmListAccept;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        boolean opppIphone = mContext.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
//        if (opppIphone){
//            Toast.makeText(mContext , getResources().getString(R.string.concave_area), Toast.LENGTH_LONG).show();
//        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        luList.clear();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 退出房间清空连麦列表
     */
    public void DestoryLmList(){
        if (luList != null && luList.size()>0) {
            luList.clear();
        }

        if (lmListAccept != null){
            lmListAccept = null;
        }
    }

    /**
     * 复制链接
     */
    public void copyShareLink(String roomId , String invite , String shareUrl , String inviteCode) {
        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        String invitationCode = shareUrl + invite + "  " +  inviteCode;
        String invitation = String.format(invitationCode,invite);
        clipboardManager.setText(invitation);
//        toastShort("复制成功");
    }

    /**
     * 打开系统软键盘
     */
    protected void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      //  imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭系统软键盘
     */
    protected void ClosKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();
        Log.e("BaseLive","isOpen:"+isOpen);
        if (isOpen){
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public void hideKeyboard(View view){

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //隐藏系统盘
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }


    public void hideKeyBoard2(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(view.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 发送加入房间消息
     */
    public void sendJoinRoomMessage(String roomid, String userid, String teacherid , String nickName , String mAvatar) {
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        JoinRoomBean stMessAge = new JoinRoomBean();
        JoinRoomBean.DataBean stDb = new JoinRoomBean.DataBean();
        stMessAge.setMessageType(Constant.JOINREQ);
        stDb.setRoomId(roomid);
        stDb.setUserId(userid);
        stDb.setNickName(nickName);
        stDb.setAvatar(mAvatar);
        stDb.setMessage("");
        stDb.setRole(1);//角色
        stDb.setLevel(0);//等级
        stDb.setMasterUserId(0);
        stDb.setMasterNickName("");
        stDb.setMasterAvatar("");
        stDb.setMasterLevel(0);
        stDb.setBalance("");
        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        //   mRoomLiveHelp.sendMessage(0, 1, seqid, ss);
        Log.e(TAG +" 加入房间  ===   ", ss);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid), Integer.parseInt(userid),1,ss,seqid,Integer.parseInt(teacherid)  , false));
    }

    /**
     * 群发通知 当前房间的用户名单
     *
     * @param roomId
     * @param userId
     * @param userName
     * @param userImage
     * @param peronnelMap
     */
    public void sendQunJoinRoomPeonnel(RoomLiveHelp mRoomLiveHelp, String roomId,
                                       String userId, String userName, String userImage,
                                       Map<String, JoinRoomBean> peronnelMap ,
                                       String cupUserId ,Map<String , Integer> cupNum ) {

        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gsonQun = new Gson();
        JoinQunRoomBean joinQunRoomBean = new JoinQunRoomBean();
        JoinQunRoomBean.DataBeans joinQunData = new JoinQunRoomBean.DataBeans();
        List<JoinQunRoomBean.UserListBeans> joinQunUserArray = new ArrayList<>();
        joinQunRoomBean.setMessageType(Constant.ROOM_JOIN_QUN_RES);
        joinQunRoomBean.setCode("0");
        joinQunRoomBean.setMessage("");
        joinQunData.setRoomId(roomId);
        joinQunData.setUserId(userId);
        joinQunData.setNickName(userName);
        joinQunData.setAvatar(userImage);
        joinQunData.setLevel("0");
        joinQunData.setCount(String.valueOf(peronnelMap.size()));
        joinQunRoomBean.setData(joinQunData);

        Set<Map.Entry<String, JoinRoomBean>> entrySet = peronnelMap.entrySet();
        Iterator<Map.Entry<String, JoinRoomBean>> mapIt = entrySet.iterator();
        while (mapIt.hasNext()) {
            JoinQunRoomBean.UserListBeans joinQunUserList = new JoinQunRoomBean.UserListBeans();
            Map.Entry<String, JoinRoomBean> me = mapIt.next();

            Set<Map.Entry<String , Integer>> cup = cupNum.entrySet();
            Iterator<Map.Entry<String , Integer>> cupIt = cup.iterator();
            while (cupIt.hasNext()){
                Map.Entry<String , Integer> cupN = cupIt.next();
                if (me.getKey().equals(cupN.getKey())){
                    joinQunUserList.setTrophyCount(cupN.getValue());
                }else {
                    joinQunUserList.setTrophyCount(joinQunUserList.getTrophyCount());
                }
            }
            joinQunUserList.setAvatar(me.getValue().getData().getAvatar());
            joinQunUserList.setNickName(me.getValue().getData().getNickName());
            joinQunUserList.setLevel(me.getValue().getData().getLevel());
            joinQunUserList.setRole(me.getValue().getData().getRole());
            joinQunUserList.setUserId(me.getValue().getData().getUserId());
            joinQunUserArray.add(joinQunUserList);
        }
        joinQunRoomBean.getData().setUserList(joinQunUserArray);
        persionQun = gsonQun.toJson(joinQunRoomBean);
        Log.e("群发消息 ===   人员名单", persionQun);
        //  mRoomLiveHelp.sendMessage(0, 1, seqid, persionQun);
//        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomId), Integer.parseInt(userId),
//                1,persionQun,seqid,0, false));

    }


    /**
     * 发送同步文档
     *
     * @param docListBean
     * @param position
     * @param roomId
     */
    public void sendDocMessage(RoomLiveHelp mRoomLiveHelp, DocsListDetailsBean docListBean, int position, String roomId) {
        Long ct = System.currentTimeMillis();
        String seqids = String.valueOf(ct);
        Gson docGson = new Gson();
        currentPage = position;
        DocContentBean docBean = new DocContentBean();
        DocContentBean.DocDataBean docDataBean = new DocContentBean.DocDataBean();
        docBean.setMessageType(Constant.DOC_CONTENT_REQ);
        docDataBean.setRoomId(roomId);
        docDataBean.setImgSrc(docListBean.getImgSrc().get(position));
        docDataBean.setDocId(docListBean.getId());
        docDataBean.setCurrent(String.valueOf(position+1));
        docDataBean.setPage(docListBean.getPage());
        docBean.setData(docDataBean);
        String sendDoc = docGson.toJson(docBean);
        Log.e("文档消息  ===   ", sendDoc);
        //mRoomLiveHelp.sendMessage(0, 1, seqids, sendDoc);
//        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomId), mUserid,1,sendDoc,seqids,0 , false));
    }

    /**
     * 发送正在共享的文档
     *
     * @param mRoomLiveHelp
     * @param docList
     * @param roomId
     */
    public void sendLoadDocMessage(RoomLiveHelp mRoomLiveHelp, DocsListDetailsBean docList, String roomId , String userId) {
        Long ct = System.currentTimeMillis();
        String sequd = String.valueOf(ct);
        Gson docsGson = new Gson();
        DocContentBean docBean = new DocContentBean();
        DocContentBean.DocDataBean docDataBean = new DocContentBean.DocDataBean();
        docBean.setMessageType(Constant.DOC_CONTENT_REQ);
        docDataBean.setRoomId(roomId);
        docDataBean.setImgSrc(docList.getImgSrc().get(currentPage));
        docDataBean.setDocId(docList.getId());
        docDataBean.setCurrent(String.valueOf(currentPage+1));
        docDataBean.setPage(docList.getPage());
        docBean.setData(docDataBean);
        String sendDoc = docsGson.toJson(docBean);
        Log.e(" 当前正在发送文档  ===   ", sendDoc+"currentPage"+currentPage);
        //  mRoomLiveHelp.sendMessage(Long.valueOf(userId), 1, sequd, sendDoc);

        Constant. wsService.sendRequest(wsSendMsg(Integer.parseInt(roomId), Integer.parseInt(userId),1,sendDoc,sequd,Integer.parseInt(userId) ,false));
    }


    /**
     * 添加连麦人员名单列表
     *
     * @param lr
     */
    public static void acceptLmListPersion(LmAgreeResBean lr) {
        Gson gson = new Gson();
        LmListPersonnelBean lp = new LmListPersonnelBean();
        LmListPersonnelBean.ListDataBean ll = new LmListPersonnelBean.ListDataBean();
        LmListPersonnelBean.ListDataBean.UserListBean lu = new LmListPersonnelBean.ListDataBean.UserListBean();
        lp.setMessageType(Constant.LM_LIST_PERSONNEL);
        lu.setAvatar("");
        lu.setNickName("");
        lu.setUserId(lr.getData().getUserId());
        lu.setIntroduction("");
        lu.setType("2");
        luList.add(lu);
        ll.setUserList(luList);
        lp.setData(ll);
        lmListAccept = gson.toJson(lp);
        Log.e("接受连麦列表 ---- ", lmListAccept);
//        return lmListAccept;
    }


    /**
     * 删除连麦列表人员
     * @param uerid
     */
    public static void deleteLmListPersion(String uerid){
        Gson gson = new Gson();
        LmListPersonnelBean llpb =gson.fromJson(lmListAccept , LmListPersonnelBean.class);
        if (llpb!=null && llpb.getData()!=null&&llpb.getData().getUserList()!= null&& llpb.getData().getUserList().size()>0){
            for (int i = 0; i < llpb.getData().getUserList().size(); i++) {
                if (llpb.getData().getUserList().get(i).getUserId().equals(uerid)) {
                    llpb.getData().getUserList().remove(i);
                    luList.remove(i);
                    lmListAccept = gson.toJson(llpb);
                }
            }
        }
    }

    /**
     * 获取当前连麦人员链表
     * @return
     */
    public static String getBaselmListAccept(){
        return lmListAccept;
    }

    /**
     * 获取到当前房间人员连表
     * @return
     */
    public static String getPersionQunList(){
        return persionQun;
    }

    /**
     * 新人加入房间后单发当前连麦人员列表
     * @return
     */
    public void sendQunLmListAccept(RoomLiveHelp mRoomLiveHelp , String data){
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        JoinRoomBean jrb = gson.fromJson(data , JoinRoomBean.class);
        LmListPersonnelBean llpb = gson.fromJson(lmListAccept , LmListPersonnelBean.class);
        if (data != null && llpb != null && lmListAccept != null){
            if (llpb.getData().getUserList() != null && llpb.getData().getUserList().size() > 0){
//                mRoomLiveHelp.sendMessage(Long.valueOf(jrb.getAccountList().getUserId()) , 1, seqid , lmListAccept);
                Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(jrb.getData().getRoomId()), Integer.parseInt(jrb.getData().getUserId()),
                        1,lmListAccept,seqid,Integer.parseInt(jrb.getData().getUserId()),false));
            }
        }
    }

    /**
     * 踢出房间
     * @param mRoomLiveHelp
     * @param data
     */
    public void sendKickingMessage(RoomLiveHelp mRoomLiveHelp , String data){
        Gson gson = new Gson();
//        JoinRoomBean jrbk = gson.fromJson(data,JoinRoomBean.class);
//        LeaveMassageBean lmb = new LeaveMassageBean();
//        LeaveMassageBean.DataBean lmdb = new LeaveMassageBean.DataBean();
//        lmb.setMessageType(Constant.OUT_ROOM);
//        lmdb.setAvatar(jrbk.getAccountList().getAvatar());
//        lmdb.setIsMaster(0);
//        lmdb.setLevel("");
//        lmdb.setMessage("");
//        lmdb.setNickName(jrbk.getAccountList().getNickName());
//        lmdb.setRoomId(Integer.parseInt(jrbk.getAccountList().getRoomId()));
//        lmdb.setUserId(jrbk.getAccountList().getUserId());
//        lmb.setData(lmdb);
//        String lmdbs = gson.toJson(lmb);

        CustomBean customBean = gson.fromJson(data , CustomBean.class);
        //调用SDK踢出房间功能
        mRoomLiveHelp.kickRoomMember(Long.valueOf(customBean.getUserId()));

    }

    /**
     * SDK踢出房间
     * @param mRoomLiveHelp
     * @param userId
     */
    public void sendKickingSDKMessage(RoomLiveHelp mRoomLiveHelp , String userId){
        //调用SDK踢出房间功能
        mRoomLiveHelp.kickRoomMember(Long.valueOf(userId));
    }

    /**
     * 学生更新custom字段
     * @return
     */
    public String getJoinRoomCustom(String roomId , int userid , String nickName ,
                                    String avatar , int role , int mLm , int mStop , int mTrophy ,
                                    boolean whiteBoard , boolean cameraClosed , boolean micClosed){
        Gson gson = new Gson();
        CustomBean cb = new CustomBean();
        cb.setAvatar(avatar);
        cb.setLevel(0);
        cb.setNickName(nickName);
        cb.setRole(role);
        cb.setLm(mLm);
        cb.setStop(mStop);
        cb.setTrophyCount(mTrophy);
        cb.setUserId(userid);
        cb.setWhiteBoardAccess(whiteBoard);
        cb.setMicClosed(micClosed);
        cb.setCameraClosed(cameraClosed);
        String custom = gson.toJson(cb);
        return custom;



    }

    /**
     * 发送退出房间消息
     */
    public void sendLeaveMessage(RoomLiveHelp mRoomLiveHelp , String roomid, String userid, int ismasterq) {
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        LeaveMassageBean stMessAge = new LeaveMassageBean();
        LeaveMassageBean.DataBean stDb = new LeaveMassageBean.DataBean();
        stMessAge.setMessageType(Constant.LEAVEREQ);
        stDb.setRoomId(Integer.parseInt(roomid.trim()));
        stDb.setUserId(userid);
        stDb.setNickName("");
        stDb.setAvatar("");
        stDb.setLevel("");
        stDb.setIsMaster(ismasterq);
        stDb.setMessage("");

        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        //  mRoomLiveHelp.sendMessage(0, 1, seqid, ss);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid), Integer.parseInt(userid),1,ss,seqid,0 , false));
    }

    /**
     * 发送申请连麦消息
     */

    public void sendLmMessage(String teachid, String roomid, String userid,String nickname) {
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        LmBean stMessAge = new LmBean();
        LmBean.DataBean stDb = new LmBean.DataBean();
        stMessAge.setMessageType(Constant.LMREQ);
        stDb.setRoomId(roomid);
        stDb.setAdminUserId(teachid);//主播id
        stDb.setUserId(userid);
        stDb.setNickName(nickname);
        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        //  mRoomLiveHelp.sendMessage(Integer.parseInt(teachid), 1, seqid, ss);
        Constant. wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid), Integer.parseInt(userid),1,
                ss,seqid,Integer.parseInt(teachid), false));

    }

    /**
     * 给某人发送奖杯
     * @param roomid
     * @param dscUserid
     * @param userId
     * @param nickName
     */
    public void sendTrophy(String roomid , String dscUserid , String userId , String nickName){
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        TrophyAwardBean tab = new TrophyAwardBean();
        TrophyAwardBean.DataBean tabd = new TrophyAwardBean.DataBean();
        tab.setMessageType(Constant.TROPHY_AWARD_REQ);
        tabd.setNickName(nickName);
        tabd.setUserId(userId);
        tab.setData(tabd);
        String tb = gson.toJson(tab);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid),Integer.parseInt(dscUserid) , 1 , tb ,
                seqid , 0 , false));
    }

    /**
     * 开始答题
     * @param roomid
     * @param Options
     * @param status
     * @param dscUserid
     * @param userId
     */

    public void sendStartAnswer(String roomid, String Options,  String status,String dscUserid,String userId) {
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        TeacherAnswerBean tab = new TeacherAnswerBean();
        TeacherAnswerBean.DataBean tabd = new TeacherAnswerBean.DataBean();
        tab.setMessageType(Constant.SELECTOR_STATUS_REQ);
        tabd.setOptions(Options);
        tabd.setCorrect("");
        tabd.setStatus(status);
        tab.setData(tabd);
        String tb = gson.toJson(tab);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid), Integer.parseInt(dscUserid), 1, tb,
                seqid, Integer.parseInt(userId), false));
    }

    /**
     * 结束答题
     */
    public void sendOverAnswer(String roomid, String correct, String status,String dscUserid,String userId) {
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        TeacherAnswerBean tab = new TeacherAnswerBean();
        TeacherAnswerBean.DataBean tabd = new TeacherAnswerBean.DataBean();
        tab.setMessageType(Constant.SELECTOR_STATUS_REQ);
        tabd.setOptions("");
        tabd.setCorrect(correct);
        tabd.setStatus(status);
        tab.setData(tabd);
        String tb = gson.toJson(tab);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid), Integer.parseInt(dscUserid), 1, tb,
                seqid, Integer.parseInt(userId), false));
    }


    /**
     * 结束答题
     * @param roomid
     * @param Options
     * @param correct
     * @param status
     * @param dscUserid
     * @param userId
     */

    public void sendOverAnswer(String roomid, String Options, String correct, String status,String dscUserid,String userId) {
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        TeacherAnswerBean tab = new TeacherAnswerBean();
        TeacherAnswerBean.DataBean tabd = new TeacherAnswerBean.DataBean();
        tab.setMessageType(Constant.SELECTOR_STATUS_REQ);
        tabd.setOptions(Options);
        tabd.setCorrect(correct);
        tabd.setStatus(status);
        tab.setData(tabd);
        String tb = gson.toJson(tab);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid), Integer.parseInt(dscUserid), 1, tb,
                seqid, Integer.parseInt(userId), false));
    }


    /**
     * 提交答案
     */

    public void sendPutAnswer(String roomid, String nickname, String reple,String dscUserid,String userId) {
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        StudentAnswerBean tab = new StudentAnswerBean();
        StudentAnswerBean.DataBean tabd = new StudentAnswerBean.DataBean();
        tab.setMessageType(Constant.SELECTOR_REPLY_REQ);
        tabd.setUserId(dscUserid);
        tabd.setNickName(nickname);
        tabd.setReply(reple);
        tab.setData(tabd);
        String tb = gson.toJson(tab);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid), Integer.parseInt(dscUserid), 1, tb,
                seqid, Integer.parseInt(userId), false));
    }
    /**
     * 同意/拒绝连麦
     * @param
     */
    public void lmToApply(LmAgreeResBean lmAgreeResBean ,String teacherId , int dstUserId){
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(lmAgreeResBean.getData().getRoomId()),
                Integer.parseInt(teacherId),1,gson.toJson(lmAgreeResBean),seqid,dstUserId,false));
        if ("1".equals(lmAgreeResBean.getData().getType())){
//            EventBus.getDefault().post(new MessageEvent(Constant.ACCEPT_RED_LM , gson.toJson(lmAgreeResBean)));
            acceptLmListPersion(lmAgreeResBean);
        }else {
//            EventBus.getDefault().post(new MessageEvent(Constant.REFUSED_RED_LM , gson.toJson(lmAgreeResBean)));
        }
    }



    /**
     * 开始上课
     * @param roomid
     * @param userid
     */
    public void startLive(String roomid ,String userid){
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        StartLiveBean startLiveBean = new StartLiveBean();
        StartLiveBean.Livebean slDataBean = new StartLiveBean.Livebean();

        startLiveBean.setMessageType(Constant.COURSE_START_REQ);
        slDataBean.setRoomId(roomid);
        slDataBean.setUserId(userid);
        startLiveBean.setData(slDataBean);

        String startLive = gson.toJson(startLiveBean);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid),Integer.parseInt(userid),
                1,startLive,seqid,0,true));
    }


    /**
     * 下课
     * @param roomid
     * @param userid
     */
    public void endLive(String roomid , String userid){
        Log.e(TAG , "老师下课  ");
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        StartLiveBean startLiveBean = new StartLiveBean();
        StartLiveBean.Livebean slDataBean = new StartLiveBean.Livebean();

        startLiveBean.setMessageType(Constant.COURSE_LEAVE_REQ);
        slDataBean.setRoomId(roomid);
        slDataBean.setUserId(userid);
        startLiveBean.setData(slDataBean);

        String endLive = gson.toJson(startLiveBean);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid),Integer.parseInt(userid),
                1,endLive,seqid,0,true));
    }

    /**
     * 学生进入房间发送老师 , 是否已经开始上课
     * 此方法想解决服务器解绑/绑定收不到服务器上课信令的持久化问题
     * @param roomid
     * @param userid
     */
    public void notStartLive(String roomid , String userid ,String type){
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        Gson gson = new Gson();
        StartNotLiveBean startLiveBean = new StartNotLiveBean();
        StartNotLiveBean.DataBean slDataBean = new StartNotLiveBean.DataBean();

        startLiveBean.setMessageType(Constant.COURSE_START_NOT_REQ);
        slDataBean.setRoomId(roomid);
        slDataBean.setUserId(userid);
        slDataBean.setType(type);
        startLiveBean.setData(slDataBean);

        String endLive = gson.toJson(startLiveBean);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomid),Integer.parseInt(userid),
                1,endLive,seqid,0,false));
    }


    /**
     * 绑定/解绑服务器
     * @return
     */
    public static String wsbind(int roomid, int userid, String seqid, int erouterType) {
        Gson gson = new Gson();
        String appid = Constant.app_id;
        String roomappid = roomid + "_" + appid + "_" + "1";
        List<String> roomidlist = new ArrayList<>();
        roomidlist.add(roomappid);

        BindWebsocket bindWebsocket = new BindWebsocket();
        bindWebsocket.setEType(17);
        BindWebsocket.MChatTransMsgBean mChatTransMsgBean = new BindWebsocket.MChatTransMsgBean();
        mChatTransMsgBean.setSSeqID(seqid);
        mChatTransMsgBean.setEBusinessType(1);
        BindWebsocket.MChatTransMsgBean.MRouterTableMsgBean mRouterTableMsgBean = new BindWebsocket.MChatTransMsgBean.MRouterTableMsgBean();
        mRouterTableMsgBean.setERouterType(erouterType);
        mRouterTableMsgBean.setNUserID(userid);
        mRouterTableMsgBean.setEUEType(1);
        mRouterTableMsgBean.setSMediaIDList(roomidlist);
        mRouterTableMsgBean.setBServer(false);
        mRouterTableMsgBean.setNGroupID(roomid);
        mRouterTableMsgBean.setSAppID(appid);
        mRouterTableMsgBean.setSConnectID(seqid);
        mChatTransMsgBean.setMRouterTableMsg(mRouterTableMsgBean);
        bindWebsocket.setMChatTransMsg(mChatTransMsgBean);
        return gson.toJson(bindWebsocket);
    }

    /**
     * 发送Im消息
     *
     * @return
     */
    public static String wsSendMsg(int roomid, int userid , int datatype, String sdata, String seqid, int NDstUserID,boolean persistence) {
        Gson gson = new Gson();
        String appid = Constant.app_id;
        String roomappid = roomid + "_" + appid + "_" + "1";
        WebsocketImBean websocketImBean = new WebsocketImBean();
        websocketImBean.setEType(17);
        WebsocketImBean.MChatTransMsgBean mChatTransMsgBean = new WebsocketImBean.MChatTransMsgBean();
        mChatTransMsgBean.setSMediaID(roomappid);
        mChatTransMsgBean.setEChatDataType(datatype);
        mChatTransMsgBean.setNSrcUserID(userid);
        mChatTransMsgBean.setNDstUserID(NDstUserID);
        mChatTransMsgBean.setSData(sdata);
        mChatTransMsgBean.setNDataLen(10000);
        mChatTransMsgBean.setSSeqID(seqid);
        mChatTransMsgBean.setSAppID(appid);
        mChatTransMsgBean.setBPersistence(persistence);
        mChatTransMsgBean.setEBusinessType(1);
        mChatTransMsgBean.setNGroupID(roomid);
        websocketImBean.setMChatTransMsg(mChatTransMsgBean);
        return gson.toJson(websocketImBean);
    }

    /**
     * 持久化数据删除
     * 老师下课调用
     * @param roomId
     * @return
     */
    public void wsDeleteMsg(String roomId){
        Gson gson = new Gson();
        WebsocketDeleteBean websocketDeleteBean = new WebsocketDeleteBean();
        WebsocketDeleteBean.MChatTransMsgBean mChatTransMsgBean = new WebsocketDeleteBean.MChatTransMsgBean();
        WebsocketDeleteBean.MChatTransMsgBean.MWBMgrMsgBean mwbMgrMsgBean = new WebsocketDeleteBean.MChatTransMsgBean.MWBMgrMsgBean();
        websocketDeleteBean.setEType(17);
        mChatTransMsgBean.setEChatDataType(7);
        mwbMgrMsgBean.setEWBMgrType(1);
        mwbMgrMsgBean.setSAppID(Constant.app_id);
        mwbMgrMsgBean.setNGroupID(Integer.parseInt(roomId));
        mChatTransMsgBean.setMWBMgrMsg(mwbMgrMsgBean);
        websocketDeleteBean.setMChatTransMsg(mChatTransMsgBean);
        String wsDelete = gson.toJson(websocketDeleteBean);
        if(Constant.wsService != null) {
            Constant.wsService.sendRequest(wsDelete);
        }

    }


    /**
     * 生成加入房间用户连表
     */
    public JoinRoomBean getJoinRoom(String roomid, String userid, String teacherid , String nickName , String mAvatar) {
        Gson gson = new Gson();
        JoinRoomBean stMessAge = new JoinRoomBean();
        JoinRoomBean.DataBean stDb = new JoinRoomBean.DataBean();
        stMessAge.setMessageType(Constant.JOINREQ);
        stDb.setRoomId(roomid);
        stDb.setUserId(userid);
        stDb.setNickName(nickName);
        stDb.setAvatar(mAvatar);
        stDb.setMessage("");
        stDb.setRole(1);//角色
        stDb.setLevel(0);//等级
        stDb.setMasterUserId(0);
        stDb.setMasterNickName("");
        stDb.setMasterAvatar("");
        stDb.setMasterLevel(0);
        stDb.setBalance("");
        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        return stMessAge;

    }

    /**
     * websocket
     * 程序启动时第一次请求
     * 获取集群地址
     */
    public static void getWsIpLocationReq(String appid , int userId){
        Gson wsGson = new Gson();
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        WsIpLocationBean wiltb = new WsIpLocationBean();
        WsIpLocationBean.MProtocolMsgBean wiltMpb = new WsIpLocationBean.MProtocolMsgBean();
        WsIpLocationBean.MProtocolMsgBean.MIpAddrMsgBean wiltMpMsgb = new WsIpLocationBean.MProtocolMsgBean.MIpAddrMsgBean();

        wiltb.setEType(1);
        wiltb.setSSEQID(seqid);
        wiltMpb.setSVersion("Test_4.2.0.777");
        wiltMpb.setBLoadBalance(false);
        wiltMpb.setBReConnect(false);
        wiltMpb.setBVipUser(false);
        wiltMpb.setSIDentify("7B4FCDBC0914E7182B8EC357F07498F1");
        wiltMpb.setSSecret("c052cd055acfabc5");
        wiltMpb.setSVerifyCode("abcd");
        wiltMpb.setSAppID(appid);
        wiltMpb.setNUserID(userId);
        wiltMpb.setSClientVer("Android_" + Constant.APP_VERSION_CODE );
        wiltMpb.setSConnectID("1530864681000");
        wiltMpMsgb.setBFec(false);
        wiltMpMsgb.setSIp(Constant.BaseUrl);
        wiltMpMsgb.setNPort(0);
        wiltMpb.setMIpAddrMsg(wiltMpMsgb);
        wiltb.setMProtocolMsg(wiltMpb);

        String iploLocation = wsGson.toJson(wiltb);
        Constant.wsService.sendRequest(iploLocation);
    }




    /**
     * 获取版本号
     */
    public String getVersionNumber() {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName() , 0);
            versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }



}
