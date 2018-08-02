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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tttlive.education.adapter.CourseWareAdapter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.basic.education.R;
import com.tttlive.education.ui.room.bean.JoinRoomBean;
import com.tttlive.education.ui.room.bean.LeaveMassageBean;
import com.tttlive.education.ui.room.bean.LmAgreeResBean;
import com.tttlive.education.ui.room.bean.LmBean;
import com.tttlive.education.ui.room.bean.LmDisconnectBean;
import com.tttlive.education.ui.room.bean.SendTextMessageBean;
import com.tttlive.education.ui.room.bean.StartNotLiveBean;
import com.tttlive.education.ui.room.bean.UploadImageBean;
import com.tttlive.education.ui.room.custom.CustomDialog;
import com.tttlive.education.ui.room.socket.WebSocketService;
import com.tttlive.education.ui.room.socket.WsListener;
import com.tttlive.education.ui.room.webviewtool.WebviewToolPopupWindow;
import com.tttlive.education.util.BaseTools;
import com.tttlive.education.util.DateUtils;
import com.tttlive.education.util.ImagePathUtil;
import com.tttlive.education.util.LiveView;
import com.tttlive.education.util.LocalSharedPreferencesStorage;
import com.tttlive.education.util.MaterialBadgeTextView;
import com.tttlive.education.util.MessageDialog;
import com.tttlive.education.util.TeacherPersonnelUtil;
import com.wushuangtech.bean.LocalAudioStats;
import com.wushuangtech.bean.LocalVideoStats;
import com.wushuangtech.bean.RemoteAudioStats;
import com.wushuangtech.bean.RemoteVideoStats;
import com.wushuangtech.bean.VideoCompositingLayout;
import com.wushuangtech.library.Constants;
import com.wushuangtech.room.core.EnterUserInfo;
import com.wushuangtech.room.core.RoomLiveHelp;
import com.wushuangtech.room.core.RoomLiveInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.tttlive.education.base.MyApplication.getContext;

/**
 * Created by Iverson on 2018/3/6 上午10:44
 * 此类用于：
 * 教师端
 */
public class TeacherActivity extends BaseLiveActivity implements View.OnClickListener, TeacherUIinterface, RoomLiveInterface, RoomMsg {

    private Context tContext;
    private static String TAG_NAME = TeacherActivity.class.getSimpleName();

    private int currentCheckedItemDefault = -1;
    private int tPage = 1;
    public static final String ROOM_TYPE = "live_type";
    public static final String ROOM_USER_ID = "user_id";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_USER_NICKNAME = "room_user_nickName";
    public static final String ROOM_TU_URL = "room_tu_url";
    public static final String ROOM_COURSE_TYPE = "room_course_type";

    private Map<String, JoinRoomBean> roomJoinMap = new LinkedHashMap<>();
    private ArrayList<LiveView> mLiveViewList = new ArrayList();
    private ArrayList<ImageView> mListImageView = new ArrayList<>();
    private ArrayList<DocsListbean.ListBean> listbean = new ArrayList<>();
    private List<String> infoUserList = new ArrayList<>();
    private ArrayList<View> mLocalTextView = new ArrayList<>();
    //申请连麦的人员
    private List<String> listLmUserid = new ArrayList<>();
    //正在连麦人员
    private List<String> nowListLmUserid = new ArrayList<>();

    private String tPageSize = "5";
    private String teacher_room_id;
    private String teacher_user_id;
    private String teacher_room_type;
    private String teacher_user_name;
    private String teacher_room_tu_url;
    private String teacher_user_image;
    private String room_course_type;
    private String docDetails;
    private boolean isCamera = false;
    private boolean isSpeak = false;
    private boolean loadDocBoolean = false;     //是否有文档共享
    private boolean switchCamera = false;
    private boolean imageHDCamera = false;
    private boolean live_start_not = false;
    private boolean isAnimation = false;
    private int lmReq = 0;
    private int popViewNum;
    private int mRoomid;
    private int mUserid;

    private RoomLiveHelp mRoomLiveHelp;

    private List<String> imagUrls;
    private ImageView image_back;
    private ImageView image_share;
    private RadioButton rg_im_button;
    private RadioButton rg_personnel_button;
    private RadioGroup rg_course_group;

    private ChatFragment mChatFragment;
    private CourseWareAdapter mCourseWareAdapter;
    private PagerAdapter teacherAdapter;
    private FragmentStatePagerAdapter paperAdapter;

    private MdocOnItemClickListener mdocOnItemClickListener = new MdocOnItemClickListener();
    private DocsPtrHadler docsPtrHadler = new DocsPtrHadler();

    private MaterialBadgeTextView material_badge_view;
    private MaterialBadgeTextView mbt_personnel_lm;
    private RelativeLayout rl_teacher_video;
    private ImageView iv_personnel_detail;
    private ImageView iv_teacher_view;
    private LiveView rl_live_video_one;
    private LiveView rl_live_video_two;
    private LiveView rl_live_video_three;
    private LiveView rl_live_video_four;
    private LiveView rl_live_video_five;
    private LiveView rl_live_video_six;
    private LiveView rl_live_video_seven;
    private LiveView rl_live_video_eight;
    private LiveView rl_live_head_video_student;

    private ImageView iv_live_video_one;
    private ImageView iv_live_video_two;
    private ImageView iv_live_video_three;
    private ImageView iv_live_video_four;
    private ImageView iv_live_video_five;
    private ImageView iv_live_video_six;
    private ImageView iv_live_video_seven;
    private ImageView iv_live_video_eight;
    private ImageView iv_live_image_view_student;

    private LinearLayout ll_teacher_room;
    private Button Microphone, Camera, Beauty;
    private RelativeLayout popup_bg;
    private ImageView iv_pop_window_back;
    private RecyclerView rv_pop_lm;
    private FrameLayout fl_live_personnel;

    private PopupWindow popupWindow;
    private PopupWindow coursePop;
    private PopupWindow popBanLiveSpeak;
    private ChatPopupWindow chatPop;
    private Button bt_radio_brush;
    private ImageView bt_radio_courseware;
    private Button bt_radio_eliminate;
    private RecyclerView rv_popup_view;
    private LinearLayout ll_item_a;
    private ViewPager mViewpage;
    private TeacherPresenter teacherPresenter;
    private PtrClassicFrameLayout ptr_load_docs;
    private TextView image_start_live;
    private LinearLayout ll_in_display;
    private ImageView bt_room_ohone;
    private ImageView bt_room_camer;
    private ImageView bt_room_tool;
    private ImageView iv_animation_view;
    private TextView page_textview_number;

    private LinearLayout mRl_live_lv_parent;
    private LinearLayout ll_live_video_one;
    private LinearLayout ll_live_video_two;
    private LinearLayout ll_live_video_three;


    private ImageView iv_live_image_close;
    private RelativeLayout rl_microphone_open_two;
    private RelativeLayout rl_microphone_open_one;
    private RadioButton rd_video_q;
    private RadioButton rd_video_h;
    private RadioButton rb_quality_smooth;
    private RadioButton rb_quality_hd;
    private CustomDialog dialog;

