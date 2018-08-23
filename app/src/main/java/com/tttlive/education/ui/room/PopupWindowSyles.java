package com.tttlive.education.ui.room;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.data.InviteCodeLoginBean;
import com.tttlive.education.ui.room.liveLand.StudentActivityLand;
import com.tttlive.education.ui.room.liveLand.TeacherActivityLand;
import com.tttlive.education.util.StatusBarUtil;

/**
 * Created by mr.li on 2018/3/20.
 * 进入直播间进度页
 */

public class PopupWindowSyles implements View.OnClickListener {
    private PopupWindow popupWindow;
    private ProgressBar pb_progress_bar;
    private TextView tv_main_text;
    private ImageView iv_close;
    private MyThread myThread;

    private Context mContext;
    private Activity mActiviity;
    private int role;
    private String userId;
    private String courseId;
    private String nickName;
    private String pushRtmp;
    private String pullRtmp;
    private String titleName;
    private String mNotice;
    private String title;
    private String timeStart;
    private String inviteCode;

    //老师id
    private String masterUserId;
    private String type;
    private String courseTyoe;
    private String remark;
    private String code;
    private ObjectAnimator animator;


    public PopupWindowSyles(Context context, Activity activity) {
        this.mContext = context;
        this.mActiviity = activity;
    }


    public void popupWindowStylesTeacher(Context context,
                                         String code, int role, String userId, String courseId, String nickName,
                                         String pushRtmp, String courseType, String title, String timeStart, String remark) {

        this.mContext = context;
        this.role = role;
        this.userId = userId;
        this.courseId = courseId;
        this.nickName = nickName;
        this.pushRtmp = pushRtmp;
        this.courseTyoe = courseType;
        this.remark = remark;
        this.title = title;
        this.timeStart = timeStart;
        this.code = code;
        this.code = code;

    }

    public void popupWindowStylesStudent(Context context, InviteCodeLoginBean mInviteCodeBean, String inviteCode) {
        this.mContext = context;
        this.role = Integer.parseInt(mInviteCodeBean.getRole());
        this.masterUserId = mInviteCodeBean.getMasterUserId();
        this.userId = mInviteCodeBean.getUserId();
        this.courseId = mInviteCodeBean.getCourseId();
        this.nickName = mInviteCodeBean.getNickName();
        this.type = mInviteCodeBean.getType();
        this.titleName = mInviteCodeBean.getTitle();
        this.mNotice = mInviteCodeBean.getNotice();
        this.pullRtmp = mInviteCodeBean.getPullRtmp();
        this.title = mInviteCodeBean.getTitle();
        this.timeStart = mInviteCodeBean.getStartTime();
        this.inviteCode = inviteCode;

    }

    public void showProgressBar(View view, Context context) {
        StatusBarUtil.setStatusBarColor(mActiviity, context.getResources().getColor(R.color.color_082234));
        //自定义PopupWindow的布局
        View contentView = LayoutInflater.from(context).inflate(R.layout.loadingroom, null);
        //初始化PopupWindow,并为其设置布局文件
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

        pb_progress_bar = contentView.findViewById(R.id.pb_progress_bar);

        tv_main_text = contentView.findViewById(R.id.tv_main_text);
        iv_close = contentView.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
//        if(myThread == null) {
//            myThread = new MyThread();
//        }
//        myThread.start();
        showProgress();

        //设置PopupWindow显示的位置
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


//    Handler handler = new Handler() {
//        //接收消息，用于更新UI界面
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            int i = msg.what;
//            tv_main_text.setText(i + "%");
//            if (i == 100) {
//
//                if (role == 3) {
//                    mContext.startActivity(TeacherActivityLand.createIntent(mContext,code, role, userId,
//                            courseId, nickName, pushRtmp, courseTyoe, remark,title, timeStart));
//
//                } else {
//                    mContext.startActivity(StudentActivityLand.createIntent(mContext,
//                            role, masterUserId, userId, courseId, type, nickName, titleName, mNotice, pullRtmp,title,timeStart));
//
//                }
//                StatusBarUtil.setStatusBarColor(mActiviity , mContext.getResources().getColor(R.color.transparent));
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                popupWindow.dismiss();
//            }
//        }
//    };

    @Override
    public void onClick(View v) {
        if (v == iv_close && popupWindow != null) {
            tv_main_text.setText(0 + "%");
            pb_progress_bar.setProgress(0);
            popupWindow.dismiss();
            if (animator != null) {
                animator.cancel();
            }
//            if(handler != null) {
//                handler.removeCallbacksAndMessages(null);
//            }
//            if(myThread != null) {
//                myThread.interrupt();
//            }
        }
    }

    private void showProgress() {
        animator = ObjectAnimator
                .ofInt(pb_progress_bar, "progress", 100)
                .setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue("progress");
                tv_main_text.setText(progress + "%");
                pb_progress_bar.setProgress(progress);

                if (progress == 100) {

                    if (role == 3) {
                        mContext.startActivity(TeacherActivityLand.createIntent(mContext, code, role, userId,
                                courseId, nickName, pushRtmp, courseTyoe, remark, title, timeStart));

                    } else {
                        mContext.startActivity(StudentActivityLand.createIntent(mContext,
                                role, masterUserId, userId, courseId, type, nickName, titleName, mNotice, pullRtmp, title, timeStart));

                    }
                    StatusBarUtil.setStatusBarColor(mActiviity, mContext.getResources().getColor(R.color.transparent));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    popupWindow.dismiss();
                }

            }
        });
        animator.start();


    }


    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 0; i <= 100; i++) {
                pb_progress_bar.setProgress(i);
                //在子线程中发送消息
//                handler.sendEmptyMessage(i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
