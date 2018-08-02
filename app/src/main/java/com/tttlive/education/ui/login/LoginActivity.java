package com.tttlive.education.ui.login;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tttlive.basic.education.R;
import com.tttlive.education.adapter.PopListAdapter;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.AccountBean;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.data.InviteCodeLoginBean;
import com.tttlive.education.ui.home.HomeActivity;
import com.tttlive.education.ui.room.bean.LocationBean;
import com.tttlive.education.ui.room.socket.WebSocketService;
import com.tttlive.education.util.ButtonUtils;
import com.tttlive.education.util.CustomPopWindow;
import com.tttlive.education.util.ListPopWindow;
import com.tttlive.education.util.SPTools;
import com.tttlive.education.util.TextCheckUtils;
import com.tttlive.education.util.Tools;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.wushuangtech.library.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements LoginInterface, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener,SplashInterface {

    private static final String TAG_CLASS = LoginActivity.class.getSimpleName();

    //缓存登录账号密码历史记录数量
    private static final int MAX_CACHE_HISTORY_SIZE = 3;

    private EtTextChangedListener etTextChangedListener = new EtTextChangedListener();

    @BindView(R.id.activity_login_main)
    RelativeLayout activity_login_main;

    @BindView(R.id.et_mobileNo)
    EditText et_mobileNo;

    @BindView(R.id.iv_del_mobileNo)
    ImageView iv_del_mobileNo;

    @BindView(R.id.cb_history_mobileNo)
    CheckBox cb_history_mobileNo;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.iv_del_password)
    ImageView iv_del_password;

    @BindView(R.id.cb_show_password)
    CheckBox cb_show_password;

    @BindView(R.id.ll_login)
    LinearLayout ll_login;

    @BindView(R.id.ll_divider_line_mobile)
    LinearLayout ll_divider_line_mobile;

    @BindView(R.id.ll_divider_line_password)
    LinearLayout ll_divider_line_password;

    @BindView(R.id.iv_login_loading)
    ImageView iv_login_loading;

    @BindView(R.id.tv_login)
    TextView tv_login;

    @BindView(R.id.ll_no_forget_password)
    LinearLayout ll_forget_password;

    @BindView(R.id.ll_invitation_login)
    LinearLayout ll_invitation_login;

    @BindView(R.id.tv_user_registered)
    TextView tv_user_registered;

    private boolean connectionServer = false;

    //输入错误信息提示弹窗
    private CustomPopWindow mPopupWindow;
    private SplashPresenter splashPresenter;

    //登录历史弹窗
    private ListPopWindow mListPopWindow;

    private LoginPresenter mPresenter;

    private TextView tv_version_code;

    private ServiceConnection wsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Mainactivity", "Service connected.");
            Constant.wsService = ((WebSocketService.ServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    protected void findView() {

        EventBus.getDefault().register(this);
        mPresenter = new LoginPresenter(this);
        tv_version_code = findViewById(R.id.tv_version_code);
    }

    @Override
    protected void initView() {
        splashPresenter = new SplashPresenter(this);
        permissionInit();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.EQUIPMENT_WIDTH = dm.widthPixels;
        Constant.EQUIPMENT_HEIGHT = dm.heightPixels;

        ll_forget_password.setOnClickListener(this);
        tv_user_registered.setOnClickListener(this);
        ll_invitation_login.setOnClickListener(this);
        ll_login.setOnClickListener(this);
        iv_del_mobileNo.setOnClickListener(this);
        iv_del_password.setOnClickListener(this);
        cb_history_mobileNo.setOnCheckedChangeListener(this);
        cb_show_password.setOnCheckedChangeListener(this);
        et_mobileNo.addTextChangedListener(etTextChangedListener);
        et_password.addTextChangedListener(etTextChangedListener);
        et_mobileNo.setOnFocusChangeListener(this);
        et_password.setOnFocusChangeListener(this);
        tv_version_code.setText(Constant.APP_VERSION_CODE);

        changeLoginBtnStatus();

    }

    /**
     * 接收忘记密码页发送的手机号
     * @param mobile
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMoBileFromForget(String mobile){
        if(mobile != null) {
            et_mobileNo.setText(mobile);
            et_password.setText("");
        }
    }
    /**
     * 接收注册页发送的手机号
     * @param accountBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMoBileFromRegister(AccountBean accountBean){
        String mobile = accountBean.getMobile();
        if(mobile != null) {
            et_mobileNo.setText(mobile);
            et_password.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 邀请码登录成功回调
     *
     * @param inviteCodeLoginBean 邀请码登录返回的结果
     */
    @Override
    public void inviteCodeLoginSuccess(InviteCodeLoginBean inviteCodeLoginBean) {

    }

    @Override
    public void inviteCodeLoginFailure(BaseResponse<Object> response) {

    }



    /**
     * 账号密码登录成功回调
     *
     * @param accountLoginBean 账号登录成功返回的结果
     */
    @Override
    public void accountLoginSuccess(AccountLoginBean accountLoginBean) {

        //保存最新的登录账号信息
        SPTools.getInstance(this).saveLoginInfo(accountLoginBean);
//        SPUtils.putConfigStrValue(mContext , Constant.KEY_LOGIN_INFO , new Gson().toJson(accountLoginBean));

        //保存账号和密码到本地
        saveLoginHistory(et_mobileNo.getText().toString(), et_password.getText().toString());
//        et_mobileNo.setText("");
        et_password.setText("");
        //跳转到主页面
        startActivity(new Intent(this, HomeActivity.class));
        bindService(WebSocketService.createIntent(LoginActivity.this), wsConnection, BIND_AUTO_CREATE);
        connectionServer = true;

    }

    @Override
    public void accountLoginFailure(BaseResponse<Object> response) {
        iv_login_loading.setVisibility(View.GONE);
        tv_login.setText(getString(R.string.bt_login));
        showPopupWindow(response.getMessage());

    }

    /**
     * 保存登录历史账号密码到本地
     *
     * @param mobile   手机号
     * @param password 密码
     */
    private void saveLoginHistory(String mobile, String password) {

        AccountBean accountBean = new AccountBean(mobile, password);
        List<AccountBean> list = new ArrayList<>();
        List<AccountBean> accountList = SPTools.getInstance(this).getAccountList();
        if (accountList != null) {
            list.addAll(accountList);
        }
        if (list.contains(accountBean)) {
            list.remove(accountBean);
        }
        list.add(0, accountBean);
        if (list.size() > MAX_CACHE_HISTORY_SIZE) {
            list = list.subList(0, MAX_CACHE_HISTORY_SIZE);
        }
        SPTools.getInstance(this).saveAccountList(list);

    }

    /**
     * 校验账号成功回调
     *
     * @param baseBean 返回的结果
     */
    @Override
    public void verifyAccount(BaseResponse<Object> baseBean) {
        //校验账号
        Log.i(TAG_CLASS, " 校验账号   : " + baseBean);

        switch (baseBean.getCode()) {
            case Constant.HTTP_REQUEST_ACCOUNT_SUCCESSFUL:
                //进行账号登录
                accountLogin(et_mobileNo.getText().toString(), et_password.getText().toString());
                break;

            case Constant.HTTP_REQUEST_ACCOUNT_ERROR:
                showPopupWindow(getString(R.string.account_parameter_error));
                iv_login_loading.setVisibility(View.GONE);
                tv_login.setText(getString(R.string.bt_login));
                break;

            case Constant.HTTP_REQUEST_ACCOUNT_NOT_EXIST:
                showPopupWindow(getString(R.string.toast_mobile_unregisted));
                iv_login_loading.setVisibility(View.GONE);
                tv_login.setText(getString(R.string.bt_login));
                break;

            case Constant.HTTP_REQUEST_ACCOUNT_ACCOUNT_SUSPENDED:
                showPopupWindow(getString(R.string.mobile_no_registered));
                iv_login_loading.setVisibility(View.GONE);
                tv_login.setText(getString(R.string.bt_login));
                break;

            case Constant.HTTP_REQUEST_SERVER_EXCEPTION:
                showPopupWindow(baseBean.getMessage());
                iv_login_loading.setVisibility(View.GONE);
                tv_login.setText(getString(R.string.bt_login));
                break;

        }

    }

    /**
     * 账号登录
     *
     * @param userName 账号
     * @param password 密码
     */
    private void accountLogin(String userName, String password) {
        if (TextUtils.isEmpty(Constant.SOCKET_URL)){
            toastShort(getResources().getString(R.string.msg_unconnection_error));
            iv_login_loading.setVisibility(View.GONE);
            tv_login.setText(getString(R.string.bt_login));
            splashPresenter.locationReqService();
        }else {
            mPresenter.accountLogin(userName, password);
        }

    }

    @Override
    public void showNetworkException() {
        super.showNetworkException();
        if (TextUtils.isEmpty(Constant.SOCKET_URL)){
            splashPresenter.locationReqService();
        }
        iv_login_loading.setVisibility(View.GONE);
        tv_login.setText(getString(R.string.bt_login));
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
        //登录异常
        toastBackShort(getResources().getString(R.string.login_load_error));
        iv_login_loading.setVisibility(View.GONE);
        tv_login.setText(getString(R.string.bt_login));

    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_login_loading.setVisibility(View.GONE);
        tv_login.setText(getString(R.string.bt_login));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.unsubscribeTasks();
        if (splashPresenter != null){
            splashPresenter.unsubscribeTasks();
        }
        if (Constant.wsService != null) {
            Constant.wsService.prepareShutdown();
            if (connectionServer){
                unbindService(wsConnection);
                connectionServer = false;
            }
        }
    }

    /**
     * 输入密码是否可见监听
     *
     * @param buttonView 复选按钮
     * @param isChecked  是否选中
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.cb_history_mobileNo:
                if (isChecked) {
                    //显示登录历史记录
                    showAccountWindow();
                }
                break;
            case R.id.cb_show_password:
                if (isChecked) {
                    //显示密文
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //显示明文
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
        }
    }

    /**
     * 编辑框焦点变化监听
     *
     * @param v        view
     * @param hasFocus 是否获取焦点
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.et_mobileNo) {
            boolean empty = TextUtils.isEmpty(et_mobileNo.getText().toString().trim());
            int isVisible = hasFocus && !empty ? View.VISIBLE : View.GONE;
            iv_del_mobileNo.setVisibility(isVisible);
        }
        if (v.getId() == R.id.et_password) {
            boolean empty = TextUtils.isEmpty(et_password.getText().toString().trim());
            int isVisible = hasFocus && !empty ? View.VISIBLE : View.GONE;
            iv_del_password.setVisibility(isVisible);
        }
        if (et_mobileNo.hasFocus()) {

            if (et_mobileNo.getText().toString().length() > 0) {
                et_mobileNo.setSelection(et_mobileNo.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_mobile, 1, R.color.color_FF1093ED);
            } else {
                changeDividerHeightAndColor(ll_divider_line_mobile, 0.5f, R.color.color_FFF1F1F1);
            }

            changeDividerHeightAndColor(ll_divider_line_password, 0.5f, R.color.color_FFF1F1F1);

        }
        if (et_password.hasFocus()) {

            if (et_password.getText().toString().length() > 0) {
                et_password.setSelection(et_password.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_password, 1, R.color.color_FF1093ED);
            } else {
                et_password.setSelection(et_password.getText().toString().length());
                changeDividerHeightAndColor(ll_divider_line_password, 0.5f, R.color.color_FFF1F1F1);
            }
            changeDividerHeightAndColor(ll_divider_line_mobile, 0.5f, R.color.color_FFF1F1F1);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_no_forget_password:
                //忘记密码
                RetrievePasswordActivity.actionStart(this, et_mobileNo.getText().toString());
                break;
            case R.id.tv_user_registered:
                //新用户注册
                Intent intentReg = new Intent();
                intentReg.setClass(mContext, RegisteredActivity.class);
                startActivity(intentReg);
                break;
            case R.id.ll_invitation_login:
                //邀请码登录
                Intent invitation = new Intent();
                invitation.setClass(this, InvitationActivity.class);
                startActivity(invitation);
                break;
            case R.id.iv_del_mobileNo:
                et_mobileNo.setText("");
                break;
            case R.id.iv_del_password:

                et_password.setText("");
                break;

            case R.id.ll_login:
                //点击登录按钮
                if (ButtonUtils.isFastDoubleClick(R.id.ll_login)) {
                    return;
                }
                //校验手机号码
                if (TextCheckUtils.isMobileNo(et_mobileNo.getText().toString())) {

                    iv_login_loading.setVisibility(View.VISIBLE);
                    tv_login.setText(getString(R.string.login_loading_text));
                    //校验账号
                    mPresenter.veridyAccount(et_mobileNo.getText().toString());
                } else {
//                    showPopupWindow(getString(R.string.toast_mobile_incorrect));
                    accountLogin(et_mobileNo.getText().toString(), et_password.getText().toString());
                }

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
        mPopupWindow.showAtLocation(activity_login_main, Gravity.CENTER, 0, 0);
    }

    /**
     * 弹出登录账号历史记录弹窗
     */
    private void showAccountWindow() {

        List<AccountBean> accountList = SPTools.getInstance(this).getAccountList();
        if (mListPopWindow == null) {
            mListPopWindow = new ListPopWindow(this);
        }
        mListPopWindow.setListener(listener);
        mListPopWindow.setWidth(ll_divider_line_mobile.getWidth());
        mListPopWindow.setData(accountList);
        mListPopWindow.showAsDropDown(ll_divider_line_mobile);
    }

    /**
     * 登录历史点击事件回调
     */
    private PopListAdapter.Listener listener = new PopListAdapter.Listener() {
        @Override
        public void onItemClick(int position) {
            List<AccountBean> accountList = mListPopWindow.getAccountList();
            String mobile = accountList.get(position).getMobile();
            String password = accountList.get(position).getPassword();
            et_mobileNo.setText(mobile);
            et_password.setText(password);
            mListPopWindow.dismiss();
        }

        @Override
        public void onDeleteClick(int position) {
            mListPopWindow.remove(position);
            List<AccountBean> accountList = mListPopWindow.getAccountList();
            SPTools.getInstance(LoginActivity.this).saveAccountList(accountList);
            et_mobileNo.setText("");
            et_password.setText("");
        }

        @Override
        public void onDismiss() {
            cb_history_mobileNo.setChecked(false);
        }
    };

    @Override
    public void locationSucess(BaseResponse<LocationBean> locationBean) {
        //请求服务器资源
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

        }
    }

    @Override
    public void getError() {
        Log.e(TAG_CLASS , "  getError ");
//        splashPresenter.locationReqService();
    }


    /**
     * 编辑框输入监听
     */
    public class EtTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            //安卓-登录页面，手机号，密码都按规则输入以后，登录按钮才可以为点击状态
            changeLoginBtnStatus();

            boolean isMobileEmpty = TextUtils.isEmpty(et_mobileNo.getText().toString().trim());
            boolean isPasswordEmpty = TextUtils.isEmpty(et_password.getText().toString().trim());
            iv_del_mobileNo.setVisibility(et_mobileNo.hasFocus() && !isMobileEmpty ? View.VISIBLE : View.GONE);
            iv_del_password.setVisibility(et_password.hasFocus() && !isPasswordEmpty ? View.VISIBLE : View.GONE);

            if (et_mobileNo.hasFocus()) {

                if (et_mobileNo.getText().toString().length() > 0) {
                    changeDividerHeightAndColor(ll_divider_line_mobile, 1, R.color.color_FF1093ED);
                } else {
                    changeDividerHeightAndColor(ll_divider_line_mobile, 0.5f, R.color.color_FFF1F1F1);
                }

                changeDividerHeightAndColor(ll_divider_line_password, 0.5f, R.color.color_FFF1F1F1);

            }
            if (et_password.hasFocus()) {

                if (et_password.getText().toString().length() > 0) {
                    changeDividerHeightAndColor(ll_divider_line_password, 1, R.color.color_FF1093ED);
                } else {
                    et_password.setSelection(et_password.getText().toString().length());
                    changeDividerHeightAndColor(ll_divider_line_password, 0.5f, R.color.color_FFF1F1F1);
                }
                changeDividerHeightAndColor(ll_divider_line_mobile, 0.5f, R.color.color_FFF1F1F1);

            }
        }
    }

    /**
     * 改变登录按钮状态
     */
    private void changeLoginBtnStatus() {
        String mobile = et_mobileNo.getText().toString();
        String password = et_password.getText().toString();

        if (TextCheckUtils.checkTextLength(mobile, 10, 11)
                && TextCheckUtils.checkTextLength(password, 6, 20)) {
            //登录按钮设为蓝色
            ll_login.setBackgroundResource(R.drawable.input_submit_hover);
            ll_login.setClickable(true);
        } else {
            //账号密码都没输入，登录按钮设为灰色
            ll_login.setBackgroundResource(R.drawable.input_submit_disable);
            ll_login.setClickable(false);
        }
    }

    /**
     * 友盟第三方登录
     * 获取对方信息
     */
    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.i(TAG_CLASS, " 授权开始 : " + platform);

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(mContext, "成功了", Toast.LENGTH_LONG).show();
            Log.i(TAG_CLASS, " 授权成功 : " + platform + " 数据  " + data);

        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(mContext, "失败：" + t.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.i(TAG_CLASS, " 授权失败 : " + platform);
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
            Log.i(TAG_CLASS, " 授权取消 : " + platform);
        }
    };


}
