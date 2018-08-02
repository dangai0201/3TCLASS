package com.tttlive.education.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.ui.home.HomeActivity;
import com.tttlive.education.util.SPTools;


import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class TeacherLoginActivity extends BaseActivity implements TeacherLoginInterface{

    @BindView(R.id.et_invite_code)
    EditText etInviteCode;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.bt_enter_room)
    Button btEnterRoom;
    @BindView(R.id.cb_password)
    CheckBox mCbPassWord ;
    private TeacherLoginPresenter mPresenter;

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_teacher_login;
    }

    @Override
    protected void findView() {
        setBtGlobalLeft(getResources().getDrawable(R.drawable.icon_back));
        setTvGlobalLeft(R.string.accont_login);
        mPresenter = new TeacherLoginPresenter(this);

        //TODO 暂时置位可以点击
        btEnterRoom.setBackground(getResources().getDrawable(R.drawable.shape_login_button_enable_bg));
        btEnterRoom.setEnabled(true);
        etInviteCode.setSelection(etInviteCode.getText().length());

        etInviteCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(editable.toString())){
                    btEnterRoom.setBackground(getResources().getDrawable(R.drawable.shape_login_button_enable_bg));
                    btEnterRoom.setEnabled(true);
                }else {
                    btEnterRoom.setBackground(getResources().getDrawable(R.drawable.shape_login_button_bg));
                    btEnterRoom.setEnabled(false);
                }
            }
        });

        findViewById(R.id.tv_forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherLoginActivity.this,ForgetActivity.class));
            }
        });
        onClick(mBtTitleLeft, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initView() {

        mCbPassWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etNickname.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etNickname.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etNickname.setSelection(etNickname.getText().length());
            }
        });
    }

    @OnClick({R.id.bt_enter_room})
    void OnClickView(View view){
        if(view.getId() == R.id.bt_enter_room){
            showLoadingDialog();
            accountLogin();
        }
    }
    //账号登录
    private void accountLogin() {
        String userName = etInviteCode.getText().toString();
        String passWord = etNickname.getText().toString();
        if(!TextUtils.isEmpty(userName)||!TextUtils.isEmpty(passWord)){
            mPresenter.accountLogin(userName,passWord);
        }else if(TextUtils.isEmpty(userName)){
            showError(1);
        }else {
            showError(2);
        }
    }

    private void showError(int i) {
        switch (i){
            case 1 :
                toastShort("账号不能为空");
                break;
            case 2 :
                toastShort("密码不能为空");
                break;
            case 3 :
                toastShort("登录失败");
                break;
        }
        dismissLoadingDialog();
    }

    @Override
    public void loginSucess(AccountLoginBean accountLoginBean) {
//        SPUtils.save(Constant.APPID,Constant.app_id);
//        TTTSDK.init(getApplicationContext(),Constant.app_id);
        SPTools.getInstance(this).saveLoginInfo(accountLoginBean);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void loginFailure() {
        showError(3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribeTasks();
    }
}
