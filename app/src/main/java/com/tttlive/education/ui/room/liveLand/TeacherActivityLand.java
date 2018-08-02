package com.tttlive.education.ui.room.liveLand;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tttlive.basic.education.R;
import com.tttlive.education.adapter.BaseTrophyAdapter;
import com.tttlive.education.adapter.CourseWareAdapter;
import com.tttlive.education.adapter.ProfessionRecyclerViewAdapter;
import com.tttlive.education.adapter.StatisicsAdapter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.ui.Interface.RoomInterface;
import com.tttlive.education.ui.SharePopu;
import com.tttlive.education.ui.room.BaseLiveActivity;
import com.tttlive.education.ui.room.ChatPopupWindow;
import com.tttlive.education.ui.room.DocsListDetailsBean;
import com.tttlive.education.ui.room.DocsListbean;
import com.tttlive.education.ui.room.MessageEvent;
import com.tttlive.education.ui.room.RoomMessageType;
import com.tttlive.education.ui.room.RoomMsg;
import com.tttlive.education.ui.room.TeacherPresenter;
import com.tttlive.education.ui.room.TeacherUIinterface;
import com.tttlive.education.ui.room.bean.CustomBean;
import com.tttlive.education.ui.room.bean.JoinRoomBean;
import com.tttlive.education.ui.room.bean.LeaveMassageBean;
import com.tttlive.education.ui.room.bean.LmAgreeResBean;
import com.tttlive.education.ui.room.bean.LmBean;
import com.tttlive.education.ui.room.bean.LmDisconnectBean;
import com.tttlive.education.ui.room.bean.SendTextMessageBean;
import com.tttlive.education.ui.room.bean.ShareBean;
import com.tttlive.education.ui.room.bean.StartNotLiveBean;
import com.tttlive.education.ui.room.bean.StudentAnswerBean;
import com.tttlive.education.ui.room.bean.UploadImageBean;
import com.tttlive.education.ui.room.custom.CustomDialog;
import com.tttlive.education.ui.room.messageEvent.PersonnelEvent;
import com.tttlive.education.ui.room.socket.WebSocketService;
import com.tttlive.education.ui.room.socket.WsListener;
import com.tttlive.education.ui.room.webviewtool.WebviewToolPopupWindowLand;
import com.tttlive.education.ui.share.ShareInterface;
import com.tttlive.education.ui.share.SharePresenter;
import com.tttlive.education.ui.widget.NetStatusView;
import com.tttlive.education.util.AlarmTimeUtils;
import com.tttlive.education.util.CustomAnimationDrawableNew;
import com.tttlive.education.util.DateUtils;
import com.tttlive.education.util.FireworksView;
import com.tttlive.education.util.GitfSpecialsStop;
import com.tttlive.education.util.ImagePathUtil;
import com.tttlive.education.util.MaterialBadgeTextView;
import com.tttlive.education.util.MessageDialog;
import com.tttlive.education.util.PermissionUtils;
import com.tttlive.education.util.PingUtil;
import com.tttlive.education.util.SPTools;
import com.tttlive.education.util.ShareUtil;
import com.tttlive.education.util.TeacherPersonnelUtil;
import com.tttlive.education.util.Tools;
import com.tttlive.education.util.VideoView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.wushuangtech.bean.LocalAudioStats;
import com.wushuangtech.bean.LocalVideoStats;
import com.wushuangtech.bean.RemoteAudioStats;
import com.wushuangtech.bean.RemoteVideoStats;
import com.wushuangtech.library.Constants;
import com.wushuangtech.room.core.EnterUserInfo;
import com.wushuangtech.room.core.RoomLiveHelp;
import com.wushuangtech.room.core.RoomLiveInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/5/29/0029.
 * 教师界面 横屏
 */
