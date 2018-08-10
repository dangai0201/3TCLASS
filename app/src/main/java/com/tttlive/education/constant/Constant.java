package com.tttlive.education.constant;


import com.tttlive.education.ui.room.socket.WebSocketService;

/**
 * Created by Iverson on 2017/10/9 下午4:53
 * 此类用于：
 */
public class Constant {

    public static final int VIEW_THROTTLE_SHORT_TIME = 100;
    public static final int VIEW_THROTTLE_MIDDLING_TIME = 3;
    public static final int VIEW_THROTTLE_LONG_TIME = 5;
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int SEND_MESSAGE_TYPE_TEXT = 1;  //文本或者表情
    public static final int SEND_MESSAGE_TYPE_IMAGE = 2;    //图片
    public static boolean USER_ISROOM = false;
    public static int EQUIPMENT_WIDTH = 0;    //屏幕宽度
    public static int EQUIPMENT_HEIGHT = 0;   //屏幕高度
    public static String APP_VERSION_CODE; //版本号
    public static final String TROPHY_MESSAGE = "trophy";
    public static boolean exitRoom = false;
    public static boolean popupType = false;



    //登录者是学生
    public static int LIVE_PERSONNEL_TYPE_STUDENT = 0x02;
    //登录者是老师
    public static int LIVE_PERSONNEL_TYPE_TEACHER = 0x03;
    //登录类型
    public static int LIVE_PERSONNEL_TYPE = 0x01;
    //课程类型默认
    public static int LIVE_COURSE_TYPE_DEFAULT = 0x00;
    //课程类型
    public static int LIVE_COURSE_TYPE;
    //主播登录的id
    public static String LIVE_TYPE_ID;
    //房间ID
    public static String LIVE_ROOM_ID;
    //bugly的keyte
    public static final String BuglyKey = "f27a557d57";
    //API接口地址
    //测试环境
    public static String BaseUrl = "http://dev.api.edu.3ttech.cn";
    //预发环境
//    public static String BaseUrl = "https://pre.api.edu.3ttech.cn";
    //生产环境
//    public static String BaseUrl = "https://api.edu.3ttech.cn";
    //白板地址
//    public static final String DocUrl = "https://dev.3ttech.cn:8093/resources/jsw/docs-wb.html";
//    public static String DocUrl = "https://room.3tclass.3ttech.cn/resources/jsw/docs-wb.html";
    public static String DocUrl;
    public static String SOCKET_URL_DEFAULT;
    //websocket 地址
//      public static String SOCKET_URL = "ws://abc.3ttech.cn:10082";
    //预发
//    public static String SOCKET_URL = "ws://stech.3ttech.cn:10081";
    //测试
//    public static String SOCKET_URL = "ws://education.3ttech.cn:10081";
    public static String SOCKET_URL;
    public static int SOCKET_URL_PORT;
//    public static String SOCKET_URL = "ws://dev1.api.demo.3ttech.cn:10082";
    public static String Websocket_url;
    public static int Websocket_port;
    public static String SOCKET_URL_IP;
    //分享链接
    public static String SHARE_CLASS_ROOM_URL;
    //用户协议链接地址
    public static String SERVER_CLAUSE_URL;
    //老师分享邀请码链接
    public static final String SHARE_TEACHER_URL = "http://3tclass.3ttech.cn/course/teacher-authority?id=";
    //分享端图片链接
    public static final String SHARE_IMAGE_URL = "http://3tlive.oss-cn-beijing.aliyuncs.com/share/144_ic_launcher_APP.png";

    /**
     * Web Socket 服务器地址。
     * wss://abc.3ttech.cn:443/chat/ 或ws://abc.3ttech.cn:10082
     * ws://dev1.api.demo.3ttech.cn:10082
     */
    public static WebSocketService wsService;

    public static final String MAIN_HOST_FOR_PING = "dev.api.livedemo.3ttech.cn";
    //拉流地址
    public static final String PULL_STREAM = "rtmp://pull.3ttech.cn/sdk/";

    //编辑课程
    public static String COURSETITLE = "course_title";//课程名称
    public static String COURSESTARTTIME = "course_start_time";//课程开始时间
    public static String COURSESENDTIME = "course_end_time";//课程结束时间
    public static final String COUSERDURTYPE = "1";//课程时间类型,长期时间类型
    public static final String BIGCOURSE = "0";//大班课
    public static final String MIDDLECOURSE = "1";//小班课
    public static final String SMALLCOURSE = "2";//一对一
    public static final String LOGININVOTECODE = "login_invote_code";//登录的邀请码
    public static final String KEY_LOGIN_INFO = "AccountLoginBean"; //登录信息
    //创建课程成功 , 返回刷新
    public static String CREATCOURSELIVE = "0";

