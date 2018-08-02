package com.tttlive.education.ui.room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.ui.room.bean.GegBannedBean;
import com.tttlive.education.ui.room.bean.JoinQunRoomBean;
import com.tttlive.education.ui.room.bean.LeaveMassageBean;
import com.tttlive.education.ui.room.bean.LmAgreeResBean;
import com.tttlive.education.ui.room.bean.LmDisconnectBean;
import com.tttlive.education.ui.room.bean.LmListPersonnelBean;
import com.tttlive.education.ui.room.bean.SendTextMessageBean;
import com.tttlive.education.ui.room.bean.StartNotLiveBean;
import com.tttlive.education.ui.room.bean.UploadImageBean;
import com.tttlive.education.ui.room.socket.WebSocketService;
import com.tttlive.education.ui.room.socket.WsListener;
import com.tttlive.education.util.BaseTools;
import com.tttlive.education.util.ImagePathUtil;
import com.tttlive.education.util.LiveView;
import com.tttlive.education.util.LocalSharedPreferencesStorage;
import com.tttlive.education.util.MaterialBadgeTextView;
import com.tttlive.education.util.MessageDialog;
import com.tttlive.education.util.SPTools;
import com.tttlive.education.util.TeacherPersonnelUtil;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ttt.ijk.media.exo.widget.media.IjkVideoView;

import static com.tttlive.education.base.MyApplication.getContext;


/**
 * Created by Iverson on 2018/3/6 上午10:44
 * 此类用于：学生端
 */

public class StudentActivity extends BaseLiveActivity implements PlayerManager.PlayerStateListener, RoomLiveInterface, RoomMsg ,RoomUIinterface{


    private static String TAG_CLASS = StudentActivity.class.getSimpleName();

    public static final String ROOM_TYPE = "live_type";
    public static final String ROOM_USER_ID = "user_id";
    public static final String ROOM_ID = "room_id";
    public static final String TEACH_ID = "teach_id";
    public static final String NICKNAMW = "nickname";
    private Context studentContext;

    @BindView(R.id.image_view_back)
    ImageView mImageBack;
    @BindView(R.id.image_view_share)
    ImageView mImageShare;
    @BindView(R.id.rg_course_group)
    RadioGroup mRgcourse;
    @BindView(R.id.main)
    LinearLayout mRlCourseRoom;
    @BindView(R.id.mbt_personnel_lm)
    MaterialBadgeTextView mbt_personnel_lm;
    @BindView(R.id.iv_raise_hand)
    ImageView iv_raise_hand;//举手
    @BindView(R.id.ll_in_display)
    LinearLayout mLlInDisplay;
    @BindView(R.id.bt_camera)
    ImageView mCamera;//摄像头
    @BindView(R.id.bt_microphone)
    ImageView mIcrophone;//麦克风
    @BindView(R.id.viewpage)
    ViewPager mViewpage;
    @BindView(R.id.teacher_head_video)
    RelativeLayout mTeacherHeadVideo;//教师主窗口
    @BindView(R.id.rl_live_head_video_student)
    LiveView rl_live_head_video_student;//自己窗口
    @BindView(R.id.teacher_image_view)
    ImageView mTeacherImageView;
    @BindView(R.id.rl_live_video_one)
    LiveView rl_live_video_one;//连麦窗口A
    @BindView(R.id.rl_live_video_two)
    LiveView rl_live_video_two;//连麦窗口B
    @BindView(R.id.rl_live_video_three)
    LiveView rl_live_video_three;//连麦窗口C
    @BindView(R.id.rl_live_video_four)
    LiveView rl_live_video_four;//连麦窗口D
    @BindView(R.id.rl_live_video_five)
    LiveView rl_live_video_five;//连麦窗口E
    @BindView(R.id.rl_live_video_six)
    LiveView rl_live_video_six;//连麦窗口F
    @BindView(R.id.rl_live_video_seven)
    LiveView rl_live_video_seven;//连麦窗口G
    @BindView(R.id.rl_live_video_eight)
    LiveView rl_live_video_eight;//连麦窗口H
    @BindView(R.id.fl_personnel_chat)
    FrameLayout fl_personnel_chat;
    @BindView(R.id.fl_personnel_live)
    FrameLayout fl_personnel_live;
    @BindView(R.id.iv_personnel_detail)
    ImageView iv_personnel_detail;


    @BindView(R.id.rg_course_personnel_radio_button)
    Button rg_personnel_size_button;
    @BindView(R.id.iv_live_image_view_student)
    ImageView iv_live_view_student;
    @BindView(R.id.iv_live_video_one)
    ImageView iv_live_video_one;
    @BindView(R.id.iv_live_video_two)
    ImageView iv_live_video_two;
    @BindView(R.id.iv_live_video_three)
    ImageView iv_live_video_three;
    @BindView(R.id.iv_live_video_four)
    ImageView iv_live_video_four;
    @BindView(R.id.iv_live_video_five)
    ImageView iv_live_video_five;
    @BindView(R.id.iv_live_video_six)
    ImageView iv_live_video_six;
    @BindView(R.id.iv_live_video_seven)
    ImageView iv_live_video_seven;
    @BindView(R.id.iv_live_video_eight)
    ImageView iv_live_video_eight;
    @BindView(R.id.rl_live_lv_parent)
    LinearLayout rl_live_lv_parent;
    @BindView(R.id.rl_gone_visible)
    RelativeLayout rl_gone_visible;//大班课隐藏该布局
    @BindView(R.id.rl_live_microphone_two)
    RelativeLayout rl_microphone_open_two;
    @BindView(R.id.rl_live_microphone_one)
    RelativeLayout rl_microphone_open_one;
    @BindView(R.id.rl_live_microphone_open_one)
    RelativeLayout rl_live_microphone_open_one;
    @BindView(R.id.rl_live_microphone_open_two)
    RelativeLayout rl_live_microphone_open_two;
    @BindView(R.id.rl_live_microphone_open_three)
    RelativeLayout rl_live_microphone_open_three;
    @BindView(R.id.rl_live_microphone_open_four)
    RelativeLayout rl_live_microphone_open_four;
    @BindView(R.id.iv_live_microphone_close_one)
    ImageView iv_live_microphone_close_one;
    @BindView(R.id.iv_live_microphone_close_two)
    ImageView iv_live_microphone_close_two;
    @BindView(R.id.iv_live_microphone_close_three)
    ImageView iv_live_microphone_close_three;
    @BindView(R.id.iv_live_microphone_close_four)
    ImageView iv_live_microphone_close_four;

