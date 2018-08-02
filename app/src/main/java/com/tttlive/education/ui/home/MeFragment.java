package com.tttlive.education.ui.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseFragment;
import com.tttlive.education.base.MyApplication;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.util.SPTools;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Iverson on 2017/10/14 下午7:43
 * 此类用于：
 */

public class MeFragment extends BaseFragment {


    @BindView(R.id.sd_me_headpic)
    SimpleDraweeView sdMeHeadpic;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_me_phone_number)
    TextView tvMePhoneNumber;
    @BindView(R.id.rl_reset_password)
    RelativeLayout rlResetPassword;
    @BindView(R.id.rl_exit_app)
    RelativeLayout rlExitApp;

    private static final int HEAD_ARRAY[] = {
            R.drawable.avatar_student_girl_01,
            R.drawable.avatar_student_boy_02,
            R.drawable.avatar_student_girl_03,
            R.drawable.avatar_student_boy_04,
            R.drawable.avatar_student_girl_05,
            R.drawable.avatar_student_girl_06,
            R.drawable.avatar_teacher_o1
    };


    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViews(View view) {

        Uri uri = new Uri.Builder()
                .scheme("res")
                .path(String.valueOf(HEAD_ARRAY[getRandomIndex(HEAD_ARRAY.length)]))
                .build();
        sdMeHeadpic.setImageURI(uri);

        AccountLoginBean loginInfo = SPTools.getInstance(getContext()).getLoginInfo();

        if (loginInfo == null) {
            return;
        }

        String nickName = loginInfo.getNickName();
        if (nickName != null) {
            tvUsername.setText(nickName);
        }

        String userName = loginInfo.getUserName();
        if (userName != null) {
            tvMePhoneNumber.setText(userName);
        }

    }


    @OnClick({R.id.rl_reset_password, R.id.rl_exit_app})
    void onClickView(View view) {
        if (view.getId() == R.id.rl_reset_password) {

        } else if (view.getId() == R.id.rl_exit_app) {
            getActivity().finish();
//            exitApp();
        }
    }


    //退出app
    private void exitApp() {
        List<Activity> activities = MyApplication.activities;
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 生成maxIndex以内的整数
     *
     * @param maxIndex
     * @return
     */
    private int getRandomIndex(int maxIndex) {
        Random rand = new Random();
        int index = rand.nextInt(maxIndex);
        return index;
    }
}