public class TeacherActivityLand extends BaseLiveActivity implements View.OnClickListener,
        TeacherUIinterface, RoomLiveInterface, RoomMsg, ShareInterface, GitfSpecialsStop, RoomInterface {

    private static final String TAG_CLASS = TeacherActivityLand.class.getSimpleName();
    private String START_TIME_CYCLE = "START_TIME_CYCLE";
    private static String TAG_NAME = TeacherActivityLand.class.getSimpleName();
    private Context mContext;
    public static final String ROOM_TYPE = "live_type";
    public static final String ROOM_USER_ID = "user_id";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_USER_NICKNAME = "room_user_nickName";
    public static final String ROOM_TU_URL = "room_tu_url";
    public static final String ROOM_STATUS_INACTIVATED = "inactivated";//未激活
    public static final String ROOM_STATUS_PUBLISHED = "published";//答题中
    public static final String ROOM_STATUS_ENDED = "ended";//答题结束


    /**
     * 答题区分
     */
    public static final String ROOM_COURSE_TYPE = "room_course_type";

    private ArrayList<DocsListbean.ListBean> listbean = new ArrayList<>();
    private Map<String, CustomBean> roomJoinMap = new LinkedHashMap<>();
    private ArrayList<VideoView> mVideoViewList = new ArrayList();
    //聊天消息列表
    private ArrayList<SendTextMessageBean> msgList = new ArrayList<>();
    //申请连麦的人员
    private List<String> listLmUserid = new ArrayList<>();
    //正在连麦人员
    private List<String> nowListLmUserid = new ArrayList<>();
    //奖杯
    private Map<Integer, Integer> cupMapList = new LinkedHashMap<>();
    //房间人数列表
    private List<CustomBean> customBeanList = new ArrayList<>();

    private AcceptOnClickListener acceptOnClickListener = new AcceptOnClickListener();
    private MdocOnItemClickListener mdocOnItemClickListener = new MdocOnItemClickListener();
    private ToolDisplacementAddListener toolDisplacementAddListener = new ToolDisplacementAddListener();

    private Handler mHandler = new Handler();

    private String tPageSize = "4";
    private String teacher_room_id;
    private String teacher_user_id;
    private String teacher_room_type;
    private String teacher_user_name;
    private String teacher_user_image;
    private String room_course_type;
    private String seqid;
    private String lmUserId;
    private String teacher_invite_code;
    private String personnelKey;
    private JoinRoomBean personnelValue;
    private StudentAnswerBean mStudentAnswerBean;

    private int tPage = 1;
    private int currentCheckedItemDefault = -1;
    private int mapWidth;
    private int mapHeight;
    private int lmReq = 0;
    private int TEACHER_WEB_STATE = 1;  //白板老师

    private boolean live_start_not = false;
    private boolean isAnimation = false;
    private boolean switchCamera = false;
    private boolean isSpeak = false;
    private boolean isCamera = false;
    private boolean cup_trophy = false;
    private boolean animation = false;
    private boolean isEnabled = false;
    private boolean mWhiteBoard = false; //白板授权
    private boolean mVideoClose = false;  //开关摄像头
    private boolean mAudeoClose = false;  //开关,麦克风
    //    大礼物动画结束
    private boolean isgiftend = true;
    private boolean animation_tool = false;
    private boolean accordVideoView = false;

    private long liveTime;

    private Gson mGson = new Gson();
    private CustomDialog dialog;
    private PopupWindow courseListPop;
    private RecyclerView rv_popup_view;
    private LinearLayout ll_item_a;
    private PtrClassicFrameLayout ptr_load_docs;
    private TeacherPresenter teacherPresenter;
    private DocsPtrHadler docsPtrHadler;
    private CourseWareAdapter mCourseWareAdapter;
    private WebviewToolPopupWindowLand mWebviewToolPopupWindow;
    private RoomLiveHelp mRoomLiveHelp;
    private VideoView.setOnClickListener videoOnClickListener;
    private WebSocketService tWsService;
    private PopupWindow personnelPopWindow;
    private PopupWindow trophyPopWindow;
    private PopupWindow answerPopWindow;
    private PopupWindow statisticsPopWindow;
    public SharePopu shareTeacherPopu;
    private PingUtil mPingUtil;


    private TeacherPersonnelUtil teacherPersonnel;
    private ChatPopupWindow chatPop;
    private PopupWindow popBanLiveSpeak;
    private SharePresenter sharePresenter;

    private ImageView image_back;
    private ImageView image_view_share;
    private WebView teacher_view_web;
    private TextView page_textview_number;
    private ImageView img_chat;
    private MaterialBadgeTextView mbt_personnel_lm;
    private ImageView iv_personnel_detail;
    private MaterialBadgeTextView material_badge_view;
    private ImageView iv_previous_page;
    private ImageView iv_next_page;
    private TextView tv_start_live;
    private ImageView iv_animation_view;
    private ImageView iv_tool_answer;
    private ImageView iv_tool_trophy;
    private ImageView iv_tool_whiteboard;
    private ImageView iv_microphone;
    private ImageView iv_camera;
    private ImageView iv_radio_courseware;
    private RelativeLayout main;
    private LinearLayout ll_courseware_button;
    private RelativeLayout rl_teacher_video_view;
    private ImageView iv_teacher_view;
    private ImageView iv_live_image_close;
    private RelativeLayout rl_microphone_open_two;
    private RelativeLayout rl_teacher_top;
    private ImageView iv_live_camera_flip;
    private RelativeLayout rl_microphone_open_one;
    private ImageView iv_pop_window_back;
    private ImageView iv_pop_answer_window_back;
    private ImageView iv_pop_statisics_window_back;
    private RecyclerView rv_pop_lm;
    private RecyclerView rv_answer_list;
    private RecyclerView rv_statisics_list;
    private TextView tv_statisics_num;
    private TextView tv_statisics_percentage;
    private TextView tv_statisics_correct_answer;
    private Button bt_anew_statisics;
    private Button bt_add_item_view;
    private Button bt_delete_item_view;
    private Button bt_start_answer;
    private Button bt_over_statisics;
    private LinearLayout land_ll_tool_chat;
    private RelativeLayout teacher_head_video_land;
    private LinearLayout ll_tran_live;
    private TextView tv_speak_cannel;
    private TextView tv_ban_live_speak;
    private ImageView iv_doc_dropdown;
    private TextView tv_pop_kicked_out_live;
    private ImageView iv_live_navbar_unfold;
    private RelativeLayout rl_web_view;
    private FireworksView fireworks;
    private RelativeLayout animLayout;
    private BaseTrophyAdapter basePerAdapter;
    private ProfessionRecyclerViewAdapter answerAdapter;
    private StatisicsAdapter statisicsAdapter;
    private int mOptionnum = 4;

    private String arr[] = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private ArrayList<StudentAnswerBean> mAnswerList = new ArrayList<>();//答题人数
    private List<String> solution;
    private LinearLayout ll_animation_filling_cup;
    private TextView tv_statisics_time;
    private RelativeLayout rl_live_video_name;
    private TextView tv_user_name;
    private TextView tv_trophy_time;
    private TextView tv_user_one;
    private ImageView iv_animation_filling;
    private RelativeLayout rl_animation_trophy;

    private static final String TITLE = "title";
    private static final String TIME_START = "timeStart";
    private String title;
    private String timeStart;

    /**
     * 网络状态view
     */
    private NetStatusView netStatusView;
    private String video_host_ip;
    private BroadcastReceiver mTimeReceiver;
    private LinearLayout tool_ll_view_displacement;
    private ImageView land_iv_raise_menu;

    /**
     * @param context
     * @param roomType 入会类型
     * @param userId
     * @param roomId
     * @return
     */
    public static Intent createIntent(Context context, int roomType, String userId,
                                      String roomId, String nickName, String tuUrl,
                                      String courseType, String remark,
                                      String title, String timeStart) {
        Intent intent = new Intent(context, TeacherActivityLand.class);
        intent.putExtra(ROOM_TYPE, String.valueOf(roomType));
        intent.putExtra(ROOM_USER_ID, userId);
        intent.putExtra(ROOM_ID, roomId);
        intent.putExtra(ROOM_USER_NICKNAME, nickName);
        intent.putExtra(ROOM_TU_URL, tuUrl);
        intent.putExtra(ROOM_COURSE_TYPE, courseType);
        intent.putExtra(TITLE, title);
        intent.putExtra(TIME_START, timeStart);
        return intent;
    }

    private ServiceConnection teWsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            tWsService = ((WebSocketService.ServiceBinder) service).getService();
            initWsListeners();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //初始化websocket消息
    private void initWsListeners() {

        WsListener notifListener = new WsListener() {

            @Override
            public void handleData(String msg) {
                RoomMessageType msgType = new RoomMessageType(TeacherActivityLand.this);
                msgType.appendData(msg);

            }
        };
        if (Constant.wsService != null && notifListener != null) {
            Constant.wsService.registerListener(notifListener);
        } else {
            Log.i(TAG_NAME, " WsListener wsService : " + Constant.wsService);
            Log.i(TAG_NAME, " WsListener notifListener : " + notifListener);
            Log.i(TAG_NAME, " WsListener tWsService : " + tWsService);
            if (tWsService != null) {
                tWsService.registerListener(notifListener);
            } else {
                toastShort("网络连接异常,请重新登录");
                onBackPressed();
            }

        }
    }

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_teacher_land;
    }

    @Override
    protected void findView() {
        mContext = this;
        netStatusView = findViewById(R.id.netStatusView);
        main = findViewById(R.id.main);
        image_back = findViewById(R.id.land_image_view_back);
        image_view_share = findViewById(R.id.land_image_view_share);
        teacher_view_web = findViewById(R.id.land_teacher_view_web);
        page_textview_number = findViewById(R.id.land_page_textview_number);
        img_chat = findViewById(R.id.land_img_chat);
        mbt_personnel_lm = findViewById(R.id.land_mbt_personnel_chat);
        iv_personnel_detail = findViewById(R.id.land_iv_personnel_detail);
        material_badge_view = findViewById(R.id.land_materialBadge_textview_chat);
        iv_previous_page = findViewById(R.id.land_iv_previous_page);
        iv_next_page = findViewById(R.id.land_iv_next_page);
        tv_start_live = findViewById(R.id.land_tv_start_live);
        iv_animation_view = findViewById(R.id.land_iv_animation_view);
        iv_tool_answer = findViewById(R.id.land_iv_tool_answer);
        iv_tool_trophy = findViewById(R.id.land_iv_tool_trophy);
        iv_tool_whiteboard = findViewById(R.id.land_iv_tool_whiteboard);
        iv_microphone = findViewById(R.id.land_iv_microphone);
        iv_camera = findViewById(R.id.land_iv_camera);
        iv_radio_courseware = findViewById(R.id.land_iv_radio_courseware);
        rl_teacher_video_view = findViewById(R.id.rl_teacher_video_view);
        land_ll_tool_chat = findViewById(R.id.land_ll_tool_chat);
        iv_live_navbar_unfold = findViewById(R.id.iv_live_navbar_unfold);
        animLayout = findViewById(R.id.room_live_show_anim_layout);
        iv_live_camera_flip = findViewById(R.id.land_iv_live_camera_flip);
        ll_animation_filling_cup = findViewById(R.id.ll_animation_filling_cup);
        tool_ll_view_displacement = findViewById(R.id.tool_ll_view_displacement);
        land_iv_raise_menu = findViewById(R.id.land_iv_raise_menu);
        ll_animation_filling_cup.setBackgroundColor(getResources().getColor(R.color.white));
        rl_web_view = findViewById(R.id.rl_web_view);
        iv_previous_page.setVisibility(View.GONE);
        iv_next_page.setVisibility(View.GONE);
        iv_tool_answer.setVisibility(View.GONE);
        iv_tool_trophy.setVisibility(View.GONE);
        tool_ll_view_displacement.setVisibility(View.GONE);
        iv_radio_courseware.setVisibility(View.VISIBLE);

        image_back.setOnClickListener(this);
        iv_radio_courseware.setOnClickListener(this);
        tv_start_live.setOnClickListener(this);
        iv_animation_view.setOnClickListener(this);
        iv_tool_whiteboard.setOnClickListener(this);
        iv_previous_page.setOnClickListener(this);
        iv_next_page.setOnClickListener(this);
        iv_microphone.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        iv_personnel_detail.setOnClickListener(this);
        img_chat.setOnClickListener(this);
        image_view_share.setOnClickListener(this);
        iv_live_navbar_unfold.setOnClickListener(this);
        iv_tool_trophy.setOnClickListener(this);
        iv_tool_answer.setOnClickListener(this);
        land_iv_raise_menu.setOnClickListener(this);
        iv_live_camera_flip.setOnClickListener(this);

    }

    @Override
    protected void initView() {
        bindService(WebSocketService.createIntent(TeacherActivityLand.this), teWsConnection, BIND_AUTO_CREATE);
        teacherPresenter = new TeacherPresenter(this);
        docsPtrHadler = new DocsPtrHadler();
        mRoomLiveHelp = new RoomLiveHelp(this, this);
        sharePresenter = new SharePresenter(this);
        mPingUtil = new PingUtil(this, this);
        EventBus.getDefault().register(this);
        initWsListeners();
        initPersonnerView();


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mapWidth = dm.widthPixels;
        mapHeight = dm.heightPixels;

        Intent mIntent = getIntent();
        title = mIntent.getStringExtra(TITLE);
        timeStart = mIntent.getStringExtra(TIME_START);
        teacher_user_name = mIntent.getStringExtra(ROOM_USER_NICKNAME);
        room_course_type = mIntent.getStringExtra(ROOM_COURSE_TYPE);
        teacher_room_id = mIntent.getStringExtra(ROOM_ID);
        teacher_user_id = mIntent.getStringExtra(ROOM_USER_ID);
        teacher_room_type = mIntent.getStringExtra(ROOM_TYPE);

        netStatusView.setCourseName(title);
        teacher_user_image = "";
        Long ct = System.currentTimeMillis();
        seqid = "binding_" + String.valueOf(ct);
        Constant.LIVE_PERSONNEL_TYPE = Constant.LIVE_PERSONNEL_TYPE_TEACHER;
        Constant.LIVE_TYPE_ID = teacher_user_id;
        Constant.LIVE_ROOM_ID = teacher_room_id;
        sharePresenter.getShareInvite(teacher_room_id);
        if (TextUtils.isEmpty(teacher_user_name)) {
            teacher_user_name = getResources().getString(R.string.live_teacher) + " " + teacher_user_id;
        } else {
            teacher_user_name = getResources().getString(R.string.live_teacher) + " " + teacher_user_id;
        }
        tv_start_live.setText(getResources().getString(R.string.start_course_req) + "\n" + getResources().getString(R.string.start_course_res));

        String teBangDingWeb = wsbind(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), seqid, 2);

        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(teBangDingWeb);
        } else {
            if (tWsService != null) {
                tWsService.sendRequest(teBangDingWeb);
            }
        }

        SPTools.getInstance(this).save(teacher_room_id , teBangDingWeb);

        chatPop = new ChatPopupWindow(mContext, teacher_room_id, teacher_user_id, teacher_user_name, teacher_user_image);
        loadRoomVideo(teacher_user_id, teacher_room_id, teacher_room_type);
        webViewInit();
        webviewTool("boardSetTool(\'clearAll2\')");
        inittrophyView();

        startTiemHeart();
        initBreceiver(mContext);

    }

    /**
     * 10秒发送ping
     * 检测网络状态
     */
    private void startTiemHeart() {
        long mNowTime = System.currentTimeMillis() + 20000;
        AlarmTimeUtils.getInstance().configureAlarmManagers(mContext, TAG_CLASS, START_TIME_CYCLE, mNowTime);
    }

    /**
     * 注册广播
     *
     * @param context
     */
    private void initBreceiver(Context context) {
        mTimeReceiver = new TimeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TAG_CLASS);
        mContext.registerReceiver(mTimeReceiver, intentFilter);
    }

    /**
     * 接受10秒到了的广播
     */
    public class TimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String alarmTime = intent.getStringExtra("heardTime");
            if (null != alarmTime && alarmTime.equals(START_TIME_CYCLE)) {
                if (!TextUtils.isEmpty(video_host_ip)) {
                    mPingUtil.PingHttp("4", "64", "1", video_host_ip);
                }
                startTiemHeart();
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isok = PermissionUtils.onRequestPermissionsResults(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
//        boolean isok = PermissionUtils.onActivityResults(this, resultCode);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (live_start_not) {
            endLive(teacher_room_id, teacher_user_id);
            webviewPage("switchWhiteBoard()");
        }
        //sendLeaveMessage(mRoomLiveHelp, teacher_room_id, teacher_user_id, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listbean != null && listbean.size() > 0) {
            listbean.clear();
        }
        if (mWebviewToolPopupWindow != null) {
            mWebviewToolPopupWindow.dismissPop();
        }
        wsDeleteMsg(teacher_room_id);
        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(wsbind(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), seqid, 3));
            Constant.wsService.prepareShutdown();
        } else {
            tWsService.sendRequest(wsbind(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), seqid, 3));
            tWsService.prepareShutdown();
        }
        if (teWsConnection != null) {
            unbindService(teWsConnection);
        }
        //退出直播
        if (mRoomLiveHelp != null) {
            mRoomLiveHelp.exitHelp();
            mRoomLiveHelp = null;
        }
        if (listLmUserid != null) {
            listLmUserid.clear();
        }
        if (nowListLmUserid != null) {
            nowListLmUserid.clear();
        }
        if (msgList != null) {
            msgList.clear();
        }
        if (chatPop.isShow()) {
            chatPop.closePop();
        }
        if (mVideoViewList != null && mVideoViewList.size() > 0) {
            mVideoViewList.clear();
        }
        if (cupMapList != null && cupMapList.size() > 0) {
            cupMapList.clear();
        }
        if (customBeanList != null && customBeanList.size() > 0) {
            customBeanList.clear();
        }
        if (shareTeacherPopu != null && shareTeacherPopu.isShowing()) {
            shareTeacherPopu.dismiss();
        }
        mHandler.removeCallbacks(timerRunnable);
        roomJoinMap.clear();
        live_start_not = false;
        accordVideoView = false;
        teacherPresenter.unsubscribeTasks();
        unregisterReceiver(mTimeReceiver);
        Constant.USER_ISROOM = false;
        Constant.popupType = false;
        Constant.exitRoom = true;
        answerListClear();
        DestoryLmList();
        PopDismiss();
        PopLmDismiss();
        popCupDismiss();
        disCourseListPop();
        disLoadDialog();
        statisticsPopWindowDiemiss();
        UMShareAPI.get(this).release();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.land_image_view_back://退出直播间
                exitRoomDialog();
                break;
            case R.id.land_image_view_share://弹出分享弹窗
                if (shareTeacherPopu != null && shareTeacherPopu.isShowing())
                    shareTeacherPopu.dismiss();
                showSharePopu();
                break;
            case R.id.land_iv_radio_courseware://弹出课件列表
                if (DateUtils.isFastClick()) {
                    showPopupCourseBottom(main);
                    dialog = new CustomDialog(this, R.style.CustomDialog);
                    dialog.show();
                }
                break;
            case R.id.ll_item_pop_a://关闭课件列表
                disCourseListPop();
                break;
            case R.id.iv_doc_dropdown://关闭课件列表
                disCourseListPop();
                break;
            case R.id.land_tv_start_live: //开始上课
                mHandler.removeCallbacks(timerRunnable);
                startTimer("network");
                startLive(teacher_room_id, teacher_user_id);
                live_start_not = true;
                tv_start_live.setVisibility(View.GONE);
                if (isAnimation) {
                    iv_animation_view.setVisibility(View.VISIBLE);
                } else {
                    iv_animation_view.setVisibility(View.GONE);
                }
                iv_tool_trophy.setVisibility(View.VISIBLE);
                iv_tool_answer.setVisibility(View.VISIBLE);

                break;
            case R.id.land_iv_animation_view:
                //开始动画
                webviewTool("nextStep()");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webviewPage("getCurrentTotalPage()");
                    }
                }, 500);

                break;
            case R.id.land_iv_tool_whiteboard:
                //画笔工具
                toolOpen();
                break;
            case R.id.land_iv_previous_page:
                //上一页
                webviewTool("previousPage()");
                webviewPage("getCurrentTotalPage()");
                break;
            case R.id.land_iv_next_page:
                //下一页
                webviewTool("nextPage()");
                webviewPage("getCurrentTotalPage()");
                break;

            case R.id.land_iv_live_camera_flip:
                //翻转摄像头
                if (switchCamera) {
                    mRoomLiveHelp.switchCamera();
                    switchCamera = false;
                    iv_live_camera_flip.setSelected(false);
                    iv_live_camera_flip.setBackground(getResources().getDrawable(R.drawable.icon_camera_before));
                } else {
                    mRoomLiveHelp.switchCamera();
                    switchCamera = true;
                    iv_live_camera_flip.setSelected(true);
                    iv_live_camera_flip.setBackground(getResources().getDrawable(R.drawable.icon_camera_after));
                }
                break;
            case R.id.land_iv_microphone://打开或者关闭语音
                if (isSpeak) {
                    speakOpen();
                } else {
                    speakClos();
                }
                break;
            case R.id.land_iv_camera://打开或者关闭摄像头
                if (isCamera) {
                    videoOpen();
                } else {
                    videoClose();
                }
                break;
            case R.id.land_iv_personnel_detail://弹出人员列表
                //人员名单
                personnelPopWindow(v);
                break;
            case R.id.iv_cup_pop_personnel_back://取消发奖杯弹窗
                popCupDismiss();
                break;
            case R.id.iv_pop_personnel_back://关闭人员列表
                PopLmDismiss();
                break;
            case R.id.land_img_chat://弹出聊天窗口
                if (!chatPop.isShow()) {
                    chatPop.showChatPop(land_ll_tool_chat, TeacherActivityLand.this, msgList, false);
                    mbt_personnel_lm.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_personnel_transparent_live:
                PopDismiss();
                break;
            case R.id.tv_pop_ban_speak_live:
                testDisconnectVideo(v, lmUserId);
                PopDismiss();
                break;
            case R.id.tv_pop_speak_cancle:
                PopDismiss();
                break;
            case R.id.tv_pop_kicked_out_live:
                //踢出房间
                sendKickingSDKMessage(mRoomLiveHelp, lmUserId);
                PopDismiss();
                break;
            case R.id.iv_live_navbar_unfold:
                Animation animationIn = AnimationUtils.loadAnimation(mContext, R.anim.gradually_hidden_in);
                Animation animationOut = AnimationUtils.loadAnimation(mContext, R.anim.gradually_accord_out);
                for (int i = 0; i < mVideoViewList.size(); i++) {
                    VideoView courVideo = mVideoViewList.get(i);
                    if (courVideo.getVisibility() == View.VISIBLE) {
                        courVideo.setVisibility(View.GONE);
                        courVideo.startAnimation(animationIn);
                        iv_live_navbar_unfold.setBackground(getResources().getDrawable(R.drawable.living_navbar_collapse_icon));
                    } else {
                        courVideo.setVisibility(View.VISIBLE);
                        courVideo.startAnimation(animationOut);
                        iv_live_navbar_unfold.setBackground(getResources().getDrawable(R.drawable.living_navbar_unfold_icon));
                    }

                }
                break;
            case R.id.land_iv_tool_trophy:
                //奖杯
                inittrophyView();
                trophyPopWindow(v);
                break;
            case R.id.land_iv_tool_answer:
                //答题
                initAnswerView();
                answerPopWindow(v);

                break;
            case R.id.bt_add_item_view:
                //添加答题选项
                mOptionnum++;
                if (mOptionnum > 8) {
                    mOptionnum = 8;
                }
                answerAdapter.setNum(mOptionnum);
                Log.e(TAG_NAME, "add" + mOptionnum);
                break;
            case R.id.bt_delete_item_view:
                //删除答题选项
                mOptionnum--;
                if (mOptionnum < 4) {
                    mOptionnum = 4;
                }
                answerAdapter.setNum(mOptionnum);
                Log.e(TAG_NAME, "delete" + mOptionnum);
                break;
            case R.id.bt_start_answer:
                //开始答题
                StringBuffer strOptions = new StringBuffer();//答题器选项
                //                StringBuffer strCorrect=new StringBuffer();//正确答案
                solution = answerAdapter.getSelectContent();
                if (solution.size() > 0) {
                    int itemCount = answerAdapter.getItemCount();
                    for (int i = 0; i < itemCount; i++) {
                        strOptions.append(arr[i]);
                    }
                    //排序
                    Collections.sort(solution);
                    sendStartAnswer(teacher_room_id, strOptions.toString(), ROOM_STATUS_PUBLISHED, teacher_user_id, "0");
                    //关掉当前pop
                    answerPopWindow.dismiss();
                    //打开答题统计pop
                    initStatisticsView();
                    statisticsPopWindow(v);

                } else {
                    toastShort("还没有预设正确答案");
                }
                break;
            case R.id.iv_cup_pop_statisics_back:
                //关闭答题器
                statisticsPopWindowDiemiss();
                answerListClear();
                sendOverAnswer(teacher_room_id, "", ROOM_STATUS_INACTIVATED, teacher_user_id, "0");
                break;
            case R.id.bt_over_statisics:

                //结束答题
                StringBuffer strCorrect = new StringBuffer();//正确答案

                if (solution.size() > 0) {
                    //排序
                    Collections.sort(solution);
                    for (int j = 0; j < solution.size(); j++) {

                        strCorrect.append(solution.get(j));
                    }

                    sendOverAnswer(teacher_room_id, strCorrect.toString(), ROOM_STATUS_ENDED, teacher_user_id, "0");
                    bt_over_statisics.setVisibility(View.GONE);
                    bt_anew_statisics.setVisibility(View.VISIBLE);

                }
                break;

            case R.id.bt_anew_statisics:
                //重新开始
                answerListClear();
                statisticsPopWindowDiemiss();
                initAnswerView();
                answerPopWindow(v);
                sendOverAnswer(teacher_room_id, "", ROOM_STATUS_INACTIVATED, teacher_user_id, "0");
                break;
            case R.id.land_iv_raise_menu:
                if (accordVideoView){
                    ToolsAnimator();
                }
                break;

        }

    }


    private int obtain_trophy = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(resultCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
        }

        switch (requestCode) {
            case Constant.PHOTO_REQUEST_GALLERY:
                if (data != null && data.getData() != null) {
                    Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        String imagePath = ImagePathUtil.getImageAbsolutePath(this, selectedUri);
                        File file = new File(imagePath);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                        teacherPresenter.getImageUpload(requestFile, body);
                        System.out.println("---------------   " + imagePath);
                    }
                }
                break;

        }

    }

    @Subscribe
    public void TeacherEvent(MessageEvent messageEvent) {
        Gson leaveGson = new Gson();
        if (Constant.ACCEPT_RED_LM.equals(messageEvent.getType())) {
            //同意连麦
            lmReq--;
            material_badge_view.setText(String.valueOf(lmReq));
            if (lmReq == 0) {
                material_badge_view.setVisibility(View.GONE);
            }
            LmAgreeResBean agreeLmbean = leaveGson.fromJson(messageEvent.getMessage(), LmAgreeResBean.class);
            lmToApply(agreeLmbean, teacher_user_id, 0);
            showRemoteView(agreeLmbean.getData().getUserId());
            removeLmUserId(agreeLmbean.getData().getUserId());
            nowListLmUserid.add(agreeLmbean.getData().getUserId());
            teacherPersonnel.setNowPersonnelLm(mContext, roomJoinMap, nowListLmUserid, listLmUserid, cupMapList);

        } else if (Constant.REFUSED_RED_LM.equals(messageEvent.getType())) {
            //拒绝连麦
            lmReq--;
            material_badge_view.setText(String.valueOf(lmReq));
            if (lmReq == 0) {
                material_badge_view.setVisibility(View.GONE);
            }
            LmAgreeResBean agreeLmbean = leaveGson.fromJson(messageEvent.getMessage(), LmAgreeResBean.class);
            lmToApply(agreeLmbean, teacher_user_id, Integer.parseInt(agreeLmbean.getData().getUserId()));
            removeLmUserId(agreeLmbean.getData().getUserId());
        } else if (Constant.LEAVE_ROOM_PERSONE.equals(messageEvent.getMessage())) {
            lmReq--;
            material_badge_view.setText(String.valueOf(lmReq));
            if (lmReq == 0) {
                material_badge_view.setVisibility(View.GONE);
            }
        } else if (Constant.LM_CLOSE_CALL.equals(messageEvent.getType())) {
            //断开连麦
            LmDisconnectBean ldb = leaveGson.fromJson(messageEvent.getMessage(), LmDisconnectBean.class);
            releaseLiveView(Long.valueOf(ldb.getData().getUserId()));
            deleteLmListPersion(ldb.getData().getUserId());
            removeLmUserId(ldb.getData().getUserId());
            nowRemoveLmUserId(ldb.getData().getUserId());
            teacherPersonnel.setNowPersonnelLm(mContext, roomJoinMap, nowListLmUserid, listLmUserid, cupMapList);
            toastShort("连麦成员断开");
        } else if (Constant.KICKED_OUT_PERSONNEL_ROOM.equals(messageEvent.getType())) {
            leaveRoomMapList(leaveGson.fromJson(messageEvent.getMessage(), LeaveMassageBean.class), 1);
        }
    }



    @Override
    public void enterRoomSuccess() {
        Log.i(TAG_NAME, "enterRoomSuccess:进入房间成功");
        accordVideoView = true;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            VideoViewLocationSize(teacher_user_id);
            iv_microphone.setBackground(getResources().getDrawable(R.drawable.icon_microphone_normal));
            iv_camera.setBackground(getResources().getDrawable(R.drawable.icon_camera_normal));
            speakOpen();
            videoOpen();
            teacherJoinBean();
            Constant.USER_ISROOM = true;
        }
    }

    @Override
    public void enterRoomFailue(int error) {
        Log.e(TAG_NAME, "enterRoomFailue:进入房间失败" + error);
        if (error == Constants.ERROR_ENTER_ROOM_TIMEOUT) {
            toastShort(R.string.msg_unknown_error);
        } else if (error == Constants.ERROR_ENTER_ROOM_FAILED) {
            toastShort(R.string.msg_unconnection_error);
        } else if (error == Constants.ERROR_ENTER_ROOM_VERIFY_FAILED) {
            toastShort(R.string.msg_unVerification_error);
        } else if (error == Constants.ERROR_ENTER_ROOM_UNKNOW) {
            toastShort(R.string.msg_unRoom_error);
        } else {
            toastShort(R.string.msg_unEnter_error);
        }
        onBackPressed();
    }

    @Override
    public void onDisconnected(int errorCode) {
        Log.i(TAG_NAME, "onDisconnected:直播中断线" + errorCode);
        if (errorCode == Constants.ERROR_KICK_BY_RELOGIN) {
            toastShort(R.string.msg_operation_other_account);
            onBackPressed();
        } else if (errorCode == 100) {
            toastShort(R.string.msg_operation_net_abnormal);
            onBackPressed();
        } else {
            toastShort(R.string.msg_operation_live_out);
        }

    }

    @Override
    public void onMemberExit(long userId) {
        Log.i(TAG_NAME, "onMemberExit:直播成员退出  " + userId);
        releaseLiveView(userId);
        LeaveMassageBean stMessAge = new LeaveMassageBean();
        LeaveMassageBean.DataBean stDb = new LeaveMassageBean.DataBean();
        stMessAge.setMessageType(Constant.LEAVEREQ);
        stDb.setRoomId(Integer.parseInt(teacher_room_id));
        stDb.setUserId(String.valueOf(userId));
        stDb.setNickName("");
        stDb.setAvatar("");
        stDb.setLevel("");
        stDb.setIsMaster(0);
        stDb.setMessage("");
        stMessAge.setData(stDb);
        leaveRoomMapList(stMessAge, 1);

    }

    @Override
    public void onMemberEnter(long userId, EnterUserInfo userInfo, String sCustom) {
        Log.e(TAG_NAME, "onMemberEnter:直播成员进入  " + userId + " sCustom  " + sCustom);

        CustomBean cb = mGson.fromJson(sCustom, CustomBean.class);
        joinRoomMapList(cb, 1);
    }

    @Override
    public void onHostEnter(long userId, EnterUserInfo userInfo, String sCustom) {
        Log.e(TAG_NAME, "onHostEnter:直播主播进入  " + userId + " sCustom  " + sCustom);
    }

    @Override
    public void onUpdateLiveView(List<EnterUserInfo> userInfos) {
        Log.e(TAG_NAME, "onUpdateLiveView ---------  ");
    }

    @Override
    public void dispatchMessage(long srcUserID, int type, String sSeqID, String data) {
        Log.e(TAG_NAME, " 收到消息srcUserID:" + srcUserID + "type:--" + type + "-sSeqID:--" + sSeqID + "-data:--" + data);
    }

    @Override
    public void sendMessageResult(int resultType, String data) {

    }

    @Override
    public void localVideoStatus(LocalVideoStats localVideoStats) {

    }

    @Override
    public void remoteVideoStatus(RemoteVideoStats mRemoteVideoStats) {

    }

    @Override
    public void LocalAudioStatus(LocalAudioStats localAudioStats) {

    }

    @Override
    public void remoteAudioStatus(RemoteAudioStats mRemoteAudioStats) {
        //接收远端音频下行速率
    }

    @Override
    public void OnupdateUserBaseInfo(Long roomId, long uid, String sCustom) {
        //用户有信息更新
        Log.e(TAG_NAME, "OnupdateUserBaseInfo roomId : " + roomId + " sCustom " + sCustom);
        //        toastShort("有数据更新了");
    }

    @Override
    public void OnConnectSuccess(String ip, int port) {
        //接受当前登录的媒体服务器
        Log.i(TAG_NAME, "OnConnectSuccess   ip = " + ip + " port = " + port);
        //        mPingUtil.PingHttp("4","64" , "1" , ip);
        video_host_ip = ip;
        mPingUtil.PingHttp("4", "64", "1", ip);
    }

    /**
     * 教师分享
     *
     * @param share_media
     * @param install
     */
    private void shareTeacher(SHARE_MEDIA share_media, boolean install) {

        if (share_media == null) {//share_media null 复制链接
            return;
        }
        if (!install) { //未安装客户端
            toastShort("你还没有安装客户端,请先安装。");
            return;
        }
        if (timeStart != null && timeStart.contains(".")) {
            timeStart = timeStart.replace(".", "-");
        }
        new ShareUtil.Build(this).listener(umShareListener).url(Constant.SHARE_TEACHER_URL + teacher_room_id)
                .title(title).description("开课时间 : " + timeStart)
                .thumb(Constant.SHARE_IMAGE_URL).shareMedia(share_media).build().share();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    public void docSucess(DocsListbean docsLists) {
        //课件列表请求成功
        Log.i(TAG_NAME, " docSucess  " + docsLists);
        listbean.clear();
        if (docsLists != null && docsLists.getList().size() > 0) {
            listbean.addAll(docsLists.getList());
            tPage++;
            dialog.dismiss();
            if (rv_popup_view != null) {
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rv_popup_view.setLayoutManager(layoutManager);
                mCourseWareAdapter = new CourseWareAdapter(mContext, listbean);
                mCourseWareAdapter.check(currentCheckedItemDefault, true);
                rv_popup_view.setAdapter(mCourseWareAdapter);
                mCourseWareAdapter.setOnItemClickListener(mdocOnItemClickListener);
            }
        } else if (tPage == 1) {
            toastShortLong("当前课程暂时没有课件");
            disLoadDialog();
        } else {
            toastShortLong("已是最后一页了");
            disLoadDialog();
        }

    }

    @Override
    public void docDetailsSucess(DocsListDetailsBean docDetail) {
        //课程列表详情
    }

    @Override
    public void imageUploadSucess(BaseResponse<UploadImageBean> uploadBean) {
        Log.e(TAG_NAME, "上传成功 url " + uploadBean.getData().getUrl());
        sendTextMessage(uploadBean.getData().getUrl(), Constant.SEND_MESSAGE_TYPE_IMAGE);

    }

    @Override
    public void receiveTextMessage(String data) {
        Gson gson = new Gson();
        SendTextMessageBean sendmsg = gson.fromJson(data, SendTextMessageBean.class);
        msgList.add(sendmsg);
        if (chatPop.isShow()) {
            chatPop.showChatList(msgList);

        } else {
            mbt_personnel_lm.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void joinRoomSuccess(String data) {
        //        Gson mGson = new Gson();
        //        Log.i(TAG_NAME, "收到观众加入房间的回调data:" + data);
        //        JoinRoomBean joinRoomBean = mGson.fromJson(data, JoinRoomBean.class);
        //        joinRoomMapList(joinRoomBean, 1);
        //        sendQunLmListAccept(mRoomLiveHelp, data);
        //        Log.i(TAG_NAME, "onMemberEnter:房间人员连接数量  " + roomJoinMap.size());

    }

    @Override
    public void leaveMessage(String data) {
        Log.i(TAG_NAME, " 退出房间的回调data:" + new Gson().toJson(data));
        //        Gson leaveGson = new Gson();
        //        LeaveMassageBean leaveRoomBean = leaveGson.fromJson(data, LeaveMassageBean.class);
        //        leaveRoomMapList(leaveRoomBean, 1);

    }

    @Override
    public void dealApplyMicMessage(String data) {
        Log.i(TAG_NAME, " 连麦请求回调data:" + new Gson().toJson(data));
        Gson lmGson = new Gson();
        LmBean lmBean = lmGson.fromJson(data, LmBean.class);
        if (listLmUserid != null && listLmUserid.size() > 0) {
            for (int i = 0; i < listLmUserid.size(); i++) {
                if (listLmUserid.get(i).equals(lmBean.getData().getUserId())) {
                    return;
                }
            }
        }
        if (nowListLmUserid != null && nowListLmUserid.size() > 0) {
            for (int i = 0; i < nowListLmUserid.size(); i++) {
                if (nowListLmUserid.get(i).equals(lmBean.getData().getUserId())) {
                    return;
                }
            }
        }
        lmReq++;
        if (lmReq > 0) {
            material_badge_view.setVisibility(View.VISIBLE);
            material_badge_view.setText(String.valueOf(lmReq));
        }
        listLmUserid.add(lmBean.getData().getUserId());
        if (nowListLmUserid != null && nowListLmUserid.size() > 0) {
            teacherPersonnel.setPersonnelAll(mContext, roomJoinMap, nowListLmUserid, listLmUserid, cupMapList);
        } else {
            teacherPersonnel.setPersonnelLm(mContext, roomJoinMap, listLmUserid, cupMapList);
        }

    }

    @Override
    public void dealApplyAgreeMessage(String data) {


    }

    @Override
    public void roomPersonnelList(String data) {

    }

    @Override
    public void roomDocConect(String data) {
        //文档同步
    }

    @Override
    public void closeLmCall(String data) {
        //断开连麦
    }

    @Override
    public void outRoomClose(String data) {
        //踢出房间
    }

    @Override
    public void lmListPersonnel(String data) {
        //当前连麦列表
    }

    @Override
    public void gagReqPersonnel(String data) {
        //禁言
    }

    @Override
    public void gagRerRemovePersonnel(String data) {
        //解除禁言
    }

    @Override
    public void courseStart(String data) {
        //开始上课
        Log.e("TAG", "开始上课");
        netStatusView.setCourseStatus(getResources().getString(R.string.network_stauts_has_classes));

    }

    @Override
    public void courseLeave(String data) {
        //下课

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        Date time = calendar.getTime();
        liveTime = time.getTime();
        String sysTimeStr = DateFormat.format("HH:mm:ss", liveTime).toString();
        netStatusView.setCourseStatus(getResources().getString(R.string.network_stauts_not_classes));
        netStatusView.setTime(sysTimeStr);
    }

    @Override
    public void courseTeacherNotStart(String data) {
        //是否已经开课
        Log.i(TAG_NAME, "是否已经开课 " + data);
        Gson notGson = new Gson();
        StartNotLiveBean sNotLiveBean = notGson.fromJson(data, StartNotLiveBean.class);
        if (sNotLiveBean.getData().getType().equals("3")) {
            if (live_start_not) {
                //已经开课
                notStartLive(teacher_room_id, teacher_user_id, "1");
            } else {
                //未开课
                notStartLive(teacher_room_id, teacher_user_id, "0");
            }
        }

    }

    @Override
    public void trophyAward(String data) {
        //收到发送的奖杯
    }

    @Override
    public void statrAnswer(String data) {

    }

    @Override
    public void statisicsAnswer(String data) {
        Double count = 0d;
        Double n = 0d;
        //收到学生提交的答案
        Gson notGson = new Gson();
        mStudentAnswerBean = notGson.fromJson(data, StudentAnswerBean.class);
        mAnswerList.add(mStudentAnswerBean);
        statisicsAdapter = new StatisicsAdapter(this, mAnswerList);
        rv_statisics_list.setAdapter(statisicsAdapter);


        StringBuffer strCorrect = new StringBuffer();//正确答案

        if (solution.size() > 0) {

            //排序
            Collections.sort(solution);
            for (int j = 0; j < solution.size(); j++) {

                strCorrect.append(solution.get(j));
            }


            for (int i = 0; i < mAnswerList.size(); i++) {
                if (mAnswerList.get(i).getData().getReply().equals(strCorrect.toString())) {
                    count++;
                }
            }

            if (count == 0) {
                n = 0.0 * 100;
            } else {
                n = count / mAnswerList.size() * 100;
            }

            tv_statisics_num.setText(mAnswerList.size() + "人");
            tv_statisics_percentage.setText(n + "%");
            tv_statisics_correct_answer.setText("  " + strCorrect.toString());


        }
    }

    @Override
    public void whiteboardAccess(String data) {

    }

    @Override
    public void liveVideoClose(String data) {
        //摄像头关闭

    }

    @Override
    public void liveAudioClose(String data) {
        //麦克风关闭

    }

    @Override
    public void showNetworkException() {
        super.showNetworkException();
        Log.e(TAG_NAME , " showNetworkException  ");
        disLoadDialog();

    }

    @Override
    public void showLoadingComplete() {
        super.showLoadingComplete();
        if (ptr_load_docs != null) {
            ptr_load_docs.refreshComplete();
        }
    }

    /**
     * 初始化人员名单布局
     */
    private void initPersonnerView() {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.pop_personnel_detail_list_land, null);
        personnelPopWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        iv_pop_window_back = popView.findViewById(R.id.iv_pop_personnel_back);
        rv_pop_lm = popView.findViewById(R.id.rv_personnel_lm_lang);
        iv_pop_window_back.setOnClickListener(this);
        rv_pop_lm.setOnClickListener(this);
        teacherPersonnel = new TeacherPersonnelUtil(mContext, rv_pop_lm);
    }

    /**
     * 初始化奖杯名单
     */
    private void inittrophyView() {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.pop_trophy_detail_list_land, null);
        trophyPopWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        iv_pop_window_back = popView.findViewById(R.id.iv_cup_pop_personnel_back);
        rv_pop_lm = popView.findViewById(R.id.rv_trophy_personnel_lm);
        tv_trophy_time = popView.findViewById(R.id.tv_trophy_time);
        iv_animation_filling = popView.findViewById(R.id.iv_animation_filling);
        rl_animation_trophy = popView.findViewById(R.id.rl_animation_trophy);

        iv_pop_window_back.setOnClickListener(this);
        rv_pop_lm.setOnClickListener(this);
        rv_pop_lm.setLayoutManager(new GridLayoutManager(mContext, 1));
        trophyPersion("0");
        startTimer("trophy");
    }

    /**
     * 更新奖杯人员连表
     */
    private void trophyPersion(String type) {
        basePerAdapter = new BaseTrophyAdapter(mContext, customBeanList, cupMapList, type);
        rv_pop_lm.setAdapter(basePerAdapter);
        basePerAdapter.setAcceptOnClickListener(acceptOnClickListener);

        if (listLmUserid != null && listLmUserid.size() > 0 || nowListLmUserid != null && nowListLmUserid.size() > 0) {
            teacherPersonnel.setPersonnelAll(mContext, roomJoinMap, nowListLmUserid, listLmUserid, cupMapList);
        } else {
            teacherPersonnel.initViewPersonnel(mContext,
                    new PersonnelEvent(Constant.SEND_EVENT_PERSONNEL, "", roomJoinMap), cupMapList);
        }

    }

    /**
     * 初始化答题
     */
    private void initAnswerView() {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.pop_answer_detail_list_land, null);
        answerPopWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        iv_pop_answer_window_back = popView.findViewById(R.id.iv_cup_pop_answer_back);
        rv_answer_list = popView.findViewById(R.id.rv_answer_list);
        bt_add_item_view = popView.findViewById(R.id.bt_add_item_view);
        bt_delete_item_view = popView.findViewById(R.id.bt_delete_item_view);
        bt_start_answer = popView.findViewById(R.id.bt_start_answer);
        iv_pop_answer_window_back.setVisibility(View.GONE);
        bt_start_answer.setOnClickListener(this);
        bt_delete_item_view.setOnClickListener(this);
        bt_add_item_view.setOnClickListener(this);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_answer_list.setLayoutManager(lm);


        answerAdapter = new ProfessionRecyclerViewAdapter(this, mOptionnum);
        rv_answer_list.setAdapter(answerAdapter);


    }

    /**
     * 初始化答题统计
     */
    private void initStatisticsView() {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.pop_statisics_detail_list_land, null);
        statisticsPopWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        iv_pop_statisics_window_back = popView.findViewById(R.id.iv_cup_pop_statisics_back);
        rv_statisics_list = popView.findViewById(R.id.rv_statisics_list);
        tv_statisics_num = popView.findViewById(R.id.tv_statisics_num);
        tv_statisics_percentage = popView.findViewById(R.id.tv_statisics_percentage);
        tv_statisics_correct_answer = popView.findViewById(R.id.tv_statisics_correct_answer);
        bt_anew_statisics = popView.findViewById(R.id.bt_anew_statisics);
        tv_statisics_time = popView.findViewById(R.id.tv_statisics_time);

        bt_over_statisics = popView.findViewById(R.id.bt_over_statisics);
        bt_anew_statisics.setOnClickListener(this);
        bt_over_statisics.setOnClickListener(this);
        iv_pop_statisics_window_back.setOnClickListener(this);
        //       LinearLayoutManager lm=new LinearLayoutManager(this);
        //       lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_statisics_list.setLayoutManager(new GridLayoutManager(this, 2));

        startTimer("Statistics");

    }

    /**
     * 人员名单
     */
    private void personnelPopWindow(View view) {
        personnelPopWindow.setAnimationStyle(R.style.AnimBottomIn);
        personnelPopWindow.setBackgroundDrawable(new BitmapDrawable());
        personnelPopWindow.setFocusable(true);
        personnelPopWindow.setOutsideTouchable(true);
        personnelPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 奖杯名单
     *
     * @param view
     */
    private void trophyPopWindow(View view) {
        trophyPopWindow.setAnimationStyle(R.style.AnimBottomIn);
        trophyPopWindow.setBackgroundDrawable(new BitmapDrawable());
        trophyPopWindow.setFocusable(true);
        trophyPopWindow.setOutsideTouchable(true);
        trophyPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

    }

    /**
     * 答题
     *
     * @param view
     */
    private void answerPopWindow(View view) {
        answerPopWindow.setAnimationStyle(R.style.AnimBottomIn);
        answerPopWindow.setBackgroundDrawable(new BitmapDrawable());
        answerPopWindow.setFocusable(true);
        answerPopWindow.setOutsideTouchable(true);
        answerPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

    }


    /**
     * 答题统计
     *
     * @param view
     */
    private void statisticsPopWindow(View view) {
        statisticsPopWindow.setAnimationStyle(R.style.AnimBottomIn);
        statisticsPopWindow.setBackgroundDrawable(new BitmapDrawable());
        statisticsPopWindow.setFocusable(true);
        statisticsPopWindow.setOutsideTouchable(true);
        statisticsPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

    }

    @Override
    public void pingLineLost(String lineLost) {
        //丢包率
        netStatusView.setLineLost(lineLost);
    }

    @Override
    public void pingLineDelay(String delay) {
        //网络延时
        Log.e(TAG_NAME, "pingLineDelay   " + delay);
        netStatusView.setDelay(delay);

    }

    @Override
    public void pingHttpDis() {
        //网络断开
        Log.e(TAG_NAME, "pingHttpDis   ");

    }


    /**
     * 发奖杯
     */
    private class AcceptOnClickListener implements BaseTrophyAdapter.tvAcceptSetOnClick {

        @Override
        public void onTvAcceptClick(final View view, int position) {
            Log.e(TAG_NAME, "发奖杯 ");
            //            obtain_trophy++;
            //            TrophyAnimation(position);
            //            if (isgiftend) {
            //                isgiftend = false;
            //                fireworks = new FireworksView(TeacherActivityLand.this);
            //                fireworks.setAnimsopt(TeacherActivityLand.this);
            //                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            //                layoutParams.width = mapHeight/2;
            //                layoutParams.height = mapHeight/2;
            //                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            //                animLayout.addView(fireworks, layoutParams);
            //            }

            AnimationDrawable animationDrawable = (AnimationDrawable) iv_animation_filling.getBackground();
            AnimationTropSend(view, animationDrawable, position);
        }
    }


    /**
     * 动画发送播放
     */
    private void AnimationTropSend(View view, final AnimationDrawable animationDrawable, int position) {
        CustomAnimationDrawableNew cadn = new CustomAnimationDrawableNew(animationDrawable) {
            @Override
            public void onAnimationFinish() {
                isEnabled = false;
                if (obtain_trophy > 0) {
                    iv_animation_filling.setBackground(getResources().getDrawable(R.drawable.animation_list_filling));
                    animationDrawable.stop();
                    animationDrawable.start();
                    obtain_trophy--;
                } else {
                    iv_animation_filling.setBackground(getResources().getDrawable(R.drawable.animation_list_filling));
                    animationDrawable.stop();
                    rl_animation_trophy.setVisibility(View.GONE);
                    animation = false;
                }
            }

            @Override
            public void onAnimationStart() {
                if (!animation) {
                    animation = true;
                    animationDrawable.start();
                    obtain_trophy--;
                }
            }
        };

        if (!isEnabled) {
            rl_animation_trophy.setVisibility(View.VISIBLE);
            view.setBackground(cadn);
            cadn.start();
            isEnabled = true;
            obtain_trophy++;
            TrophyAnimation(position);
        }
    }


    /**
     * 发送奖杯更新数据
     */
    private void TrophyAnimation(int position) {
        Map<Integer, Integer> cupMap = cupMapList;
        Set<Map.Entry<Integer, Integer>> entrySet = cupMap.entrySet();
        Iterator<Map.Entry<Integer, Integer>> mapIt = entrySet.iterator();
        while (mapIt.hasNext()) {
            Map.Entry<Integer, Integer> me = mapIt.next();
            if (me.getKey() == customBeanList.get(position).getUserId()) {
                me.setValue(me.getValue() + 1);
            }
        }
        sendTrophy(teacher_room_id, teacher_user_id,
                String.valueOf(customBeanList.get(position).getUserId()),
                customBeanList.get(position).getNickName());
        //        sendQunJoinRoomPeonnel(mRoomLiveHelp, teacher_room_id,
        //                teacher_user_id, teacher_user_name, teacher_user_image,
        //                roomJoinMap, jqrb.getAccountList().getUserList().get(position).getUserId(),
        //                cupMapList);
        trophyPersion(Constant.TROPHY_MESSAGE);


    }

    /**
     * 取消弹窗
     */
    private void PopLmDismiss() {
        if (personnelPopWindow != null && personnelPopWindow.isShowing()) {
            personnelPopWindow.dismiss();
        }
    }

    /**
     * 取消发奖杯弹窗
     */
    private void popCupDismiss() {
        if (trophyPopWindow != null && trophyPopWindow.isShowing()) {
            trophyPopWindow.dismiss();
        }
        mHandler.removeCallbacks(timerRunnable);
    }

    /**
     * 关闭答题统计界面
     */
    private void statisticsPopWindowDiemiss() {
        if (statisticsPopWindow != null && statisticsPopWindow.isShowing()) {
            statisticsPopWindow.dismiss();
        }
        mHandler.removeCallbacks(timerRunnable);
    }

    /**
     * 清除答题统计列表
     */
    private void answerListClear() {
        if (mAnswerList != null && mAnswerList.size() > 0) {
            mAnswerList.clear();
        }
    }

    /**
     * 断开连麦弹窗
     *
     * @param view
     */
    private void showPopupTeacherWindow(View view, String userId) {
        lmUserId = userId;
        View popLiveView = LayoutInflater.from(mContext).inflate(R.layout.pop_live_speak_window_land, null);
        ll_tran_live = popLiveView.findViewById(R.id.ll_personnel_transparent_live);
        tv_ban_live_speak = popLiveView.findViewById(R.id.tv_pop_ban_speak_live);
        tv_speak_cannel = popLiveView.findViewById(R.id.tv_pop_speak_cancle);
        tv_pop_kicked_out_live = popLiveView.findViewById(R.id.tv_pop_kicked_out_live);

        popBanLiveSpeak = new PopupWindow(popLiveView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popBanLiveSpeak.setAnimationStyle(R.style.AnimBottomIn);
        popBanLiveSpeak.setBackgroundDrawable(new BitmapDrawable());
        popBanLiveSpeak.setFocusable(true);
        popBanLiveSpeak.setOutsideTouchable(true);
        popBanLiveSpeak.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        ll_tran_live.setOnClickListener(this);
        tv_ban_live_speak.setOnClickListener(this);
        tv_speak_cannel.setOnClickListener(this);
        tv_pop_kicked_out_live.setOnClickListener(this);
    }

    /**
     * 视频窗口点击监听
     */
    private void videoListener() {
        videoOnClickListener = new VideoView.setOnClickListener() {
            @Override
            public void OnClickListener(String id) {
                Log.e("===", "onClick = " + id);
                for (int i = 0; i < mVideoViewList.size(); i++) {
                    if (mVideoViewList.get(i).getFlagUserId().equals(id)) {
                        if (!mVideoViewList.get(i).getFlagUserId().equals(teacher_user_id)) {
                            showPopupTeacherWindow(main, id);
                        }

                    }
                }

            }
        };
    }


    /**
     * 设置视频窗口的位置和大小
     */
    private void VideoViewLocationSize(String userId) {
        if (userId.equals(teacher_user_id)) {
            VideoViewLayout(userId, mapWidth);
            rl_teacher_top.setVisibility(View.VISIBLE);
            mRoomLiveHelp.openLocalVideo(teacher_head_video_land, false, Constants.CLIENT_ROLE_ANCHOR, 30);
        } else {
            VideoViewLayout(userId, mapWidth);
            rl_live_video_name.setVisibility(View.VISIBLE);

            for (int i = 0; i < customBeanList.size(); i++) {
                if (customBeanList.get(i).getUserId() == Integer.parseInt(userId)) {
                    if (TextUtils.isEmpty(customBeanList.get(i).getNickName())) {
                        tv_user_one.setVisibility(View.VISIBLE);
                        tv_user_name.setText(userId);
                    } else {
                        tv_user_one.setVisibility(View.GONE);
                        tv_user_name.setText(customBeanList.get(i).getNickName());
                    }
                }
            }

            mRoomLiveHelp.openIdRemoteVideo(teacher_head_video_land, Long.valueOf(userId), true);
        }
    }

    /**
     * 添加窗口视频窗口
     */
    private void VideoViewLayout(String userId, int mapWidth) {
        videoListener();
        VideoView mVideoView = new VideoView(this, userId, videoOnClickListener);
        teacher_head_video_land = mVideoView.findViewById(R.id.teacher_head_video_land);
        if (userId.equals(teacher_user_id)) {
            iv_teacher_view = mVideoView.findViewById(R.id.land_teacher_image_view);
            iv_live_image_close = mVideoView.findViewById(R.id.land_iv_live_microphone_close);
            rl_microphone_open_two = mVideoView.findViewById(R.id.land_rl_live_microphone_two);
            rl_teacher_top = mVideoView.findViewById(R.id.rl_teacher_top);
            rl_microphone_open_one = mVideoView.findViewById(R.id.land_rl_live_microphone_one);
        } else {
            tv_user_one = mVideoView.findViewById(R.id.tv_user_name_one);
            rl_live_video_name = mVideoView.findViewById(R.id.rl_live_video_name);
            tv_user_name = mVideoView.findViewById(R.id.tv_user_name);

        }
        mVideoView.setFlagUserId(userId);
        mVideoViewList.add(mVideoView);

        int networkHeight = netStatusView.getHeight();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mapWidth / 6, mapWidth / 6 * 11 / 16);
        if (mVideoViewList.size() < 5) {
            layoutParams.topMargin = networkHeight;
            layoutParams.leftMargin = mapWidth / 6 * (mVideoViewList.size() - 1);
        } else {
            layoutParams.topMargin = mapWidth / 6 * 11 / 16 + networkHeight;
            layoutParams.leftMargin = mapWidth / 6 * (mVideoViewList.size() - 5);

        }
        mVideoView.setLayoutParams(layoutParams);
        mVideoView.setClickable(true);
        rl_teacher_video_view.addView(mVideoView);

    }

    /**
     * 释放直播控件
     * 不补位
     *
     * @param userId
     */
    private void releaseLiveView(long userId) {
        if (userId == 0)
            return;
        for (int i = 0; i < mVideoViewList.size(); i++) {
            VideoView currentViewLive = mVideoViewList.get(i);
            if (userId == Long.valueOf(currentViewLive.getFlagUserId())) {
                currentViewLive.removeAllViews();
                VideoView videoView = (VideoView) currentViewLive.getChildAt(0);
                currentViewLive.removeView(videoView);
                mVideoViewList.remove(i);
            }
        }
    }

    /**
     * 显示视频
     *
     * @param
     */
    private void showRemoteView(String userid) {
        VideoViewLocationSize(userid);
    }

    /**
     * 打开主讲视频窗口
     */
    private void loadRoomVideo(String userId, String roomId, String roomType) {
        Log.e(TAG_NAME, " userId:" + userId + "==roomId:" + roomId);
        int i = 30;
        String custom = getJoinRoomCustom(teacher_room_id, Integer.parseInt(teacher_user_id), teacher_user_name,
                teacher_user_image, 3, 0, 0, 0, mWhiteBoard, mVideoClose, mAudeoClose);
        mRoomLiveHelp.enterRoom(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING,
                Constants.CLIENT_ROLE_ANCHOR, Integer.parseInt(roomId), Long.parseLong(userId), i, custom);
    }

    private void speakClos() {
        mRoomLiveHelp.controlLocalAudio(true);
        iv_microphone.setBackground(getResources().getDrawable(R.drawable.icon_microphone_close));
        isSpeak = true;
        videoSpeakType();
    }

    private void speakOpen() {
        mRoomLiveHelp.controlLocalAudio(false);
        iv_microphone.setBackground(getResources().getDrawable(R.drawable.icon_microphone_normal));
        isSpeak = false;
        videoSpeakType();
    }

    private void videoClose() {
        mRoomLiveHelp.controlLocalVideo(true);
        iv_camera.setBackground(getResources().getDrawable(R.drawable.icon_camera_close));
        isCamera = true;
        videoSpeakType();
    }

    private void videoOpen() {
        mRoomLiveHelp.controlLocalVideo(false);
        iv_camera.setBackground(getResources().getDrawable(R.drawable.icon_camera_normal));
        isCamera = false;
        videoSpeakType();
    }

    private void videoSpeakType() {
        if (isCamera) {
            rl_microphone_open_two.setVisibility(View.VISIBLE);
        } else {
            rl_microphone_open_two.setVisibility(View.GONE);
        }

        if (isSpeak) {
            iv_live_image_close.setVisibility(View.VISIBLE);
        } else {
            iv_live_image_close.setVisibility(View.GONE);
        }

        if (isCamera && isSpeak) {
            rl_microphone_open_two.setVisibility(View.GONE);
            iv_live_image_close.setVisibility(View.GONE);
            rl_microphone_open_one.setBackgroundColor(getResources().getColor(R.color.color_ff353535));
            iv_teacher_view.setVisibility(View.VISIBLE);

        } else {
            rl_microphone_open_one.setBackgroundColor(getResources().getColor(R.color.transparent));
            iv_teacher_view.setVisibility(View.GONE);
        }
    }

    /**
     * 课件列表弹窗
     *
     * @param view
     */
    private void showPopupCourseBottom(View view) {
        View courseView = LayoutInflater.from(mContext).inflate(R.layout.pop_courseware_window, null);
        courseListPop = new PopupWindow(courseView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        rv_popup_view = courseView.findViewById(R.id.popup_recycler_view);
        ll_item_a = courseView.findViewById(R.id.ll_item_pop_a);
        ll_courseware_button = courseView.findViewById(R.id.linearLayout_courseware_button);
        ptr_load_docs = courseView.findViewById(R.id.ptr_load_data_docs);
        iv_doc_dropdown = courseView.findViewById(R.id.iv_doc_dropdown);
        ptr_load_docs.disableWhenHorizontalMove(true);
        ptr_load_docs.setPtrHandler(docsPtrHadler);
        tPage = 1;
        Log.i(TAG_NAME, "userid:" + teacher_user_id);
        teacherPresenter.getDocsLists(teacher_user_id, String.valueOf(tPage), tPageSize, "");

        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) ll_item_a.getLayoutParams();
        layoutParams1.height = 0;
        layoutParams1.weight = 0.3f;
        ll_item_a.setLayoutParams(layoutParams1);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) ll_courseware_button.getLayoutParams();
        layoutParams1.height = 0;
        layoutParams2.weight = 0.7f;
        ll_courseware_button.setLayoutParams(layoutParams2);

        courseListPop.setAnimationStyle(R.style.AnimBottomIn);
        courseListPop.setBackgroundDrawable(new BitmapDrawable());
        courseListPop.setFocusable(true);
        courseListPop.setOutsideTouchable(true);
        courseListPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        ll_item_a.setOnClickListener(this);
        iv_doc_dropdown.setOnClickListener(this);

    }

    @Override
    public void shareRoomInvite(ShareBean shareBean) {
        //邀请码返回
        Gson gson = new Gson();
        String ss = gson.toJson(shareBean);
        Log.e(TAG_CLASS + "邀请码返回 : ", ss);
        if (shareBean != null) {
            for (int i = 0; i < shareBean.getTeacher().size(); i++) {
                if (teacher_room_id.equals(shareBean.getTeacher().get(i).getCourseId())) {
                    teacher_invite_code = shareBean.getTeacher().get(i).getInviteCode();
                }
            }

        }
    }

    @Override
    public void animend() {
        Log.e("ssss", "animend");
        isgiftend = true;
        animLayout.removeAllViews();
        fireworks = null;
        obtain_trophy--;
        if (obtain_trophy > 0 && isgiftend) {
            isgiftend = false;
            fireworks = new FireworksView(this);
            fireworks.setAnimsopt(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.width = mapHeight / 2;
            layoutParams.height = mapHeight / 2;
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            animLayout.addView(fireworks, layoutParams);
            return;
        }


    }


    /**
     * 课件点击事件
     */
    private class MdocOnItemClickListener implements CourseWareAdapter.CourseWareViewItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            Gson gson = new Gson();
            if (listbean != null && listbean.size() > 0) {
                if (listbean.get(position).getPage().equals("0")) {
                    toastShort(getResources().getString(R.string.current_courseware_empty));
                    return;
                }
                //teacherPresenter.getDocsListDetails(listbean.get(position).getId());
                String docdata = gson.toJson(listbean.get(position));
                currentCheckedItemDefault = position;
                mCourseWareAdapter.check(position, true);
                if (TextUtils.isEmpty(listbean.get(position).getHtmlUrl())) {
                    isAnimation = false;
                    iv_animation_view.setVisibility(View.GONE);
                } else {
                    iv_animation_view.setVisibility(View.VISIBLE);
                    isAnimation = true;
                }
                iv_previous_page.setVisibility(View.VISIBLE);
                iv_next_page.setVisibility(View.VISIBLE);
                DocOpenCount(docdata);
            }

        }
    }

    /**
     * 白板工具点击
     */
    private void toolOpen() {
        if (mWebviewToolPopupWindow == null) {
            mWebviewToolPopupWindow = new WebviewToolPopupWindowLand(this, teacher_view_web, TEACHER_WEB_STATE);
        }
        if (chatPop.isShow()) {
            chatPop.closePop();
        }
        mWebviewToolPopupWindow.showPop(main);
    }

    /**
     * 发送课件详情到JS调用
     *
     * @param docdata
     */
    private void DocOpenCount(String docdata) {

        //调js
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            teacher_view_web.evaluateJavascript("javascript:docsOpen(\'" + docdata + "\')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i(TAG_NAME, " js回调  " + value);
                }
            });
        } else {
            teacher_view_web.loadUrl("javascript:docsOpen(\'" + docdata + "\')");
        }
        webviewPage("getCurrentTotalPage()");
        if (mWebviewToolPopupWindow != null) {
            //mWebviewToolPopupWindow.setCurrentWebViewTool();
            mWebviewToolPopupWindow.trunPage();
        }


    }

    /**
     * webview
     * 获取当前页码
     */
    private void webviewPage(String tool) {
        if (teacher_view_web != null) {
            String js = "javascript:" + tool;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                teacher_view_web.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.e(TAG_NAME + " webViewPage : ", value);
                        String page = value.replace("\"", "");
                        page_textview_number.setVisibility(View.VISIBLE);
                        page_textview_number.setText(page);
                    }
                });
            } else {
                teacher_view_web.loadUrl(js);
            }
        }
    }

    /**
     * webview 白板工具栏
     */
    private void webviewTool(String tool) {
        if (teacher_view_web != null) {
            String js = "javascript:" + tool;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                teacher_view_web.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.e(TAG_NAME + " webview", value);
                    }
                });
            } else {
                teacher_view_web.loadUrl(js);
            }
        }
    }

    /**
     * 初始化webView
     */
    private void webViewInit() {

        WebSettings webSettings = teacher_view_web.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowContentAccess(false);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) teacher_view_web.getLayoutParams();