    @BindView(R.id.bt_radio_courseware)
    ImageView bt_radio_courseware;//课件
    @BindView(R.id.img_chat)
    ImageView img_chat;//聊天按钮
    @BindView(R.id.bottom_tool)
    LinearLayout bottom_tool;
    @BindView(R.id.student_view_web)
    WebView student_view_web;//webview

    @BindView(R.id.local_video_up)
    TextView local_video_up;
    @BindView(R.id.local_audio_up)
    TextView local_audio_up;
    @BindView(R.id.page_textview_number)
    TextView page_textview_number;
    @BindView(R.id.clude_local_two)
    View clude_local_two;
    @BindView(R.id.clude_local_three)
    View clude_local_three;
    @BindView(R.id.clude_local_four)
    View clude_local_four;
    @BindView(R.id.clude_local_five)
    View clude_local_five;
    @BindView(R.id.clude_local_six)
    View clude_local_six;
    @BindView(R.id.clude_local_seven)
    View clude_local_seven;
    @BindView(R.id.clude_local_eight)
    View clude_local_eight;
    @BindView(R.id.clude_local_nine)
    View clude_local_nine;
    @BindView(R.id.local_video_teacher)
    TextView local_video_teacher;
    @BindView(R.id.local_audio_teacher)
    TextView local_audio_teacher;
    @BindView(R.id.bt_tool)
    ImageView bt_tool;
    @BindView(R.id.local_student_user_id)
    TextView local_student_user_id;
    @BindView(R.id.local_student_room_id)
    TextView local_student_room_id;
    @BindView(R.id.ll_tool_chat)
    LinearLayout ll_tool_chat;
    @BindView(R.id.ll_live_video_one)
    LinearLayout ll_live_video_one;
    @BindView(R.id.ll_live_video_two)
    LinearLayout ll_live_video_two;
    @BindView(R.id.ll_live_video_three)
    LinearLayout ll_live_video_three;
    @BindView(R.id.iv_live_audio_close_two)
    ImageView iv_live_audio_close_two;
    @BindView(R.id.rl_live_video_close_two)
    RelativeLayout rl_live_video_close_two;
    @BindView(R.id.rl_live_microphone_student_two)
    RelativeLayout rl_live_microphone_student_two;


    private PopupWindow popupWindow;
    private ChatFragment mChatFragment;
    private IjkVideoView mVideoView;
    private PlayerManager playerManager;
    private TeacherPersonnelUtil teacherPersonnel;
    private PopupWindow personnelPopWindow;
    private ImageView iv_pop_window_back;
    private RecyclerView rv_pop_lm;
    private EnterUserInfo mUserInfo;

    private String roomId;
    private String userId;
    private String teachid;
    private String userimage;
    private String nackname;
    private String mPullStreamUrl;
    private String seqid;
    private int resolution = -1;
    private int mRoomid;
    private int mUserid;

    private boolean isVideo = false;//是否打开视频
    private boolean isAudio = false;//是否打开麦克风
    private boolean isLmstate = false;
    private boolean switchCamera = false;//摄像头前后切换
    private boolean isBanned = false;
    private boolean startCourse = false;
    private boolean applyLmState = false;
    private String mroomType = "1";

    private ArrayList<LiveView> mLiveViewList = new ArrayList();
    private ArrayList<ImageView> mListImageView = new ArrayList<>();
    private ArrayList<RelativeLayout> mOpenMicrophonelist = new ArrayList<>();
    private ArrayList<ImageView> mCloseMicrophonelist = new ArrayList<>();
    private ArrayList<View> mLocalList = new ArrayList<>();

    private ChatPopupWindow chatPop;
    private ArrayList<SendTextMessageBean> msgList=new ArrayList<>();//聊天消息列表
    private RoomPresenter mPresenter ;
    private WebSocketService sWebservice;

    private StudentWebViewClient studentWebViewClient = new StudentWebViewClient();
    private HandOnClickListener handOnClickListener = new HandOnClickListener();
    private ShareOnClickListener shareOnClickListener = new ShareOnClickListener();
    private BackOnClickListener backOnClickListener = new BackOnClickListener();
    private ChatOnClickListener chatOnClickListener = new ChatOnClickListener();
    private CameraOnClickListener cameraOnClickListener = new CameraOnClickListener();
    private OphoneOnClickListener ophoneOnClickListener = new OphoneOnClickListener();
    private PersonnelOnClickListener personnelOnClickListener = new PersonnelOnClickListener();
    private PopOnClickListener popOnClickListener = new PopOnClickListener();

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


