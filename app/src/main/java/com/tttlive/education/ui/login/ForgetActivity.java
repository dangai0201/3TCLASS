package com.tttlive.education.ui.login;

import android.os.Bundle;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;

import rx.functions.Action1;

/**
 * Created by Iverson on 2018/3/6 下午2:38
 * 此类用于：忘记密码获取验证码
 */

public class ForgetActivity extends BaseActivity {
    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_forget;
    }

    @Override
    protected void findView() {
        setBtGlobalLeft(getResources().getDrawable(R.drawable.icon_back));
    }

    @Override
    protected void initView() {
        onClick(mBtTitleLeft, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackPressed();
            }
        });
    }
}