//        layoutParams.width = mapHeight * 16 / 9;
//        layoutParams.height = mapHeight;
        layoutParams.width = mapWidth;
        if (mapWidth * 9/16 > mapHeight){
            layoutParams.height = mapHeight;
        }else {
            layoutParams.height = mapWidth * 9/16;
        }
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        teacher_view_web.setLayoutParams(layoutParams);

        //        final int width = BaseTools.getWindowsWidth(this);
        //        if (width > 960) {
        //            teacher_view_web.setInitialScale((int) (960f / width * 100));
        //        } else {
        //            teacher_view_web.setInitialScale((int) (width / 960f * 100));
        //        }


        final int height = mapHeight;
        if (height > 540) {
            teacher_view_web.setInitialScale((int) (540f / height * 100));
        } else {
            teacher_view_web.setInitialScale((int) (height / 540f * 100));
        }

        teacher_view_web.loadUrl(Constant.DocUrl + "?inviteCode=&courseId=" +
                teacher_room_id + "&role=3&userId=" + teacher_user_id + "&appId=" + Constant.app_id);
        teacher_view_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

    }

    /**
     * 分页加载
     */
    private class DocsPtrHadler extends PtrDefaultHandler2 {

        @Override
        public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
            return super.checkCanDoLoadMore(frame, rv_popup_view, footer);
        }

        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
            return super.checkCanDoRefresh(frame, rv_popup_view, header);
        }

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            //加载更多
            teacherPresenter.getDocsLists(teacher_user_id, String.valueOf(tPage), tPageSize, "");
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            //刷新
            tPage = 1;
            teacherPresenter.getDocsLists(teacher_user_id, String.valueOf(tPage), tPageSize, "");

        }
    }

    /**
     * 通过userid获取用户信息
     * 断开连麦
     *
     * @param v
     */
    private void testDisconnectVideo(View v, String userId) {
        Gson disGson = new Gson();
        if (mVideoViewList != null && mVideoViewList.size() > 0) {
            Iterator<String> leaveKey = roomJoinMap.keySet().iterator();
            while (leaveKey.hasNext()) {
                String key = leaveKey.next();
                if (userId.equals(key)) {
                    LmDisconnectBean lDb = new LmDisconnectBean();
                    LmDisconnectBean.DisData ld = new LmDisconnectBean.DisData();
                    lDb.setMessageType(Constant.LM_CLOSE_CALL);
                    ld.setAdminUserId(teacher_user_id);
                    ld.setRoomId(teacher_room_id);
                    ld.setUserId(userId);
                    lDb.setData(ld);
                    String lDbs = disGson.toJson(lDb);
                    if (Constant.wsService != null) {
                        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), 1, lDbs, seqid, 0, false));
                    } else {
                        tWsService.sendRequest(wsSendMsg(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), 1, lDbs, seqid, 0, false));
                    }
                    EventBus.getDefault().post(new MessageEvent(Constant.LM_CLOSE_CALL, lDbs));

                }
            }

        }
    }

    /**
     * 发送聊天消息
     *
     * @param message
     */
    public void sendTextMessage(String message, int messageType) {
        Long ct = System.currentTimeMillis();
        String seqid = "binding_" + String.valueOf(ct);
        Gson gson = new Gson();
        SendTextMessageBean stMessAge = new SendTextMessageBean();
        SendTextMessageBean.DataBean stDb = new SendTextMessageBean.DataBean();
        stMessAge.setMessageType(Constant.BARRAGE_REQ);
        stDb.setMessage(message);
        stDb.setNickName(teacher_user_name);
        stDb.setAvatar(teacher_user_image);
        stDb.setUserId(teacher_user_id);
        stDb.setRoomId(teacher_room_id);
        stDb.setType(messageType);
        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        msgList.add(stMessAge);

        chatPop.showChatList(msgList);
        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), 1, ss, seqid, 0, true));
        } else {
            tWsService.sendRequest(wsSendMsg(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), 1, ss, seqid, 0, true));
        }


    }

    /**
     * 退出直播间
     */
    private void exitRoomDialog() {
        final MessageDialog exitDialog = new MessageDialog(mContext);
        exitDialog.setHeight(mapHeight * 2 / 3);
        exitDialog.setWidth(mapWidth * 2 / 3);
        exitDialog.setHint(R.string.mes_hint);
        exitDialog.setContent(R.string.exit_room_dialog);
        exitDialog.setLeftButton(R.string.confirm);
        exitDialog.setRightButton(R.string.cancle);

        exitDialog.setMessageDialogListener(new MessageDialog.MessageDialogListener() {
            @Override
            public void onCancelClick(MessageDialog dialog) {
                //确定
                //退出直播
                onBackPressed();
                if (exitDialog != null && exitDialog.isShowing()) {
                    exitDialog.dismiss();
                }
            }

            @Override
            public void onCommitClick(MessageDialog dialog) {
                //取消
                if (exitDialog != null && exitDialog.isShowing()) {
                    exitDialog.dismiss();
                }

            }
        });

        exitDialog.show();

    }

    /**
     * 老师进入房间个人消息
     */
    private void teacherJoinBean() {
        String custom = getJoinRoomCustom(teacher_room_id, Integer.parseInt(teacher_user_id), teacher_user_name,
                teacher_user_image, 3, 0, 0, 0, mWhiteBoard, mVideoClose, mAudeoClose);
        CustomBean customBean = mGson.fromJson(custom, CustomBean.class);
        joinRoomMapList(customBean, 0);
    }

    /**
     * 加入房间增加用户名单
     *
     * @param mCustomBean
     */
    private void joinRoomMapList(CustomBean mCustomBean, int type) {
        //判断人员是否存在
        Iterator<String> leaveKey = roomJoinMap.keySet().iterator();
        String joinId = String.valueOf(mCustomBean.getUserId());
        while (leaveKey.hasNext()) {
            String key = leaveKey.next();
            if (joinId.equals(key)) {
                return;
            }
        }
        if (roomJoinMap != null && roomJoinMap.size() >= 7) {
            //toastShort("房间人数达到上限");
            sendKickingMessage(mRoomLiveHelp, new Gson().toJson(mCustomBean));
            return;
        }
        roomJoinMap.put(String.valueOf(mCustomBean.getUserId()), mCustomBean);
        if (!String.valueOf(mCustomBean.getUserId()).equals(teacher_user_id)) {
            cupMapList.put(mCustomBean.getUserId(), mCustomBean.getTrophyCount());
        }

        //打开当前连麦成员视频窗口
        if (mCustomBean.getLm() != 0) {
            if (mCustomBean.getLm() == 1) {
                showRemoteView(String.valueOf(mCustomBean.getUserId()));
                nowListLmUserid.add(String.valueOf(mCustomBean.getUserId()));
            }
        }

        if (listLmUserid != null && listLmUserid.size() > 0 || nowListLmUserid != null && nowListLmUserid.size() > 0) {
            teacherPersonnel.setPersonnelAll(mContext, roomJoinMap, nowListLmUserid, listLmUserid, cupMapList);
        } else {
            teacherPersonnel.initViewPersonnel(mContext,
                    new PersonnelEvent(Constant.SEND_EVENT_PERSONNEL, "", roomJoinMap), cupMapList);
        }

        customBeanList.clear();
        Map<String, CustomBean> persionM = roomJoinMap;
        Set<Map.Entry<String, CustomBean>> perEntrySet = persionM.entrySet();
        Iterator<Map.Entry<String, CustomBean>> perMapIt = perEntrySet.iterator();
        while (perMapIt.hasNext()) {
            Map.Entry<String, CustomBean> meNext = perMapIt.next();
            if (!meNext.getKey().equals(teacher_user_id)) {
                customBeanList.add(meNext.getValue());
            }
        }

        if (type == 1) {
            //            sendQunJoinRoomPeonnel(mRoomLiveHelp, teacher_room_id,
            //                    teacher_user_id, teacher_user_name, teacher_user_image, roomJoinMap, "", cupMapList);
            trophyPersion("0");
        }

    }

    /**
     * 离开房间删除用户名单
     *
     * @param mjoinRoomBean
     */
    private void leaveRoomMapList(LeaveMassageBean mjoinRoomBean, int type) {
        Iterator<String> leaveKey = roomJoinMap.keySet().iterator();
        String leaveId = String.valueOf(mjoinRoomBean.getData().getUserId());
        while (leaveKey.hasNext()) {
            String key = leaveKey.next();
            String removeKey = mjoinRoomBean.getData().getUserId();
            if (removeKey.equals(key)) {
                leaveKey.remove();
            }
        }
        if (listLmUserid != null && listLmUserid.size() > 0) {
            for (int i = 0; i < listLmUserid.size(); i++) {
                if (mjoinRoomBean.getData().getUserId().equals(listLmUserid.get(i))) {
                    EventBus.getDefault().post(new MessageEvent(Constant.LEAVE_ROOM_PERSONE, "leave_room_persone_one"));
                }
            }
        }

        removeLmUserId(leaveId);
        nowRemoveLmUserId(leaveId);
        deleteLmListPersion(leaveId);

        if (cupMapList != null && cupMapList.size() > 0) {
            Map<Integer, Integer> cupMap = cupMapList;
            Set<Map.Entry<Integer, Integer>> entrySet = cupMap.entrySet();
            Iterator<Map.Entry<Integer, Integer>> mapIt = entrySet.iterator();
            while (mapIt.hasNext()) {
                Map.Entry<Integer, Integer> me = mapIt.next();
                if (String.valueOf(me.getKey()).equals(mjoinRoomBean.getData().getUserId())) {
                    mapIt.remove();
                }
            }
        }

        if (listLmUserid != null && listLmUserid.size() > 0 || nowListLmUserid != null && nowListLmUserid.size() > 0) {
            teacherPersonnel.setPersonnelAll(mContext, roomJoinMap, nowListLmUserid, listLmUserid, cupMapList);
        } else {
            teacherPersonnel.initViewPersonnel(mContext,
                    new PersonnelEvent(Constant.SEND_EVENT_PERSONNEL, "", roomJoinMap), cupMapList);
        }

        //该成员退出直播间，移除掉答题统计里边该成员信息
        for (int i = 0; i < mAnswerList.size(); i++) {
            if (mjoinRoomBean.getData().getUserId().equals(mAnswerList.get(i).getData().getUserId())) {
                mAnswerList.remove(i);
            }
        }
        statisicsAdapter = new StatisicsAdapter(this, mAnswerList);
        if (rv_statisics_list != null) {
            rv_statisics_list.setAdapter(statisicsAdapter);
        }

        customBeanList.clear();
        Map<String, CustomBean> persionM = roomJoinMap;
        Set<Map.Entry<String, CustomBean>> perEntrySet = persionM.entrySet();
        Iterator<Map.Entry<String, CustomBean>> perMapIt = perEntrySet.iterator();
        while (perMapIt.hasNext()) {
            Map.Entry<String, CustomBean> meNext = perMapIt.next();
            if (!meNext.getKey().equals(teacher_user_id)) {
                customBeanList.add(meNext.getValue());
            }
        }


        if (type == 1) {
            //            sendQunJoinRoomPeonnel(mRoomLiveHelp, teacher_room_id, teacher_user_id, teacher_user_name,
            //                    teacher_user_image, roomJoinMap, "", cupMapList);
            trophyPersion("0");
        }

    }

    /**
     * 删除同意/拒绝/退出/断开后储存的连麦人员列表
     *
     * @param userId
     */
    private void removeLmUserId(String userId) {
        for (int i = 0; i < listLmUserid.size(); i++) {
            if (listLmUserid.get(i).equals(userId)) {
                listLmUserid.remove(i);
            }
        }
    }

    /**
     * 删除正在连麦人员
     *
     * @param userId
     */
    private void nowRemoveLmUserId(String userId) {
        for (int i = 0; i < nowListLmUserid.size(); i++) {
            if (nowListLmUserid.get(i).equals(userId)) {
                nowListLmUserid.remove(i);
            }
        }
    }

    /**
     * 取消弹窗
     */
    private void PopDismiss() {
        if (popBanLiveSpeak != null && popBanLiveSpeak.isShowing()) {
            popBanLiveSpeak.dismiss();
            popBanLiveSpeak = null;
        }
    }

    private void disCourseListPop() {
        if (courseListPop != null && courseListPop.isShowing()) {
            courseListPop.dismiss();
            courseListPop = null;
        }
    }

    private void disLoadDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private String timeType;

    /**
     * 开始计时功能
     */
    private void startTimer(String type) {
        timeType = type;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        Date time = calendar.getTime();
        liveTime = time.getTime();
        mHandler.removeCallbacks(timerRunnable);
        mHandler.post(timerRunnable);
    }

    /**
     * 循环执行线程
     */
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(timerRunnable, 1000);
            long sysTime = System.currentTimeMillis();
            liveTime += 1000;
            CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", liveTime);
            CharSequence sysDateStr = DateFormat.format("yyyy/MM/dd", sysTime);
            if (timeType.equals("trophy")) {
                tv_trophy_time.setText(sysTimeStr);
            } else if (timeType.equals("Statistics")) {
                tv_statisics_time.setText(sysTimeStr);
            } else if (timeType.equals("network")) {
                netStatusView.setTime(sysTimeStr.toString());

            }
        }
    };


    /**
     * 底部工具栏位移动画
     */
    private void ToolsAnimator() {
        if (!animation_tool){
            ObjectAnimator translationX = new ObjectAnimator().ofFloat(tool_ll_view_displacement ,"translationX",600f,0);
            ObjectAnimator translationY = new ObjectAnimator().ofFloat(tool_ll_view_displacement,"translationY",0,0);

            AnimatorSet animatorSet = new AnimatorSet();  //组合动画
            animatorSet.playTogether(translationX,translationY); //设置动画
            animatorSet.setDuration(1000);  //设置动画时间
            animatorSet.start(); //启动
            land_iv_raise_menu.setBackground(getResources().getDrawable(R.drawable.icon_menu_hover));
            tool_ll_view_displacement.setVisibility(View.VISIBLE);
            animation_tool = true;
            animatorSet.addListener(toolDisplacementAddListener);
        }else {
            ObjectAnimator translationX = new ObjectAnimator().ofFloat(tool_ll_view_displacement ,"translationX",0,600f);
            ObjectAnimator translationY = new ObjectAnimator().ofFloat(tool_ll_view_displacement,"translationY",0,0);

            AnimatorSet animatorSet = new AnimatorSet();  //组合动画
            animatorSet.playTogether(translationX,translationY); //设置动画
            animatorSet.setDuration(1000);  //设置动画时间
            animatorSet.start(); //启动
            land_iv_raise_menu.setBackground(getResources().getDrawable(R.drawable.icon_menu_normal));
            animation_tool = false;
            animatorSet.addListener(toolDisplacementAddListener);
        }
    }

    /**
     * 平移动画播放
     * 监听
     */
    class ToolDisplacementAddListener implements  Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            Log.e(TAG_CLASS , "动画开始");
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            Log.e(TAG_CLASS , "动画结束");
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Log.e(TAG_CLASS , "动画取消");

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            Log.e(TAG_CLASS , "动画重复");

        }
    }

    /**
     * 显示分享
     */
    private void showSharePopu() {
        if (shareTeacherPopu == null) {
            shareTeacherPopu = new SharePopu(this, new SharePopu.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0:
                            shareTeacher(SHARE_MEDIA.WEIXIN, UMShareAPI.get(mContext).isInstall(TeacherActivityLand.this, SHARE_MEDIA.WEIXIN));
                            break;
                        case 1:
                            shareTeacher(SHARE_MEDIA.WEIXIN_CIRCLE, UMShareAPI.get(mContext).isInstall(TeacherActivityLand.this, SHARE_MEDIA.WEIXIN_CIRCLE));
                            break;
                        case 2:
                            copyShareLink(teacher_room_id, teacher_invite_code,
                                    Constant.SHARE_TEACHER_URL, getResources().getString(R.string.teacher_invite_code));
                            break;
                    }
                    shareTeacherPopu.dismiss();
                }
            }
            );
        }
        Tools.setBackground(getWindow(), 0.7f);
        shareTeacherPopu.setOnDismissListener(new PopupWindow.OnDismissListener()

        {

            @Override
            public void onDismiss() {
                Tools.setBackground(getWindow(), 1f);
            }
        });

        shareTeacherPopu.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);
    }

    //分享成功失败回调
    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.e("Shart Listener  ", "开始了 ");

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Log.e("Shart Listener  ", "成功了 ");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.e("Shart Listener  ", "失败了 ");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Log.e("Shart Listener  ", "取消了 ");
        }
    };


}