    public static Intent createIntent(Context context, int roomType, String teacherid, String userId, String roomId, String type, String nickname) {
        Intent intent = new Intent(context, StudentActivity.class);
        intent.putExtra(ROOM_TYPE, type);
        intent.putExtra(ROOM_USER_ID, userId);
        intent.putExtra(ROOM_ID, roomId);
        intent.putExtra(TEACH_ID, teacherid);
        intent.putExtra(NICKNAMW, nickname);
        return intent;
    }

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_student;
    }

    @Override
    protected void findView() {
        studentContext = this;
        Constant.LIVE_PERSONNEL_TYPE = Constant.LIVE_PERSONNEL_TYPE_STUDENT;
        roomId = getIntent().getStringExtra(ROOM_ID);
        userId = getIntent().getStringExtra(ROOM_USER_ID);
        teachid = getIntent().getStringExtra(TEACH_ID);
        nackname = getIntent().getStringExtra(NICKNAMW);
        if (TextUtils.isEmpty(nackname)) {
            nackname = getResources().getString(R.string.live_student) + " " + userId;
        }
        mroomType = getIntent().getStringExtra(ROOM_TYPE);
        userimage = "";
        mPullStreamUrl = Constant.PULL_STREAM + roomId;
        mRoomid = Integer.parseInt(roomId);
        mUserid = Integer.parseInt(userId);
        Constant.LIVE_TYPE_ID = teachid;
        //初始化websocket消息
        initWsListeners();
        Long ct = System.currentTimeMillis();
        seqid = "binding_" + String.valueOf(ct);

        rl_live_lv_parent.setVisibility(View.VISIBLE);
        bt_radio_courseware.setVisibility(View.GONE);
        bt_tool.setVisibility(View.GONE);

        mLiveViewList.add(rl_live_video_one);
        mLiveViewList.add(rl_live_video_two);
        mLiveViewList.add(rl_live_video_three);
//        mLiveViewList.add(rl_live_video_four);
        mLiveViewList.add(rl_live_video_five);
        mLiveViewList.add(rl_live_video_six);
//        mLiveViewList.add(rl_live_video_seven);
//        mLiveViewList.add(rl_live_video_eight);

        mListImageView.add(iv_live_video_one);
        mListImageView.add(iv_live_video_two);
        mListImageView.add(iv_live_video_three);
//        mListImageView.add(iv_live_video_four);
        mListImageView.add(iv_live_video_five);
        mListImageView.add(iv_live_video_six);
//        mListImageView.add(iv_live_video_seven);
//        mListImageView.add(iv_live_video_eight);


        mOpenMicrophonelist.add(rl_live_microphone_open_one);
        mOpenMicrophonelist.add(rl_live_microphone_open_two);
        mOpenMicrophonelist.add(rl_live_microphone_open_three);
//        mOpenMicrophonelist.add(rl_live_microphone_open_four);
        mCloseMicrophonelist.add(iv_live_microphone_close_one);
        mCloseMicrophonelist.add(iv_live_microphone_close_two);
        mCloseMicrophonelist.add(iv_live_microphone_close_three);
//        mCloseMicrophonelist.add(iv_live_microphone_close_four);

        mLocalList.add(clude_local_two);
        mLocalList.add(clude_local_three);
        mLocalList.add(clude_local_four);
//        mLocalList.add(clude_local_five);
        mLocalList.add(clude_local_six);
        mLocalList.add(clude_local_seven);
//        mLocalList.add(clude_local_eight);
//        mLocalList.add(clude_local_nine);

        setRadioGroupListener();
        mRadioButtons[0] = findViewById(R.id.rg_course_im_radio_button);
        mRadioButtons[1] = findViewById(R.id.rg_course_personnel_radio_button);
        mFragments[0] = mChatFragment.newInstance();

        FragmentStatePagerAdapter paperAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments[arg0];
            }
        };
        mViewpage.setAdapter(paperAdapter);
        mViewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mRgcourse.setOnCheckedChangeListener(null);
                mRadioButtons[arg0].setChecked(true);
                setRadioGroupListener();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    protected void initView() {
        bindService(WebSocketService.createIntent(StudentActivity.this), stWsConnection, BIND_AUTO_CREATE);
        mRoomLiveHelp = new RoomLiveHelp(this, this);
        mPresenter = new RoomPresenter(this);
        //chatPop = new ChatPopupWindow(tContext , teacher_room_id , teacher_user_id ,teacher_user_name,teacher_user_image);
        chatPop = new ChatPopupWindow(studentContext , roomId , userId ,nackname , userimage);
        initPersonnerView();
        mCamera.setVisibility(View.GONE);
        mIcrophone.setVisibility(View.GONE);
        iv_raise_hand.setVisibility(View.GONE);
//        fl_personnel_chat.setVisibility(View.GONE);
        fl_personnel_live.setVisibility(View.GONE);

        iv_raise_hand.setOnClickListener(handOnClickListener);
        mImageShare.setOnClickListener(shareOnClickListener);
        mImageBack.setOnClickListener(backOnClickListener);
        img_chat.setOnClickListener(chatOnClickListener);
        mCamera.setOnClickListener(cameraOnClickListener);
        mIcrophone.setOnClickListener(ophoneOnClickListener);
        iv_personnel_detail.setOnClickListener(personnelOnClickListener);

        LocalSharedPreferencesStorage.putConfigStrValue(studentContext, "teacher_user_name", nackname);
        LocalSharedPreferencesStorage.putConfigStrValue(studentContext, "teacher_user_arar", userimage);
        LocalSharedPreferencesStorage.putConfigStrValue(studentContext, "teacher_room_id", roomId);
        LocalSharedPreferencesStorage.putConfigStrValue(studentContext, "teacher_user_id", userId);
        LocalSharedPreferencesStorage.putConfigStrValue(studentContext, "teacher_user_id_teacher", teachid);

//        //进入房间发送人员信息
//        sendJoinRoomMessage(roomId, userId,teachid, nackname, userimage);

        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(wsbind(mRoomid, mUserid, seqid, 2));
        }else {
            sWebservice.sendRequest(wsbind(mRoomid, mUserid, seqid, 2));
        }

        String inviteCode = SPTools.getInstance(this).getString(SPTools.KEY_LOGIN_INVITE_CODE,"");//邀请码
        WebSettings webSettings = student_view_web.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setDisplayZoomControls(false);
//        webSettings.setAllowContentAccess(false);

        final int width = BaseTools.getWindowsWidth(this);
        if (width > 960) {
            student_view_web.setInitialScale((int)(960f / width * 100));
        } else {
            student_view_web.setInitialScale((int)(width / 960f * 100));
        }
        student_view_web.loadUrl(Constant.DocUrl + "?inviteCode="+inviteCode+"&courseId="+roomId+"&role=1&userId="+userId+"&appId="+ Constant.app_id);
        Log.e(TAG_CLASS , "WebUrl : " + Constant.DocUrl + "?inviteCode="+inviteCode+"&courseId="+roomId+"&role=1&userId="+userId+"&appId="+ Constant.app_id);
        student_view_web.setWebViewClient(studentWebViewClient);

