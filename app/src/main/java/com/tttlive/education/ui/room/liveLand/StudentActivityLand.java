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
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tttlive.basic.education.R;
import com.tttlive.education.adapter.ProfessionRecyclerViewAdapter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.ui.Interface.RoomInterface;
import com.tttlive.education.ui.SharePopu;
import com.tttlive.education.ui.room.BaseLiveActivity;
import com.tttlive.education.ui.room.ChatPopupWindow;
import com.tttlive.education.ui.room.PlayerManager;
import com.tttlive.education.ui.room.RoomMessageType;
import com.tttlive.education.ui.room.RoomMsg;
import com.tttlive.education.ui.room.RoomPresenter;
import com.tttlive.education.ui.room.RoomUIinterface;
import com.tttlive.education.ui.room.bean.CustomBean;
import com.tttlive.education.ui.room.bean.GegBannedBean;
import com.tttlive.education.ui.room.bean.LeaveMassageBean;
import com.tttlive.education.ui.room.bean.LmAgreeResBean;
import com.tttlive.education.ui.room.bean.LmDisconnectBean;
import com.tttlive.education.ui.room.bean.ModeVieoChangeBean;
import com.tttlive.education.ui.room.bean.SendTextMessageBean;
import com.tttlive.education.ui.room.bean.ShareBean;
import com.tttlive.education.ui.room.bean.StartNotLiveBean;
import com.tttlive.education.ui.room.bean.TeacherAnswerBean;
import com.tttlive.education.ui.room.bean.TrophyAwardBean;
import com.tttlive.education.ui.room.bean.UploadImageBean;
import com.tttlive.education.ui.room.bean.VAClosedBean;
import com.tttlive.education.ui.room.bean.WhiteBoardBean;
import com.tttlive.education.ui.room.socket.WebSocketService;
import com.tttlive.education.ui.room.socket.WsListener;
import com.tttlive.education.ui.room.webviewtool.WebviewToolPopupWindowLand;
import com.tttlive.education.ui.share.ShareInterface;
import com.tttlive.education.ui.share.SharePresenter;
import com.tttlive.education.util.AlarmTimeUtils;
import com.tttlive.education.util.BaseTools;
import com.tttlive.education.util.CustomPopWindow;
import com.tttlive.education.util.FireworksView;
import com.tttlive.education.util.GitfSpecialsStop;
import com.tttlive.education.util.ImagePathUtil;
import com.tttlive.education.util.MagicTextView;
import com.tttlive.education.util.MaterialBadgeTextView;
import com.tttlive.education.util.MessageDialog;
import com.tttlive.education.util.PingUtil;
import com.tttlive.education.util.SPTools;
import com.tttlive.education.util.ShareUtil;
import com.tttlive.education.util.StatusBarUtil;
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

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ttt.ijk.media.exo.widget.media.IjkVideoView;

/**
 * Created by Administrator on 2018/5/29/0029.
 * 学生端横屏界面
 */

public class StudentActivityLand extends BaseLiveActivity implements PlayerManager.PlayerStateListener,
        RoomLiveInterface, RoomMsg, RoomUIinterface,
        View.OnClickListener, ShareInterface, GitfSpecialsStop, RoomInterface {

    private static String TAG_CLASS = StudentActivityLand.class.getSimpleName();
    private String START_TIME_CYCLE = "START_TIME_CYCLE";
    private Context mContext;

    public static final String ROOM_TYPE = "live_type";
    public static final String ROOM_USER_ID = "user_id";
    public static final String ROOM_ID = "room_id";
    public static final String TEACH_ID = "teach_id";
    public static final String NICKNAMW = "nickname";
    public static final String TITLE_NAME = "title_name";
    public static final String ROOM_PULL_RTMP = "room_pull";
    public static final String ROOM_NOTICE = "room_notice"; //大班课公告
    private static final String LIVE_LARGE_CLASS = "0"; //大班课
    private static final String LIVE_SMALL_CLASS = "1"; //小班课


    private int resolution = -1;

    private Gson stGson = new Gson();

    private ArrayList<SendTextMessageBean> msgList = new ArrayList<>();//聊天消息列表
    private ArrayList<VideoView> mVideoViewList = new ArrayList<>();
    private Map<Integer, CustomBean> personelMapList = new LinkedHashMap<>();
    private List<CustomBean> customBeanList = new ArrayList<>();
    //奖杯
    private Map<Integer, Integer> trophyMapList = new LinkedHashMap<>();
    private List<String> lmUserIdList = new ArrayList<>();

    private PopOnClickListener popOnClickListener = new PopOnClickListener();
    private StudentWebViewClient studentWebViewClient = new StudentWebViewClient();
    private ToolDisplacementAddListener toolDisplacementAddListener = new ToolDisplacementAddListener();
    private VideoView.setOnClickListener videoOnClickListener;

    private Handler mHandler = new Handler();

    private String roomId;
    private String mUerId;
    private String teachid;
    private String userimage;
    private String nackname;
    private String mroomType = "1";//0是大班课，1是小班课
    private String seqid;
    private String timeType;
    private String title_name;
    private String video_host_ip;
    private String mNotice;
    private String mPullRtmp;

    private boolean isBanned = false;
    private boolean startCourse = false;
    private boolean applyLmState = false;
    private boolean showSelfVideo = false;
    private boolean isVideo = false;//是否打开视频
    private boolean isAudio = false;//是否打开麦克风
    private boolean isLmstate = false;
    private boolean isgiftend = true;
    private boolean switchCamera = false;
    private boolean mWhiteBoard = false; //白板授权
    private boolean mVideoClose = false;  //开关摄像头,默认关闭
    private boolean mAudeoClose = false;  //开关,麦克风,默认关闭
    private boolean animation_tool = false;
    private boolean loadWebView = false;

    private int mapWidth;
    private int mapHeight;
    private int parentWidth;
    private int parentHeight;
    private int obtain_trophy = 0;
    private int mLiveLm = 0;        //连麦
    private int mUserRole = 1;      //角色
    private int mSpeakStop = 0;     //禁言
    private int mTrophyCount = 0;     //奖杯数量
    private int STUDENT_WEB_STATE = 3;  //白板学生

    private long liveTime;

    private WebSocketService sWebservice;
    private ChatPopupWindow chatPop;
    private MaterialBadgeTextView land_mbt_personnel_chat;
    private TeacherPersonnelUtil teacherPersonnel;
    private PopupWindow personnelPopWindow;
    private PopupWindow noticePopWindow;
    private PopupWindow answerPopWindow;
    private PopupWindow resultrPopWindow;
    private EnterUserInfo mUserInfo;
    private RoomPresenter mPresenter;
    private FireworksView fireworks;
    private NumAnim giftNumAnim;
    public SharePopu shareStudentPopu;
    private SharePresenter sharePresenter;
    private PingUtil mPingUtil;
    private TimeReceiver mTimeReceiver;
    private WebviewToolPopupWindowLand mWebviewToolPopupWindow;
    private PlayerManager playerManager;

    private LinearLayout ll_start_live;
    private LinearLayout ll_start_tool;
    private ImageView iv_radio_courseware;
    private ImageView iv_raise_hand;
    private ImageView mIcrophone;
    private ImageView mCamera;
    private FrameLayout fl_personnel_live;
    private ImageView iv_personnel_detail;
    private MaterialBadgeTextView materialBadge_textview_chat;
    private MaterialBadgeTextView mbt_personnel_chat;
    private ImageView land_img_chat;
    private ImageView image_view_share;
    private ImageView image_view_back;
    private LinearLayout land_ll_tool_chat;
    private ImageView iv_pop_window_back;
    private RecyclerView rv_pop_lm;
    private WebView student_view_web;
    private TextView page_textview_number;
    private RelativeLayout student_head_video_land;
    private ImageView iv_live_image_close;
    private RelativeLayout rl_microphone_open_two;
    private RelativeLayout rl_teacher_top;
    private ImageView iv_live_camera_flip;
    private RelativeLayout rl_microphone_open_one;
    private AbsoluteLayout rl_student_video_view;
    private String student_invite_code;
    private ImageView iv_student_live_navbar_unfold;
    private ImageView iv_pop_answer_window_back;
    private TextView tv_me_answer;
    private RecyclerView rv_answer_list;
    private TextView tv_correct_answer;
    private Button bt_add_item_view;
    private Button bt_delete_item_view;
    private Button bt_start_answer;
    private ProfessionRecyclerViewAdapter answerAdapter;
    private LinearLayout ll_tips;
    private TextView tv_tips;
    private LinearLayout ll_time;
    private FrameLayout fl_student_view;
    private RelativeLayout rl_room_show_anim;
    private TextView tv_trophy_num;
    private MagicTextView mtv_gif_num;
    private StringBuffer strCorrect;
    private List<String> solution;
    private RelativeLayout ll_trophy_personel;
    private View id_animation_cup;
    private TextView tv_answer_time;
    private TextView tv_result_time;
    private RelativeLayout rl_live_video_name;
    private TextView tv_user_name;
    private TextView tv_user_one;
    private TextView tv_teacher;
    private MagicTextView mtv_gif_num_x;
    private LinearLayout ll_animation_filling;
    private RelativeLayout rl_network_bar;
    private ImageView iv_network_stauts_excellent;
    private TextView tv_network_stauts_ms;
    private TextView tv_network_stauts_percentage;
    private TextView tv_network_stauts_op;
    private TextView network_stauts_course_name;
    private TextView network_stauts_classes_tv;
    private TextView network_stauts_time;
    private ImageView land_iv_tool_whiteboard;
    private RelativeLayout rl_student_land_main;
    private ImageView land_iv_tool_answer;
    private ImageView land_iv_tool_trophy;
    private LinearLayout ll_no_class_background;
    private LinearLayout ll_tool_right;
    private LinearLayout large_ll_right_bottom;
    private LinearLayout ll_video_tool_zoom;
    private ImageView large_iv_chat;
    private MaterialBadgeTextView large_mbtv_chat_red;
    private ImageView large_iv_persone_list;
    private ImageView large_iv_notice;
    private MaterialBadgeTextView large_mbtv_notice_num;
    private RelativeLayout rl_pop_room_nitice_main;
    private TextView tv_notice_pop;
    private ImageView land_iv_raise_menu;
    private LinearLayout tool_ll_view_displacement;
    private IjkVideoView mVideoIjkPlayer;

    private static final String TITLE = "title";
    private static final String TIME_START = "timeStart";
    private String title;
    private String timeStart;

    private static final int MODEL_NORMAL = 0;//常规模式
    private static final int MODEL_VIDEO = 1;//视频模式

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    private int curModel = MODEL_NORMAL;//当前模式，常规模式

    private int singleWidth;
    private int singleHeight;

    /**
     * @param context
     * @param roomType 入会类型
     * @param userId
     * @param roomId
     * @return
     */
    public static Intent createIntent(Context context, int roomType,
                                      String teacherid, String userId, String roomId,
                                      String type, String nickname, String titleName,
                                      String notice, String pullRtmp, String title, String timeStart) {
        Intent intent = new Intent(context, StudentActivityLand.class);
        intent.putExtra(ROOM_TYPE, type);
        intent.putExtra(ROOM_USER_ID, userId);
        intent.putExtra(ROOM_ID, roomId);
        intent.putExtra(TEACH_ID, teacherid);
        intent.putExtra(NICKNAMW, nickname);
        intent.putExtra(TITLE_NAME, titleName);
        intent.putExtra(ROOM_NOTICE, notice);
        intent.putExtra(ROOM_PULL_RTMP, pullRtmp);
        intent.putExtra(TITLE, title);
        intent.putExtra(TIME_START, timeStart);
        return intent;
    }


    private ServiceConnection stWsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sWebservice = ((WebSocketService.ServiceBinder) service).getService();
            initWsListeners();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_student_land;
    }

    @Override
    protected void findView() {
        mContext = this;
        roomId = getIntent().getStringExtra(ROOM_ID);
        mUerId = getIntent().getStringExtra(ROOM_USER_ID);
        teachid = getIntent().getStringExtra(TEACH_ID);
        nackname = getIntent().getStringExtra(NICKNAMW);
        title_name = getIntent().getStringExtra(TITLE_NAME);
        mNotice = getIntent().getStringExtra(ROOM_NOTICE);
        mPullRtmp = getIntent().getStringExtra(ROOM_PULL_RTMP);
        title = getIntent().getStringExtra(TITLE);
        timeStart = getIntent().getStringExtra(TIME_START);

        Constant.LIVE_PERSONNEL_TYPE = Constant.LIVE_PERSONNEL_TYPE_STUDENT;
        Constant.LIVE_TYPE_ID = teachid;
        Constant.LIVE_ROOM_ID = roomId;
        if (TextUtils.isEmpty(nackname)) {
            nackname = getResources().getString(R.string.live_student) + " " + mUerId;
        }
        userimage = "";
        mroomType = getIntent().getStringExtra(ROOM_TYPE);

        //初始化websocket消息
//        initWsListeners();
        Long ct = System.currentTimeMillis();
        seqid = "binding_" + String.valueOf(ct);
        ll_start_live = findViewById(R.id.linearLayout_start_live);
        ll_start_tool = findViewById(R.id.linearLayout_right_tool);
        iv_radio_courseware = findViewById(R.id.land_iv_radio_courseware);
        iv_raise_hand = findViewById(R.id.land_iv_raise_hand);
        mIcrophone = findViewById(R.id.land_iv_microphone);
        mCamera = findViewById(R.id.land_iv_camera);
        fl_personnel_live = findViewById(R.id.land_fl_personnel_live);
        iv_personnel_detail = findViewById(R.id.land_iv_personnel_detail);
        materialBadge_textview_chat = findViewById(R.id.land_materialBadge_textview_chat);
        land_img_chat = findViewById(R.id.land_img_chat);
        mbt_personnel_chat = findViewById(R.id.land_mbt_personnel_chat);
        image_view_share = findViewById(R.id.land_image_view_share);
        image_view_back = findViewById(R.id.land_image_view_back);
        land_ll_tool_chat = findViewById(R.id.land_ll_tool_chat);
        land_mbt_personnel_chat = findViewById(R.id.land_mbt_personnel_chat);
        student_view_web = findViewById(R.id.land_student_view_web);
        page_textview_number = findViewById(R.id.land_page_textview_number);
        rl_student_video_view = findViewById(R.id.rl_student_video_view);
        iv_student_live_navbar_unfold = findViewById(R.id.iv_live_navbar_unfold);
        rl_room_show_anim = findViewById(R.id.room_live_show_anim_layout);
        tv_trophy_num = findViewById(R.id.tv_trophy_num);
        mtv_gif_num = findViewById(R.id.mtv_gif_num);
        mtv_gif_num_x = findViewById(R.id.mtv_gif_num_x);
        ll_trophy_personel = findViewById(R.id.ll_trophy_personel);
        id_animation_cup = findViewById(R.id.id_animation_cup);
        ll_animation_filling = findViewById(R.id.ll_animation_filling);
        rl_network_bar = findViewById(R.id.rl_network_stauts_bar);
        iv_network_stauts_excellent = findViewById(R.id.network_stauts_excellent_iv);
        tv_network_stauts_ms = findViewById(R.id.network_stauts_ms_tv);
        tv_network_stauts_percentage = findViewById(R.id.network_stauts_percentage_tv);
        tv_network_stauts_op = findViewById(R.id.network_stauts_op_tv);
        network_stauts_course_name = findViewById(R.id.network_stauts_course_name);
        network_stauts_classes_tv = findViewById(R.id.network_stauts_classes_tv);
        network_stauts_time = findViewById(R.id.network_stauts_time_tv);
        land_iv_tool_whiteboard = findViewById(R.id.land_iv_tool_whiteboard);
        rl_student_land_main = findViewById(R.id.rl_student_land_main);
        land_iv_tool_answer = findViewById(R.id.land_iv_tool_answer);
        land_iv_tool_trophy = findViewById(R.id.land_iv_tool_trophy);
        ll_no_class_background = findViewById(R.id.ll_no_class_background);
        ll_tool_right = findViewById(R.id.ll_tool_right);
        large_ll_right_bottom = findViewById(R.id.large_ll_right_bottom);
        ll_video_tool_zoom = findViewById(R.id.ll_video_tool_zoom);
        large_iv_chat = findViewById(R.id.large_iv_chat);
        large_mbtv_chat_red = findViewById(R.id.large_mbtv_chat_red);
        large_iv_persone_list = findViewById(R.id.large_iv_persone_list);
        large_iv_notice = findViewById(R.id.large_iv_notice);
        large_mbtv_notice_num = findViewById(R.id.large_mbtv_notice_num);
        land_iv_raise_menu = findViewById(R.id.land_iv_raise_menu);
        tool_ll_view_displacement = findViewById(R.id.tool_ll_view_displacement);
        iv_live_camera_flip = findViewById(R.id.land_iv_live_camera_flip);

        if (LIVE_LARGE_CLASS.equals(mroomType)) {
            //大班课
            ll_tool_right.setVisibility(View.GONE);
            large_ll_right_bottom.setVisibility(View.VISIBLE);
            land_ll_tool_chat.setVisibility(View.GONE);
            ll_video_tool_zoom.setVisibility(View.GONE);

        } else if (LIVE_SMALL_CLASS.equals(mroomType)) {
            //小班课
            ll_tool_right.setVisibility(View.VISIBLE);
            large_ll_right_bottom.setVisibility(View.GONE);
            land_ll_tool_chat.setVisibility(View.VISIBLE);
            ll_video_tool_zoom.setVisibility(View.GONE);
        }

        ll_start_live.setVisibility(View.GONE);
        ll_start_tool.setVisibility(View.VISIBLE);
        iv_radio_courseware.setVisibility(View.GONE);
        land_iv_tool_answer.setVisibility(View.GONE);
        land_iv_tool_trophy.setVisibility(View.GONE);
        land_iv_tool_whiteboard.setVisibility(View.GONE);
        tool_ll_view_displacement.setVisibility(View.GONE);
        land_iv_raise_menu.setVisibility(View.GONE);

        image_view_back.setOnClickListener(this);
        land_img_chat.setOnClickListener(this);
        iv_personnel_detail.setOnClickListener(this);
        mCamera.setOnClickListener(this);
        mIcrophone.setOnClickListener(this);
        iv_raise_hand.setOnClickListener(this);
        image_view_share.setOnClickListener(this);
        iv_student_live_navbar_unfold.setOnClickListener(this);
        land_iv_tool_whiteboard.setOnClickListener(this);
        large_iv_chat.setOnClickListener(this);
        large_iv_persone_list.setOnClickListener(this);
        large_iv_notice.setOnClickListener(this);
        land_iv_raise_menu.setOnClickListener(this);
        iv_live_camera_flip.setOnClickListener(this);

        network_stauts_course_name.setText(title_name);
        if (!TextUtils.isEmpty(mNotice)) {
            large_mbtv_notice_num.setVisibility(View.VISIBLE);
            large_mbtv_notice_num.setText("1");
        }
    }

    @Override
    protected void initView() {
        bindService(WebSocketService.createIntent(StudentActivityLand.this), stWsConnection, BIND_AUTO_CREATE);
        //注册直播相关的广播接收者
        mRoomLiveHelp = new RoomLiveHelp(this, this);
        mPresenter = new RoomPresenter(this);
        sharePresenter = new SharePresenter(this);
        chatPop = new ChatPopupWindow(mContext, roomId, mUerId, nackname, userimage);
        giftNumAnim = new NumAnim();
        mPingUtil = new PingUtil(this, this);
//        video_view_ijkView.setHudView(new TableLayout(mContext));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mapWidth = dm.widthPixels;
        mapHeight = dm.heightPixels;
        parentWidth = mapWidth;
        parentHeight = mapHeight;

        sharePresenter.getShareInvite(roomId);//获取房间邀请码

        StatusBarUtil.setTatileRelatLayoutBar(this, rl_network_bar);//设置网络状态的高度为系统状态栏高度

        String bangDingWeb = wsbind(Integer.parseInt(roomId), Integer.parseInt(mUerId), seqid, 2);

        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(bangDingWeb);
        } else {
            sWebservice.sendRequest(bangDingWeb);
        }
        SPTools.getInstance(this).save(roomId, bangDingWeb);

        //初始化人员名单
        initPersonnerView();

        //判断是否已经开始上课
        whetherCourseStart();

        //ping网络状态
        startTiemHeart();

        //注册广播
        initBreceiver(mContext);

        //进入房间，获取视频分辨率
        mPresenter.getVideoProfile();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

