package com.tttlive.education.ui.room;

/**
 * Created by mr.li on 2018/3/16.
 */

public interface RoomMsg {
   //聊天消息
   void receiveTextMessage (String data);
   //加入房间
   void joinRoomSuccess (String data);
   //退出房间
   void leaveMessage (String data);
   //申请连麦消息
   void dealApplyMicMessage (String data);
   //连麦响应
   void dealApplyAgreeMessage(String data);
   /**
    * 房间人员名单
    */
   void roomPersonnelList(String data);
   /**
    * 文档同步功能
    */
   void roomDocConect(String data);
   /**
    * 禁止发言
    */
   void closeLmCall(String data);

   /**
    * 踢出房间
    */
   void outRoomClose(String data);

   /**
    * 连麦列表
    */
   void lmListPersonnel(String data);

   /**
    * 禁言
    * @param data
    */
   void gagReqPersonnel(String data);

   /**
    * 解除禁言
    * @param data
    */
   void gagRerRemovePersonnel(String data);

   /**
    * 开始上课
    * @param data
    */
   void courseStart(String data);

   /**
    * 下课
    * @param data
    */
   void courseLeave(String data);

   /**
    * 咨询主播是否已经开始上课
    * @param data
    */
   void courseTeacherNotStart(String  data);

   /**
    * 收到发送的奖杯
    * @param data
    */
   void trophyAward(String data);

   /**
    * 收到答题信令
    * @param data
    */
   void statrAnswer(String data);

   /**
    * 收到学生提交答案
    */
   void statisicsAnswer(String data);

   /**
    * 白板授权
    */
   void whiteboardAccess(String data);

   /**
    * 关闭摄像头
    */
   void liveVideoClose(String data);

   /**
    * 关闭麦克风
    */
   void liveAudioClose(String data);

   /**
    * 接收视频模式
    */
   void modeChangeReq(String data);


}