//        SendJoinRoomMessage();
        DisplayScreenSize();
        whetherCourseStart();
        //进入房间
        mPresenter.getVideoProfile();

    }

    //初始化拉流播放器
    public void initializeVideoView() {

        Log.e(TAG_CLASS, " 拉流：" + mPullStreamUrl);
        mVideoView = new IjkVideoView(this);
        mTeacherHeadVideo.addView(mVideoView);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp.height = getSurfaceViewHeight();
        mVideoView.setLayoutParams(lp);
        playerManager = new PlayerManager(this, mVideoView);
        playerManager.setPlayerStateListener(this);
        playerManager.play(mPullStreamUrl);
        playerManager.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        playerManager.start();
    }


    private void setRadioGroupListener() {

        mRgcourse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_course_im_radio_button:
                        mViewpage.setCurrentItem(0);
                        break;
                    case R.id.rg_course_personnel_radio_button:
                        mViewpage.setCurrentItem(1);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 发送进入房间消息
     */
    private void SendJoinRoomMessage(){
        //进入房间发送消息
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendJoinRoomMessage(roomId, userId,teachid, nackname, userimage);
            }
        }, 2000);
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

    @Override
    public void enterRoomSuccess() {
        Log.e(TAG_CLASS, "enterRoomSuccess:进入房间成功  userid " + userId);
        if (!startCourse){
            mRoomLiveHelp.controlAllRemoteAudioStreams(true);
            mRoomLiveHelp.contorlAllRemoteVideoStreams(true);
        }
        AudioClose(userId);
        VideoClose(userId);
        SendJoinRoomMessage();
        Constant.LIVE_TYPE_ID = teachid;

    }

    @Override
    public void enterRoomFailue(int error) {
        Log.e(TAG_CLASS, "enterRoomFailue:进入房间失败" + error);
        if (error == Constants.ERROR_ENTER_ROOM_UNKNOW){
            toastShort("当前课程未开始,请稍后再进");
            onBackPressed();
        }
    }

    @Override
    public void onDisconnected(int errorCode) {
        Log.e(TAG_CLASS, "onDisconnected:直播中断线" + errorCode);
        if (errorCode == Constants.ERROR_KICK_BY_HOST) {
            toastShort(R.string.msg_operation_kicked_out);
        } else if (errorCode == Constants.ERROR_KICK_BY_MASTER_EXIT) {
            toastShort(R.string.msg_operation_taecher_exit);
        }
        onBackPressed();
    }

    @Override
    public void onMemberExit(long userId) {
        Log.e(TAG_CLASS, "onMemberExit:直播成员退出");
        releaseLiveView(userId);
    }

    @Override
    public void onMemberEnter(long userIds, EnterUserInfo userInfo , String sCustom) {
        Log.e(TAG_CLASS, "onMemberEnter:直播成员进入" + userIds);
    }


    @Override
    public void onHostEnter(long userId, EnterUserInfo userInfo , String sCustom) {
        //打开主播视频窗口
        Log.e(TAG_CLASS, "onHostEnter:" + userInfo);
        mUserInfo = userInfo;
        if (startCourse){
            if (!"0".equals(mroomType)) {
                mRoomLiveHelp.openRemoteVideo(mTeacherHeadVideo, userInfo, true);
            }
        }
    }

    @Override
    public void onUpdateLiveView(List<EnterUserInfo> userInfos) {
        Log.e(TAG_CLASS, "onUpdateLiveViewonUpdateLiveView:" + userInfos.size());
    }

    @Override
    public void dispatchMessage(long srcUserID, int type, String sSeqID, String data) {
//        RoomMessageType msgType=new RoomMessageType(this);
//        msgType.appendData(data);
        Log.e(TAG_CLASS, "收到消息srcUserID:" + srcUserID + "type:--" + type + "-sSeqID:--" + sSeqID + "-data:--" + data);
    }


    //初始化websocket消息
    private void initWsListeners() {

        WsListener notifListener = new WsListener() {

            @Override
            public void handleData(String msg) {
                Log.e(TAG_CLASS, "收到消息:" + msg);
                RoomMessageType msgType = new RoomMessageType(StudentActivity.this);
                msgType.appendData(msg);

            }
        };
        if (Constant.wsService != null&&notifListener!=null){
            Constant.wsService.registerListener(notifListener);
        }else {
            Log.e(TAG_CLASS, "WsListener wsService : " + Constant.wsService);
            Log.e(TAG_CLASS, "WsListener notifListener : " +  notifListener );
            Log.e(TAG_CLASS, "WsListener sWebservice : " +  notifListener );
            sWebservice.registerListener(notifListener);
        }
    }

    @Override
    public void sendMessageResult(int resultType, String data) {

    }

    @Override
    public void localVideoStatus(LocalVideoStats localVideoStats) {
        //接受视频本地上行速率
        local_video_up.setText("V- " + String.valueOf(localVideoStats.getSentBitrate())+" " + getResources().getString(R.string.video_up));
    }

    @Override
    public void remoteVideoStatus(RemoteVideoStats mRemoteVideoStats) {
        //视频下行速率

    }

    @Override
    public void LocalAudioStatus(LocalAudioStats localAudioStats) {
        //音频上行
        local_audio_up.setText("A- " + String.valueOf(localAudioStats.getSentBitrate())+" " + getResources().getString(R.string.video_up));

    }

    @Override
    public void remoteAudioStatus(RemoteAudioStats mRemoteAudioStats) {
        //音频下行
    }

    @Override
    public void OnupdateUserBaseInfo(Long roomId, long uid, String sCustom) {
        //更新用户信息
    }

    @Override
    public void OnConnectSuccess(String ip, int port) {
        //返回当前媒体服务器ip地址
    }

    /**
     * 聊天消息回调
     *
     * @param data
     */
    @Override
    public void receiveTextMessage(String data) {
        Log.e(TAG_CLASS,"im_req:data:"+data);
        Gson gson=new Gson();
        SendTextMessageBean sendmsg= gson.fromJson(data,SendTextMessageBean.class);
        msgList.add(sendmsg);
        if (chatPop.isShow()){
            chatPop.showChatList(msgList);
        } else {
            mbt_personnel_lm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void roomPersonnelList(String data) {
        //人员名单
        Gson perListGson = new Gson();
        JoinQunRoomBean qunGson = perListGson.fromJson(data, JoinQunRoomBean.class);
        Log.e(TAG_CLASS, "qunGson:" + data);
        teacherPersonnel.initViewStudentPersonnel(studentContext, null ,mroomType);

    }

    @Override
    public void roomDocConect(String data) {
        //文档同步
        Log.e(TAG_CLASS , "docConect : " + data);
    }

    @Override
    public void closeLmCall(String data) {
        //断开连麦
        Log.e(TAG_CLASS + " closeLmCall ", data);
        Gson leaveGson = new Gson();
        LmDisconnectBean lmAgreeBean = leaveGson.fromJson(data, LmDisconnectBean.class);
        //关闭摄像头与麦克风
        if (lmAgreeBean.getData().getUserId().equals(userId)) {
            AudioVideoClose();
            mLlInDisplay.setVisibility(View.GONE);
            rl_live_head_video_student.removeAllViews();
            iv_live_view_student.setVisibility(View.VISIBLE);
            mCamera.setVisibility(View.GONE);
            mIcrophone.setVisibility(View.GONE);
            applyLmState = false;
            showSelfVideo = false;
            iv_raise_hand.setBackground(getResources().getDrawable(R.drawable.icon_hand_normal));
        }else {
            releaseLiveView(Long.parseLong(lmAgreeBean.getData().getUserId()));
        }
    }

    @Override
    public void outRoomClose(String data) {
        //踢出房间
        Log.e(TAG_CLASS + " outRoomClose ", data);
        Gson leaveGson = new Gson();
        LeaveMassageBean lmAgreeBean = leaveGson.fromJson(data, LeaveMassageBean.class);
        onBackPressed();
    }

    @Override
    public void lmListPersonnel(String data) {
        //当前连麦列表
        Log.e(TAG_CLASS, "当前连麦列表data:" + new Gson().toJson(data));
        Gson showGson = new Gson();
        LmListPersonnelBean agreeLmbean = showGson.fromJson(data, LmListPersonnelBean.class);
        if (agreeLmbean != null && agreeLmbean.getData().getUserList().size() > 0 && agreeLmbean.getData().getUserList() != null && agreeLmbean.getData() != null) {
            for (int i = 0; i < agreeLmbean.getData().getUserList().size(); i++) {
                showRemoteView(agreeLmbean.getData().getUserList().get(i).getUserId());
            }
        }
    }

    @Override
    public void gagReqPersonnel(String data) {
        //禁言
        if (!TextUtils.isEmpty(data)) {
            Gson gGson = new Gson();
            Log.e(TAG_CLASS + "  gagReqPersonnel", gGson.toJson(data));
            GegBannedBean gbb = gGson.fromJson(data, GegBannedBean.class);
            if (gbb != null) {
//                EventBus.getDefault().post(new MessageEvent(Constant.SEND_EVENT_GAG_REG, data));
                if (gbb.getData().getUserId().equals("0")||gbb.getData().getUserId().equals(userId)) {
                    if (chatPop != null && chatPop.isShow()) {
                        chatPop.closePop();
                        toastShort(R.string.live_banned);
                    }
                    isBanned = true;
                }
            }
        }
    }

    @Override
    public void gagRerRemovePersonnel(String data) {
        //解除禁言
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            Log.e(TAG_CLASS + "  gagRerRemovePersonnel", gson.toJson(data));
            GegBannedBean gbbr = gson.fromJson(data, GegBannedBean.class);
            if (gbbr != null) {
//                EventBus.getDefault().post(new MessageEvent(Constant.SEND_EVENT_REMOVE_GAG_RER, data));
                if (gbbr.getData().getUserId().equals("0")||gbbr.getData().getUserId().equals(userId)){
                    isBanned = false;
                }
            }
        }
    }

    @Override
    public void courseStart(String data) {
        //上课消息
        Log.e(TAG_CLASS , "开始上课 "+ data);
        if (!startCourse){
            startLiveInit();

        }

    }

    /**
     * 收到上课后逻辑处理
     */
    public void startLiveInit(){
        Log.e(TAG_CLASS  ,  "startLiveInit ");
        startCourse = true;
        local_student_user_id.setText(userId);
        local_student_room_id.setText(roomId);
        mTeacherImageView.setVisibility(View.GONE);
        iv_raise_hand.setVisibility(View.VISIBLE);
        fl_personnel_chat.setVisibility(View.VISIBLE);
        fl_personnel_live.setVisibility(View.VISIBLE);
        Constant.USER_ISROOM = true;
        mRoomLiveHelp.controlAllRemoteAudioStreams(false);
        mRoomLiveHelp.contorlAllRemoteVideoStreams(false);

        if (mUserInfo != null){
            if (!"0".equals(mroomType)) {
                mRoomLiveHelp.openRemoteVideo(mTeacherHeadVideo, mUserInfo, true);
            }
        }

    }


    @Override
    public void courseLeave(String data) {
        //下课消息
        Log.e(TAG_CLASS , "老师下课 "+ data);
//        Gson leaveGson = new Gson();
//        StartLiveBean startLiveBean = leaveGson.fromJson(data , StartLiveBean.class);
//        if (startLiveBean.getAccountList().getUserId().equals(teachid)){
//            toastShort("老师退出课堂");
//            onBackPressed();
//        }
    }

    @Override
    public void courseTeacherNotStart(String data) {
        //是否已经开课
        Log.e(TAG_CLASS , "老师是否已经开课 ");
        Gson nGson = new Gson();
        StartNotLiveBean snlBean = nGson.fromJson(data , StartNotLiveBean.class);
        if (snlBean.getData().getType().equals("1")){
            //开课
            Log.e(TAG_CLASS , "开课  "+ data);
            if (!startCourse){
                startLiveInit();
            }
        }else if (snlBean.getData().getType().equals("0")){
            //未开课
            Log.e(TAG_CLASS , "未开课  "+ data);
        }
    }

    @Override
    public void trophyAward(String data) {
        //收到奖杯
    }

    @Override
    public void statrAnswer(String data) {

    }

    @Override
    public void statisicsAnswer(String data) {

    }

    @Override
    public void whiteboardAccess(String data) {

    }

    @Override
    public void liveVideoClose(String data) {

    }

    @Override
    public void liveAudioClose(String data) {

    }

    /**
     * 判断是否上课
     */
    private void whetherCourseStart(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (startCourse){
                    Log.e(TAG_CLASS , "已经上课了----");
                }else {
                    Log.e(TAG_CLASS , "没有收到上课的信令-----");
                    notStartLive(roomId , userId , "3" );

                }

            }
        },3000);
    }



    @Override
    public void joinRoomSuccess(String data) {
    }

    @Override
    public void leaveMessage(String data) {
        //退出房间
        Gson leaveGson = new Gson();
        LeaveMassageBean leaveRoomBean = leaveGson.fromJson(data, LeaveMassageBean.class);
        if (String.valueOf(leaveRoomBean.getData().getUserId()).equals(teachid)) {
            toastShort("老师退出课堂");
            onBackPressed();
        }
    }

    @Override
    public void dealApplyMicMessage(String data) {
        //邀请发言
        Log.e(TAG_CLASS + " dealApplyMicMessage ", data);
        Gson leaveGson = new Gson();
        LmAgreeResBean lmAgreeBean = leaveGson.fromJson(data, LmAgreeResBean.class);
        if (lmAgreeBean.getData().getUserId().equals(userId)) {
            showLminviteDialog(lmAgreeBean.getData().getRoomId(), lmAgreeBean.getData().getUserId());
        }
    }

    /**
     * 连麦响应
     * 打开本地视频
     *
     * @param data
     */
    @Override
    public void dealApplyAgreeMessage(String data) {
        Gson leaveGson = new Gson();
        LmAgreeResBean lmAgreeBean = leaveGson.fromJson(data, LmAgreeResBean.class);
        if (lmAgreeBean.getData().getType().equals("1")) {
            if (lmAgreeBean.getData().getUserId().equals(userId)){
                mIcrophone.setVisibility(View.VISIBLE);
                mCamera.setVisibility(View.VISIBLE);
                AudioOpen(lmAgreeBean.getData().getUserId());
                VideoOpen(lmAgreeBean.getData().getUserId());
            }
            showRemoteView(lmAgreeBean.getData().getUserId());
        } else if ("0".equals(lmAgreeBean.getData().getType())) {
            toastShort("教师拒绝你的连麦申请！");
            applyLmState = false;
            iv_raise_hand.setBackground(getResources().getDrawable(R.drawable.icon_hand_normal));
        }

    }

    /**
     * 接受到邀请发言消息
     *
     * @param roomId
     * @param userId
     */
    private void showLminviteDialog(final String roomId, final String userId) {
        MessageDialog dialog = new MessageDialog(StudentActivity.this);
        dialog.setHint(R.string.mes_hint);
        dialog.setContent(R.string.is_lm_invite);
        dialog.setLeftButton(R.string.refuse_lm);
        dialog.setRightButton(R.string.ccept_lm);
        dialog.setCancelable(false);
        dialog.setMessageDialogListener(new MessageDialog.MessageDialogListener() {
            @Override
            public void onCancelClick(MessageDialog dialog) {
                Long ct = System.currentTimeMillis();
                String seqid = String.valueOf(ct);
                Gson gson = new Gson();
                LmAgreeResBean lmAgreeResBean = new LmAgreeResBean();
                LmAgreeResBean.LmDataBean lmDataBean = new LmAgreeResBean.LmDataBean();
                lmAgreeResBean.setMessageType(Constant.LM_RES);
                lmDataBean.setRoomId(roomId);
                lmDataBean.setUserId(userId);
                lmDataBean.setType("0");
                lmAgreeResBean.setData(lmDataBean);
                //  mRoomLiveHelp.sendMessage(Long.valueOf(teachid) , 1, seqid , gson.toJson(lmAgreeResBean));
                if (Constant.wsService != null) {
                    Constant.wsService.sendRequest(wsSendMsg(mRoomid, mUserid, 1, gson.toJson(lmAgreeResBean), seqid, Integer.parseInt(teachid),false));
                }else {
                    sWebservice.sendRequest(wsSendMsg(mRoomid, mUserid, 1, gson.toJson(lmAgreeResBean), seqid, Integer.parseInt(teachid),false));
                }
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }

            @Override
            public void onCommitClick(MessageDialog dialog) {
                Long ct = System.currentTimeMillis();
                String seqid = String.valueOf(ct);
                Gson gson = new Gson();
                LmAgreeResBean lmAgreeResBean = new LmAgreeResBean();
                LmAgreeResBean.LmDataBean lmDataBean = new LmAgreeResBean.LmDataBean();

                lmAgreeResBean.setMessageType(Constant.LM_RES);
                lmDataBean.setRoomId(roomId);
                lmDataBean.setUserId(userId);
                lmDataBean.setType("1");
                lmAgreeResBean.setData(lmDataBean);

                //  mRoomLiveHelp.sendMessage(0 , 1, seqid , gson.toJson(lmAgreeResBean));
                if (Constant.wsService != null) {
                    Constant.wsService.sendRequest(wsSendMsg(mRoomid, mUserid, 1, gson.toJson(lmAgreeResBean), seqid, 0 , false));
                }else {
                    sWebservice.sendRequest(wsSendMsg(mRoomid, mUserid, 1, gson.toJson(lmAgreeResBean), seqid, 0 , false));
                }
                showRemoteView(userId);
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
        dialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
        }

        switch (requestCode){
            case Constant.PHOTO_REQUEST_GALLERY:
                if (data != null && data.getData() != null) {
                    Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        String imagePath = ImagePathUtil.getImageAbsolutePath(this , selectedUri);
                        File file = new File(imagePath);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                        mPresenter.getImageUpload(requestFile , body);
                        System.out.println("---------------   " + imagePath);

                    }
                }
                break;

        }
    }

    /**
     * 获取到分辨率后进入房间
     * @param i
     */
    @Override
    public void getProfileSuccess(int i) {
        //加入直播频道
        resolution = i;
        mRoomLiveHelp.initTTTEngine();
        mRoomLiveHelp.enterRoom(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING, Constants.CLIENT_ROLE_BROADCASTER, Integer.parseInt(roomId), Long.parseLong(userId),i , "");

    }

    @Override
    public void imageUploadSucess(BaseResponse<UploadImageBean> uploadBean) {
        //上传图片成功
        sendTextMessage(uploadBean.getData().getUrl() , Constant.SEND_MESSAGE_TYPE_IMAGE);
    }

    @Override
    public void getError() {

    }

    /**
     * 发送消息
     * @param message
     */
    private void sendTextMessage(String message , int mesageType) {
        Long ct = System.currentTimeMillis();
        String seqid = "binding_"+String.valueOf(ct);

        String user_name = LocalSharedPreferencesStorage.getConfigStrValue(studentContext , "teacher_user_name");
        String user_avatar = LocalSharedPreferencesStorage.getConfigStrValue(studentContext , "teacher_user_arar");
        String room_id = LocalSharedPreferencesStorage.getConfigStrValue(studentContext , "teacher_room_id");
        String user_id = LocalSharedPreferencesStorage.getConfigStrValue(studentContext , "teacher_user_id");
        Gson gson = new Gson();
        SendTextMessageBean stMessAge = new SendTextMessageBean();
        SendTextMessageBean.DataBean stDb = new SendTextMessageBean.DataBean();
        stMessAge.setMessageType(Constant.BARRAGE_REQ);
        stDb.setMessage(message);
        stDb.setNickName(user_name);
        stDb.setAvatar(user_avatar);
        stDb.setUserId(user_id);
        stDb.setRoomId(room_id);
        stDb.setType(mesageType);
        stMessAge.setData(stDb);
        String ss = gson.toJson(stMessAge);
        msgList.add(stMessAge);
//        adapter = new ChatAdapter(getContext(),msgList);
//        mImRecyclerView.setAdapter(adapter);

        chatPop.showChatList(msgList);
        Constant. wsService.sendRequest(wsSendMsg(Integer.parseInt(room_id), Integer.parseInt(user_id),1,ss,seqid,0 ,true));

    }

    /**
     * 底部弹出PopupWindow
     * 点击PopupWindow以外部分或点击返回键时,PopupWindow 会 消失
     *
     * @param
     */
    public void showPopFormBottom(View view) {
        //自定义PopupWindow的布局
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.settings, null);
        //初始化PopupWindow,并为其设置布局文件
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        //此处是强制点击空白区域，popupwindow消失
        contentView.findViewById(R.id.popup_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });

        popupWindow.setAnimationStyle(R.style.AnimBottomIn);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置PopupWindow显示的位置
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private boolean showSelfVideo = false;

    /**
     * 显示视频
     * @param
     */
    private void showRemoteView(String lmuserId) {

        if (lmuserId != null){
            if (lmuserId.equals(userId)) {
                //打开本地视频
                if (!showSelfVideo){
                    mRoomLiveHelp.openLocalVideo(rl_live_head_video_student, false , Constants.CLIENT_ROLE_BROADCASTER , resolution);
                    iv_live_view_student.setVisibility(View.GONE);
                    showSelfVideo = true;
                }
            } else {
                //打开远端视频
                LiveView freeViewLive = getFreeViewLive();
                freeViewLive.setFlagUserId(Long.valueOf(lmuserId));
                freeViewLive.setFree(false);
                mRoomLiveHelp.openIdRemoteVideo(freeViewLive, Long.valueOf(lmuserId), false);
            }
        }
    }

    /**
     * 获取空闲的View用于播放或者发布.
     */
    protected LiveView getFreeViewLive() {
        LiveView lvFreeView = null;
        for (int i = 0, size = mLiveViewList.size(); i < size; i++) {
            LiveView viewLive = mLiveViewList.get(i);
            if (viewLive.isFree()) {
                lvFreeView = viewLive;
                releaseImageView(i);
                break;
            }
        }
        return lvFreeView;
    }


    /**
     * 释放直播控件
     * 不补位
     * @param userId
     */
    private void releaseLiveView(long userId) {
        if (userId == 0) return;
        for (int i = 0, size = mLiveViewList.size(); i < size; i++) {
            LiveView currentViewLive = mLiveViewList.get(i);
            if (userId == currentViewLive.getFlagUserId()) {
                currentViewLive.removeAllViews();
                SurfaceView mChildSurfaceView = (SurfaceView) currentViewLive.getChildAt(0);
                currentViewLive.removeView(mChildSurfaceView);
                currentViewLive.setFree(true);
                currentViewLive.setFlagUserId(0);
                // 标记最后一个View可用
                mLiveViewList.get(i).setFree(true);
                mLiveViewList.get(i).removeAllViews();
                visibleImageView(i);
                break;
            }
        }

    }




    /**
     * 释放直播控件
     * 可以自动补位
     * @param userId
     */
    private void releaseLiveFillView(long userId) {
        if (userId == 0) return;
        for (int i = 0, size = mLiveViewList.size(); i < size; i++) {
            LiveView currentViewLive = mLiveViewList.get(i);
            if (userId == currentViewLive.getFlagUserId()) {
                int j = i;
                for (; j < size - 1; j++) {
                    currentViewLive = mLiveViewList.get(j);
                    LiveView nextViewLive = mLiveViewList.get(j + 1);
                    if (nextViewLive.isFree()) {
                        break;
                    }
                    currentViewLive.removeAllViews();
                    SurfaceView mChildSurfaceView = (SurfaceView) nextViewLive.getChildAt(0);
                    nextViewLive.removeView(mChildSurfaceView);
                    currentViewLive.addView(mChildSurfaceView);
                    currentViewLive.setFlagUserId(nextViewLive.getFlagUserId());
                    currentViewLive.setFree(false);
                    nextViewLive.setFree(true);
                    nextViewLive.setFlagUserId(0);
                }
                // 标记最后一个View可用
                mLiveViewList.get(j).setFree(true);
                mLiveViewList.get(j).removeAllViews();
                visibleImageView(j);
                break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG_CLASS ,  "onDestroy");
        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(wsbind(mRoomid, mUserid, seqid, 3));
        }else {
            sWebservice.sendRequest(wsbind(mRoomid, mUserid, seqid, 3));
        }
        if (stWsConnection != null){
            unbindService(stWsConnection);
        }
        EventBus.getDefault().unregister(this);
        //退出直播
        if (mRoomLiveHelp != null) {
            mRoomLiveHelp.exitHelp();
            mRoomLiveHelp = null;
        }

        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
        if (chatPop.isShow()) {
            chatPop.closePop();
        }
        PopLmDismiss();
        startCourse =false;
        showSelfVideo = false;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG_CLASS ,  "onBackPressed");
        sendLeaveMessage(mRoomLiveHelp, roomId, userId, 0);
    }


    /**
     * 申请连麦
     */
    private class HandOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (!applyLmState){
                sendLmMessage(teachid , roomId , userId , nackname);
                applyLmState = true;
                iv_raise_hand.setBackground(getResources().getDrawable(R.drawable.icon_hand_disable));
            }else if (applyLmState){
                if (mCamera.getVisibility() == View.VISIBLE && mIcrophone.getVisibility() == View.VISIBLE){
                    toastShort(R.string.apply_lm_agree_ing);
                }else {
                    toastShort(R.string.apply_lm_state_ing);
                }
            }
        }
    }

    /**
     * 分享
     */
    private class ShareOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
