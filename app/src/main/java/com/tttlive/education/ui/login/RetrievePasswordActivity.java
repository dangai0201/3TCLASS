package com.tttlive.education.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.AccountBean;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.data.InviteCodeLoginBean;
import com.tttlive.education.util.CustomPopWindow;
import com.tttlive.education.util.SPTools;
import com.tttlive.education.util.TextCheckUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2018/7/10/0010.
 * 忘记密码界面
 */
public class RetrievePasswordActivity extends BaseActivity implements View.OnClickListener, LoginInterface, CompoundButton.OnCheckedChangeListener {
    private static final String TAG_CLASS = RetrievePasswordActivity.class.getSimpleName();
    private static final String KEY_MOBILE = "KEY_MOBILE";

    private ImageView retrueve_back;
    private LoginPresenter mPresenter;
    private EditText et_enter_mobile_unm;
    private EditText et_enter_verification;
    private LinearLayout ll_obtain_verification;
    private EditText et_enter_pass_word;
    private LinearLayout ll_retrueve_password_carry;
    private TextView tv_obtain_verification_code;
    private LinearLayout ll_root;
    private TextView tv_version_code;
    private CheckBox cb_show_password;

    private String mobile_number;
    private String mobile_num;
    private String password_num;
    private String code_num;

    private EtTextChangedListener etTextChangedListener = new EtTextChangedListener();

    //输入错误信息提示弹窗
    private CustomPopWindow mPopupWindow;