    /********返回状态码表*************/
    public static final int HTTP_REQUEST_ACCOUNT_SUCCESSFUL = 0;
    public static final int HTTP_REQUEST_ACCOUNT_ERROR = 100;
    public static final int HTTP_REQUEST_ACCOUNT_NOT_EXIST = 101;
    public static final int HTTP_REQUEST_ACCOUNT_ACCOUNT_SUSPENDED = 102;
    public static final int HTTP_REQUEST_SERVER_EXCEPTION = 110;



    /********收消息类型*************/
    public static final String FIELD_TYPE = "messageType";
    //加入房间
    public static final String JOINREQ = "join_req";
    //退出房间
    public static final String LEAVEREQ = "leave_req";
    //聊天消息
    public static final String BARRAGE_REQ = "im_req";
    //申请连麦
    public static final String LMREQ = "lm_req";
    //群发进入房间人员名单
    public static final String ROOM_JOIN_QUN_RES = "join_notify_res";
    //连麦响应
    public static final String LM_RES = "lm_agree_req";
    //文档同步
    public static final String DOC_CONTENT_REQ = "document_content_broadcast_req";
    //发送连麦列表
    public static final String LM_LIST_PERSONNEL = "lm_list_res";
    //断开连麦
    public static final String LM_CLOSE_CALL = "close_call_req";
    //踢出房间
    public static final String OUT_ROOM = "out_leave_req";
    //禁止发言
    public static final String GAG_REG = "gag_req";
    //解除禁言
    public static final String GAG_RES_REMOVE = "gag_res";
    //上课
    public static final String COURSE_START_REQ = "course_start_req";
    //下课
    public static final String COURSE_LEAVE_REQ = "course_over_req";
    //当前状态是否为上课
    public static final String COURSE_START_NOT_REQ = "course_start_not_req";
    //触发奖杯动画
    public static final String TROPHY_AWARD_REQ = "trophy_award_req";
    //开始答题
    public static final String SELECTOR_STATUS_REQ = "selector_status_req";
    //提交答案
    public static final String SELECTOR_REPLY_REQ = "selector_reply_req";
    //白板授权
    public static final String WHITEBOARD_ACCESS_REQ = "whiteboard_access_req";
    //关闭摄像头
    public static final String VIDEO_CLOSE_OPEN = "camera_closed_req";
    //开关麦克风
    public static final String AUDIO_CLOSE_OPEN = "mic_closed_req";
    //视频模式切换
    public static final String MODE_CHANGE_REQ = "mode_change_req";


    /**********发送消息广播类型*********/
    //发送文字聊天广播
    public static final String SEND_EVENT_TEXT = "messageText";
    //发送人员名单Bean类广播
    public static final String SEND_EVENT_PERSONNEL = "message_personnel";
    //学生接受人员名单even类广播
    public static final String SEND_EVENT_PERSONNEL_STUDENT = "message_personnel_student";
    //接受到连麦请求广播
    public static final String SEND_EVENT_LMREQ = "message_lm_req";
    //学生拒绝老师邀请连麦广播
    public static final String SEND_EVENT_STUDENT_REFUSED = "student_refused_lm";
    //学生接受到老师禁言广播
    public static final String SEND_EVENT_GAG_REG = "send_gag_reg";
    //学生接受到老师解除禁言广播
    public static final String SEND_EVENT_REMOVE_GAG_RER = "send_gag_remove_rer";
    //正在已经同意连麦
    public static final String SEND_EVENT_NOW_MESSAGE_LM = "Now_message_lm";
    //更新当前人员连表信息,(包含连麦中,申请连麦,普通成员)
    public static final String SEND_EVENT_OVERALL_PERSONNEL = "overall_personnel";


    /*********发送普通广播类型********/
    //发送更新小红点数量
    public static final String ORDINARY_SEND_RED_DOT = "little_red_dot";
    //同意连麦减少小红点数量
    public static final String ACCEPT_RED_LM = "accept_red_lm";
    //拒绝连麦减少小红点数量
    public static final String REFUSED_RED_LM = "refused_red_lm";
    //连麦申请中退出刷新小红点数量
    public static final String LEAVE_ROOM_PERSONE = "leave_room_persone_one";
    //将某人踢出房间
    public static final String KICKED_OUT_PERSONNEL_ROOM = "kicked_out_room";

    /**************************/
    /**
     * 错误类型：被踢出房间。
     */
    public static final String ERROR_KICKED = "error.kicked";

    public static final String GIFT_NOTIFY_RES = "gift_notify_res";//群发

    public static String app_id;



}