    private String seqid;
    private ImageView img_chat;//弹出聊天页面
    private LinearLayout bottom_tool;

    private ArrayList<SendTextMessageBean> msgList = new ArrayList<>();//聊天消息列表
    private WebView teach_web_view;
    private ImageView iv_live_camera_flip;
    private LinearLayout ll_tran_live;
    private TextView tv_speak_cannel;
    private TextView tv_ban_live_speak;
    private TextView local_video_up;
    private TextView local_audio_up;
    private View clude_local_one;
    private View clude_local_two;
    private View clude_local_three;
    private View clude_local_four;
    private View clude_local_five;
    private View clude_local_six;
    private View clude_local_seven;
    private View clude_local_eight;
    private View clide_local_nine;
    private TextView local_video_text;
    private TextView local_audio_text;
    private TextView local_user_id;
    private TextView local_room_id;

    private WebviewToolPopupWindow mWebviewToolPopupWindow;
    private PopupWindow personnelPopWindow;
    private ImageView mIvPreviousPage;
    private ImageView mIvNextPage;
    private WebSocketService tWsService;

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

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_teacher;

    }

    /**
     * @param context
     * @param roomType 入会类型
     * @param userId
     * @param roomId
     * @return
     */
    public static Intent createIntent(Context context, int roomType, String userId, String roomId, String naickNima, String tuUrl, String courseType, String remark) {
        Intent intent = new Intent(context, TeacherActivity.class);
        intent.putExtra(ROOM_TYPE, String.valueOf(roomType));
        intent.putExtra(ROOM_USER_ID, userId);
        intent.putExtra(ROOM_ID, roomId);
        intent.putExtra(ROOM_USER_NICKNAME, naickNima);
        intent.putExtra(ROOM_TU_URL, tuUrl);
        intent.putExtra(ROOM_COURSE_TYPE, courseType);
        return intent;
    }

    @Override
    protected void findView() {
        tContext = this;
        image_back = findViewById(R.id.image_view_back);
        image_share = findViewById(R.id.image_view_share);
        rg_course_group = findViewById(R.id.rg_course_group);
        rg_im_button = findViewById(R.id.rg_course_im_radio_button);
        rg_personnel_button = findViewById(R.id.rg_course_personnel_radio_button);
        material_badge_view = findViewById(R.id.materialBadge_textview_chat);
        img_chat = findViewById(R.id.img_chat);
        bottom_tool = findViewById(R.id.bottom_tool);
        teach_web_view = findViewById(R.id.teacher_view_web);
        iv_live_camera_flip = findViewById(R.id.iv_live_camera_flip);
        iv_live_camera_flip.setVisibility(View.VISIBLE);
        iv_personnel_detail = findViewById(R.id.iv_personnel_detail);
        mbt_personnel_lm = findViewById(R.id.mbt_personnel_lm);
        fl_live_personnel = findViewById(R.id.fl_personnel_live);
        fl_live_personnel.setVisibility(View.VISIBLE);
        ll_live_video_one = findViewById(R.id.ll_live_video_one);
        ll_live_video_two = findViewById(R.id.ll_live_video_two);
        ll_live_video_three = findViewById(R.id.ll_live_video_three);
        image_start_live = findViewById(R.id.image_start_live);
        image_start_live.setText(getResources().getString(R.string.start_course_req) + "\n" + getResources().getString(R.string.start_course_res));
        iv_animation_view = findViewById(R.id.iv_animation_view);
        page_textview_number = findViewById(R.id.page_textview_number);

        local_video_up = findViewById(R.id.local_video_up);
        local_audio_up = findViewById(R.id.local_audio_up);

        rl_teacher_video = findViewById(R.id.teacher_head_video);
        iv_teacher_view = findViewById(R.id.teacher_image_view);
        rl_live_video_one = findViewById(R.id.rl_live_video_one);
        rl_live_video_two = findViewById(R.id.rl_live_video_two);
        rl_live_video_three = findViewById(R.id.rl_live_video_three);
        rl_live_video_four = findViewById(R.id.rl_live_video_four);
        rl_live_video_five = findViewById(R.id.rl_live_video_five);
        rl_live_video_six = findViewById(R.id.rl_live_video_six);
        rl_live_video_seven = findViewById(R.id.rl_live_video_seven);
        rl_live_video_eight = findViewById(R.id.rl_live_video_eight);
        rl_live_head_video_student = findViewById(R.id.rl_live_head_video_student);

        iv_live_video_one = findViewById(R.id.iv_live_video_one);
        iv_live_video_two = findViewById(R.id.iv_live_video_two);
        iv_live_video_three = findViewById(R.id.iv_live_video_three);
        iv_live_video_four = findViewById(R.id.iv_live_video_four);
        iv_live_video_five = findViewById(R.id.iv_live_video_five);
        iv_live_video_six = findViewById(R.id.iv_live_video_six);
        iv_live_video_seven = findViewById(R.id.iv_live_video_seven);
        iv_live_video_eight = findViewById(R.id.iv_live_video_eight);
        iv_live_image_view_student = findViewById(R.id.iv_live_image_view_student);

        iv_live_image_close = findViewById(R.id.iv_live_microphone_close);
        rl_microphone_open_two = findViewById(R.id.rl_live_microphone_two);
        rl_microphone_open_one = findViewById(R.id.rl_live_microphone_one);

        ll_teacher_room = findViewById(R.id.main);
        bt_radio_brush = findViewById(R.id.bt_radio_brush);
        bt_radio_courseware = findViewById(R.id.bt_radio_courseware);
        bt_radio_eliminate = findViewById(R.id.bt_radio_eliminate);
        mViewpage = findViewById(R.id.viewpage);

        ll_in_display = findViewById(R.id.ll_in_display);
        bt_room_ohone = findViewById(R.id.bt_microphone);
        bt_room_camer = findViewById(R.id.bt_camera);
        bt_room_tool = findViewById(R.id.bt_tool);
        EventBus.getDefault().register(this);

        //小班课
        mRl_live_lv_parent = findViewById(R.id.rl_live_lv_parent);

        clude_local_one = findViewById(R.id.clude_local_one);
        clude_local_two = findViewById(R.id.clude_local_two);
        clude_local_three = findViewById(R.id.clude_local_three);
        clude_local_four = findViewById(R.id.clude_local_four);
        clude_local_five = findViewById(R.id.clude_local_five);
        clude_local_six = findViewById(R.id.clude_local_six);
        clude_local_seven = findViewById(R.id.clude_local_seven);
        clude_local_eight = findViewById(R.id.clude_local_eight);
        clide_local_nine = findViewById(R.id.clude_local_nine);
        mIvPreviousPage = findViewById(R.id.iv_previous_page);
        mIvNextPage = findViewById(R.id.iv_next_page);

        local_user_id = findViewById(R.id.local_user_id);
        local_room_id = findViewById(R.id.local_room_id);


    }

    @Override
    protected void initView() {
        bindService(WebSocketService.createIntent(TeacherActivity.this), teWsConnection, BIND_AUTO_CREATE);
        teacherPresenter = new TeacherPresenter(this);
        mRoomLiveHelp = new RoomLiveHelp(this, this);
        Constant.LIVE_PERSONNEL_TYPE = Constant.LIVE_PERSONNEL_TYPE_TEACHER;
        initPersonnerView();
        Intent mIntent = getIntent();

        teacher_user_name = mIntent.getStringExtra(ROOM_USER_NICKNAME);
        room_course_type = mIntent.getStringExtra(ROOM_COURSE_TYPE);
        teacher_room_id = mIntent.getStringExtra(ROOM_ID);
        teacher_user_id = mIntent.getStringExtra(ROOM_USER_ID);
        teacher_room_type = mIntent.getStringExtra(ROOM_TYPE);
        //teacher_room_tu_url = mIntent.getStringExtra(ROOM_TU_URL);
        teacher_user_image = "";

        mRoomid = Integer.parseInt(teacher_room_id);
        mUserid = Integer.parseInt(teacher_user_id);

        if (TextUtils.isEmpty(teacher_user_name)) {
            teacher_user_name = getResources().getString(R.string.live_teacher) + " " + teacher_user_id;
        } else {
            teacher_user_name = getResources().getString(R.string.live_teacher) + " " + teacher_user_id;
        }
        Log.e(TAG_NAME, " nickname:" + teacher_user_name);
        Constant.LIVE_TYPE_ID = teacher_user_id;
        imagUrls = new ArrayList<>();
        mRadioButtons[0] = findViewById(R.id.rg_course_im_radio_button);
        mRadioButtons[1] = findViewById(R.id.rg_course_personnel_radio_button);
        mFragments[0] = mChatFragment.newInstance();

        rg_course_group.check(R.id.rg_course_im_radio_button);

        bt_radio_courseware.setVisibility(View.VISIBLE);
        //画笔功能暂时不显示
        //bt_radio_brush.setVisibility(View.VISIBLE);

        bt_radio_brush.setSelected(false);
        image_back.setOnClickListener(this);
        image_share.setOnClickListener(this);
        img_chat.setOnClickListener(this);
        bt_radio_brush.setOnClickListener(this);
        bt_radio_courseware.setOnClickListener(this);
        bt_room_tool.setOnClickListener(this);
        bt_radio_eliminate.setOnClickListener(this);
        bt_room_camer.setOnClickListener(this);
        bt_room_ohone.setOnClickListener(this);
        iv_live_camera_flip.setOnClickListener(this);
        image_start_live.setOnClickListener(this);
        iv_animation_view.setOnClickListener(this);

        rl_live_head_video_student.setOnClickListener(this);
        rl_live_video_one.setOnClickListener(this);
        rl_live_video_two.setOnClickListener(this);
        rl_live_video_three.setOnClickListener(this);
        rl_live_video_four.setOnClickListener(this);
        rl_live_video_five.setOnClickListener(this);
        rl_live_video_six.setOnClickListener(this);
        rl_live_video_seven.setOnClickListener(this);
        rl_live_video_eight.setOnClickListener(this);
        mIvPreviousPage.setOnClickListener(this);
        mIvNextPage.setOnClickListener(this);
        iv_personnel_detail.setOnClickListener(this);

        if ("1".equals(room_course_type)) {
            mLiveViewList.add(rl_live_head_video_student);
            mLiveViewList.add(rl_live_video_one);
            mLiveViewList.add(rl_live_video_two);
            mLiveViewList.add(rl_live_video_three);
            //            mLiveViewList.add(rl_live_video_four);
            mLiveViewList.add(rl_live_video_five);
            mLiveViewList.add(rl_live_video_six);
            //            mLiveViewList.add(rl_live_video_seven);
            //            mLiveViewList.add(rl_live_video_eight);

            mListImageView.add(iv_live_image_view_student);
            mListImageView.add(iv_live_video_one);
            mListImageView.add(iv_live_video_two);
            mListImageView.add(iv_live_video_three);
            //            mListImageView.add(iv_live_video_four);
            mListImageView.add(iv_live_video_five);
            mListImageView.add(iv_live_video_six);
            //            mListImageView.add(iv_live_video_seven);
            //            mListImageView.add(iv_live_video_eight);

            //            mLocalTextView.add(clude_local_one);
            mLocalTextView.add(clude_local_two);
            mLocalTextView.add(clude_local_three);
            mLocalTextView.add(clude_local_four);
            //            mLocalTextView.add(clude_local_five);
            mLocalTextView.add(clude_local_six);
            mLocalTextView.add(clude_local_seven);
            //            mLocalTextView.add(clude_local_eight);
            //            mLocalTextView.add(clide_local_nine);

        } else if ("2".equals(room_course_type)) {
            //如果是一对一
            Constant.LIVE_COURSE_TYPE = Integer.parseInt(Constant.SMALLCOURSE);
        }
        chatPop = new ChatPopupWindow(tContext, teacher_room_id, teacher_user_id, teacher_user_name, teacher_user_image);
        setRadioGroupListener();
        groudViewPage();
        //GlobalConfig.mPushUrl = teacher_room_tu_url;
        //初始化websocket消息
        initWsListeners();
        Long ct = System.currentTimeMillis();
        seqid = "binding_" + String.valueOf(ct);
        LocalSharedPreferencesStorage.putConfigStrValue(tContext, "teacher_user_name", teacher_user_name);
        LocalSharedPreferencesStorage.putConfigStrValue(tContext, "teacher_user_arar", teacher_user_image);
        LocalSharedPreferencesStorage.putConfigStrValue(tContext, "teacher_room_id", teacher_room_id);
        LocalSharedPreferencesStorage.putConfigStrValue(tContext, "teacher_user_id", teacher_user_id);
        loadRoomVideo(teacher_user_id, teacher_room_id, teacher_room_type);

        //一对一
        if ("2".equals(room_course_type)) {
            mRl_live_lv_parent.setVisibility(View.GONE);
        } else if ("1".equals(room_course_type)) {
            mRl_live_lv_parent.setVisibility(View.VISIBLE);
        } else if ("0".equals(room_course_type)) {
            //大班课
            Constant.LIVE_COURSE_TYPE = Integer.parseInt(Constant.MIDDLECOURSE);
            mRl_live_lv_parent.setVisibility(View.GONE);
        }

        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(wsbind(mRoomid, mUserid, seqid, 2));
        } else {
            tWsService.sendRequest(wsbind(mRoomid, mUserid, seqid, 2));
        }

        String inviteCode = "";//邀请码
        Log.e(TAG_NAME, " 课程id：" + teacher_room_id + "--用户id" + teacher_user_id + "--邀请码" + inviteCode);
        WebSettings webSettings = teach_web_view.getSettings();
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

        final int width = BaseTools.getWindowsWidth(this);
        if (width > 960) {
            teach_web_view.setInitialScale((int) (960f / width * 100));
        } else {
            teach_web_view.setInitialScale((int) (width / 960f * 100));
        }
        teach_web_view.loadUrl(Constant.DocUrl + "?inviteCode=&courseId=" + teacher_room_id + "&role=3&userId=" + teacher_user_id + "&appId=" + Constant.app_id);
        teach_web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webviewTool("boardSetTool(\'clearAll2\')");
        DisplayScreenSize();

    }

    /**
     * 打开主讲视频窗口
     */
    private void loadRoomVideo(String userId, String roomId, String roomType) {
        Log.e(TAG_NAME, " userId:" + userId + "==roomId:" + roomId);
        int i = 30;
        mRoomLiveHelp.enterRoom(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING, Constants.CLIENT_ROLE_ANCHOR, Integer.parseInt(roomId), Long.parseLong(userId), i ,"");
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
            //竖版暂时不维护
//            teacherPersonnel.setNowPersonnelLm(tContext, roomJoinMap, nowListLmUserid, listLmUserid,null);

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
            //竖版暂时不维护
//            teacherPersonnel.setNowPersonnelLm(tContext, roomJoinMap, nowListLmUserid, listLmUserid,null);
            toastShort("连麦成员断开");
        } else if (Constant.KICKED_OUT_PERSONNEL_ROOM.equals(messageEvent.getType())) {
            leaveRoomMapList(leaveGson.fromJson(messageEvent.getMessage(), LeaveMassageBean.class), 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wsDeleteMsg(teacher_room_id);
        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(wsbind(mRoomid, mUserid, seqid, 3));
        } else {
            tWsService.sendRequest(wsbind(mRoomid, mUserid, seqid, 3));
        }
        if (teWsConnection != null) {
            unbindService(teWsConnection);
        }
        teacherPresenter.unsubscribeTasks();
        roomJoinMap.clear();
        if (coursePop != null && coursePop.isShowing()) {
            coursePop.dismiss();
            coursePop = null;
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }

        //退出直播
        if (mRoomLiveHelp != null) {
            mRoomLiveHelp.exitHelp();
            mRoomLiveHelp = null;
        }
        if (infoUserList != null) {
            infoUserList.clear();
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
        if (mWebviewToolPopupWindow != null) {
            mWebviewToolPopupWindow.dismissPop();
        }
        live_start_not = false;
        DestoryLmList();
        PopDismiss();
        PopLmDismiss();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (live_start_not) {
            endLive(teacher_room_id, teacher_user_id);
        }
        sendLeaveMessage(mRoomLiveHelp, teacher_room_id, teacher_user_id, 1);
    }

    @Override
    public void showLoadingComplete() {
        super.showLoadingComplete();
        if (ptr_load_docs != null) {
            ptr_load_docs.refreshComplete();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_back:
                exitRoomDialog();
                break;
            case R.id.image_view_share:
//                showShare();
                break;
            case R.id.image_start_live:
                //开始上课
                startLive(teacher_room_id, teacher_user_id);
                live_start_not = true;
                image_start_live.setVisibility(View.GONE);
                if (isAnimation) {
                    iv_animation_view.setVisibility(View.VISIBLE);
                } else {
                    iv_animation_view.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_live_camera_flip:
                //showPopFormBottom(ll_teacher_room);
                if (switchCamera) {
                    mRoomLiveHelp.switchCamera();
                    switchCamera = false;
                    iv_live_camera_flip.setSelected(false);
                } else {
                    mRoomLiveHelp.switchCamera();
                    switchCamera = true;
                    iv_live_camera_flip.setSelected(true);

                }
                break;
            case R.id.bt_radio_brush:
                if (bt_radio_brush.isSelected()) {
                    bt_radio_brush.setSelected(false);
                } else {
                    bt_radio_brush.setSelected(true);
                }
                break;
            case R.id.bt_radio_courseware:
                if (DateUtils.isFastClick()) {
                    showPopupCourseBottom(ll_teacher_room);
                    dialog = new CustomDialog(this, R.style.CustomDialog);
                    dialog.show();
                }

                break;
            case R.id.bt_tool:
                toolOpen();
                break;
            case R.id.iv_previous_page:
                webviewTool("previousPage()");
                if (mWebviewToolPopupWindow != null) {
                    //                    mWebviewToolPopupWindow.setCurrentWebViewTool();
                }
                webviewPage("getCurrentTotalPage()");
                break;
            case R.id.iv_next_page:
                webviewTool("nextPage()");
                if (mWebviewToolPopupWindow != null) {
                    //                    mWebviewToolPopupWindow.setCurrentWebViewTool();
                }
                webviewPage("getCurrentTotalPage()");
                break;
            case R.id.bt_radio_eliminate:
                break;
            case R.id.camera:
                if (Camera.isSelected()) {
                    Camera.setSelected(false);
                    videoClose();
                } else {
                    Camera.setSelected(true);
                    videoOpen();
                }
                break;
            case R.id.bt_selector_microphone:
                if (Microphone.isSelected()) {
                    Microphone.setSelected(false);
                    speakClos();
                } else {
                    Microphone.setSelected(true);
                    speakOpen();
                }
                break;
            case R.id.beauty:
                if (Beauty.isSelected()) {
                    Beauty.setSelected(false);
                } else {
                    Beauty.setSelected(true);
                }
                break;
            case R.id.popup_bg:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                break;

            case R.id.ll_item_pop_a:
                if (coursePop != null && coursePop.isShowing()) {
                    coursePop.dismiss();
                    coursePop = null;
                }
                break;
            case R.id.bt_camera:
                if (isCamera) {
                    videoOpen();
                } else {
                    videoClose();
                }
                break;
            case R.id.bt_microphone:
                if (isSpeak) {
                    speakOpen();

                } else {
                    speakClos();
                }
                break;
            case R.id.rd_video_q:
                if (switchCamera) {
                    mRoomLiveHelp.switchCamera();
                    switchCamera = false;
                }
                break;
            case R.id.rd_video_h:
                if (!switchCamera) {
                    mRoomLiveHelp.switchCamera();
                    switchCamera = true;
                }
                break;
            case R.id.rb_quality_smooth:
                imageHDCamera = true;
                break;
            case R.id.rb_quality_hd:
                imageHDCamera = false;
                break;
            case R.id.rl_live_head_video_student:
                disconnectLive(v);
                break;
            case R.id.rl_live_video_one:
                disconnectLive(v);
                break;
            case R.id.rl_live_video_two:
                disconnectLive(v);
                break;
            case R.id.rl_live_video_three:
                disconnectLive(v);
                break;
            case R.id.rl_live_video_four:
                disconnectLive(v);
                break;
            case R.id.rl_live_video_five:
                disconnectLive(v);
                break;
            case R.id.rl_live_video_six:
                disconnectLive(v);
                break;
            case R.id.rl_live_video_seven:
                disconnectLive(v);
                break;
            case R.id.rl_live_video_eight:
                disconnectLive(v);
                break;
            case R.id.ll_personnel_transparent_live:
                PopDismiss();
                break;
            case R.id.tv_pop_ban_speak_live:
                testDisconnectVideo(v, popViewNum);
                PopDismiss();
                break;
            case R.id.tv_pop_speak_cancle:
                PopDismiss();
                break;
            case R.id.img_chat:
                if (!chatPop.isShow()) {
                    chatPop.showChatPop(bottom_tool, TeacherActivity.this, msgList , false);
                    mbt_personnel_lm.setVisibility(View.GONE);

                }
                break;
            case R.id.iv_personnel_detail:
                personnelPopWindow(v);
                break;
            case R.id.iv_pop_personnel_back:
                PopLmDismiss();
                break;
            case R.id.rv_personnel_lm:
                PopLmDismiss();
                break;
            case R.id.iv_animation_view:
                //播放PPT动画
                webviewTool("nextStep()");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webviewPage("getCurrentTotalPage()");
                    }
                }, 500);

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * 显示和隐藏断开连麦 功能
     *
     * @param v
     */
    private void disconnectLive(View v) {
        Log.e(TAG_NAME, " disconnectLive : " + v);
        for (int i = 0; i < mLiveViewList.size(); i++) {
            if (mLiveViewList.get(i).equals(v)) {
                LiveView liveView = mLiveViewList.get(i);
                if (!liveView.isFree()) {
                    showPopupTeacherWindow(v, i);
                }
            }
        }
    }

    /**
     * 断开连麦弹窗
     *
     * @param view
     */
    private void showPopupTeacherWindow(View view, int popNum) {
        popViewNum = popNum;
        View popLiveView = LayoutInflater.from(tContext).inflate(R.layout.pop_live_speak_window, null);
        ll_tran_live = popLiveView.findViewById(R.id.ll_personnel_transparent_live);
        tv_ban_live_speak = popLiveView.findViewById(R.id.tv_pop_ban_speak_live);
        tv_speak_cannel = popLiveView.findViewById(R.id.tv_pop_speak_cancle);

        popBanLiveSpeak = new PopupWindow(popLiveView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popBanLiveSpeak.setAnimationStyle(R.style.AnimBottomIn);
        popBanLiveSpeak.setBackgroundDrawable(new BitmapDrawable());
        popBanLiveSpeak.setFocusable(true);
        popBanLiveSpeak.setOutsideTouchable(true);
        popBanLiveSpeak.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        ll_tran_live.setOnClickListener(this);
        tv_ban_live_speak.setOnClickListener(this);
        tv_speak_cannel.setOnClickListener(this);
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

    /**
     * 通过userid获取用户信息
     * 断开连麦
     *
     * @param v
     * @param num
     */
    private void testDisconnectVideo(View v, int num) {
        Gson disGson = new Gson();
        if (mLiveViewList != null && mLiveViewList.size() > 0) {
            LiveView liveView = mLiveViewList.get(num);
            if (!liveView.isFree()) {
                Iterator<String> leaveKey = roomJoinMap.keySet().iterator();
                String userId = String.valueOf(liveView.getFlagUserId());
                while (leaveKey.hasNext()) {
                    String key = leaveKey.next();
                    if (userId.equals(key)) {
                        String value_roomid = roomJoinMap.get(key).getData().getRoomId();
                        LmDisconnectBean lDb = new LmDisconnectBean();
                        LmDisconnectBean.DisData ld = new LmDisconnectBean.DisData();
                        lDb.setMessageType(Constant.LM_CLOSE_CALL);
                        ld.setAdminUserId(teacher_user_id);
                        ld.setRoomId(value_roomid);
                        ld.setUserId(userId);
                        lDb.setData(ld);
                        String lDbs = disGson.toJson(lDb);
                        if (Constant.wsService != null) {
                            Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(value_roomid), Integer.parseInt(teacher_user_id), 1, lDbs, seqid, 0, false));
                        } else {
                            tWsService.sendRequest(wsSendMsg(Integer.parseInt(value_roomid), Integer.parseInt(teacher_user_id), 1, lDbs, seqid, 0, false));
                        }
                        EventBus.getDefault().post(new MessageEvent(Constant.LM_CLOSE_CALL, lDbs));

                    }
                }
            }
        }
    }


    @Override
    public void showNetworkException() {
        super.showNetworkException();
        dialog.dismiss();
    }

    private void setRadioGroupListener() {
        rg_course_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
     * 课件列表弹窗
     *
     * @param view
     */
    private void showPopupCourseBottom(View view) {
        View courseView = LayoutInflater.from(tContext).inflate(R.layout.pop_courseware_window, null);
        coursePop = new PopupWindow(courseView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        rv_popup_view = courseView.findViewById(R.id.popup_recycler_view);
        ll_item_a = courseView.findViewById(R.id.ll_item_pop_a);
        ptr_load_docs = courseView.findViewById(R.id.ptr_load_data_docs);
        ptr_load_docs.disableWhenHorizontalMove(true);
        ptr_load_docs.setPtrHandler(docsPtrHadler);
        tPage = 1;
        Log.e(TAG_NAME, "userid:" + teacher_user_id);
        teacherPresenter.getDocsLists(teacher_user_id, String.valueOf(tPage), tPageSize, "");


        coursePop.setAnimationStyle(R.style.AnimBottomIn);
        coursePop.setBackgroundDrawable(new BitmapDrawable());
        coursePop.setFocusable(true);
        coursePop.setOutsideTouchable(true);
        coursePop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        ll_item_a.setOnClickListener(this);

    }


    /**
     * 设置按钮
     *
     * @param view
     */
    private void showPopFormBottom(View view) {
        //自定义PopupWindow的布局
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.settings, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        Microphone = contentView.findViewById(R.id.bt_selector_microphone);
        Camera = contentView.findViewById(R.id.camera);
        Beauty = contentView.findViewById(R.id.beauty);
        popup_bg = contentView.findViewById(R.id.popup_bg);
        rd_video_q = contentView.findViewById(R.id.rd_video_q);
        rd_video_h = contentView.findViewById(R.id.rd_video_h);
        rb_quality_smooth = contentView.findViewById(R.id.rb_quality_smooth);
        rb_quality_hd = contentView.findViewById(R.id.rb_quality_hd);

        Microphone.setSelected(true);
        Camera.setSelected(true);
        Beauty.setSelected(false);
        if (switchCamera) {
            rd_video_h.setChecked(true);
        } else {
            rd_video_q.setChecked(true);
        }
        if (imageHDCamera) {
            rb_quality_smooth.setChecked(true);
        } else {
            rb_quality_hd.setChecked(true);
        }
        Microphone.setOnClickListener(this);
        Camera.setOnClickListener(this);
        Beauty.setOnClickListener(this);
        popup_bg.setOnClickListener(this);
        rd_video_q.setOnClickListener(this);
        rd_video_h.setOnClickListener(this);
        rb_quality_smooth.setOnClickListener(this);
        rb_quality_hd.setOnClickListener(this);

        popupPhoneCrame();

        popupWindow.setAnimationStyle(R.style.AnimBottomIn);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置PopupWindow显示的位置
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);


    }

    /**
     * 课程列表
     *
     * @param docsLists
     */
    @Override
    public void docSucess(DocsListbean docsLists) {
        Log.e(TAG_NAME, "    " + docsLists);
        listbean.clear();
        if (docsLists != null && docsLists.getList().size() > 0) {
            listbean.addAll(docsLists.getList());
            tPage++;
            dialog.dismiss();
            if (rv_popup_view != null) {
                rv_popup_view.setLayoutManager(new LinearLayoutManager(tContext));
                mCourseWareAdapter = new CourseWareAdapter(tContext, listbean);
                mCourseWareAdapter.check(currentCheckedItemDefault, true);
                rv_popup_view.setAdapter(mCourseWareAdapter);
                mCourseWareAdapter.setOnItemClickListener(mdocOnItemClickListener);
            }
        } else if (tPage == 1) {
            toastShortLong("当前课程暂时没有课件");
            //            if (coursePop != null && coursePop.isShowing()) {
            //                coursePop.dismiss();
            //                coursePop = null;
            //            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            toastShortLong("已是最后一页了");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 课程列表详情
     *
     * @param docDetail
     */
    @Override
    public void docDetailsSucess(DocsListDetailsBean docDetail) {

        docDetail.setImgSrcData("");
        String docdata = new Gson().toJson(docDetail);
        Log.e(TAG_NAME, " --  " + docdata);

        //调js
        DocOpenCount(docdata);
    }

    /**
     * 上传图片返回值
     *
     * @param uploadBean
     */
    @Override
    public void imageUploadSucess(BaseResponse<UploadImageBean> uploadBean) {
        Log.e(TAG_NAME, "上传成功 url " + uploadBean.getData().getUrl());
        sendTextMessage(uploadBean.getData().getUrl(), Constant.SEND_MESSAGE_TYPE_IMAGE);
    }

    /**
     * 收到聊天消息
     *
     * @param data
     */
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

    /**
     * 收到观众加入房间的回调
     *
     * @param data
     */
    @Override
    public void joinRoomSuccess(String data) {
        Gson mGson = new Gson();
        Log.e(TAG_NAME, "收到观众加入房间的回调data:" + data);
        JoinRoomBean joinRoomBean = mGson.fromJson(data, JoinRoomBean.class);
        joinRoomMapList(joinRoomBean, 1);
        sendQunLmListAccept(mRoomLiveHelp, data);
        Log.e(TAG_NAME, "onMemberEnter:房间人员连接数量  " + roomJoinMap.size());


    }

    /**
     * 学生退出房间的回调
     *
     * @param data
     */
    @Override
    public void leaveMessage(String data) {
        Log.e(TAG_NAME, " 退出房间的回调data:" + new Gson().toJson(data));
        Gson leaveGson = new Gson();
        LeaveMassageBean leaveRoomBean = leaveGson.fromJson(data, LeaveMassageBean.class);
        leaveRoomMapList(leaveRoomBean, 1);
    }

    /**
     * 加入房间增加用户名单
     *
     * @param mJoinRoomBean
     */
    private void joinRoomMapList(JoinRoomBean mJoinRoomBean, int type) {
        //判断人员是否存在
        Iterator<String> leaveKey = roomJoinMap.keySet().iterator();
        String joinId = String.valueOf(mJoinRoomBean.getData().getUserId());
        while (leaveKey.hasNext()) {
            String key = leaveKey.next();
            if (joinId.equals(key)) {
                return;
            }
        }

        if (roomJoinMap != null && roomJoinMap.size() >= 7) {
            //            toastShort("房间人数达到上限");
            sendKickingMessage(mRoomLiveHelp, new Gson().toJson(mJoinRoomBean));
            return;
        }
        roomJoinMap.put(mJoinRoomBean.getData().getUserId(), mJoinRoomBean);

        if (listLmUserid != null && listLmUserid.size() > 0 || nowListLmUserid != null && nowListLmUserid.size() > 0) {
//            teacherPersonnel.setPersonnelAll(tContext, roomJoinMap, nowListLmUserid, listLmUserid,null);
        } else {
//            teacherPersonnel.initViewPersonnel(tContext, new PersonnelEvent(Constant.SEND_EVENT_PERSONNEL, "", roomJoinMap),null);
        }
        if (type == 1) {
            sendQunJoinRoomPeonnel(mRoomLiveHelp, teacher_room_id,
                    teacher_user_id, teacher_user_name, teacher_user_image, roomJoinMap , "",null);
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
        //        EventBus.getDefault().post(new PersonnelEvent(Constant.SEND_EVENT_PERSONNEL, leaveId, roomJoinMap));
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

        if (listLmUserid != null && listLmUserid.size() > 0 || nowListLmUserid != null && nowListLmUserid.size() > 0) {
//            teacherPersonnel.setPersonnelAll(tContext, roomJoinMap, nowListLmUserid, listLmUserid,null);
        } else {
//            teacherPersonnel.initViewPersonnel(tContext, new PersonnelEvent(Constant.SEND_EVENT_PERSONNEL, "", roomJoinMap),null);
        }

        if (type == 1) {
            sendQunJoinRoomPeonnel(mRoomLiveHelp, teacher_room_id, teacher_user_id,
                    teacher_user_name, teacher_user_image, roomJoinMap, "" ,null);
        }

    }

    /**
     * 连麦请求回调
     *
     * @param data
     */
    @Override
    public void dealApplyMicMessage(String data) {
        Log.e(TAG_NAME, " 连麦请求回调data:" + new Gson().toJson(data));
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
        //        EventBus.getDefault().post(new MessageEvent(Constant.SEND_EVENT_LMREQ, data));
        listLmUserid.add(lmBean.getData().getUserId());
        if (nowListLmUserid != null && nowListLmUserid.size() > 0) {
//            teacherPersonnel.setPersonnelAll(tContext, roomJoinMap, nowListLmUserid, listLmUserid,null);
        } else {
//            teacherPersonnel.setPersonnelLm(tContext, roomJoinMap, listLmUserid,null);
        }
    }

    /**
     * 连麦响应
     *
     * @param data
     */
    @Override
    public void dealApplyAgreeMessage(String data) {
        Log.e(TAG_NAME, "连麦响应data:" + new Gson().toJson(data));
        Gson lGson = new Gson();
        LmAgreeResBean lag = lGson.fromJson(data, LmAgreeResBean.class);
        if (lag.getData().getType().equals("1")) {
            //                        sendAcceptLmList(lag);
            showRemoteView(lag.getData().getUserId());
        } else {
            Toast.makeText(tContext, "对方拒绝了你的请求", Toast.LENGTH_SHORT).show();
        }
        EventBus.getDefault().post(new MessageEvent(Constant.SEND_EVENT_STUDENT_REFUSED, data));
    }

    /**
     * 发送连麦列表
     *
     * @param lmAgreeResBean
     */
    private void sendAcceptLmList(LmAgreeResBean lmAgreeResBean) {
        Long ct = System.currentTimeMillis();
        String seqid = String.valueOf(ct);
        //        String lmList = BaseLiveActivity.acceptLmListPersion(lmAgreeResBean);
        //        mRoomLiveHelp.sendMessage(0, 1, seqid,lmList );
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
    }

    @Override
    public void courseLeave(String data) {
        //下课
    }

    @Override
    public void courseTeacherNotStart(String data) {
        //是否已经开课
        Log.e(TAG_NAME, "是否已经开课 " + data);
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
        //收到件奖杯
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
        //adapter = new ChatAdapter(getContext(),msgList);
        //mImRecyclerView.setAdapter(adapter);

        chatPop.showChatList(msgList);
        //   mRoomLiveHelp.sendMessage(0, 1, seqid, ss);

        if (Constant.wsService != null) {
            Constant.wsService.sendRequest(wsSendMsg(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), 1, ss, seqid, 0, true));
        } else {
            tWsService.sendRequest(wsSendMsg(Integer.parseInt(teacher_room_id), Integer.parseInt(teacher_user_id), 1, ss, seqid, 0, true));
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
                //                teacherPresenter.getDocsListDetails(listbean.get(position).getId());
                String docdata = gson.toJson(listbean.get(position));
                currentCheckedItemDefault = position;
                mCourseWareAdapter.check(position, true);
                if (!TextUtils.isEmpty(listbean.get(position).getHtmlUrl())) {
                    isAnimation = true;
                } else {
                    isAnimation = false;
                }
                if (live_start_not) {
                    iv_animation_view.setVisibility(View.VISIBLE);
                } else {
                    iv_animation_view.setVisibility(View.GONE);
                }
                DocOpenCount(docdata);
            }

        }
    }

    /**
     * 发送课件详情到JS调用
     *
     * @param docdata
     */
    private void DocOpenCount(String docdata) {

        //调js
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            teach_web_view.evaluateJavascript("javascript:docsOpen(\'" + docdata + "\')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.e(TAG_NAME, " js回调  " + value);
                }
            });
        } else {
            teach_web_view.loadUrl("javascript:docsOpen(\'" + docdata + "\')");
        }
        webviewTool("boardSetTool(\'clearAll2\')");
        webviewPage("getCurrentTotalPage()");
        if (mWebviewToolPopupWindow != null) {
            //mWebviewToolPopupWindow.setCurrentWebViewTool();
            mWebviewToolPopupWindow.trunPage();
        }
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
     * viwePager 滑动操作
     */
    private void groudViewPage() {
        paperAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

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
                rg_course_group.setOnCheckedChangeListener(null);
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
    public void enterRoomSuccess() {
        Log.e(TAG_NAME, "enterRoomFailue:进入房间成功");
        iv_teacher_view.setVisibility(View.GONE);
        ll_in_display.setVisibility(View.VISIBLE);
        speakOpen();
        videoOpen();
        bt_room_ohone.setBackground(getResources().getDrawable(R.drawable.icon_microphone_normal));
        bt_room_camer.setBackground(getResources().getDrawable(R.drawable.icon_camera_normal));
        mRoomLiveHelp.openLocalVideo(rl_teacher_video, false, Constants.CLIENT_ROLE_ANCHOR, 30);
        teacherJoinBean();
        local_user_id.setText(teacher_user_id);
        local_room_id.setText(teacher_room_id);
    }

    /**
     * 老师进入房间个人消息
     */
    private void teacherJoinBean() {
        JoinRoomBean stMessAge = new JoinRoomBean();
        JoinRoomBean.DataBean stDb = new JoinRoomBean.DataBean();
        stMessAge.setMessageType(Constant.JOINREQ);
        stDb.setRoomId(teacher_room_id);
        stDb.setUserId(teacher_user_id);
        stDb.setNickName(teacher_user_name);
        stDb.setAvatar(teacher_user_image);
        stDb.setMessage("");
        stDb.setRole(0);//角色
        stDb.setLevel(0);//等级
        stDb.setMasterUserId(Integer.parseInt(teacher_user_id));
        stDb.setMasterNickName(teacher_user_name);
        stDb.setMasterAvatar(teacher_user_image);
        stDb.setMasterLevel(0);
        stDb.setBalance("");
        stMessAge.setData(stDb);
        joinRoomMapList(stMessAge, 0);
    }

    @Override
    public void enterRoomFailue(int error) {
        Log.e(TAG_NAME, "enterRoomFailue:进入房间失败" + error);

    }

    @Override
    public void onDisconnected(int errorCode) {
        Log.e(TAG_NAME, "onDisconnected:直播中断线" + errorCode);
        if (errorCode == 205) {
            toastShort("此账号在其他地方登陆");
            onBackPressed();
        } else if (errorCode == 100) {
            toastShort("网络异常,服务已断开");
            onBackPressed();
        } else {
            toastShort("直播中断线");
        }
    }

    @Override
    public void onMemberExit(long userId) {
        Log.e(TAG_NAME, "onMemberExit:直播成员退出  " + userId);
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
    public void onMemberEnter(long userId, EnterUserInfo userInfo , String sCustom) {
        Log.e(TAG_NAME, "onMemberEnter:直播成员进入  " + userId);

    }

    @Override
    public void onHostEnter(long userId, EnterUserInfo userInfo , String sCustom) {
        Log.e(TAG_NAME, "onHostEnter:直播主播进入  " + userId);

    }

    @Override
    public void onUpdateLiveView(List<EnterUserInfo> userInfos) {
        Log.e(TAG_NAME, "onUpdateLiveView ---------  ");
    }

    @Override
    public void dispatchMessage(long srcUserID, int type, String sSeqID, String data) {
        Log.e(TAG_NAME, " 收到消息srcUserID:" + srcUserID + "type:--" + type + "-sSeqID:--" + sSeqID + "-data:--" + data);
    }


    //初始化websocket消息
    private void initWsListeners() {

        WsListener notifListener = new WsListener() {

            @Override
            public void handleData(String msg) {
                RoomMessageType msgType = new RoomMessageType(TeacherActivity.this);
                msgType.appendData(msg);

            }
        };
        if (Constant.wsService != null && notifListener != null) {
            Constant.wsService.registerListener(notifListener);
        } else {
            Log.e(TAG_NAME, " WsListener wsService : " + Constant.wsService);
            Log.e(TAG_NAME, " WsListener notifListener : " + notifListener);
            Log.e(TAG_NAME, " WsListener tWsService : " + tWsService);
            tWsService.registerListener(notifListener);

        }
    }

    @Override
    public void sendMessageResult(int resultType, String data) {
        //收到自己发送的消息
    }

    @Override
    public void localVideoStatus(LocalVideoStats localVideoStats) {
        //接受到本地视频上行速率
        local_video_up.setText("V- " + String.valueOf(localVideoStats.getSentBitrate()) + " " + getResources().getString(R.string.video_up));
    }

    @Override
    public void remoteVideoStatus(RemoteVideoStats mRemoteVideoStats) {
    }

    @Override
    public void LocalAudioStatus(LocalAudioStats localAudioStats) {
        //接收本地音频上行速率
        local_audio_up.setText("A- " + String.valueOf(localAudioStats.getSentBitrate()) + " " + getResources().getString(R.string.video_up));
    }

    @Override
    public void remoteAudioStatus(RemoteAudioStats mRemoteAudioStats) {
        //接收远端音频下行速率
    }

    @Override
    public void OnupdateUserBaseInfo(Long roomId, long uid, String sCustom) {
        //用户信息更新
    }

    @Override
    public void OnConnectSuccess(String ip, int port) {

    }

    /**
     * 显示视频
     *
     * @param
     */
    private void showRemoteView(String userid) {
        LiveView freeViewLive = getFreeViewLive();
        freeViewLive.setFlagUserId(Long.valueOf(userid));
        freeViewLive.setFree(false);
        mRoomLiveHelp.openIdRemoteVideo(freeViewLive, Long.valueOf(userid), true);
        //updateViewSei();
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
     * 处理连麦窗口图片隐藏
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
     * 处理连麦窗口的显示
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


    /**
     * 释放直播控件
     * 不补位
     *
     * @param userId
     */
    private void releaseLiveView(long userId) {
        if (userId == 0)
            return;
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
     * 释放直播控件,
     * 可以自动补位
     *
     * @param userId
     */
    private void releaseLiveFillView(long userId) {
        if (userId == 0)
            return;
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
        //        updateViewSei(userId);
    }


    /**
     * 退出直播间
     */
    private void exitRoomDialog() {
        final MessageDialog exitDialog = new MessageDialog(tContext);
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

    private void videoClose() {
        mRoomLiveHelp.controlLocalVideo(true);
        bt_room_camer.setBackground(getResources().getDrawable(R.drawable.icon_camera_close));
        isCamera = true;
        videoSpeakType();
    }

    private void videoOpen() {
        mRoomLiveHelp.controlLocalVideo(false);
        bt_room_camer.setBackground(getResources().getDrawable(R.drawable.icon_camera_normal));
        isCamera = false;
        videoSpeakType();
    }

    private void toolOpen() {
        if (mWebviewToolPopupWindow == null) {
            mWebviewToolPopupWindow = new WebviewToolPopupWindow(this, teach_web_view);
        }
        if (chatPop.isShow()) {
            chatPop.closePop();
        }
        mWebviewToolPopupWindow.showPop(ll_teacher_room);


    }

    private TeacherPersonnelUtil teacherPersonnel;

    /**
     * 初始化人员名单布局
     */
    private void initPersonnerView() {
        View popView = LayoutInflater.from(tContext).inflate(R.layout.pop_personnel_detail_list, null);
        personnelPopWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        iv_pop_window_back = popView.findViewById(R.id.iv_pop_personnel_back);
        rv_pop_lm = popView.findViewById(R.id.rv_personnel_lm);
        iv_pop_window_back.setOnClickListener(this);
        rv_pop_lm.setOnClickListener(this);
        teacherPersonnel = new TeacherPersonnelUtil(tContext, rv_pop_lm);
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

    private void speakClos() {
        mRoomLiveHelp.controlLocalAudio(true);
        bt_room_ohone.setBackground(getResources().getDrawable(R.drawable.icon_microphone_close));
        isSpeak = true;
        videoSpeakType();
    }

    private void speakOpen() {
        mRoomLiveHelp.controlLocalAudio(false);
        bt_room_ohone.setBackground(getResources().getDrawable(R.drawable.icon_microphone_normal));
        isSpeak = false;
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
     * 更新弹窗按钮的状态
     */
    private void popupPhoneCrame() {
        if (isCamera) {
            Camera.setSelected(false);
        } else {
            Camera.setSelected(true);
        }
        if (isSpeak) {
            Microphone.setSelected(false);
        } else {
            Microphone.setSelected(true);
        }

        if (switchCamera) {
            rd_video_q.setChecked(false);
            rd_video_h.setChecked(true);
        } else {
            rd_video_q.setChecked(true);
            rd_video_h.setChecked(false);
        }
    }

    /**
     * webview 白板工具栏
     */
    private void webviewTool(String tool) {
        if (teach_web_view != null) {
            String js = "javascript:" + tool;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                teach_web_view.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.e(TAG_NAME + " webview", value);
                    }
                });
            } else {
                teach_web_view.loadUrl(js);
            }
        }
    }

    /**
     * webview
     * 获取当前页码
     */
    private void webviewPage(String tool) {
        if (teach_web_view != null) {
            String js = "javascript:" + tool;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                teach_web_view.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.e(TAG_NAME + " webViewPage : ", value);
                        String page = value.replace("\"", "");
                        page_textview_number.setVisibility(View.VISIBLE);
                        page_textview_number.setText(page);
                    }
                });
            } else {
                teach_web_view.loadUrl(js);
            }
        }
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
     * 设置视频控件比例
     */
    private void DisplayScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mapWidth = dm.widthPixels;
        int mapHeight = dm.heightPixels;
        LinearLayout.LayoutParams linParams = (LinearLayout.LayoutParams) ll_live_video_one.getLayoutParams();
        linParams.width = mapWidth;
        linParams.height = mapWidth * 2 / 3 * 9 / 16;
        linParams.weight = 0;
        ll_live_video_one.setLayoutParams(linParams);
        LinearLayout.LayoutParams linparamsTwo = (LinearLayout.LayoutParams) ll_live_video_two.getLayoutParams();
        linparamsTwo.width = mapWidth;
        linparamsTwo.height = mapWidth / 3 * 11 / 16;
        linparamsTwo.weight = 0;
        ll_live_video_two.setLayoutParams(linparamsTwo);
        LinearLayout.LayoutParams linparamsThree = (LinearLayout.LayoutParams) ll_live_video_three.getLayoutParams();
        linparamsThree.width = mapWidth;
        linparamsThree.height = mapWidth / 3 * 11 / 16;
        linparamsThree.weight = 0;
        ll_live_video_three.setLayoutParams(linparamsThree);

        Log.e(TAG_NAME, "mapWidth : " + mapWidth + " mapHeight : " + mapHeight);
    }

    //重新设置sei
    private void updateViewSei() {
        List<VideoCompositingLayout.Region> tempList = new ArrayList<>();
        //        VideoCompositingLayout.Region mhostRegion = new VideoCompositingLayout.Region();
        //        mhostRegion.uid = Long.parseLong(mUserId);
        //        mhostRegion.x = 0.4;
        //        mhostRegion.y = 0.8;
        //        mhostRegion.width = 0.2;
        //        mhostRegion.height = 0.2;
        //        mhostRegion.zOrder = 0;
        //        tempList.add(mhostRegion);
        int j = 0;
        for (LiveView liveView : mLiveViewList) {
            if (liveView.isFree())
                break;
            VideoCompositingLayout.Region mRegion = new VideoCompositingLayout.Region();
            mRegion.mUserID = liveView.getFlagUserId();
            mRegion.width = 0.33;
            mRegion.height = mRegion.width * 4 / 3;
            mRegion.x = j * 0.33;
            mRegion.y = 0.56;
            mRegion.zOrder = 1;
            tempList.add(mRegion);
            j++;
        }
        VideoCompositingLayout.Region[] mRegions = new VideoCompositingLayout.Region[tempList.size()];
        for (int k = 0; k < tempList.size(); k++) {
            VideoCompositingLayout.Region region = tempList.get(k);
            mRegions[k] = region;
        }
        mRoomLiveHelp.resetRemoteLayout(mRegions);
    }
}