    //标记是否开启倒计时
    private boolean isTimerRun = false;


    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_retrueve_password;
    }

    @Override
    protected void findView() {
        retrueve_back = findViewById(R.id.retrueve_back);
        et_enter_mobile_unm = findViewById(R.id.et_enter_mobile_num);
        et_enter_verification = findViewById(R.id.et_enter_verification_code);
        ll_obtain_verification = findViewById(R.id.ll_obtain_verification_code);
        et_enter_pass_word = findViewById(R.id.et_enter_pass_word);
        ll_retrueve_password_carry = findViewById(R.id.ll_retrueve_password_carry);
        tv_obtain_verification_code = findViewById(R.id.tv_obtain_verification_code);
        tv_version_code = findViewById(R.id.tv_version_code);
        ll_root = findViewById(R.id.ll_root);
        cb_show_password = findViewById(R.id.cb_show_password);

    }

    @Override
    protected void initView() {
        mContext = this;
        mPresenter = new LoginPresenter(this);
        retrueve_back.setOnClickListener(this);
        ll_retrueve_password_carry.setOnClickListener(this);
        ll_obtain_verification.setOnClickListener(this);
        cb_show_password.setOnCheckedChangeListener(this);
        et_enter_mobile_unm.addTextChangedListener(etTextChangedListener);
        et_enter_pass_word.addTextChangedListener(etTextChangedListener);
        et_enter_verification.addTextChangedListener(etTextChangedListener);
        ll_obtain_verification.setClickable(false);
        tv_version_code.setText(Constant.APP_VERSION_CODE);
        initData();

        changeLoginBtnStatus();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.cb_show_password) {//密码显示与隐藏切换按钮
            if (isChecked) {
                //显示密文
                et_enter_pass_word.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                //显示明文
                et_enter_pass_word.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
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

            //安卓-忘记密码页面，手机号，密码都按规则输入以后，登录按钮才可以为点击状态
            changeLoginBtnStatus();

            if (isTimerRun) {
                return;
            }
            if (TextCheckUtils.checkTextLength(et_enter_mobile_unm.getText().toString(), 11, 11)) {
                ll_obtain_verification.setClickable(true);
                ll_obtain_verification.setBackgroundResource(R.drawable.shape_login_button_enable_bg);
            } else {
                ll_obtain_verification.setClickable(false);
                ll_obtain_verification.setBackgroundResource(R.drawable.shape_login_button_bg);
            }

        }
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null || intent.getStringExtra(KEY_MOBILE) == null) {
            return;
        }
        if (TextCheckUtils.isMobileNo(intent.getStringExtra(KEY_MOBILE))) {
            et_enter_mobile_unm.setText(intent.getStringExtra(KEY_MOBILE));
        }

    }


    /**
     * 改变登录按钮状态
     */
    private void changeLoginBtnStatus() {
        if (TextCheckUtils.checkTextLength(et_enter_mobile_unm.getText().toString(), 11, 11)
                && TextCheckUtils.checkTextLength(et_enter_pass_word.getText().toString(), 6, 20)
                && TextCheckUtils.checkTextLength(et_enter_verification.getText().toString(), 6, 6)) {
            //登录按钮设为蓝色
            ll_retrueve_password_carry.setBackgroundResource(R.drawable.input_submit_hover);
            ll_retrueve_password_carry.setClickable(true);
        } else {
            //登录按钮设为灰色
            ll_retrueve_password_carry.setBackgroundResource(R.drawable.input_submit_disable);
            ll_retrueve_password_carry.setClickable(false);
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
        mPopupWindow.showAtLocation(ll_root, Gravity.CENTER, 0, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrueve_back:
                onBackPressed();
                finish();
                break;
            case R.id.ll_retrueve_password_carry:
                //完成
                mobile_num = et_enter_mobile_unm.getText().toString().trim();
                password_num = et_enter_pass_word.getText().toString().trim();
                code_num = et_enter_verification.getText().toString().trim();

                mPresenter.checkSMSCode(mobile_num, code_num);

                break;
            case R.id.ll_obtain_verification_code:
                //获取验证码
                ObtainVerification();
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cdTimer.cancel();
    }

    @Override
    public void inviteCodeLoginSuccess(InviteCodeLoginBean inviteCodeLoginBean) {

    }

    @Override
    public void inviteCodeLoginFailure(BaseResponse<Object> response) {

    }

    @Override
    public void accountLoginSuccess(AccountLoginBean accountLoginBean) {

    }

    @Override
    public void accountLoginFailure(BaseResponse<Object> response) {

    }

    @Override
    public void verifyAccount(BaseResponse<Object> baceBean) {
        Log.i(TAG_CLASS, " 校验帐号   : " + baceBean);
        if (baceBean.getCode() == Constant.HTTP_REQUEST_ACCOUNT_SUCCESSFUL) {
//            showPopupWindow("帐号正确");
            mPresenter.sendSMSCode(mobile_number);
            isTimerRun = true;
            cdTimer.start();
        } else if (baceBean.getCode() == Constant.HTTP_REQUEST_ACCOUNT_ERROR) {
            showPopupWindow(getResources().getString(R.string.account_parameter_error));
        } else if (baceBean.getCode() == Constant.HTTP_REQUEST_ACCOUNT_NOT_EXIST) {
            showPopupWindow(getString(R.string.mobile_no_registered));
        } else if (baceBean.getCode() == Constant.HTTP_REQUEST_ACCOUNT_ACCOUNT_SUSPENDED) {
            showPopupWindow(baceBean.getMessage());
        } else if (baceBean.getCode() == Constant.HTTP_REQUEST_SERVER_EXCEPTION) {
            showPopupWindow(baceBean.getMessage());
        }

    }

    @Override
    public void getSMSVerificationCode(BaseResponse<Object> verificationCode) {
        if (verificationCode.getCode() == Constant.HTTP_REQUEST_ACCOUNT_SUCCESSFUL) {
            //发送验证码正确
//            showPopupWindow(getString(R.string.verification_code_send_successful));
        } else if (verificationCode.getCode() == Constant.HTTP_REQUEST_ACCOUNT_ERROR) {
            //参数错误
            showPopupWindow(getResources().getString(R.string.account_parameter_error));
        } else if (verificationCode.getCode() == Constant.HTTP_REQUEST_ACCOUNT_NOT_EXIST) {
            //发送失败
            showPopupWindow(verificationCode.getMessage());
        } else if (verificationCode.getCode() == Constant.HTTP_REQUEST_SERVER_EXCEPTION) {
            //服务器异常
            showPopupWindow(verificationCode.getMessage());
        }


    }

    @Override
    public void getCheckSMSCode(BaseResponse<Object> checkCode) {
        if (checkCode.getCode() == Constant.HTTP_REQUEST_ACCOUNT_SUCCESSFUL) {
            //校验成功
            mPresenter.forgetResetPassword(mobile_num, password_num, code_num);
        } else if (checkCode.getCode() == Constant.HTTP_REQUEST_ACCOUNT_ERROR) {
            //参数错误
            showPopupWindow(getResources().getString(R.string.account_parameter_error));
        } else if (checkCode.getCode() == Constant.HTTP_REQUEST_ACCOUNT_NOT_EXIST) {
            //验证失败
            showPopupWindow(checkCode.getMessage());
        } else if (checkCode.getCode() == Constant.HTTP_REQUEST_SERVER_EXCEPTION) {
            //服务器异常
            showPopupWindow(checkCode.getMessage());
        }

    }

    @Override
    public void getForgetPassword(BaseResponse<Object> frogetPassword) {
        if (frogetPassword.getCode() == Constant.HTTP_REQUEST_ACCOUNT_SUCCESSFUL) {
            //成功
            showPopupWindow(getString(R.string.modify_password_successful));
            //修改本地保存的相应的密码
            modifyLocalAccount(et_enter_mobile_unm.getText().toString(), et_enter_pass_word.getText().toString());
            onBackPressed();
            EventBus.getDefault().post(et_enter_mobile_unm.getText().toString());
            finish();
        } else if (frogetPassword.getCode() == Constant.HTTP_REQUEST_ACCOUNT_ERROR) {
            //参数错误
            showPopupWindow(getResources().getString(R.string.account_parameter_error));
        } else if (frogetPassword.getCode() == Constant.HTTP_REQUEST_ACCOUNT_NOT_EXIST) {
            //验证码错误
            showPopupWindow(getString(R.string.msg_unVerification_error));
        } else if (frogetPassword.getCode() == Constant.HTTP_REQUEST_ACCOUNT_ACCOUNT_SUSPENDED) {
            //修改密码失败
            showPopupWindow(frogetPassword.getMessage());
        } else if (frogetPassword.getCode() == Constant.HTTP_REQUEST_SERVER_EXCEPTION) {
            //服务器异常
            showPopupWindow(frogetPassword.getMessage());
        }

    }

    /**
     * 修改本地保存的账号密码
     *
     * @param mobile
     * @param password
     */
    private void modifyLocalAccount(String mobile, String password) {

        AccountBean accountBean = new AccountBean(mobile, password);

        List<AccountBean> accountList = SPTools.getInstance(this).getAccountList();

        if (accountList == null || !accountList.contains(accountBean)) {
            return;
        }
        //修改本地存储的账号和密码
        accountList.set(accountList.indexOf(accountBean), accountBean);
        if (accountList.size() > SPTools.MAX_CACHE_HISTORY_SIZE) {
            accountList = accountList.subList(0, SPTools.MAX_CACHE_HISTORY_SIZE);
        }
        SPTools.getInstance(this).saveAccountList(accountList);

    }

    @Override
    public void getRegisterAccount(BaseResponse<Object> accountBaen) {

    }

    @Override
    public void loginError() {

    }

    /**
     * 获取验证码
     */
    private void ObtainVerification() {
        //校验手机号码
        mobile_number = et_enter_mobile_unm.getText().toString().trim();

        if (TextCheckUtils.isMobileNo(mobile_number)) {
            mPresenter.veridyAccount(mobile_number);
        } else {
            showPopupWindow(getString(R.string.toast_mobile_incorrect));
        }
    }

    /**
     * 显示倒计时
     */
    private CountDownTimer cdTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            ll_obtain_verification.setEnabled(false);

            ll_obtain_verification.setBackground(getResources().getDrawable(R.drawable.shape_login_button_bg));
            tv_obtain_verification_code.setTextColor(getResources().getColor(R.color.white));
            tv_obtain_verification_code.setText((millisUntilFinished / 1000) + "s");

        }

        @Override
        public void onFinish() {
//            showPopupWindow("倒计时结束");
            ll_obtain_verification.setEnabled(true);
            ll_obtain_verification.setBackground(getResources().getDrawable(R.drawable.shape_login_button_enable_bg));
            tv_obtain_verification_code.setTextColor(getResources().getColor(R.color.white));
            tv_obtain_verification_code.setText(getResources().getString(R.string.get_code));
            isTimerRun = false;

        }
    };

    public static void actionStart(Context context, String mobile) {
        Intent intent = new Intent(context, RetrievePasswordActivity.class);
        intent.putExtra(KEY_MOBILE, mobile);
        context.startActivity(intent);

    }

}