//            if (mapWidth * 9 > mapHeight * 16) {//宽高比大于16:9的屏幕
            parentHeight = mapHeight - rl_network_bar.getHeight();
            parentWidth = 16 * parentHeight / 9;

            singleWidth = parentWidth * 116 / 640;
            singleHeight = singleWidth * 11 / 16;
            Log.e("TAG", "singleWidth == " + singleWidth);
            Log.e("TAG", "singleHeight == " + singleHeight);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(parentWidth, parentHeight);
            layoutParams.leftMargin = (mapWidth - (16 * parentHeight / 9)) / 2;
            layoutParams.rightMargin = (mapWidth - (16 * parentHeight / 9)) / 2;
            layoutParams.topMargin = rl_network_bar.getHeight();
            rl_student_video_view.setLayoutParams(layoutParams);
        }
    }

    //获取视频分辨率回调
    @Override
    public void getProfileSuccess(int i) {
        //获取分辨率加入直播频道
        Log.e(TAG_CLASS, " 回调： getProfileSuccess()");
        Log.e(TAG_CLASS, " 视频分辨率  " + i + "  mroomType " + mroomType);
        resolution = i;
        String custom = getJoinRoomCustom(roomId, Integer.parseInt(mUerId), nackname,
                userimage, mUserRole, mLiveLm,
                mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
        mRoomLiveHelp.initTTTEngine();
        if (LIVE_LARGE_CLASS.equals(mroomType)) {
//            mRoomLiveHelp.enterRoom(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING,
//                    Constants.CLIENT_ROLE_AUDIENCE, Integer.parseInt(roomId), Long.parseLong(mUerId),i , custom);

            mRoomLiveHelp.enterRoom(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING,
                    Constants.CLIENT_ROLE_BROADCASTER, Integer.parseInt(roomId), Long.parseLong(mUerId), i, custom);

        } else if (LIVE_SMALL_CLASS.equals(mroomType)) {
            mRoomLiveHelp.enterRoom(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING,
                    Constants.CLIENT_ROLE_BROADCASTER, Integer.parseInt(roomId), Long.parseLong(mUerId), i, custom);
        }

    }

    //获取房间邀请码回调
    @Override
    public void shareRoomInvite(ShareBean shareBean) {
        //邀请码返回
        Gson gson = new Gson();
        String ss = gson.toJson(shareBean);
        Log.e(TAG_CLASS + "邀请码返回 : ", ss);
        if (shareBean != null) {
            for (int i = 0; i < shareBean.getTeacher().size(); i++) {
                if (roomId.equals(shareBean.getTeacher().get(i).getCourseId())) {
                    student_invite_code = shareBean.getStudent().get(i).getInviteCode();
                }
            }

        }
    }

    //初始化websocket消息
    private void initWsListeners() {

        WsListener notifListener = new WsListener() {

            @Override
            public void handleData(String msg) {
                Log.e(TAG_CLASS, "接收到socket发来的消息:" + msg);
                WeakReference<StudentActivityLand> studentActivityReference = new WeakReference<>(StudentActivityLand.this);
                RoomMessageType msgType = new RoomMessageType(studentActivityReference.get());
                msgType.appendData(msg);
            }
        };
        if (Constant.wsService != null && notifListener != null) {
            Constant.wsService.registerListener(notifListener);
        } else {
            Log.e(TAG_CLASS, "WsListener wsService : " + Constant.wsService);
            Log.e(TAG_CLASS, "WsListener notifListener : " + notifListener);
            Log.e(TAG_CLASS, "WsListener sWebservice : " + sWebservice);
            sWebservice.registerListener(notifListener);
        }
    }

    //学生进入房间成功
    @Override
    public void enterRoomSuccess() {
        Log.e(TAG_CLASS, "enterRoomSuccess:进入房间成功  userid " + mUerId);
        if (!startCourse || LIVE_LARGE_CLASS.equals(mroomType)) {
            mRoomLiveHelp.controlAllRemoteAudioStreams(true);
            mRoomLiveHelp.contorlAllRemoteVideoStreams(true);
        }
        AudioClose(mUerId);
        VideoClose(mUerId);
        SendJoinRoomMessage();
        Constant.LIVE_TYPE_ID = teachid;

    }

    //学生进入房间失败
    @Override
    public void enterRoomFailue(int error) {
        Log.e(TAG_CLASS, "enterRoomFailue:进入房间失败" + error);
        if (error == Constants.ERROR_ENTER_ROOM_TIMEOUT) {
            toastShort(R.string.msg_unknown_error);
        } else if (error == Constants.ERROR_ENTER_ROOM_FAILED) {
            toastShort(R.string.msg_unconnection_error);
        } else if (error == Constants.ERROR_ENTER_ROOM_VERIFY_FAILED) {
            toastShort(R.string.msg_unVerification_error);
        } else if (error == Constants.ERROR_ENTER_ROOM_UNKNOW) {
            toastShort(R.string.msg_unCourse_start);
        } else {
            toastShort(R.string.msg_unEnter_error);
        }
        onBackPressed();
    }

    //直播过程中掉线
    @Override
    public void onDisconnected(int errorCode) {
        Log.e(TAG_CLASS, "onDisconnected:直播中断线" + errorCode);
        if (errorCode == Constants.ERROR_KICK_BY_HOST) {
            toastShort(R.string.msg_operation_kicked_out);
            onBackPressed();
        } else if (errorCode == Constants.ERROR_KICK_BY_MASTER_EXIT) {
            toastShort(R.string.msg_operation_taecher_exit);
            teacherEnd(teachid);
        } else if (errorCode == Constants.ERROR_KICK_BY_NEWCHAIRENTER) {
            toastShort(R.string.msg_operation_number_max);
            onBackPressed();
        } else if (errorCode == 100) {
            toastShort(R.string.msg_operation_net_abnormal);
            onBackPressed();
        } else {
            toastShort(R.string.msg_operation_live_out);
            onBackPressed();
        }

    }

    //有成员退出直播间
    @Override
    public void onMemberExit(long userId) {
        Log.e(TAG_CLASS, "onMemberExit:直播成员退出" + userId);
        if (userId == Integer.parseInt(teachid)) {
            teacherEnd(teachid);
        } else {
            releaseLiveView(userId);
            leaveRoomPersonel(userId);

        }
    }

    //有成员进入直播间
    @Override
    public void onMemberEnter(long userId, EnterUserInfo userInfo, String sCustom) {
        Log.e(TAG_CLASS, "onMemberEnter:直播成员进入 " + userId + " scustom " + sCustom);

        CustomBean cb = stGson.fromJson(sCustom, CustomBean.class);
        joinRoomPersonel(cb);
    }

    //主播进入直播间
    @Override
    public void onHostEnter(long userId, EnterUserInfo userInfo, String sCustom) {
        //打开主播视频窗口
        Log.e(TAG_CLASS, "onHostEnter: 直播主播进入 " + userInfo.getDevice() + " userId  :  " + userInfo.getId() + " sCustom  " + sCustom);
        mUserInfo = userInfo;
        if (startCourse) {
            startLiveInit();
        }
        CustomBean cb = stGson.fromJson(sCustom, CustomBean.class);
        joinRoomPersonel(cb);

    }

    @Override
    public void onUpdateLiveView(List<EnterUserInfo> userInfos) {
        Log.e(TAG_CLASS, "onUpdateLiveViewonUpdateLiveView:" + userInfos.size());
    }

    //收到im消息处理
    @Override
    public void dispatchMessage(long srcUserID, int type, String sSeqID, String data) {
        Log.e(TAG_CLASS, "收到消息srcUserID:" + srcUserID + "type:--" + type + "-sSeqID:--" + sSeqID + "-data:--" + data);

    }

    //发送的消息的结果
    @Override
    public void sendMessageResult(int resultType, String data) {

    }

    //接受视频上行速率
    @Override
    public void localVideoStatus(LocalVideoStats localVideoStats) {

    }

    //接受视频下行速率
    @Override
    public void remoteVideoStatus(RemoteVideoStats mRemoteVideoStats) {

    }

    //接收音频上行
    @Override
    public void LocalAudioStatus(LocalAudioStats localAudioStats) {

    }

    //接收音频下行
    @Override
    public void remoteAudioStatus(RemoteAudioStats mRemoteAudioStats) {

    }

    //用户数据更新
    @Override
    public void OnupdateUserBaseInfo(Long roomId, long uid, String sCustom) {
        //用户有信息更新
        Log.e(TAG_CLASS, " 用户信息更新 roomId : " + roomId + " sCustom " + sCustom);
        CustomBean upCustom = stGson.fromJson(sCustom, CustomBean.class);

        for (int i = 0; i < customBeanList.size(); i++) {
            if (upCustom.getUserId() == customBeanList.get(i).getUserId()) {
                if (customBeanList.get(i).getLm() == 0) {
                    if (upCustom.getLm() == 1) {
                        showRemoteView(String.valueOf(upCustom.getUserId()), mUserInfo);
                        customBeanList.get(i).setLm(1);
                    }
                } else if (customBeanList.get(i).getLm() == 1) {
                    if (upCustom.getLm() == 0) {
                        releaseLiveView(upCustom.getUserId());
                        customBeanList.get(i).setLm(0);
                    } else {
                        if (customBeanList.get(i).isMicClosed()) {
                            if (!upCustom.isMicClosed()) {
                                for (int i1 = 0; i1 < mVideoViewList.size(); i1++) {
                                    if (mVideoViewList.get(i1).getFlagUserId().equals(String.valueOf(upCustom.getUserId()))) {
                                        CurModelTools(i1);
                                        mVideoViewList.get(i1).getLive_stauts_phone().setVisibility(View.GONE);
                                        customBeanList.get(i).setMicClosed(false);
                                    }
                                }
                            }
                        } else {
                            if (upCustom.isMicClosed()) {
                                for (int i1 = 0; i1 < mVideoViewList.size(); i1++) {
                                    if (mVideoViewList.get(i1).getFlagUserId().equals(String.valueOf(upCustom.getUserId()))) {
                                        CurModelTools(i1);
                                        mVideoViewList.get(i1).getLive_stauts_phone().setVisibility(View.VISIBLE);
                                        customBeanList.get(i).setMicClosed(true);
                                    }
                                }
                            }

                        }

                        if (customBeanList.get(i).isCameraClosed()) {
                            if (!upCustom.isCameraClosed()) {
                                for (int i1 = 0; i1 < mVideoViewList.size(); i1++) {
                                    if (mVideoViewList.get(i1).getFlagUserId().equals(String.valueOf(upCustom.getUserId()))) {
                                        CurModelTools(i1);
                                        mVideoViewList.get(i1).getLive_stauts_camera().setVisibility(View.GONE);
                                        mVideoViewList.get(i1).getLand_rl_live_microphone_one().setVisibility(View.GONE);
                                        customBeanList.get(i).setCameraClosed(false);
                                    }
                                }
                            }

                        } else {
                            if (upCustom.isCameraClosed()) {
                                for (int i1 = 0; i1 < mVideoViewList.size(); i1++) {
                                    if (mVideoViewList.get(i1).getFlagUserId().equals(String.valueOf(upCustom.getUserId()))) {
                                        CurModelTools(i1);
                                        mVideoViewList.get(i1).getLive_stauts_camera().setVisibility(View.VISIBLE);
                                        mVideoViewList.get(i1).getLand_rl_live_microphone_one().setVisibility(View.VISIBLE);
                                        customBeanList.get(i).setCameraClosed(true);
                                    }
                                }

                            }

                        }
                    }
                }

                if (customBeanList.get(i).isWhiteBoardAccess()) {
                    if (!upCustom.isWhiteBoardAccess()) {
                        for (int i1 = 0; i1 < mVideoViewList.size(); i1++) {
                            if (mVideoViewList.get(i1).getFlagUserId().equals(String.valueOf(upCustom.getUserId()))) {
                                CurModelTools(i1);
                                mVideoViewList.get(i1).getLive_stauts_authorization().setVisibility(View.GONE);
                                customBeanList.get(i).setWhiteBoardAccess(false);
                            }
                        }
                    }
                } else {
                    if (upCustom.isWhiteBoardAccess()) {

                        for (int i1 = 0; i1 < mVideoViewList.size(); i1++) {
                            if (mVideoViewList.get(i1).getFlagUserId().equals(String.valueOf(upCustom.getUserId()))) {
                                CurModelTools(i1);
                                mVideoViewList.get(i1).getLive_stauts_authorization().setVisibility(View.VISIBLE);
                                customBeanList.get(i).setWhiteBoardAccess(true);
                            }
                        }

                    }

                }
                whiteboardStype(String.valueOf(upCustom.getUserId()), upCustom.isWhiteBoardAccess());
            }

        }

    }

    //链接成功，返回当前登录的媒体服务器
    @Override
    public void OnConnectSuccess(String ip, int port) {
        //接受当前登录的媒体服务器
        Log.i(TAG_CLASS, "媒体服务器 ip : " + ip + ":" + port);
        video_host_ip = ip;
        mPingUtil.PingHttp("4", "64", "1", ip);

    }

    /*******************************************
     * 收到Socket信令
     *******************************************
     */
    //聊天消息回调
    @Override
    public void receiveTextMessage(String data) {
        Log.e(TAG_CLASS, "im_req:data:" + data);
        Gson gson = new Gson();
        SendTextMessageBean sendmsg = gson.fromJson(data, SendTextMessageBean.class);
        msgList.add(sendmsg);
        if (chatPop.isShow()) {
            chatPop.showChatList(msgList);
        } else {
            land_mbt_personnel_chat.setVisibility(View.VISIBLE);
            large_mbtv_chat_red.setVisibility(View.VISIBLE);
        }
    }

    //加入房间成功回调
    @Override
    public void joinRoomSuccess(String data) {

    }

    //退出房间回调
    @Override
    public void leaveMessage(String data) {
        Gson leaveGson = new Gson();
        LeaveMassageBean leaveRoomBean = leaveGson.fromJson(data, LeaveMassageBean.class);
        if (String.valueOf(leaveRoomBean.getData().getUserId()).equals(teachid)) {
            toastShort(R.string.msg_operation_taecher_exit);
            teacherEnd(teachid);
        }
    }


    @Override
    public void dealApplyMicMessage(String data) {
        //邀请发言
        Log.e(TAG_CLASS + " dealApplyMicMessage ", data);
        Gson leaveGson = new Gson();
        LmAgreeResBean lmAgreeBean = leaveGson.fromJson(data, LmAgreeResBean.class);
        if (lmAgreeBean.getData().getUserId().equals(mUerId)) {
        }
    }


    //学生申请连麦的回调
    @Override
    public void dealApplyAgreeMessage(String data) {
        Log.e("TAG", "连麦响应：" + data);
        Log.e("TAG", "mUserId :" + mUerId);
        Gson leaveGson = new Gson();
        LmAgreeResBean lmAgreeBean = leaveGson.fromJson(data, LmAgreeResBean.class);
        if (lmAgreeBean.getData().getType().equals("1")) {//老师同意了学生的连麦请求

            if (lmAgreeBean.getData().getUserId().equals(mUerId)) {
                AudioOpen(lmAgreeBean.getData().getUserId());
                VideoOpen(lmAgreeBean.getData().getUserId());
                land_iv_raise_menu.setVisibility(View.VISIBLE);
                isLmstate = true;
                mVideoClose = false;
                mAudeoClose = false;
                mLiveLm = 1;
                String update = getJoinRoomCustom(roomId, Integer.parseInt(mUerId),
                        nackname, userimage, mUserRole,
                        mLiveLm, mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
                mRoomLiveHelp.updateUserInfo(update);
                showRemoteView(lmAgreeBean.getData().getUserId(), mUserInfo);
                for (int i = 0; i < customBeanList.size(); i++) {
                    if (customBeanList.get(i).getUserId() == Integer.parseInt(mUerId)) {
                        customBeanList.get(i).setLm(mLiveLm);
                        customBeanList.get(i).setMicClosed(mAudeoClose);
                        customBeanList.get(i).setCameraClosed(mVideoClose);
                    }
                }
            }


        } else if (mUerId.equals(lmAgreeBean.getData().getUserId()) && "0".equals(lmAgreeBean.getData().getType())) {
            toastShort("教师拒绝你的连麦申请！");
            applyLmState = false;
            iv_raise_hand.setBackground(getResources().getDrawable(R.drawable.icon_hand_normal));
            iv_raise_hand.setClickable(true);
        }

    }

    //房间人员名单回调
    @Override
    public void roomPersonnelList(String data) {
    }

    //文档同步回调
    @Override
    public void roomDocConect(String data) {
    }

    //收到老师禁止学生发言的消息
    @Override
    public void closeLmCall(String data) {
        //断开连麦
        Log.e(TAG_CLASS + " closeLmCall ", data);
        Gson leaveGson = new Gson();
        LmDisconnectBean lmAgreeBean = leaveGson.fromJson(data, LmDisconnectBean.class);
        //关闭摄像头与麦克风
        if (lmAgreeBean.getData().getUserId().equals(mUerId)) {
            AudioVideoClose();
            applyLmState = false;
            showSelfVideo = false;
            if (animation_tool) {
                ToolsAnimator();
            }
            iv_raise_hand.setBackground(getResources().getDrawable(R.drawable.icon_hand_normal));
            if (mWhiteBoard) {
                land_iv_tool_whiteboard.setVisibility(View.VISIBLE);
            } else {
                land_iv_tool_whiteboard.setVisibility(View.GONE);
            }
            land_iv_raise_menu.setVisibility(View.GONE);
            mLiveLm = 0;
            mVideoClose = true;
            mAudeoClose = true;
            iv_raise_hand.setClickable(true);
            String update = getJoinRoomCustom(roomId, Integer.parseInt(mUerId),
                    nackname, userimage, mUserRole, mLiveLm,
                    mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
            mRoomLiveHelp.updateUserInfo(update);
        }
        releaseLiveView(Long.parseLong(lmAgreeBean.getData().getUserId()));
    }

    //老师将学生踢出房间
    @Override
    public void outRoomClose(String data) {
        //踢出房间
        Log.e(TAG_CLASS + " outRoomClose ", data);
        Gson leaveGson = new Gson();
        LeaveMassageBean lmAgreeBean = leaveGson.fromJson(data, LeaveMassageBean.class);
        onBackPressed();
    }

    //当前连麦列表
    @Override
    public void lmListPersonnel(String data) {

        Log.e(TAG_CLASS, "当前连麦列表data:" + new Gson().toJson(data));
    }

    //禁言
    @Override
    public void gagReqPersonnel(String data) {
        //
        if (!TextUtils.isEmpty(data)) {
            Gson gGson = new Gson();
            Log.e(TAG_CLASS + "  gagReqPersonnel", gGson.toJson(data));
            GegBannedBean gbb = gGson.fromJson(data, GegBannedBean.class);
            if (gbb != null) {
//                EventBus.getDefault().post(new MessageEvent(Constant.SEND_EVENT_GAG_REG, data));
                if (gbb.getData().getUserId().equals("0") || gbb.getData().getUserId().equals(mUerId)) {
                    if (chatPop != null && chatPop.isShow()) {
                        chatPop.closePop();
                        toastShort(R.string.live_banned);
                    }
                    mSpeakStop = 1;
                    isBanned = true;
                    chatPop.banned(isBanned);
                    String update = getJoinRoomCustom(roomId, Integer.parseInt(mUerId),
                            nackname, userimage, mUserRole, mLiveLm,
                            mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
                    mRoomLiveHelp.updateUserInfo(update);
                }
            }
        }
    }

    //解除禁言
    @Override
    public void gagRerRemovePersonnel(String data) {

        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            Log.e(TAG_CLASS + "  gagRerRemovePersonnel", gson.toJson(data));
            GegBannedBean gbbr = gson.fromJson(data, GegBannedBean.class);
            if (gbbr != null) {
//                EventBus.getDefault().post(new MessageEvent(Constant.SEND_EVENT_REMOVE_GAG_RER, data));
                if (gbbr.getData().getUserId().equals("0") || gbbr.getData().getUserId().equals(mUerId)) {
                    isBanned = false;
                    mSpeakStop = 0;
                    chatPop.banned(isBanned);
                    String update = getJoinRoomCustom(roomId, Integer.parseInt(mUerId),
                            nackname, userimage, mUserRole, mLiveLm,
                            mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
                    mRoomLiveHelp.updateUserInfo(update);
                }
            }
        }
    }

    //收到开始上课消息
    @Override
    public void courseStart(String data) {
        Log.e(TAG_CLASS, "开始上课 " + data);
        if (!startCourse) {
            network_stauts_classes_tv.setText(getResources().getString(R.string.network_stauts_has_classes));
            mHandler.removeCallbacks(timerRunnable);
            startTimer("network");
            startLiveInit();

        }
    }

    //收到下课消息
    @Override
    public void courseLeave(String data) {
        Log.e(TAG_CLASS, "老师下课 " + data);
        StartNotLiveBean snb = stGson.fromJson(data, StartNotLiveBean.class);
        network_stauts_classes_tv.setText(getResources().getString(R.string.network_stauts_not_classes));
        mHandler.removeCallbacks(timerRunnable);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        Date time = calendar.getTime();
        liveTime = time.getTime();
        CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", liveTime);
        network_stauts_time.setText(sysTimeStr);
        network_stauts_time.setVisibility(View.GONE);
        teacherEnd(snb.getData().getUserId());

    }

    //咨询主播是否已经开始上课
    @Override
    public void courseTeacherNotStart(String data) {
        //是否已经开课
        Log.e(TAG_CLASS, "老师是否已经开课 ");
        Log.e(TAG_CLASS, "老师是否已经开课 :" + data);
        Gson nGson = new Gson();
        StartNotLiveBean snlBean = nGson.fromJson(data, StartNotLiveBean.class);
        if (snlBean.getData().getType().equals("1")) {
            //开课
            Log.e(TAG_CLASS, "开课  " + data);
            if (!startCourse) {
                startLiveInit();
            }
        } else if (snlBean.getData().getType().equals("0")) {
            //未开课
            Log.e(TAG_CLASS, "未开课  " + data);
        }

    }

    //收到奖杯
    @Override
    public void trophyAward(String data) {
        //收到发送的奖杯
        Gson gson = new Gson();
        TrophyAwardBean tyb = gson.fromJson(data, TrophyAwardBean.class);
        if (trophyMapList != null && trophyMapList.size() > 0) {
            Map<Integer, Integer> cupMap = trophyMapList;
            Set<Map.Entry<Integer, Integer>> entrySet = cupMap.entrySet();
            Iterator<Map.Entry<Integer, Integer>> mapIt = entrySet.iterator();
            while (mapIt.hasNext()) {
                Map.Entry<Integer, Integer> me = mapIt.next();
                if (String.valueOf(me.getKey()).equals(tyb.getData().getUserId())) {
                    me.setValue(me.getValue() + 1);
                    mTrophyCount = me.getValue();
                    updateGiftNum(String.valueOf(me.getKey()), mTrophyCount);
                    trophyNumChang(tyb);

                }
            }
        }

//        getNumTrophyList.add(tyb);
        obtain_trophy++;
        Log.i(TAG_CLASS, obtain_trophy + " ");
        if (isgiftend) {
            isgiftend = false;
            fireworks = new FireworksView(this);
            fireworks.setAnimsopt(this);
            id_animation_cup.setVisibility(View.VISIBLE);
            ll_trophy_personel.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.width = mapHeight / 2;
            layoutParams.height = mapHeight / 2;
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            rl_room_show_anim.addView(fireworks, layoutParams);
            giftNumAnim.start(ll_animation_filling);
//            getTrophyUserId = tyb.getAccountList().getUserId();

//            if (getNumTrophyList != null && getNumTrophyList.size() >0) {
//                trophyNumChang(getNumTrophyList.get(getNumTrophyList.size() - obtain_trophy));
//                getNumTrophyList.get(getNumTrophyList.size() - obtain_trophy);
//            }

        }
    }

    //收到答题信令
    @Override
    public void statrAnswer(String data) {
        //收到答题信令
        Log.e(TAG_CLASS, data);
        Gson gson = new Gson();
        TeacherAnswerBean mTeacherAnswerBean = gson.fromJson(data, TeacherAnswerBean.class);

        if (mTeacherAnswerBean.getData().getStatus().equals("published")) {
            if (solution != null) {
                solution.clear();
            }

            int mOptionnum = mTeacherAnswerBean.getData().getOptions().length();

            resultrPopWindowDismiss();
            //弹出答题器
            initAnswerView(mOptionnum);
            answerPopWindow(ll_time);
            startTimer("published");
        } else if (mTeacherAnswerBean.getData().getStatus().equals("ended")) {
            answerPopWindowDismiss();
            //弹出结果
            initResultrView();
            resultrPopWindow(ll_time);
            startTimer("ended");
            StringBuffer meAnswer = new StringBuffer();
            if (solution != null) {
                if (solution.size() > 0) {
                    Collections.sort(solution);
                    for (int j = 0; j < solution.size(); j++) {

                        meAnswer.append(solution.get(j));
                    }
                } else {
                    meAnswer.append("未答题");
                }
            }

            tv_me_answer.setText(meAnswer.toString());
            tv_correct_answer.setText(mTeacherAnswerBean.getData().getCorrect());
            if (meAnswer.toString().equals(mTeacherAnswerBean.getData().getCorrect())) {
                tv_me_answer.setTextColor(getResources().getColor(R.color.color_02AFFC));
            } else {
                tv_me_answer.setTextColor(getResources().getColor(R.color.color_3F3F3F));
            }
        } else if (mTeacherAnswerBean.getData().getStatus().equals("inactivated")) {
            resultrPopWindowDismiss();
            answerPopWindowDismiss();

        }
    }

    //老师收到学生提交答案
    @Override
    public void statisicsAnswer(String data) {

    }

    //学生收到白板授权
    @Override
    public void whiteboardAccess(String data) {
        //白板授权
        WhiteBoardBean mWhiteBoardBean = stGson.fromJson(data, WhiteBoardBean.class);
        if (mWhiteBoardBean.getData().isAccess()) {
            land_iv_tool_whiteboard.setBackground(getResources().getDrawable(R.drawable.icon_pen_normal));
            if (curModel == MODEL_NORMAL) {
                land_iv_tool_whiteboard.setVisibility(View.VISIBLE);
            }
            mWhiteBoard = true;
        } else {
            mWhiteBoard = false;
            land_iv_tool_whiteboard.setVisibility(View.GONE);
            witeTool();
        }

        for (int i = 0; i < mVideoViewList.size(); i++) {
            if (mVideoViewList.get(i).getFlagUserId().equals(mUerId)) {
                if (mWhiteBoard) {
                    mVideoViewList.get(i).getLive_stauts_authorization().setVisibility(View.VISIBLE);
                } else {
                    mVideoViewList.get(i).getLive_stauts_authorization().setVisibility(View.GONE);

                }
            }
        }

        whiteboardStype(mUerId, mWhiteBoard);

        String update = getJoinRoomCustom(roomId, Integer.parseInt(mUerId),
                nackname, userimage, mUserRole, mLiveLm, mSpeakStop,
                mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
        mRoomLiveHelp.updateUserInfo(update);
    }

    //老师关闭摄像头
    @Override
    public void liveVideoClose(String data) {
        Log.e(TAG_CLASS, "主播关闭摄像头 " + data);
        //摄像头关闭
        VAClosedBean vaco = stGson.fromJson(data, VAClosedBean.class);
        if (vaco.getData().isClosed()) {
            //关
            mVideoClose = true;
            VideoClose(mUerId);
        } else {
            //开
            mVideoClose = false;
            VideoOpen(mUerId);
        }
    }

    //老师关闭麦克风
    @Override
    public void liveAudioClose(String data) {
        Log.e(TAG_CLASS, "主播关闭麦克风 " + data);
        //麦克风关闭
        VAClosedBean vaco = stGson.fromJson(data, VAClosedBean.class);
        if (vaco.getData().isClosed()) {
            //关
            mAudeoClose = true;
            AudioClose(mUerId);

        } else {
            //开
            mAudeoClose = false;
            AudioOpen(mUerId);
        }
    }

    private CustomPopWindow customPopWindow;

    private void showPopupWindow(String message) {
        if (customPopWindow == null) {
            customPopWindow = new CustomPopWindow(this);
        }
        customPopWindow.setMessage(message);
        customPopWindow.showAtLocation(rl_student_land_main, Gravity.CENTER, 0, 0);
    }


    @Override
    public void modeChangeReq(String data) {

        Log.i(TAG_CLASS, "切换视频模式 : " + data);

        ModeVieoChangeBean mvcBean = stGson.fromJson(data, ModeVieoChangeBean.class);
        if ("video".equals(mvcBean.getData().getMode()) && curModel != MODEL_VIDEO) {
            //视频模式
            Log.i(TAG_CLASS, "视频模式 ");
            curModel = MODEL_VIDEO;
            showPopupWindow(getString(R.string.model_video));
            int childCount = rl_student_video_view.getChildCount();
            ArrayList<ViewInfo> viewInfoList = getViewInfoList(childCount);
            for (int i = 0; i < childCount; i++) {
                VideoView videoView = (VideoView) rl_student_video_view.getChildAt(i);
                int width = viewInfoList.get(i).getWidth();
                int height = viewInfoList.get(i).getHeight();
                int x = viewInfoList.get(i).getX();
                int y = viewInfoList.get(i).getY();
                videoView.getValueAnimator(width, height, x, y).start();
//                videoView
//                        .animate()
//                        .translationX(x - videoView.getX())
//                        .translationY(y - videoView.getY())
//                        .scaleX(1.0F * width / videoView.getWidth())
//                        .scaleY(1.0F * height / videoView.getHeight())
//                        .setListener(new AnimatorListenerAdapter() {
//                        })
//                        .start();
            }

        } else if ("normal".equals(mvcBean.getData().getMode()) && curModel != MODEL_NORMAL) {

            //白板模式
            curModel = MODEL_NORMAL;
            showPopupWindow(getString(R.string.model_normal));
            int childCount = rl_student_video_view.getChildCount();
            int eachWidth = parentWidth / 7;
            int eachHeight = eachWidth * 9 / 16;
            for (int i = 0; i < childCount; i++) {
                VideoView videoView = (VideoView) rl_student_video_view.getChildAt(i);
                videoView.getValueAnimator(eachWidth, eachHeight, i * eachWidth, 0).start();

//                videoView
//                        .animate()
//                        .translationX(eachWidth * i - videoView.getX())
//                        .translationY(-videoView.getY())
//                        .scaleX(1.0F * eachWidth / videoView.getWidth())
//                        .scaleY(1.0F * eachHeight / videoView.getHeight())
//                        .setListener(new AnimatorListenerAdapter() {
//                        })
//                        .start();
            }

        }
        for (int i = 0; i < customBeanList.size(); i++) {
            if (customBeanList.get(i).getLm() == 1) {
                for (int i1 = 0; i1 < mVideoViewList.size(); i1++) {
                    if (String.valueOf(customBeanList.get(i).getUserId()).equals(mVideoViewList.get(i1).getFlagUserId())) {
                        CurModelTools(i1);
                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.land_image_view_back:
                exitRoomDialog();
                break;
            case R.id.land_image_view_share:
                showSharePopu();
                break;
            case R.id.land_img_chat:
                if (!chatPop.isShow()) {
                    chatPop.showChatPop(land_ll_tool_chat, StudentActivityLand.this, msgList, isBanned);
                    land_mbt_personnel_chat.setVisibility(View.GONE);
                }
                break;
            case R.id.large_iv_chat:
                if (!chatPop.isShow()) {
                    chatPop.showChatPop(land_ll_tool_chat, StudentActivityLand.this, msgList, isBanned);
                    large_mbtv_chat_red.setVisibility(View.GONE);
                }

                break;
            case R.id.land_iv_personnel_detail:
                //人员名单
                personnelPopWindow(v);
                break;
            case R.id.large_iv_persone_list:
                //大班课人员名单
                personnelPopWindow(v);
                break;
            case R.id.land_iv_camera:
                if (getStartCourse())
                    return;
                if (isVideo && isAudio && !isLmstate) {
                    toastShort("当前未连麦状态,不可打开本地视频");
                    return;
                }
                if (mCamera.isSelected()) {
                    VideoOpen(mUerId);
                } else {
                    VideoClose(mUerId);
                }
                break;
            case R.id.land_iv_microphone:
                if (getStartCourse())
                    return;
                if (isAudio && isVideo && !isLmstate) {
                    toastShort("当前未连麦状态,不可打开本地麦克风");
                    return;
                }
                if (mIcrophone.isSelected()) {
                    AudioOpen(mUerId);
                } else {
                    AudioClose(mUerId);

                }

                break;
            case R.id.land_iv_raise_hand:
                if (getStartCourse())
                    return;
                if (!applyLmState) {
                    sendLmMessage(teachid, roomId, mUerId, nackname);
                    applyLmState = true;
                    iv_raise_hand.setBackground(getResources().getDrawable(R.drawable.icon_hand_disable));
                } else if (applyLmState) {
                    if (!isVideo || !isAudio) {
                        toastShort(R.string.apply_lm_agree_ing);
                        iv_raise_hand.setClickable(false);
                    } else {
                        toastShort(R.string.apply_lm_state_ing);
                        iv_raise_hand.setClickable(false);
                    }
                }
                break;
            case R.id.iv_live_navbar_unfold:
                Animation animationIn = AnimationUtils.loadAnimation(mContext, R.anim.gradually_hidden_in);
                Animation animationOut = AnimationUtils.loadAnimation(mContext, R.anim.gradually_accord_out);
                for (int i = 0; i < mVideoViewList.size(); i++) {
                    VideoView courVideo = mVideoViewList.get(i);
                    if (courVideo.getVisibility() == View.VISIBLE) {
                        courVideo.setVisibility(View.GONE);
                        courVideo.startAnimation(animationIn);
                        iv_student_live_navbar_unfold.setBackground(getResources().getDrawable(R.drawable.living_navbar_collapse_icon));
                    } else {
                        courVideo.setVisibility(View.VISIBLE);
                        courVideo.startAnimation(animationOut);
                        iv_student_live_navbar_unfold.setBackground(getResources().getDrawable(R.drawable.living_navbar_unfold_icon));
                    }
                }
                break;
            case R.id.bt_start_answer:
                //我的答案
                strCorrect = new StringBuffer();
                solution = answerAdapter.getSelectContent();
                if (solution.size() > 0) {
                    //排序
                    Collections.sort(solution);
                    for (int j = 0; j < solution.size(); j++) {

                        strCorrect.append(solution.get(j));
                    }

                    sendPutAnswer(roomId, nackname, strCorrect.toString(), mUerId, teachid);
                    bt_start_answer.setText("已提交答案");
                    fl_student_view.setVisibility(View.VISIBLE);

                } else {
                    toastShort("还没有选择答案");
                }
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
            case R.id.land_iv_tool_whiteboard:
                //白板
                toolOpen();
                break;
            case R.id.large_iv_notice:
                //公告
                roomNoticePopWindow(v);
                break;
            case R.id.rl_pop_room_nitice_main:
                //关闭公告
                if (noticePopWindow != null) {
                    noticePopWindow.dismiss();
                }
                break;
            case R.id.land_iv_raise_menu:
                ToolsAnimator();
                break;
        }

    }

    /**
     * 获取当前是否已经开始上课
     *
     * @return
     */
    private boolean getStartCourse() {
        if (!startCourse) {
            toastShort(R.string.current_room_no_classe);
            return true;
        }
        return false;
    }


    //本地相册选择图片回调
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
                        mPresenter.getImageUpload(requestFile, body);
                        Log.i(TAG_CLASS, "上传图片路径 : " + imagePath);
                    }
                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG_CLASS, "onBackPressed");
//        sendLeaveMessage(mRoomLiveHelp, roomId, mUerId, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG_CLASS, "onDestroy");
        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(wsbind(Integer.parseInt(roomId), Integer.parseInt(mUerId), seqid, 3));
            Constant.wsService.prepareShutdown();
        } else {
            sWebservice.sendRequest(wsbind(Integer.parseInt(roomId), Integer.parseInt(mUerId), seqid, 3));
            sWebservice.prepareShutdown();
        }
        if (stWsConnection != null) {
            unbindService(stWsConnection);
        }
        mLiveLm = 0;
        mSpeakStop = 0;
        mTrophyCount = 0;
        mWhiteBoard = false;
        mVideoClose = true;
        mAudeoClose = true;
        String update = getJoinRoomCustom(roomId, Integer.parseInt(mUerId),
                nackname, userimage, mUserRole, mLiveLm,
                mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
        if (!TextUtils.isEmpty(update)) {
            mRoomLiveHelp.updateUserInfo(update);
        }
        EventBus.getDefault().unregister(this);

        //退出直播
        if (mRoomLiveHelp != null) {
            mRoomLiveHelp.exitHelp();
            mRoomLiveHelp = null;
        }
        if (chatPop.isShow()) {
            chatPop.closePop();
        }

        if (mWebviewToolPopupWindow != null) {
            mWebviewToolPopupWindow.dismissPop();
        }

        if (mVideoViewList != null && mVideoViewList.size() > 0) {
            mVideoViewList.clear();
        }

        if (personelMapList != null && personelMapList.size() > 0) {
            personelMapList.clear();
        }
        if (customBeanList != null && customBeanList.size() > 0) {
            customBeanList.clear();
        }

        if (trophyMapList != null && trophyMapList.size() > 0) {
            trophyMapList.clear();
        }

        if (shareStudentPopu != null && shareStudentPopu.isShowing()) {
            shareStudentPopu.dismiss();
        }

        if (mVideoIjkPlayer != null) {
            mVideoIjkPlayer.stopPlayback();
        }

        AlarmTimeUtils.getInstance().cancelAlarmManager();
        unregisterReceiver(mTimeReceiver);
        mHandler.removeCallbacks(timerRunnable);
        PopLmDismiss();
        resultrPopWindowDismiss();
        answerPopWindowDismiss();
        startCourse = false;
        showSelfVideo = false;
        loadWebView = false;
        Constant.USER_ISROOM = false;
        Constant.popupType = false;
        Constant.exitRoom = true;
        UMShareAPI.get(this).release();
    }


    @Override
    public void showNetworkException() {
        super.showNetworkException();
    }

    @Override
    public void pingLineLost(String lineLost) {
        //丢包率
        tv_network_stauts_percentage.setText(lineLost.trim());

    }

    @Override
    public void pingLineDelay(String delay) {
        //网络延迟
        Log.e(TAG_CLASS, "网络延时 : " + delay);

        int intDelay = Integer.parseInt(delay.trim());
        if (intDelay < 70) {
            //网络优
            iv_network_stauts_excellent.setBackground(getResources().getDrawable(R.drawable.living_network_stauts_excellent_icon));
            tv_network_stauts_percentage.setTextColor(getResources().getColor(R.color.color_43EB73));
            tv_network_stauts_ms.setTextColor(getResources().getColor(R.color.color_43EB73));
            tv_network_stauts_op.setTextColor(getResources().getColor(R.color.color_43EB73));
            tv_network_stauts_op.setText(getResources().getString(R.string.network_stauts_optimal));
        } else if (intDelay > 70 && intDelay < 150) {
            //网络一般
            iv_network_stauts_excellent.setBackground(getResources().getDrawable(R.drawable.living_network_stauts_general_icon));
            tv_network_stauts_percentage.setTextColor(getResources().getColor(R.color.color_FFB500));
            tv_network_stauts_ms.setTextColor(getResources().getColor(R.color.color_FFB500));
            tv_network_stauts_op.setTextColor(getResources().getColor(R.color.color_FFB500));
            tv_network_stauts_op.setText(getResources().getString(R.string.network_stauts_good));
        } else if (intDelay > 150) {
            //网络差
            iv_network_stauts_excellent.setBackground(getResources().getDrawable(R.drawable.living_network_stauts_difference_icon));
            tv_network_stauts_percentage.setTextColor(getResources().getColor(R.color.color_FF3B30));
            tv_network_stauts_ms.setTextColor(getResources().getColor(R.color.color_FF3B30));
            tv_network_stauts_op.setTextColor(getResources().getColor(R.color.color_FF3B30));
            tv_network_stauts_op.setText(getResources().getString(R.string.network_stauts_poor));
        }

        tv_network_stauts_ms.setText(delay.trim() + "ms");

    }

    @Override
    public void pingHttpDis() {

    }

    /**
     * 学员分享
     */
    private void studentShare(SHARE_MEDIA share_media, boolean install) {

        if (share_media == null) {//share_media null 复制链接
            return;
        }
        if (!install) { //未安装客户端
            toastShort("你还没有安装客户端,请先安装。");
            return;
        }

        Log.e(TAG_CLASS, "分享的链接 : " + Constant.SHARE_CLASS_ROOM_URL + student_invite_code);
        new ShareUtil.Build(this).listener(umShareListener).url(Constant.SHARE_CLASS_ROOM_URL + student_invite_code)
                .title(title).description("开课时间 : " + timeStart)
                .thumb(Constant.SHARE_IMAGE_URL).shareMedia(share_media).build().share();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }


    @Override
    public void animend() {
        //奖杯
        isgiftend = true;
        rl_room_show_anim.removeAllViews();
        fireworks = null;
        obtain_trophy--;
        if (obtain_trophy > 0 && isgiftend) {
            isgiftend = false;
            fireworks = new FireworksView(this);
            fireworks.setAnimsopt(this);
            id_animation_cup.setVisibility(View.VISIBLE);
            ll_trophy_personel.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.width = mapHeight / 2;
            layoutParams.height = mapHeight / 2;
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            rl_room_show_anim.addView(fireworks, layoutParams);
            giftNumAnim.start(ll_animation_filling);
            return;
        }
        ll_trophy_personel.setVisibility(View.GONE);
        id_animation_cup.setVisibility(View.GONE);

    }

    @Override
    public void imageUploadSucess(BaseResponse<UploadImageBean> uploadBean) {
        //上传图片成功
        sendTextMessage(uploadBean.getData().getUrl(), Constant.SEND_MESSAGE_TYPE_IMAGE);
    }

    @Override
    public void getError() {
//        请求分辨率失败
        onBackPressed();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onGetSeiMessage(String msg) {

    }

    /**
     * 初始化webView
     */
    private void studentWebViewInit() {
        loadWebView = true;
        String inviteCode = SPTools.getInstance(this).getString(SPTools.KEY_LOGIN_INVITE_CODE, "");//邀请码
        WebSettings webSettings = student_view_web.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) student_view_web.getLayoutParams();
        layoutParams.width = mapWidth;
        if (mapWidth * 9 / 16 > mapHeight) {
            layoutParams.height = mapHeight;
        } else {
            layoutParams.height = mapWidth * 9 / 16;
        }
//        layoutParams.topMargin = rl_network_bar.getHeight();
        Log.e(TAG_CLASS, "屏幕高度 11: " + rl_network_bar.getHeight());
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        student_view_web.setLayoutParams(layoutParams);

        final int height = BaseTools.getWindowsHeight(this);
        Log.e(TAG_CLASS, "屏幕高度 : " + mapHeight);
        if (height > 540) {
            student_view_web.setInitialScale((int) (540f / height * 100));
        } else {
            student_view_web.setInitialScale((int) (height / 540f * 100));
        }

        student_view_web.loadUrl(Constant.DocUrl + "?inviteCode=" + inviteCode + "&courseId=" + roomId + "&role=3&userId=" + mUerId + "&appId=" + Constant.app_id);
        Log.e(TAG_CLASS, "WebUrl : " + Constant.DocUrl + "?inviteCode=" + "&courseId=" + roomId + "&role=3&userId=" + teachid + "&appId=" + Constant.app_id);
        student_view_web.setWebViewClient(studentWebViewClient);
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
     * 关闭答题结果界面
     */
    private void resultrPopWindowDismiss() {
        if (resultrPopWindow != null && resultrPopWindow.isShowing()) {
            resultrPopWindow.dismiss();
        }
        mHandler.removeCallbacks(timerRunnable);
    }

    /**
     * 关闭答题统计弹窗
     */
    private void answerPopWindowDismiss() {
        if (answerPopWindow != null && answerPopWindow.isShowing()) {
            answerPopWindow.dismiss();
        }
        mHandler.removeCallbacks(timerRunnable);
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
        iv_pop_window_back.setOnClickListener(popOnClickListener);
        teacherPersonnel = new TeacherPersonnelUtil(mContext, rv_pop_lm);
    }

    /**
     * 初始化答题
     */
    private void initAnswerView(int mOptionnum) {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.pop_answer_detail_list_land, null);
        answerPopWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        iv_pop_answer_window_back = popView.findViewById(R.id.iv_cup_pop_answer_back);
        rv_answer_list = popView.findViewById(R.id.rv_answer_list);
        bt_add_item_view = popView.findViewById(R.id.bt_add_item_view);
        bt_delete_item_view = popView.findViewById(R.id.bt_delete_item_view);
        bt_start_answer = popView.findViewById(R.id.bt_start_answer);
        fl_student_view = popView.findViewById(R.id.fl_student_view);
        ll_tips = popView.findViewById(R.id.ll_tips);
        tv_tips = popView.findViewById(R.id.tv_tips);
        ll_time = popView.findViewById(R.id.ll_answer_time);
        tv_answer_time = popView.findViewById(R.id.tv_answer_time);
        fl_student_view.setVisibility(View.GONE);
        bt_start_answer.setText("提交答案");
        bt_add_item_view.setVisibility(View.GONE);
        bt_delete_item_view.setVisibility(View.GONE);
        iv_pop_answer_window_back.setVisibility(View.GONE);
        ll_tips.setVisibility(View.GONE);
        tv_tips.setVisibility(View.VISIBLE);
        ll_time.setVisibility(View.VISIBLE);

        bt_start_answer.setOnClickListener(this);
        bt_delete_item_view.setOnClickListener(this);
        bt_add_item_view.setOnClickListener(this);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_answer_list.setLayoutManager(lm);
        answerAdapter = new ProfessionRecyclerViewAdapter(this, mOptionnum);
        rv_answer_list.setAdapter(answerAdapter);

        //拦截点击事件
        fl_student_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


    }

    /**
     * 初始化答题结果
     */
    private void initResultrView() {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.pop_result_land, null);
        resultrPopWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        tv_me_answer = popView.findViewById(R.id.tv_me_answer);
        tv_result_time = popView.findViewById(R.id.tv_result_time);
        tv_correct_answer = popView.findViewById(R.id.tv_correct_answer);

    }


    /**
     * 课程公告
     *
     * @param view
     */
    private void roomNoticePopWindow(View view) {
        View noticePop = LayoutInflater.from(mContext).inflate(R.layout.pop_room_notice, null);
        noticePopWindow = new PopupWindow(noticePop, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        RelativeLayout ll_notice_layout = noticePop.findViewById(R.id.ll_notice_layout);
        tv_notice_pop = noticePop.findViewById(R.id.tv_notice_pop);
        rl_pop_room_nitice_main = noticePop.findViewById(R.id.rl_pop_room_nitice_main);
        rl_pop_room_nitice_main.setOnClickListener(this);
        if (TextUtils.isEmpty(mNotice)) {
            tv_notice_pop.setText(getResources().getString(R.string.pop_notice_room));
        } else {
            tv_notice_pop.setText(mNotice);
            large_mbtv_notice_num.setVisibility(View.GONE);
        }
        noticePopWindow.setAnimationStyle(R.style.AnimBottomIn);
        noticePopWindow.setBackgroundDrawable(new BitmapDrawable());
        noticePopWindow.setFocusable(true);
        noticePopWindow.setOutsideTouchable(true);
        noticePopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);


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
     * 结果
     *
     * @param view
     */
    private void resultrPopWindow(View view) {
        resultrPopWindow.setAnimationStyle(R.style.AnimBottomIn);
        resultrPopWindow.setBackgroundDrawable(new BitmapDrawable());
        resultrPopWindow.setFocusable(true);
        resultrPopWindow.setOutsideTouchable(true);
        resultrPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    /**
     * webview
     * 获取当前页码
     */
    private void webviewPage(String tool) {
        if (student_view_web != null) {
            String js = "javascript:" + tool;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                student_view_web.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        String page = value.replace("\"", "");
                        Log.e(TAG_CLASS + " webViewPage : ", value + "    " + page);
                        if (startCourse) {
                            if (!page.equals("0/0") && !page.equals("null")) {
                                page_textview_number.setVisibility(View.VISIBLE);
                            }
                        } else {
                            page_textview_number.setVisibility(View.GONE);
                        }
                        page_textview_number.setText(page);
                    }
                });
            } else {
                student_view_web.loadUrl(js);
            }
        }
    }

    /**
     * WebViewClient
     * 加载webView
     */
    private class StudentWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.e(TAG_CLASS, "onLoadResource " + url);
            webviewPage("getCurrentTotalPage()");
        }
    }

    /**
     * 关闭人员列表弹窗
     */
    private class PopOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            PopLmDismiss();
        }
    }

    /**
     * 判断是否上课
     */
    private void whetherCourseStart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (startCourse) {
                    Log.e(TAG_CLASS, "已经上课了----");
                } else {
                    Log.e(TAG_CLASS, "没有收到上课的信令-----");
                    notStartLive(roomId, mUerId, "3");

                }

            }
        }, 3000);
    }

    /**
     * 收到上课后逻辑处理
     */
    public void startLiveInit() {
        Log.e(TAG_CLASS, "startLiveInit " + " mUserInfo " + mUserInfo);
        startCourse = true;
//        mTeacherImageView.setVisibility(View.GONE);
        if (LIVE_SMALL_CLASS.equals(mroomType)) {
            iv_raise_hand.setVisibility(View.VISIBLE);
            ll_video_tool_zoom.setVisibility(View.VISIBLE);
            fl_personnel_live.setVisibility(View.VISIBLE);
        }

        Constant.USER_ISROOM = true;
        if (!loadWebView) {
            studentWebViewInit();
        }
        if (LIVE_SMALL_CLASS.equals(mroomType)) {
            mRoomLiveHelp.controlAllRemoteAudioStreams(false);
            mRoomLiveHelp.contorlAllRemoteVideoStreams(false);
        }

        if (mUserInfo != null) {
            showRemoteView(teachid, mUserInfo);
        }
        ll_no_class_background.setVisibility(View.GONE);


    }

    /**
     * 显示视频
     *
     * @param
     */
    private void showRemoteView(String userId, EnterUserInfo userInfo) {
        Log.e(TAG_CLASS, "显示视频 " + userId);
        lmUserIdList.add(userId);
        if (LIVE_LARGE_CLASS.equals(mroomType)) {
            if (userId.equals(teachid)) {
                Log.e(TAG_CLASS, "老师进入拉。。。。。。。。。。 " + userId);
                videoViewLocationSize(userId, userInfo);
            }
        } else if (LIVE_SMALL_CLASS.equals(mroomType)) {
            videoViewLocationSize(userId, userInfo);
        }

//
//
//        //执行人员增加动画
//        if (curModel == MODEL_VIDEO) {
//            playVideoModeAddAnimation();
//            Log.e("TAG", "当前模式为视频模式");
//        }
//
//        if (curModel == MODEL_NORMAL) {
//            playNormalModeAddAnimation();
//            Log.e("TAG", "当前模式为普通模式");
//        }


    }

    /**
     * 设置视频窗口的位置和大小
     */
    private void videoViewLocationSize(final String userId, final EnterUserInfo userInfo) {

        //判断是否是学生本人

        VideoView videoView = videoViewLayout(userId, mapWidth);
        if (userId.equals(teachid)) {

            Log.e("TAG", "老师ID == " + teachid);

            //打开主播视频
            addViewWithMode(videoView, curModel);

            rl_teacher_top.setVisibility(View.VISIBLE);
            setTextPlaceByMode(rl_teacher_top);
            if (LIVE_LARGE_CLASS.equals(mroomType)) {
                Log.e(TAG_CLASS, " 拉流：" + mPullRtmp);
                mVideoIjkPlayer = new IjkVideoView(this);
                student_head_video_land.addView(mVideoIjkPlayer);
                playerManager = new PlayerManager(this, mVideoIjkPlayer);
                playerManager.setPlayerStateListener(this);
                playerManager.play(mPullRtmp);
                playerManager.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
                playerManager.start();
            } else {
                Log.e(TAG_CLASS, "打开主播视频 " + userId);
                mRoomLiveHelp.openRemoteVideo(student_head_video_land, userInfo, true);


            }

        } else {
            Log.e(TAG_CLASS, "其它端视频 " + userId);
//            addViewWithMode(videoViewLayout(userId, mapWidth), curModel);
            //先动画再添加
            if (curModel == MODEL_VIDEO) {
                addViewWithMode(videoView, curModel);
//                startAddAnimation(null);

            }

            if (curModel == MODEL_NORMAL) {
                addViewWithMode(videoView, curModel);
            }

            if (userId.equals(mUerId)) {
                //打开本地视频
                if (!showSelfVideo) {
                    mRoomLiveHelp.openLocalVideo(student_head_video_land, false, Constants.CLIENT_ROLE_BROADCASTER, resolution);
                    rl_teacher_top.setVisibility(View.VISIBLE);
                    setTextPlaceByMode(rl_teacher_top);
                    tv_teacher.setVisibility(View.INVISIBLE);

                    showSelfVideo = true;
                }
            } else {
                //打开远端视频
                mRoomLiveHelp.openIdRemoteVideo(student_head_video_land, Long.valueOf(userId), false);
            }
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

        }


    }

    //执行添加人员动画playNormalModeAddAnimation
    private void playNormalModeAddAnimation() {

        int eachWidth = parentWidth / 7;
        int eachHeight = eachWidth * 9 / 16;

        ArrayList<ViewInfo> viewInfoList = getViewInfoList(rl_student_video_view.getChildCount());
        if (rl_student_video_view.getChildCount() > 1) {
            for (int i = 0; i < rl_student_video_view.getChildCount(); i++) {
                VideoView videoWindow = (VideoView) rl_student_video_view.getChildAt(i);
                int width = viewInfoList.get(i).getWidth();
                int height = viewInfoList.get(i).getHeight();
                float translationX = viewInfoList.get(i).getTranslationX();
                float translationY = viewInfoList.get(i).getTranslationY();
                videoWindow.getPropertyAnimator(1.0F * eachWidth / parentWidth, 1.0F * eachHeight / parentHeight, i * parentWidth / 7, 0)
                        .setDuration(1000)
                        .start();
            }
            rl_student_video_view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rl_student_video_view.getChildAt(rl_student_video_view.getChildCount() - 1)
                            .animate()
                            .alpha(1)
                            .setDuration(500).start();
                }
            }, 1500);


        } else {
            rl_student_video_view.getChildAt(0).animate().alpha(1).setDuration(500).start();
        }

    }

    //执行添加人员动画
    private void playVideoModeAddAnimation() {
        ArrayList<ViewInfo> viewInfoList = getViewInfoList(rl_student_video_view.getChildCount());
        if (rl_student_video_view.getChildCount() > 1) {
            for (int i = 0; i < rl_student_video_view.getChildCount(); i++) {
                VideoView videoWindow = (VideoView) rl_student_video_view.getChildAt(i);
                int width = viewInfoList.get(i).getWidth();
                int height = viewInfoList.get(i).getHeight();
                float translationX = viewInfoList.get(i).getTranslationX();
                float translationY = viewInfoList.get(i).getTranslationY();
                videoWindow.getPropertyAnimator(1.0F * width / parentWidth, 1.0F * height / parentHeight, translationX, translationY)
                        .setDuration(1000)
                        .start();
            }
            rl_student_video_view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rl_student_video_view.getChildAt(rl_student_video_view.getChildCount() - 1)
                            .animate()
                            .alpha(1)
                            .setDuration(500).start();
                }
            }, 1500);


        } else {
            rl_student_video_view.getChildAt(0).animate().alpha(1).setDuration(500).start();
        }

    }

    /**
     * 添加窗口视频窗口
     */
    private VideoView videoViewLayout(String userId, int mapWidth) {
        videoListener();
        VideoView mVideoView = new VideoView(this, userId, videoOnClickListener);
        student_head_video_land = mVideoView.findViewById(R.id.teacher_head_video_land);
        if (userId.equals(teachid)) {
            rl_teacher_top = mVideoView.findViewById(R.id.rl_teacher_top);
        } else if (userId.equals(mUerId)) {
            iv_live_image_close = mVideoView.findViewById(R.id.land_iv_live_microphone_close);
            rl_microphone_open_two = mVideoView.findViewById(R.id.land_rl_live_microphone_two);
            rl_microphone_open_one = mVideoView.findViewById(R.id.land_rl_live_microphone_one);
            rl_live_video_name = mVideoView.findViewById(R.id.rl_live_video_name);
            tv_user_name = mVideoView.findViewById(R.id.tv_user_name);
            tv_user_one = mVideoView.findViewById(R.id.tv_user_name_one);
            rl_teacher_top = mVideoView.findViewById(R.id.rl_teacher_top);
            tv_teacher = mVideoView.findViewById(R.id.tv_teacher);

        } else {
            rl_live_video_name = mVideoView.findViewById(R.id.rl_live_video_name);
            tv_user_name = mVideoView.findViewById(R.id.tv_user_name);
            tv_user_one = mVideoView.findViewById(R.id.tv_user_name_one);
        }
        mVideoView.setFlagUserId(userId);
        mVideoViewList.add(mVideoView);

        return mVideoView;
    }

    private void addViewWithMode(VideoView videoView, int mode) {

        if (mode == MODEL_NORMAL) {
            if (!videoView.getFlagUserId().equals(mUerId)) {
                int childCount = rl_student_video_view.getChildCount();
                int x = childCount % 4 * singleWidth;
                int y = childCount >= 4 ? singleHeight : 0;
                AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(singleWidth, singleHeight, x, y);
                videoView.setLayoutParams(layoutParams);
                if (videoView.getFlagUserId().equals(teachid)) {
                    rl_student_video_view.addView(videoView, 0);
                } else {
                    rl_student_video_view.addView(videoView, childCount - 1);
                }

            } else {
                int childCount = rl_student_video_view.getChildCount();
                for (int i = 0; i < mVideoViewList.size() - 1; i++) {
                    VideoView childView = mVideoViewList.get(i);
                    if (i == 1 || i == 2) {//从第二个开始向右移动

                        childView.getValueAnimator(singleWidth, singleHeight, childView.getX() + singleWidth, 0).start();
//                        childView.animate().translationX(1.0F * eachWidth).setDuration(DURATION).start();
                    } else if (i == 3) {
                        childView.getValueAnimator(singleWidth, singleHeight, 0, singleHeight).start();
                    } else if (i > 3) {
                        childView.getValueAnimator(singleWidth, singleHeight, childView.getX() + singleWidth, singleHeight).start();
                    }
                }
                AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(singleWidth, singleHeight, singleWidth, 0);
                videoView.setLayoutParams(layoutParams);
                rl_student_video_view.addView(videoView, 1);

            }
        }
        if (mode == MODEL_VIDEO) {
            if (!videoView.getFlagUserId().equals(mUerId)) {

                int childCount = rl_student_video_view.getChildCount();
                if (childCount == 0) {
                    AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(parentWidth, parentHeight, 0, 0);
                    videoView.setLayoutParams(layoutParams);
                    rl_student_video_view.addView(videoView);
                } else {

                    //改变添加前窗口的大小和位置
                    ArrayList<ViewInfo> viewInfoList = getViewInfoList(childCount + 1);
                    for (int i = 0; i < childCount; i++) {
                        ViewInfo viewInfo = viewInfoList.get(i);
                        int width = viewInfo.getWidth();
                        int height = viewInfo.getHeight();
                        int x = viewInfo.getX();
                        int y = viewInfo.getY();
                        VideoView child = (VideoView) rl_student_video_view.getChildAt(i);
                        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(width, height, x, y);
                        child.setLayoutParams(layoutParams);
                    }

                    ViewInfo viewInfo = getViewInfo(childCount);
                    AbsoluteLayout.LayoutParams layoutParams =
                            new AbsoluteLayout.LayoutParams(viewInfo.getWidth(), viewInfo.getHeight(), viewInfo.getX(), viewInfo.getY());
                    rl_student_video_view.addView(videoView, layoutParams);

                }

            } else {

                int childCount = rl_student_video_view.getChildCount();
                if (childCount == 0) {
                    AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(parentWidth, parentHeight, 0, 0);
                    videoView.setLayoutParams(layoutParams);
                    rl_student_video_view.addView(videoView);
                } else {

                    //改变添加前窗口的大小和位置
                    ArrayList<ViewInfo> viewInfoList = getViewInfoList(childCount + 1);
                    for (int i = 0; i < childCount; i++) {
                        ViewInfo viewInfo = viewInfoList.get(i);
                        if (i >= 1) {
                            viewInfo = viewInfoList.get(i + 1);
                        }
                        int width = viewInfo.getWidth();
                        int height = viewInfo.getHeight();
                        int x = viewInfo.getX();
                        int y = viewInfo.getY();

                        VideoView child = (VideoView) rl_student_video_view.getChildAt(i);
                        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(width, height, x, y);
                        child.setLayoutParams(layoutParams);
                    }

                    ViewInfo viewInfo = viewInfoList.get(1);
                    AbsoluteLayout.LayoutParams layoutParams =
                            new AbsoluteLayout.LayoutParams(viewInfo.getWidth(), viewInfo.getHeight(), viewInfo.getX(), viewInfo.getY());
                    rl_student_video_view.addView(videoView, 1, layoutParams);

                }

//                int childCount = rl_student_video_view.getChildCount();
//                ArrayList<ViewInfo> viewInfoList = getViewInfoList(childCount + 1);
//                ViewInfo viewInfo = viewInfoList.get(1);
//                int width = viewInfo.getWidth();
//                int height = viewInfo.getHeight();
//                int x = viewInfo.getX();
//                int y = viewInfo.getY();
//                AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(width, height, x, y);
//                videoView.setLayoutParams(layoutParams);
//                rl_student_video_view.addView(videoView, 1);
            }

        }
    }

    private long DURATION = 500L;
    private int num;


    private void setTextPlaceByMode(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (curModel == MODEL_VIDEO) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }
        if (curModel == MODEL_NORMAL) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        view.requestLayout();

    }


    //获取指定索引的窗口的信息
    private ViewInfo getViewInfo(int index) {
        ViewInfo viewInfo = new ViewInfo();
        switch (index) {
            case 0:
                viewInfo = new ViewInfo(parentWidth, parentHeight, 0, 0);
                break;
            case 1:
                viewInfo = new ViewInfo(parentWidth / 2, parentHeight, parentWidth / 2, 0);
                break;
            case 2:
                viewInfo = new ViewInfo(parentWidth / 2, parentHeight / 2, parentWidth / 2, parentHeight / 2);
                break;
            case 3:
                viewInfo = new ViewInfo(parentWidth / 2, parentHeight / 2, parentWidth / 2, parentHeight / 2);
                break;
            case 4:
                viewInfo = new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth * 2 / 3, parentHeight / 2);
                break;
            case 5:
                viewInfo = new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth * 2 / 3, parentHeight / 2);
                break;
            case 6:
                viewInfo = new ViewInfo(parentWidth / 4, parentHeight / 2, parentWidth * 3 / 4, parentHeight / 2);
                break;
        }

        return viewInfo;
    }

    //根据当前直播间人数，获取每个视频窗口信息的集合
    private ArrayList<ViewInfo> getViewInfoList(int count) {

        ArrayList<ViewInfo> listViewInfo = new ArrayList<>();
        switch (count) {
            case 1:
                listViewInfo.add(new ViewInfo(parentWidth, parentHeight, 0, 0));
                break;
            case 2:
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight, 0, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight, parentWidth / 2, 0));
                break;
            case 3:
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight, 0, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight / 2, parentWidth / 2, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight / 2, parentWidth / 2, parentHeight / 2));
                break;
            case 4:
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight / 2, 0, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight / 2, parentWidth / 2, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight / 2, 0, parentHeight / 2));
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight / 2, parentWidth / 2, parentHeight / 2));
                break;
            case 5:
                listViewInfo.add(new ViewInfo(parentWidth * 2 / 3, parentHeight / 2, 0, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth * 2 / 3, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, 0, parentHeight / 2));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth / 3, parentHeight / 2));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth * 2 / 3, parentHeight / 2));
                break;
            case 6:
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, 0, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth / 3, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth * 2 / 3, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, 0, parentHeight / 2));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth / 3, parentHeight / 2));
                listViewInfo.add(new ViewInfo(parentWidth / 3, parentHeight / 2, parentWidth * 2 / 3, parentHeight / 2));

                break;
            case 7:
                listViewInfo.add(new ViewInfo(parentWidth / 2, parentHeight / 2, 0, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 4, parentHeight / 2, parentWidth / 2, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 4, parentHeight / 2, parentWidth * 3 / 4, 0));
                listViewInfo.add(new ViewInfo(parentWidth / 4, parentHeight / 2, 0, parentHeight / 2));
                listViewInfo.add(new ViewInfo(parentWidth / 4, parentHeight / 2, parentWidth / 4, parentHeight / 2));
                listViewInfo.add(new ViewInfo(parentWidth / 4, parentHeight / 2, parentWidth / 2, parentHeight / 2));
                listViewInfo.add(new ViewInfo(parentWidth / 4, parentHeight / 2, parentWidth * 3 / 4, parentHeight / 2));

                break;
        }

        return listViewInfo;

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
//                currentViewLive.removeView(videoView);
                rl_student_video_view.removeView(mVideoViewList.get(i));
                mVideoViewList.remove(i);
                onUserSub(i);
            }
        }

        for (int i = 0; i < lmUserIdList.size(); i++) {
            if (String.valueOf(userId).equals(lmUserIdList.get(i))) {
                lmUserIdList.remove(i);
            }
        }

    }

    private void onUserSub(int removeIndex) {

        if (curModel == MODEL_NORMAL) {
//            int childCount = rl_student_video_view.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                VideoView childView = (VideoView) rl_student_video_view.getChildAt(i);
//                childView.getValueAnimator(singleWidth, singleHeight, i * singleWidth, 0).start();
//            }


            for (int i = 0; i < mVideoViewList.size(); i++) {
                int x = i % 4 * singleWidth;
                int y = i >= 4 ? singleHeight : 0;
                mVideoViewList.get(i).getValueAnimator(singleWidth, singleHeight, x, y).start();
            }

        }

        if (curModel == MODEL_VIDEO) {
            int childCount = rl_student_video_view.getChildCount();
            ArrayList<ViewInfo> viewInfoList = getViewInfoList(childCount);
            for (int i = 0; i < childCount; i++) {
                VideoView videoView = (VideoView) rl_student_video_view.getChildAt(i);
                int width = viewInfoList.get(i).getWidth();
                int height = viewInfoList.get(i).getHeight();
                int x = viewInfoList.get(i).getX();
                int y = viewInfoList.get(i).getY();
                videoView.getValueAnimator(width, height, x, y).start();
            }
        }


    }

    /**
     * 视频窗口点击监听
     */
    private void videoListener() {
        videoOnClickListener = new VideoView.setOnClickListener() {
            @Override
            public void OnClickListener(String id) {
                Log.e("===", "onClick = " + id);
            }
        };
    }

    /**
     * 发送进入房间消息
     */
    private void SendJoinRoomMessage() {
        //进入房间发送消息
        String custom = getJoinRoomCustom(roomId, Integer.parseInt(mUerId), nackname,
                userimage, mUserRole, mLiveLm,
                mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);

        CustomBean sCustomBean = stGson.fromJson(custom, CustomBean.class);

        joinRoomPersonel(sCustomBean);
    }

    /**
     * 发送消息
     *
     * @param message
     */
    private void sendTextMessage(String message, int mesageType) {
        Long ct = System.currentTimeMillis();
        String seqid = "binding_" + String.valueOf(ct);

        Gson gson = new Gson();
        SendTextMessageBean stMessAge = new SendTextMessageBean();
        SendTextMessageBean.DataBean stDb = new SendTextMessageBean.DataBean();
        stMessAge.setMessageType(Constant.BARRAGE_REQ);
        stDb.setMessage(message);
        stDb.setNickName(nackname);
        stDb.setAvatar(userimage);
        stDb.setUserId(mUerId);
        stDb.setRoomId(roomId);
        stDb.setType(mesageType);
        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        msgList.add(stMessAge);
        chatPop.showChatList(msgList);
        Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(roomId), Integer.parseInt(mUerId), 1, ss, seqid, 0, true));

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
     * 摄像头开启
     */
    private void VideoOpen(String userid) {
        mCamera.setSelected(false);
        mRoomLiveHelp.controlLocalVideo(false);
        isVideo = false;
        videoAudioSpeakType();
    }

    /**
     * 摄像头关闭
     */
    private void VideoClose(String userid) {
        mCamera.setSelected(true);
        mRoomLiveHelp.controlLocalVideo(true);
        isVideo = true;
        videoAudioSpeakType();
    }

    /**
     * 麦克风开启
     */
    private void AudioOpen(String userid) {
        mIcrophone.setSelected(false);
        mRoomLiveHelp.controlLocalAudio(false);
        isAudio = false;
        videoAudioSpeakType();

    }

    /**
     * 麦克风关闭
     */
    private void AudioClose(String userid) {
        mIcrophone.setSelected(true);
        mRoomLiveHelp.controlLocalAudio(true);
        isAudio = true;
        videoAudioSpeakType();
    }

    /**
     * 断开连麦直接关闭麦克风和摄像头
     */
    private void AudioVideoClose() {
        isLmstate = false;
        mIcrophone.setSelected(true);
        mRoomLiveHelp.controlLocalAudio(true);
        isAudio = true;
        mCamera.setSelected(true);
        mRoomLiveHelp.controlLocalVideo(true);
        isVideo = true;
        videoAudioSpeakType();

    }

    private void videoAudioSpeakType() {
        if (!isLmstate) {
            return;
        }
        if (isVideo) {
            rl_microphone_open_two.setVisibility(View.GONE);
            mVideoClose = true;
            localVideoStype(mUerId, View.VISIBLE, isVideo);
        } else {
            rl_microphone_open_two.setVisibility(View.GONE);
            mVideoClose = false;
            localVideoStype(mUerId, View.GONE, isVideo);
        }

        if (isAudio) {
            iv_live_image_close.setVisibility(View.GONE);
            mAudeoClose = true;
            localAudioStype(mUerId, View.VISIBLE, isAudio);


        } else {
            iv_live_image_close.setVisibility(View.GONE);
            mAudeoClose = false;
            localAudioStype(mUerId, View.GONE, isAudio);
        }
    }

    //本地音频状态修改
    private void localAudioStype(String mUerId, int visible, boolean isAudio) {
        for (int i = 0; i < mVideoViewList.size(); i++) {
            if (mVideoViewList.get(i).getFlagUserId().equals(mUerId)) {
                CurModelTools(i);
                mVideoViewList.get(i).getLive_stauts_phone().setVisibility(visible);
            }
        }

        for (int i = 0; i < customBeanList.size(); i++) {
            if (String.valueOf(customBeanList.get(i).getUserId()).equals(mUerId)) {
                customBeanList.get(i).setMicClosed(isAudio);
            }
        }
        String upate = getJoinRoomCustom(roomId, Integer.parseInt(mUerId),
                nackname, userimage, mUserRole,
                mLiveLm, mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
        mRoomLiveHelp.updateUserInfo(upate);
    }

    //本地视频状态修改
    private void localVideoStype(String mUerId, int visible, boolean isVideo) {
        for (int i = 0; i < mVideoViewList.size(); i++) {
            if (mVideoViewList.get(i).getFlagUserId().equals(mUerId)) {
                CurModelTools(i);
                mVideoViewList.get(i).getLive_stauts_camera().setVisibility(visible);
                mVideoViewList.get(i).getLand_rl_live_microphone_one().setVisibility(visible);

            }
        }
        for (int i = 0; i < customBeanList.size(); i++) {
            if (String.valueOf(customBeanList.get(i).getUserId()).equals(mUerId)) {
                customBeanList.get(i).setCameraClosed(isVideo);
            }
        }

        String upate = getJoinRoomCustom(roomId, Integer.parseInt(mUerId),
                nackname, userimage, mUserRole,
                mLiveLm, mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
        mRoomLiveHelp.updateUserInfo(upate);
    }

    //不同模式布局状态图标显示方位
    private void CurModelTools(int i) {
        if (curModel == MODEL_NORMAL) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mVideoViewList.get(i).getLl_video_tool().setLayoutParams(layoutParams);

            if (mVideoViewList.get(i).getLand_rl_live_microphone_one().getVisibility() == View.VISIBLE) {
                mVideoViewList.get(i).getVideo_image_view().setBackground(getResources().getDrawable(R.drawable.icon_camera_close2));
                mVideoViewList.get(i).getTv_video_close().setVisibility(View.GONE);
            }

        } else if (curModel == MODEL_VIDEO) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            mVideoViewList.get(i).getLl_video_tool().setLayoutParams(layoutParams);

            if (mVideoViewList.get(i).getLand_rl_live_microphone_one().getVisibility() == View.VISIBLE) {
                mVideoViewList.get(i).getVideo_image_view().setBackground(getResources().getDrawable(R.drawable.icon_video_close_big));
                mVideoViewList.get(i).getTv_video_close().setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 数字放大动画
     */
    public class NumAnim {
        private Animator lastAnimator = null;

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
    }

    /**
     * 更新奖杯数量
     *
     * @param num
     */
    public void updateGiftNum(String userId, int num) {

        if (userId.equals(mUerId)) {
            String update = getJoinRoomCustom(roomId, Integer.parseInt(mUerId), nackname, userimage,
                    mUserRole, mLiveLm, mSpeakStop, num, mWhiteBoard, mVideoClose, mAudeoClose);
            mRoomLiveHelp.updateUserInfo(update);
        }

        Map<Integer, CustomBean> cupMap = personelMapList;
        Set<Map.Entry<Integer, CustomBean>> entrySet = cupMap.entrySet();
        Iterator<Map.Entry<Integer, CustomBean>> mapIt = entrySet.iterator();
        while (mapIt.hasNext()) {
            Map.Entry<Integer, CustomBean> me = mapIt.next();
            if (String.valueOf(me.getKey()).equals(userId)) {
                me.getValue().setTrophyCount(num);
            }
        }

        teacherPersonnel.initViewStudentPersonnel(mContext, personelMapList, mroomType);
    }

    /**
     * 有人员加入房间
     *
     * @param customBean
     */
    public void joinRoomPersonel(CustomBean customBean) {
        //判断人员是否存在
        Iterator<Integer> leaveKey = personelMapList.keySet().iterator();
        int joinId = customBean.getUserId();
        while (leaveKey.hasNext()) {
            int key = leaveKey.next();
            if (joinId == key) {
                return;
            }
        }

        Log.e(TAG_CLASS, "有人加入房间  " + customBean);

        customBeanList.add(customBean);
        if (customBeanList.size() > 7){
            onBackPressed();
            toastShort(R.string.msg_operation_number_max);
        }

        if (String.valueOf(customBean.getUserId()).equals(teachid)) {
            customBeanList.add(0, customBean);
            customBeanList.remove(customBeanList.size() - 1);
            personelMapList.clear();
        }

        for (int i = 0; i < customBeanList.size(); i++) {
            personelMapList.put(customBeanList.get(i).getUserId(), customBeanList.get(i));
        }

        //奖杯处理
        if (!String.valueOf(customBean.getUserId()).equals(teachid)) {
//            if (customBean.getTrophyCount() != 0){
//                trophyMapList.put(customBean.getUserId() , customBean.getTrophyCount());
//            }else {
//                trophyMapList.put(customBean.getUserId() , 0);
//            }
            trophyMapList.put(customBean.getUserId(), customBean.getTrophyCount());
        }

        //禁言
        if (customBean.getStop() == 1) {
            isBanned = true;
        }
        startLiveCustom(customBean);

        //人员连表展示
        teacherPersonnel.initViewStudentPersonnel(mContext, personelMapList, mroomType);

    }

    //开课后视频窗口状态变化
    private void startLiveCustom(CustomBean customBean) {

        if (startCourse) {
            //连麦处理
            if (customBean.getLm() != 0) {
                if (customBean.getLm() == 1) {
                    if (customBean.getUserId() != Integer.parseInt(teachid)) {
                        showRemoteView(String.valueOf(customBean.getUserId()), mUserInfo);
                    }
                }
            }

            //摄像头开启和关闭
            if (customBean.isCameraClosed()) {
                //关闭
                if (customBean.getLm() == 1) {
                    for (int i = 0; i < mVideoViewList.size(); i++) {
                        if (mVideoViewList.get(i).getFlagUserId().equals(String.valueOf(customBean.getUserId()))) {
                            CurModelTools(i);
                            mVideoViewList.get(i).getLive_stauts_camera().setVisibility(View.VISIBLE);
                            mVideoViewList.get(i).getLand_rl_live_microphone_one().setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            //麦克风关闭和开启
            if (customBean.isMicClosed()) {
                if (customBean.getLm() == 1) {
                    for (int i = 0; i < mVideoViewList.size(); i++) {
                        if (mVideoViewList.get(i).getFlagUserId().equals(String.valueOf(customBean.getUserId()))) {
                            CurModelTools(i);
                            mVideoViewList.get(i).getLive_stauts_phone().setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

        }
        //白板授权
        if (customBean.isWhiteBoardAccess()) {
            for (int i = 0; i < mVideoViewList.size(); i++) {
                if (mVideoViewList.get(i).getFlagUserId().equals(String.valueOf(customBean.getUserId()))) {
                    CurModelTools(i);
                    mVideoViewList.get(i).getLive_stauts_authorization().setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 学员离开房间
     */
    private void leaveRoomPersonel(Long userId) {
        String mUserId = String.valueOf(userId);
        Map<Integer, CustomBean> cupMap = personelMapList;
        Set<Map.Entry<Integer, CustomBean>> entrySet = cupMap.entrySet();
        Iterator<Map.Entry<Integer, CustomBean>> mapIt = entrySet.iterator();
        while (mapIt.hasNext()) {
            Map.Entry<Integer, CustomBean> me = mapIt.next();
            if (me.getKey() == Integer.parseInt(mUserId)) {
                mapIt.remove();
            }
        }

        if (customBeanList != null && customBeanList.size() > 0) {
            for (int i = 0; i < customBeanList.size(); i++) {
                if (Integer.parseInt(mUserId) == customBeanList.get(i).getUserId()) {
                    customBeanList.remove(i);
                }
            }
        }

        //人员列表表展示
        teacherPersonnel.initViewStudentPersonnel(mContext, personelMapList, mroomType);


    }

    /**
     * 老师退出房间
     *
     * @param userId
     */
    private void teacherEnd(String userId) {
        Log.e(TAG_CLASS, "老师退出房间 " + userId);
        releaseLiveView(Long.valueOf(teachid));
        for (int i = 0; i < customBeanList.size(); i++) {
            if (customBeanList.get(i).getUserId() != Integer.parseInt(teachid)) {
                releaseLiveView(customBeanList.get(i).getUserId());
            }
        }

        leaveRoomPersonel(Long.valueOf(userId));
        startCourse = false;
        mWhiteBoard = false;
        if (isLmstate || applyLmState) {
            AudioVideoClose();
            applyLmState = false;
            showSelfVideo = false;
            isLmstate = false;
            iv_raise_hand.setBackground(getResources().getDrawable(R.drawable.icon_hand_normal));
            mLiveLm = 0;
            mVideoClose = true;
            mAudeoClose = true;
            String update = getJoinRoomCustom(roomId, Integer.parseInt(mUerId), nackname,
                    userimage, mUserRole, mLiveLm, mSpeakStop, mTrophyCount, mWhiteBoard, mVideoClose, mAudeoClose);
            mRoomLiveHelp.updateUserInfo(update);

        }
        mRoomLiveHelp.controlAllRemoteAudioStreams(true);
        mRoomLiveHelp.contorlAllRemoteVideoStreams(true);

        land_iv_raise_menu.setBackground(getResources().getDrawable(R.drawable.icon_menu_normal));
        land_iv_tool_whiteboard.setVisibility(View.GONE);
        land_iv_raise_menu.setVisibility(View.GONE);
        iv_raise_hand.setVisibility(View.GONE);
        ll_no_class_background.setVisibility(View.VISIBLE);

        witeTool();
        for (int i = 0; i < customBeanList.size(); i++) {
            if (customBeanList.get(i).isWhiteBoardAccess()) {
                whiteboardStype(String.valueOf(customBeanList.get(i).getUserId()), mWhiteBoard);
            }
        }

        if (LIVE_SMALL_CLASS.equals(mroomType)) {
            ll_video_tool_zoom.setVisibility(View.GONE);
        }
        if (animation_tool) {
            ToolsAnimator();
        }
    }

    //更新人员连表白板权限
    private void whiteboardStype(String userId, boolean whiteBoard) {
        Map<Integer, CustomBean> cupMap = personelMapList;
        Set<Map.Entry<Integer, CustomBean>> entrySet = cupMap.entrySet();
        Iterator<Map.Entry<Integer, CustomBean>> mapIt = entrySet.iterator();
        while (mapIt.hasNext()) {
            Map.Entry<Integer, CustomBean> me = mapIt.next();
            if (me.getKey() == Integer.parseInt(userId)) {
                me.getValue().setWhiteBoardAccess(whiteBoard);
            }
        }
        //授权后更新人员连表
        teacherPersonnel.initViewStudentPersonnel(mContext, personelMapList, mroomType);
    }

    /**
     * 显示获得奖杯数量
     *
     * @param tyb
     */
    private void trophyNumChang(TrophyAwardBean tyb) {
        if (TextUtils.isEmpty(tyb.getData().getNickName())) {
            tv_trophy_num.setText(tyb.getData().getUserId());
        } else {
            tv_trophy_num.setText(tyb.getData().getNickName());
        }
        mtv_gif_num_x.setText("x");
        mtv_gif_num.setText(String.valueOf(mTrophyCount));

    }

    //关闭白板
    private void witeTool() {
        if (mWebviewToolPopupWindow != null) {
            mWebviewToolPopupWindow.defaultWite();
            mWebviewToolPopupWindow.dismissPop();
        }
    }

    /**
     * 白板工具点击
     */
    private void toolOpen() {
        if (mWebviewToolPopupWindow == null) {
            mWebviewToolPopupWindow = new WebviewToolPopupWindowLand(this, student_view_web, STUDENT_WEB_STATE);
        }
        if (chatPop.isShow()) {
            chatPop.closePop();
        }
        mWebviewToolPopupWindow.showPop(rl_student_land_main);
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
            if (timeType.equals("published")) {
                tv_answer_time.setText(sysTimeStr);
            } else if (timeType.equals("ended")) {
                tv_result_time.setText(sysTimeStr);
            } else if (timeType.equals("network")) {
                network_stauts_time.setVisibility(View.VISIBLE);
                network_stauts_time.setText(sysTimeStr);

            }
        }
    };

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

    /**
     * 显示分享
     */
    public void showSharePopu() {
        if (shareStudentPopu == null) {
            shareStudentPopu = new SharePopu(this, new SharePopu.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0:
                            studentShare(SHARE_MEDIA.WEIXIN, UMShareAPI.get(mContext).isInstall(StudentActivityLand.this, SHARE_MEDIA.WEIXIN));
                            break;
                        case 1:
                            studentShare(SHARE_MEDIA.WEIXIN_CIRCLE, UMShareAPI.get(mContext).isInstall(StudentActivityLand.this, SHARE_MEDIA.WEIXIN_CIRCLE));
                            break;
                        case 2:
                            copyShareLink(roomId, student_invite_code, Constant.SHARE_CLASS_ROOM_URL,
                                    getResources().getString(R.string.student_invite_code));
                            break;
                    }
                    shareStudentPopu.dismiss();
                }
            }
            );
        }
        Tools.setBackground(getWindow(), 0.7f);
        shareStudentPopu.setOnDismissListener(new PopupWindow.OnDismissListener()

        {

            @Override
            public void onDismiss() {
                Tools.setBackground(getWindow(), 1f);
            }
        });

        shareStudentPopu.showAtLocation(rl_student_land_main, Gravity.BOTTOM, 0, 0);
    }

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

    /**
     * 底部工具栏位移动画
     */
    private void ToolsAnimator() {
        if (!animation_tool) {
            ObjectAnimator translationX = new ObjectAnimator().ofFloat(tool_ll_view_displacement, "translationX", 600f, 0);
            ObjectAnimator translationY = new ObjectAnimator().ofFloat(tool_ll_view_displacement, "translationY", 0, 0);

            AnimatorSet animatorSet = new AnimatorSet();  //组合动画
            animatorSet.playTogether(translationX, translationY); //设置动画
            animatorSet.setDuration(500);  //设置动画时间
            animatorSet.start(); //启动
            land_iv_raise_menu.setBackground(getResources().getDrawable(R.drawable.icon_menu_hover));
            tool_ll_view_displacement.setVisibility(View.VISIBLE);
            animation_tool = true;
            animatorSet.addListener(toolDisplacementAddListener);
        } else {
            ObjectAnimator translationX = new ObjectAnimator().ofFloat(tool_ll_view_displacement, "translationX", 0, 600f);
            ObjectAnimator translationY = new ObjectAnimator().ofFloat(tool_ll_view_displacement, "translationY", 0, 0);

            AnimatorSet animatorSet = new AnimatorSet();  //组合动画
            animatorSet.playTogether(translationX, translationY); //设置动画
            animatorSet.setDuration(500);  //设置动画时间
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
    class ToolDisplacementAddListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            Log.e(TAG_CLASS, "动画开始");
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            Log.e(TAG_CLASS, "动画结束");
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Log.e(TAG_CLASS, "动画取消");

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            Log.e(TAG_CLASS, "动画重复");

        }
    }


}
