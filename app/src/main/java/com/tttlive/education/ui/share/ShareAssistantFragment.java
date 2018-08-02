package com.tttlive.education.ui.share;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseFragment;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrliu on 2018/3/26.
 * 此类用于:分享助教端
 */

public class ShareAssistantFragment extends BaseFragment {

    @BindView(R.id.share_code)
    LinearLayout mShareCode ;
    @BindView(R.id.share_title)
    TextView mShareTitle ;
    @BindView(R.id.share_time)
    TextView mShareTime ;
    @BindView(R.id.share_tv_code)
    TextView mShareTvCode ;
    @BindView(R.id.layout_share_student)
    LinearLayout mShareStudent ;
    @BindView(R.id.layout_share_teacher)
    LinearLayout mShareAssistant ;
    @BindView(R.id.teacher_share_wechat)
    LinearLayout mShareWechat;
    @BindView(R.id.teacher_share_qq)
    LinearLayout mShareQQ;
    @BindView(R.id.teacher_share_message)
    LinearLayout mShareMes;

    private static final String TIMESTART = "time_start";
    private static final String TIMEEND = "time_end";
    private static final String TIMETITLE = "title";

    private String mTimeStart ;
    private String mTimeEnd ;
    private String mTitle ;

    public static ShareAssistantFragment newInstance(String timeStart, String timeEnd, String title) {
        Bundle args = new Bundle();
        ShareAssistantFragment fragment = new ShareAssistantFragment();
        args.putString(TIMESTART,timeStart);
        args.putString(TIMEEND,timeEnd);
        args.putString(TIMETITLE,title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_share;
    }

    @Override
    protected void initViews(View view) {
        mShareCode.setVisibility(View.VISIBLE);
        mShareStudent.setVisibility(View.GONE);
        mShareAssistant.setVisibility(View.VISIBLE);
        Bundle arguments = getArguments();
        if(arguments!=null){
            mTimeStart = arguments.getString(TIMESTART);
            mTimeEnd = arguments.getString(TIMEEND);
            mTitle = arguments.getString(TIMETITLE);
        }

        mShareTitle.setText(mTitle);
        mShareTime.setText(mTimeStart+"-"+mTimeEnd);

        String invitationCode = getResources().getString(R.string.invitation_teacher_code);
        String invitation = String.format(invitationCode,"c4ce79");
        mShareTvCode.setText(invitation);
    }

    @OnClick({R.id.teacher_share_wechat,R.id.teacher_share_qq,R.id.teacher_share_message})
    public void shareClick(View view){
        switch (view.getId()){
            case R.id.teacher_share_wechat:
                ((ShareCourseActivity) getActivity()).share(SHARE_MEDIA.WEIXIN, UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.WEIXIN),"","","");
                break;
            case R.id.teacher_share_qq:
                ((ShareCourseActivity) getActivity()).share(SHARE_MEDIA.QQ, UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.QQ),"","","");
                break;
            case R.id.teacher_share_message:
                ((ShareCourseActivity) getActivity()).share(SHARE_MEDIA.SMS, true,"","","");
                break;
        }
    }
}
