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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.AccountBean;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.data.InviteCodeLoginBean;
import com.tttlive.education.util.ButtonUtils;
import com.tttlive.education.util.CustomPopWindow;
import com.tttlive.education.util.TextCheckUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/7/10/0010.
 * 注册界面
 */
public class RegisteredActivity extends BaseActivity implements View.OnClickListener, LoginInterface, CompoundButton.OnCheckedChangeListener {

    private static final String TAG_CLASS = RegisteredActivity.class.getSimpleName();
    private Context mContext;

    private String mobile_num_iphone;
    private String mobile_number;
    private String password_number;
    private String verification_number;

    private ImageView registered_back;
    private LoginPresenter mPresenter;
    private EditText mobile_num;
    private EditText verification_code;
    private EditText password_num;
    private LinearLayout ll_obtain_verification_code;
    private LinearLayout ll_registered_correct;
    private TextView tv_obtain_verification_code;
    private CheckBox cb_agree;
    private CheckBox cb_show_password;
    private LinearLayout ll_agree;
    private LinearLayout ll_content;

    private boolean isFinish;

    private int mTime = 3;

    //注册成功信息提示弹窗
    private CustomPopWindow mPopupWindow;

    private EtTextChangedListener etTextChangedListener = new EtTextChangedListener();
    private TextView tv_version_code;

