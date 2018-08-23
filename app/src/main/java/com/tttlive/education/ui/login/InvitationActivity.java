package com.tttlive.education.ui.login;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.data.InviteCodeLoginBean;
import com.tttlive.education.ui.room.BaseLiveActivity;
import com.tttlive.education.ui.room.PopupWindowSyles;
import com.tttlive.education.ui.room.bean.LocationBean;
import com.tttlive.education.ui.room.socket.WebSocketService;
import com.tttlive.education.util.CustomPopWindow;
import com.tttlive.education.util.DateUtils;
import com.tttlive.education.util.SPTools;
import com.tttlive.education.util.Tools;
import com.wushuangtech.library.Constants;
import com.wushuangtech.room.core.TTTSDK;

/**
 * Created by Administrator on 2018/7/9/0009.
 * 邀请码登录界面
 */
public class InvitationActivity extends BaseActivity implements View.OnClickListener, LoginInterface,
        View.OnFocusChangeListener, TextWatcher ,SplashInterface {

    private static final String TAG_CLASS = InvitationActivity.class.getSimpleName();

    //输入错误信息提示弹窗
    private CustomPopWindow mPopupWindow;
    private SplashPresenter splashPresenter;

    private boolean connection = false;

    private TextView tv_account_back;
    private EditText et_invitation_code;
    private EditText et_invitation_name;
    private LinearLayout invitation_login_live;
    private String invitation_code;
    private String invitation_name;
    private LoginPresenter mPresenter;
    private PopupWindowSyles popupWindowSyles;
    private LinearLayout ll_invitation_main;
    private ImageView iv_del_invitation_code;
    private ImageView iv_del_nickname;
    private LinearLayout ll_divider_line_code;
    private LinearLayout ll_divider_line_nickname;
    private TextView tv_version_code;


    private ServiceConnection wsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Mainactivity", "Service connected.");
            Constant.wsService = ((WebSocketService.ServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // L.i(LOG_TAG, "Service disconnected.");
        }
    };


    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_invitation_code;
    }

    @Override
    protected void findView() {
        tv_account_back = findViewById(R.id.tv_account_back);
        et_invitation_code = findViewById(R.id.et_invitation_code);
        et_invitation_name = findViewById(R.id.et_invitation_nickname);
        invitation_login_live = findViewById(R.id.invitation_login_live);
        ll_invitation_main = findViewById(R.id.ll_invitation_main);
        iv_del_invitation_code = findViewById(R.id.iv_del_invitation_code);
        iv_del_nickname = findViewById(R.id.iv_del_nickname);
        ll_divider_line_code = findViewById(R.id.ll_divider_line_code);
        ll_divider_line_nickname = findViewById(R.id.ll_divider_line_nickname);
        tv_version_code = findViewById(R.id.tv_version_code);

    }


    @Override
    protected void initView() {
        mContext = this;
        mPresenter = new LoginPresenter(this);
        popupWindowSyles = new PopupWindowSyles(this , this);
        splashPresenter = new SplashPresenter(this);
        Tools.fullScreen(this);
        tv_account_back.setOnClickListener(this);
        invitation_login_live.setOnClickListener(this);
        iv_del_invitation_code.setOnClickListener(this);
        iv_del_nickname.setOnClickListener(this);
        et_invitation_name.setOnFocusChangeListener(this);
        et_invitation_code.setOnFocusChangeListener(this);
        et_invitation_code.addTextChangedListener(this);
        et_invitation_name.addTextChangedListener(this);
        invitation_login_live.setClickable(false);

        String nickName = SPTools.getInstance(this).getString(SPTools.KEY_NICKNAME, "");
        et_invitation_name.setText(nickName);
        tv_version_code.setText(Constant.APP_VERSION_CODE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG_CLASS, "onDestroy");
        splashPresenter.unsubscribeTasks();
        Constant.popupType = false;
        if (Constant.wsService != null) {
            Constant.wsService.prepareShutdown();
            if (wsConnection != null){
                if (connection){
                    unbindService(wsConnection);
                    connection = false;

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_account_back:
                onBackPressed();
                finish();
                break;
            case R.id.invitation_login_live:
                if (DateUtils.isFastClick()) {
                    showLoadingDialog();
                    inviteCodeLogin();
                }
                break;
            case R.id.iv_del_invitation_code:
                et_invitation_code.setText("");
                break;
            case R.id.iv_del_nickname:
                et_invitation_name.setText("");
                break;
        }

    }

    /**
     * 弹出验证弹窗
     *
     * @param message 弹窗消息
     */
    private void showPopupWindow(String message) {
        if (mPopupWindow == null) {
            mPopupWindow = new CustomPopWindow(this);
        }
        mPopupWindow.setMessage(message);
        mPopupWindow.showAtLocation(ll_invitation_main, Gravity.CENTER, 0, 0);
    }

    /**
     * 输入框焦点变化监听
     *
     * @param v        view
     * @param hasFocus 是否获取焦点
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.et_invitation_code) {
            boolean empty = TextUtils.isEmpty(et_invitation_code.getText().toString().trim());
            int isVisible = hasFocus && !empty ? View.VISIBLE : View.GONE;
            iv_del_invitation_code.setVisibility(isVisible);
        }
        if (v.getId() == R.id.et_invitation_nickname) {
            boolean empty = TextUtils.isEmpty(et_invitation_name.getText().toString().trim());
            int isVisible = hasFocus && !empty ? View.VISIBLE : View.GONE;
            iv_del_nickname.setVisibility(isVisible);
        }
        if (et_invitation_code.hasFocus()) {

            if (et_invitation_code.getText().toString().length() > 0) {
                et_invitation_code.setSelection(et_invitation_code.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_code, 1, R.color.color_FF1093ED);
            } else {
                changeDividerHeightAndColor(ll_divider_line_code, 0.5f, R.color.color_FFF1F1F1);
            }

            changeDividerHeightAndColor(ll_divider_line_nickname, 0.5f, R.color.color_FFF1F1F1);

        }
        if (et_invitation_name.hasFocus()) {

            if (et_invitation_name.getText().toString().length() > 0) {
                et_invitation_name.setSelection(et_invitation_name.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_nickname, 1, R.color.color_FF1093ED);
            } else {
                et_invitation_name.setSelection(et_invitation_name.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_nickname, 0.5f, R.color.color_FFF1F1F1);
            }
            changeDividerHeightAndColor(ll_divider_line_code, 0.5f, R.color.color_FFF1F1F1);

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        changeLoginBtnStatus();

        boolean isCodeEmpty = TextUtils.isEmpty(et_invitation_code.getText().toString().trim());
        boolean isNickNameEmpty = TextUtils.isEmpty(et_invitation_name.getText().toString().trim());
        iv_del_invitation_code.setVisibility(et_invitation_code.hasFocus() && !isCodeEmpty ? View.VISIBLE : View.GONE);
        iv_del_nickname.setVisibility(et_invitation_name.hasFocus() && !isNickNameEmpty ? View.VISIBLE : View.GONE);

        if (et_invitation_code.hasFocus()) {

            if (et_invitation_code.getText().toString().length() > 0) {
                et_invitation_code.setSelection(et_invitation_code.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_code, 1, R.color.color_FF1093ED);
            } else {
                changeDividerHeightAndColor(ll_divider_line_code, 0.5f, R.color.color_FFF1F1F1);
            }

            changeDividerHeightAndColor(ll_divider_line_nickname, 0.5f, R.color.color_FFF1F1F1);

        }
        if (et_invitation_name.hasFocus()) {

            if (et_invitation_name.getText().toString().length() > 0) {
                et_invitation_name.setSelection(et_invitation_name.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_nickname, 1, R.color.color_FF1093ED);
            } else {
                et_invitation_name.setSelection(et_invitation_name.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_nickname, 0.5f, R.color.color_FFF1F1F1);
            }
            changeDividerHeightAndColor(ll_divider_line_code, 0.5f, R.color.color_FFF1F1F1);

        }
    }

    /**
     * 改变下划线的高度和颜色
     *
     * @param dividerView 下划线view
     * @param height      高度
     * @param color       颜色
     */
    private void changeDividerHeightAndColor(View dividerView, float height, int color) {

        ViewGroup.LayoutParams mobileParams = dividerView.getLayoutParams();
        mobileParams.height = Tools.dip2px(dividerView.getContext(), height);
        dividerView.setLayoutParams(mobileParams);
        dividerView.setBackgroundResource(color);
    }


    /**
     * 改变按钮状态
     */
    private void changeLoginBtnStatus() {
        String code = et_invitation_code.getText().toString();
        String nickName = et_invitation_name.getText().toString();
        if (checkTextLength(code, 5, 6)
                && checkTextLength(nickName, 1, 20)) {
            invitation_login_live.setBackgroundResource(R.drawable.input_submit_hover);
            invitation_login_live.setClickable(true);
        } else {
            invitation_login_live.setBackgroundResource(R.drawable.input_submit_disable);
            invitation_login_live.setClickable(false);
        }
    }

    /**
     * 校验文本长度 (包含最小和最大边界)
     *
     * @param text      校验的文本
     * @param minLength 最小文本长度
     * @param maxLength 最大文本长度
     * @return
     */
    private boolean checkTextLength(String text, int minLength, int maxLength) {
        int length = text.length();
        if (length < minLength || length > maxLength) {
            return false;
        }
        return true;
    }


    /**
     * 邀请码登录
     * 判断逻辑
     */
    private void inviteCodeLogin() {
        invitation_code = et_invitation_code.getText().toString().trim();
        invitation_name = et_invitation_name.getText().toString().trim();
        Log.e(TAG_CLASS, "invitation_code  : " + invitation_code + "   invitation_name  : " + invitation_name);
        if (TextUtils.isEmpty(Constant.SOCKET_URL)){
            splashPresenter.locationReqService();
        }else {
            invitationLog();
        }
    }

    @Override
    public void inviteCodeLoginSuccess(InviteCodeLoginBean inviteCodeLoginBean) {
        //保存邀请码
        SPTools.getInstance(this).save(SPTools.KEY_LOGIN_INVITE_CODE, et_invitation_code.getText().toString().trim());
        //保存昵称
        SPTools.getInstance(this).save(SPTools.KEY_NICKNAME, et_invitation_name.getText().toString());
        dismissLoadingDialog();
        Log.i(TAG_CLASS, " inviteCodeLoginSuccess  : " + new Gson().toJson(inviteCodeLoginBean));
        String role = inviteCodeLoginBean.getRole();
        String userId = inviteCodeLoginBean.getUserId();
        String courseId = inviteCodeLoginBean.getCourseId();
        String masterUserId = inviteCodeLoginBean.getMasterUserId();
        String nickName = inviteCodeLoginBean.getNickName();
        String type = inviteCodeLoginBean.getType();
        String pushRtmp = inviteCodeLoginBean.getPushRtmp();
        String pullRtmp = inviteCodeLoginBean.getPullRtmp();
        String appId = inviteCodeLoginBean.getAppId();
        String className = inviteCodeLoginBean.getTitle();
        String notice = inviteCodeLoginBean.getNotice();
        Constant.app_id = appId;
        SPTools.getInstance(this).save(Constant.app_id, appId);
        TTTSDK.init(getApplicationContext(), Constant.app_id);
        if ("3".equals(role)) {
//            popupWindowSyles.popupWindowStylesTeacher(this, Integer.parseInt(role),
//                    String.valueOf(userId), courseId, inviteCodeLoginBean.getMasterNickName(),
//                    pushRtmp, type, "");
//            popupWindowSyles.showProgressBar(ll_invitation_main, this);
            toastBackShort(getResources().getString(R.string.invitation_code_error));
        } else if ("1".equals(role)) {
            if (Tools.isNetworkAvailable(mContext)){
                Constant.popupType = true;
                popupWindowSyles.popupWindowStylesStudent(this,inviteCodeLoginBean, et_invitation_code.getText().toString());
                popupWindowSyles.showProgressBar(ll_invitation_main, this);
//                et_invitation_code.setText("");
                BaseLiveActivity.getWsIpLocationReq(appId, Integer.parseInt(userId));
            }else {
                toastBackShort(getResources().getString(R.string.msg_network_error));
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (Constant.popupType){
                return true;
            }else {
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 邀请码登录失败
     * @param response
     */
    @Override
    public void inviteCodeLoginFailure(BaseResponse<Object> response) {

          showPopupWindow(response.getMessage());
    }

    @Override
    public void locationSucess(BaseResponse<LocationBean> locationBean) {
        Log.e(TAG_CLASS , " locationBean  " + new Gson().toJson(locationBean));
        if (locationBean != null){
            Constant.DocUrl = locationBean.getData().getDocsWbUrl();
            Constants.SDKVideo_url = locationBean.getData().getSdk().getHost();
            Constants.SDKVideo_port = locationBean.getData().getSdk().getPort();
            Constant.Websocket_port = locationBean.getData().getIpLocation().getPortBusiness();
            Constant.SERVER_CLAUSE_URL = locationBean.getData().getServerClause();
            String default_url = locationBean.getData().getIpLocation().getHost();
            Constant.SOCKET_URL_PORT = locationBean.getData().getIpLocation().getPort();
            Constant.SOCKET_URL = "ws://" + default_url + ":" + Constant.SOCKET_URL_PORT;
            Constant.SOCKET_URL_DEFAULT = "ws://" + default_url + ":" + Constant.SOCKET_URL_PORT;
            Constant.SHARE_CLASS_ROOM_URL = locationBean.getData().getClassroomUrl() + "/?code=";
            invitationLog();
        }
    }

    //邀请码登录请求
    private void invitationLog() {
        websCluster();
        mPresenter.inviteCodeLogin(invitation_code, invitation_name);
        showLoadingDialog();
    }

    @Override
    public void getError() {
        Log.e(TAG_CLASS , " getError ");
//        toastBackShort(getResources().getString(R.string.login_load_error));
    }

    //邀请码登录成功后请求集群地址
    private void websCluster() {
        if (Constant.wsService != null && Constant.wsService.webSocket != null){
            if (Constant.wsService.webSocket.isOpen()){
                if (Constant.exitRoom){
                    Constant.wsService.prepareShutdown();
                    Constant.exitRoom = false;
                }
            }else {
                Constant.exitRoom = false;
                Constant.wsService.prepareShutdown();
                Constant.wsService.startInitWebSocket();
            }
        }else {
            connection  = true;
            bindService(WebSocketService.createIntent(InvitationActivity.this), wsConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void accountLoginSuccess(AccountLoginBean accountLoginBean) {

    }

    @Override
    public void accountLoginFailure(BaseResponse<Object> response) {

    }

    @Override
    public void verifyAccount(BaseResponse<Object> baceBean) {

    }

    @Override
    public void getSMSVerificationCode(BaseResponse<Object> VerificationCode) {

    }

    @Override
    public void getCheckSMSCode(BaseResponse<Object> checkCode) {

    }

    @Override
    public void getForgetPassword(BaseResponse<Object> frogetPassword) {

    }

    @Override
    public void getRegisterAccount(BaseResponse<Object> accountBaen) {

    }

    @Override
    public void loginError() {

    }


}
