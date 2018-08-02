package com.tttlive.education.ui.share;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tttlive.education.constant.Constant;
import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseFragment;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrliu on 2018/3/26.
 * 此类用于:分享到老师端
 */

public class ShareTeacherFragment extends BaseFragment {


    @BindView(R.id.share_path)
    LinearLayout mSharePath;
    @BindView(R.id.share_code)
    LinearLayout mShareCode;
    @BindView(R.id.share_title)
    TextView mShareTitle;
    @BindView(R.id.share_time)
    TextView mShareTime;
    @BindView(R.id.share_tv_code)
    TextView mShareTvCode;
    //    @BindView(R.id.layout_share_student)
    //    LinearLayout mShareStudent ;
    //    @BindView(R.id.layout_share_teacher)
    //    LinearLayout mShareTeacher ;
    @BindView(R.id.teacher_share_wechat)
    LinearLayout mShareWechat;
    @BindView(R.id.teacher_share_qq)
    LinearLayout mShareQQ;
    @BindView(R.id.teacher_share_message)
    LinearLayout mShareMes;
    private PopupWindow popupWindow;
    private static final String TIMESTART = "time_start";
    private static final String TIMEEND = "time_end";
    private static final String TIMETITLE = "title";
    private static final String TIME_INVITE_CODE = "invite_code";
    private static final String TIME_INVITE_ROOMID = "roomId";
    private String teacherType = "3";

    private String mTimeStart;
    private String mTimeEnd;
    private String mTitle;
    private String mInvite;
    private String mRoomId;

    public static ShareTeacherFragment newInstance(String timeStart, String timeEnd, String title, String invite , String roomId) {
        Bundle args = new Bundle();
        ShareTeacherFragment fragment = new ShareTeacherFragment();
        args.putString(TIMESTART, timeStart);
        args.putString(TIMEEND, timeEnd);
        args.putString(TIMETITLE, title);
        args.putString(TIME_INVITE_CODE, invite);
        args.putString(TIME_INVITE_ROOMID , roomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_share;
    }

    @Override
    protected void initViews(View view) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mTimeStart = arguments.getString(TIMESTART);
            mTimeEnd = arguments.getString(TIMEEND);
            mTitle = arguments.getString(TIMETITLE);
            mInvite = arguments.getString(TIME_INVITE_CODE);
            mRoomId = arguments.getString(TIME_INVITE_ROOMID);
        }

        mShareTitle.setText(mTitle);
        mShareTime.setText(mTimeStart + "-" + mTimeEnd);
        String invitationCode = getResources().getString(R.string.invitation_teacher_code);
        String invitation = String.format(invitationCode, mInvite);
        mShareTvCode.setText(invitation);

        mShareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String invitationCode = getResources().getString(R.string.invitation_student_code);
                String invitation = String.format(invitationCode, mInvite);
                copyLink(mInvite);
//                toastShort("复制成功");
            }
        });

        //分享
        mSharePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharepop(mContentView);
            }
        });
    }

    private void showSharepop(View view) {
        {
            //自定义PopupWindow的布局
            View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_share_teacher, null);
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            RelativeLayout layout_share_teacher = contentView.findViewById(R.id.layout_share_teacher);

            LinearLayout teacher_share_wechat = contentView.findViewById(R.id.teacher_share_wechat);
            LinearLayout teacher_share_qq = contentView.findViewById(R.id.teacher_share_qq);
            LinearLayout teacher_share_message = contentView.findViewById(R.id.teacher_share_message);


            teacher_share_wechat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ShareCourseActivity) getActivity()).share(SHARE_MEDIA.WEIXIN, UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.WEIXIN), Constant.SHARE_TEACHER_URL, mInvite, teacherType);
                }
            });
            teacher_share_qq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ShareCourseActivity) getActivity()).share(SHARE_MEDIA.QQ, UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.WEIXIN_CIRCLE), Constant.SHARE_TEACHER_URL, mInvite, teacherType);
                }
            });
            teacher_share_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ShareCourseActivity) getActivity()).share(SHARE_MEDIA.SMS, true, Constant.SHARE_TEACHER_URL, mInvite, teacherType);
                }
            });


            layout_share_teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
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


    }



    /**
     * 复制链接
     */
    private void copyLink(String invite) {
        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        String invitationCode = Constant.SHARE_TEACHER_URL + mRoomId + "  " + getResources().getString(R.string.teacher_invite_code);
        String invitation = String.format(invitationCode,invite);
        clipboardManager.setText(invitation);
        toastShort("复制成功");
    }

}