//            showShare();
        }
    }

    /**
     * 退出房间
     */
    private class BackOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }

    /**
     * 聊天
     *
     */
    private class ChatOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (!chatPop.isShow()){
                if (isBanned){
                    toastShort(R.string.live_banned);
                    return;
                }
                chatPop.showChatPop(bottom_tool,StudentActivity.this,msgList  , isBanned);
                mbt_personnel_lm.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 初始化人员名单布局
     */
    private void initPersonnerView(){
        View popView = LayoutInflater.from(studentContext).inflate(R.layout.pop_personnel_detail_list , null);
        personnelPopWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        iv_pop_window_back = popView.findViewById(R.id.iv_pop_personnel_back);
        rv_pop_lm = popView.findViewById(R.id.rv_personnel_lm);
        iv_pop_window_back.setOnClickListener(popOnClickListener);
        teacherPersonnel = new TeacherPersonnelUtil(studentContext , rv_pop_lm);
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
     * 关闭人员列表弹窗
     */
    private class PopOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            PopLmDismiss();
        }
    }

    /**
     * 加载人员列表
     */
    private class PersonnelOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            personnelPopWindow(v);

        }
    }

    /**
     * 取消弹窗
     */
    private void PopLmDismiss() {
        if (personnelPopWindow != null && personnelPopWindow.isShowing()){
            personnelPopWindow.dismiss();
        }
    }

    /**
     * 相机
     */
    private class CameraOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (isVideo && isAudio && isLmstate){
                toastShort("你已断开连麦,不可打开本地视频");
                return;
            }
            if (mCamera.isSelected()) {
                VideoOpen(userId);
            } else {
                VideoClose(userId);
            }
        }
    }

    /**
     * 麦克风
     */
    private class OphoneOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (isAudio && isVideo && isLmstate){
                toastShort("你已断开连麦,不可打开本地麦克风");
                return;
            }
            if (mIcrophone.isSelected()) {
                AudioOpen(userId);
            } else {
                AudioClose(userId);

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
            Log.e(TAG_CLASS , "onLoadResource " + url);
            webviewPage("getCurrentTotalPage()");
        }
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
                        String page = value.replace("\"","");
                        Log.e(TAG_CLASS + " webViewPage : ", value + "    " + page);
                        if (startCourse){
                            if (!page.equals("0/0") && !page.equals("null")){
                                page_textview_number.setVisibility(View.VISIBLE);
                            }
                        }else {
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
     * 摄像头开启
     */
    private void VideoOpen(String userid) {
        mCamera.setSelected(false);
        mRoomLiveHelp.controlLocalVideo(false);
        isVideo = false;
        videoSpeakType(userid);
        videoAudioSpeakType();
    }

    /**
     * 摄像头关闭
     */
    private void VideoClose(String userid) {
        mCamera.setSelected(true);
        mRoomLiveHelp.controlLocalVideo(true);
        isVideo = true;
        videoSpeakType(userid);
        videoAudioSpeakType();
    }

    /**
     * 麦克风开启
     */
    private void AudioOpen(String userid) {
        mIcrophone.setSelected(false);
        mRoomLiveHelp.controlLocalAudio(false);
        isAudio = false;
        videoSpeakType(userid);
        videoAudioSpeakType();
    }

    /**
     * 麦克风关闭
     */
    private void AudioClose(String userid) {
        mIcrophone.setSelected(true);
        mRoomLiveHelp.controlLocalAudio(true);
        isAudio = true;
        videoSpeakType(userid);
        videoAudioSpeakType();
    }

    /**
     * 断开连麦直接关闭麦克风和摄像头
     */
    private void AudioVideoClose() {
        isLmstate = true;
        mIcrophone.setSelected(true);
        mRoomLiveHelp.controlLocalAudio(true);
        isAudio = true;
        mCamera.setSelected(true);
        mRoomLiveHelp.controlLocalVideo(true);
        isVideo = true;
        videoAudioSpeakType();

    }


    private void videoAudioSpeakType() {
        if (isVideo) {
            rl_live_video_close_two.setVisibility(View.VISIBLE);
        } else {
            rl_live_video_close_two.setVisibility(View.GONE);
        }

        if (isAudio) {
            iv_live_audio_close_two.setVisibility(View.VISIBLE);
        } else {
            iv_live_audio_close_two.setVisibility(View.GONE);
        }

        if (isVideo && isAudio) {
            rl_live_video_close_two.setVisibility(View.GONE);
            iv_live_audio_close_two.setVisibility(View.GONE);
            iv_live_view_student.setVisibility(View.VISIBLE);
        } else {
            iv_live_view_student.setVisibility(View.GONE);
        }
    }

    /**
     * 查找屬於自己的窗口關閉或打開攝像頭操作
     */
    private void videoSpeakType(String userId) {
        if (Long.parseLong(userId) == 0) return;
        for (int i = 0, size = mLiveViewList.size(); i < size; i++) {
            LiveView currentViewLive = mLiveViewList.get(i);
            if (Long.parseLong(userId) == currentViewLive.getFlagUserId()) {
                int index = i;
                for (int j = 0; j < mOpenMicrophonelist.size(); j++) {
                    if (j == index) {
                        if (isVideo) {
                            mOpenMicrophonelist.get(j).setVisibility(View.VISIBLE);

                        } else {
                            mOpenMicrophonelist.get(j).setVisibility(View.GONE);
                        }
                        if (isAudio) {
                            mCloseMicrophonelist.get(j).setVisibility(View.VISIBLE);
                        } else {
                            mCloseMicrophonelist.get(j).setVisibility(View.GONE);
                        }

                        break;
                    }


                }

                break;
            }
        }


    }

    /**
     * 处理连麦窗口图片显示和隐藏
     *
     * @param index
     */
    private void releaseImageView(int index) {
        for (int i = 0; i < mLiveViewList.size(); i++) {
            if (i == index) {
                ImageView imageView = mListImageView.get(i);
                imageView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 处理连麦窗口图片显示
     *
     * @param index
     */
    private void visibleImageView(int index) {
        for (int i = 0; i < mListImageView.size(); i++) {
            if (i == index) {
                ImageView imageView = mListImageView.get(i);
                imageView.setVisibility(View.VISIBLE);
            }

        }

    }

    //获取手机窗口的大小
    protected int getSurfaceViewHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        if (this != null) {
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        //因为部分（乐视）手机的系统栏高度不是标准的25/24dp，所以首选获得系统内置的高度，出现异常时则用预定义的高度。
        int statusBarHeight;
        try {
            statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier
                    ("status_bar_height", "dimen", "android"));
        } catch (Exception e) {
            statusBarHeight = (int) getResources().getDimension(R.dimen.dp40);
        }
        //窗口的宽度
        return dm.heightPixels - statusBarHeight;
    }

    /**
     * 设置视频控件比例
     */
    private void DisplayScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mapWidth = dm.widthPixels;
        int mapHeight = dm.heightPixels;
        LinearLayout.LayoutParams linParams = (LinearLayout.LayoutParams) ll_live_video_one.getLayoutParams();
        linParams.width = mapWidth;
        linParams.height = mapWidth * 2/3 * 9/16;
        linParams.weight = 0;
        ll_live_video_one.setLayoutParams(linParams);
        LinearLayout.LayoutParams linparamsTwo = (LinearLayout.LayoutParams) ll_live_video_two.getLayoutParams();
        linparamsTwo.width = mapWidth;
        linparamsTwo.height = mapWidth /3 * 11/16;
        linparamsTwo.weight = 0;
        ll_live_video_two.setLayoutParams(linparamsTwo);
        LinearLayout.LayoutParams linparamsThree = (LinearLayout.LayoutParams) ll_live_video_three.getLayoutParams();
        linparamsThree.width = mapWidth;
        linparamsThree.height = mapWidth/3 * 11/16;
        linparamsThree.weight=0;
        ll_live_video_three.setLayoutParams(linparamsThree);
    }


}