    //标记是否开启倒计时
    private boolean isTimerRun = false;


    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_registered;
    }

    @Override
    protected void findView() {
        registered_back = findViewById(R.id.registered_back);
        mobile_num = findViewById(R.id.et_enter_mobile_num);
        verification_code = findViewById(R.id.et_enter_verification_code);
        password_num = findViewById(R.id.et_enter_pass_word);
        tv_obtain_verification_code = findViewById(R.id.tv_obtain_verification_code);
        ll_obtain_verification_code = findViewById(R.id.ll_obtain_verification_code);
        ll_registered_correct = findViewById(R.id.ll_registered_correct);
        cb_agree = findViewById(R.id.cb_agree);
        ll_agree = findViewById(R.id.ll_agree);
        ll_content = findViewById(R.id.ll_content);
        cb_show_password = findViewById(R.id.cb_show_password);
        tv_version_code = findViewById(R.id.tv_version_code);



    }

    @Override
    protected void initView() {
        mContext = this;
        mPresenter = new LoginPresenter(this);
        registered_back.setOnClickListener(this);
        ll_obtain_verification_code.setOnClickListener(this);
        ll_registered_correct.setOnClickListener(this);
        ll_agree.setOnClickListener(this);
        cb_agree.setOnCheckedChangeListener(this);
        cb_show_password.setOnCheckedChangeListener(this);

        mobile_num.addTextChangedListener(etTextChangedListener);
        password_num.addTextChangedListener(etTextChangedListener);
        verification_code.addTextChangedListener(etTextChangedListener);

        ll_obtain_verification_code.setClickable(false);
        tv_version_code.setText(Constant.APP_VERSION_CODE);
        changeLoginBtnStatus();
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
        mPopupWindow.showAtLocation(ll_content, Gravity.CENTER, 0, 0);
    }

    /**
     * 改变登录按钮状态
     */
    private void changeLoginBtnStatus() {
        if (TextCheckUtils.checkTextLength(mobile_num.getText().toString(), 11, 11)
                && TextCheckUtils.checkTextLength(password_num.getText().toString(),6,20)
                && TextCheckUtils.checkTextLength(verification_code.getText().toString(),6,6)
                && cb_agree.isChecked()) {
            //登录按钮设为蓝色
            ll_registered_correct.setBackgroundResource(R.drawable.input_submit_hover);
            ll_registered_correct.setClickable(true);
        } else {
            //登录按钮设为灰色
            ll_registered_correct.setBackgroundResource(R.drawable.input_submit_disable);
            ll_registered_correct.setClickable(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFinish = true;
        cDownTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registered_back:
                onBackPressed();
                finish();
                break;
            case R.id.ll_registered_correct:
                //确认注册

                if (ButtonUtils.isFastDoubleClick(R.id.ll_registered_correct)) {
                    return;
                }

                mobile_number = mobile_num.getText().toString().trim();
                password_number = password_num.getText().toString().trim();
                verification_number = verification_code.getText().toString().trim();

                mPresenter.checkSMSCode(mobile_number, verification_number);

                break;
            case R.id.ll_obtain_verification_code:
                //获取验证码
                ObtainVerification();
                break;
            case R.id.ll_agree:
                startActivity(new Intent(this, AgreeActivity.class));
                break;
        }
    }


    @Override
    public void showDataException(String msg) {
        super.showDataException(msg);
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
        Log.i(TAG_CLASS, " 校验账号   : " + baceBean);
        if (baceBean.getCode() == Constant.HTTP_REQUEST_ACCOUNT_SUCCESSFUL) {

            showPopupWindow(getString(R.string.registered_account_confirm));
        } else if (baceBean.getCode() == Constant.HTTP_REQUEST_ACCOUNT_ERROR) {
            showPopupWindow(getString(R.string.account_parameter_error));
        } else if (baceBean.getCode() == Constant.HTTP_REQUEST_ACCOUNT_NOT_EXIST) {
            //账号不存在
            isTimerRun = true;
            cDownTimer.start();
            mPresenter.sendSMSCode(mobile_num_iphone);
        } else if (baceBean.getCode() == Constant.HTTP_REQUEST_ACCOUNT_ACCOUNT_SUSPENDED) {
            showPopupWindow(baceBean.getMessage());
        } else if (baceBean.getCode() == Constant.HTTP_REQUEST_SERVER_EXCEPTION) {
            showPopupWindow(baceBean.getMessage());
        }

    }

    @Override
    public void getSMSVerificationCode(BaseResponse<Object> verificationCode) {
        if (verificationCode.getCode() == Constant.HTTP_REQUEST_ACCOUNT_SUCCESSFUL) {
            //发送验证码成功
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
            mPresenter.registeredAccountNum(mobile_number, password_number, verification_number);
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

    }

    @Override
    public void getRegisterAccount(BaseResponse<Object> accountBaen) {
        Log.e(TAG_CLASS, accountBaen + "");
        if (accountBaen.getCode() == Constant.HTTP_REQUEST_ACCOUNT_SUCCESSFUL) {
            //注册成功
            showPopupWindow(getString(R.string.registered_success));
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    AccountBean accountBean = new AccountBean(mobile_num.getText().toString(),"");
                    //发送帐号到帐号登录页
                    EventBus.getDefault().post(accountBean);
                    finish();
                }
            });
        } else {
            showPopupWindow(accountBaen.getMessage());
        }

    }

    @Override
    public void loginError() {

    }


    /**
     * 获取验证码
     */
    private void ObtainVerification() {

        mobile_num_iphone = mobile_num.getText().toString().trim();
        if (TextCheckUtils.isMobileNo(mobile_num_iphone)) {
            mPresenter.veridyAccount(mobile_num_iphone);
        } else {
            showPopupWindow(getString(R.string.toast_mobile_incorrect));
        }
    }

    /**
     * 倒计时显示
     */
    private CountDownTimer cDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            ll_obtain_verification_code.setEnabled(false);
            ll_obtain_verification_code.setBackground(getResources().getDrawable(R.drawable.shape_login_button_bg));
            tv_obtain_verification_code.setTextColor(getResources().getColor(R.color.white));
            tv_obtain_verification_code.setText((millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
//            showPopupWindow("倒计时结束");
            ll_obtain_verification_code.setEnabled(true);
            ll_obtain_verification_code.setBackground(getResources().getDrawable(R.drawable.shape_login_button_enable_bg));
            tv_obtain_verification_code.setTextColor(getResources().getColor(R.color.white));
            tv_obtain_verification_code.setText(getResources().getString(R.string.get_code));
            isTimerRun = false;

        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.cb_agree) {//同意协议按钮
            changeLoginBtnStatus();
        }

        if (buttonView.getId() == R.id.cb_show_password) {//密码显示与隐藏切换按钮
            if (isChecked) {
                //显示密文
                password_num.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                //显示明文
                password_num.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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

            if(isTimerRun) {
                return;
            }

            if (TextCheckUtils.checkTextLength(mobile_num.getText().toString(), 11, 11)) {
                ll_obtain_verification_code.setClickable(true);
                ll_obtain_verification_code.setBackgroundResource(R.drawable.shape_login_button_enable_bg);
            } else {
                ll_obtain_verification_code.setClickable(false);
                ll_obtain_verification_code.setBackgroundResource(R.drawable.shape_login_button_bg);
            }


        }
    }

}
