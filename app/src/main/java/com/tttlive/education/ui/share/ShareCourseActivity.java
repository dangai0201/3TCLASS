package com.tttlive.education.ui.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.tttlive.education.constant.Constant;
import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseShareActivity;
import com.tttlive.education.ui.room.bean.ShareBean;
import com.tttlive.education.util.ShareUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by mrliu on 2018/3/26.
 * 此类用于:分享课程页面
 */

public class ShareCourseActivity extends BaseShareActivity implements ShareInterface{

    private static final String TAG_CLASS = ShareCourseActivity.class.getSimpleName();

    @BindView(R.id.student)
    RadioButton mStudent ;
    @BindView(R.id.teacher)
    RadioButton mTeacher;
//    @BindView(R.id.assistant)
//    RadioButton mAssistant;

    private SharePresenter sharePresenter;
    private ShareStudentFragment mShareStudentFragment;
    private ShareTeacherFragment mShareTeacherFragment;
  //  private ShareAssistantFragment mShareAssistantFragment;
    private int CURRENT_FRAGMENT_TYPE = 0;
    private int STUDENT_TYPE = 1 ;
    private int TEACHER_TYPE = 2 ;

  //  private int ASSISTANT_TYPE = 3 ;
    private static final String TIMESTART = "time_start";
    private static final String TIMEEND = "time_end";
    private static final String TIMETITLE = "title";
    private static final String TEACHER_ROOM_ID = "roomId";
    private String mTimeStart ;
    private String mTimeEnd ;
    private String mTitle ;
    private String mLinkUrl ;
    private String mInvitation ;
    private String mRoomId;
    private String student_invite_code;
    private String teacher_invite_code;
    private String inviteType;
    public static Intent creatIntent(Context context, String timeStart, String timeEnd, String title , String roomId){
        Intent intent = new Intent(context,ShareCourseActivity.class);
        intent.putExtra(TIMESTART,timeStart);
        intent.putExtra(TIMEEND,timeEnd);
        intent.putExtra(TIMETITLE,title);
        intent.putExtra(TEACHER_ROOM_ID , roomId);
        return intent ;
    }


    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_share_course;
    }

    @Override
    protected void findView() {
        sharePresenter = new SharePresenter(this);
        setBtGlobalLeft(getResources().getDrawable(R.drawable.icon_back));
        setTvGlobalTitleName(R.string.share_course);
        switchTitleStatus(STUDENT_TYPE);
        Intent intent = getIntent();
        if(intent!=null){
            mTimeStart = intent.getStringExtra(TIMESTART);
            mTimeEnd = intent.getStringExtra(TIMEEND);
            mTitle = intent.getStringExtra(TIMETITLE);
            mRoomId = intent.getStringExtra(TEACHER_ROOM_ID);
        }
        sharePresenter.getShareInvite(mRoomId);




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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.student,R.id.teacher})
    public void shareButtonClick(View view){

        switch (view.getId()){
            case R.id.student:
                CURRENT_FRAGMENT_TYPE = STUDENT_TYPE ;
                showStudentFragment();
                break;
            case R.id.teacher:
                CURRENT_FRAGMENT_TYPE = TEACHER_TYPE ;
                showTeacherFragement();
                break;

        }
        switchTitleStatus(CURRENT_FRAGMENT_TYPE);
    }

    /**
     * 状态改变
     */
    private void switchTitleStatus(int CURRENT_FRAGMENT_TYPE) {

        mStudent.setSelected(false);
        mTeacher.setSelected(false);


        switch (CURRENT_FRAGMENT_TYPE){
            case 1:
                mStudent.setSelected(true);
                break;
            case 2:
                mTeacher.setSelected(true);
                break;
//            case 3:
//                mAssistant.setSelected(true);
//                break;
        }
    }

    /**
     * 显示分享学生端
     */
    private void showStudentFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        if(mShareStudentFragment==null){
            if (student_invite_code != null){
                mShareStudentFragment = ShareStudentFragment.newInstance(mTimeStart,mTimeEnd,mTitle , student_invite_code , mRoomId);
                fragmentTransaction.add(R.id.fragmentContent,mShareStudentFragment);
            }
        }
        commitShowFragment(fragmentTransaction,mShareStudentFragment);
    }

    /**
     * 显示分享老师端
     */
    private void showTeacherFragement(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        if(mShareTeacherFragment==null){
            if (teacher_invite_code != null){
                mShareTeacherFragment = ShareTeacherFragment.newInstance(mTimeStart,mTimeEnd,mTitle,teacher_invite_code,mRoomId);
                fragmentTransaction.add(R.id.fragmentContent,mShareTeacherFragment);

            }
        }
        commitShowFragment(fragmentTransaction,mShareTeacherFragment);
    }

    /**
     * 显示分享助教端
     */
//    private void showAssistantFragement(){
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        hideAllFragment(fragmentTransaction);
//        if(mShareAssistantFragment==null){
//            mShareAssistantFragment = ShareAssistantFragment.newInstance(mTimeStart,mTimeEnd,mTitle);
//            fragmentTransaction.add(R.id.fragmentContent,mShareAssistantFragment);
//        }
//        commitShowFragment(fragmentTransaction,mShareAssistantFragment);
//    }

    /**
     * 显示某个Fragment
     * @param fragmentTransaction
     * @param fragment
     */
    private void commitShowFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 隐藏所有的Fragment
     * @param fragmentTransaction Fragment管理器
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        hideFragment(fragmentTransaction,mShareStudentFragment);
        hideFragment(fragmentTransaction,mShareTeacherFragment);
       // hideFragment(fragmentTransaction,mShareAssistantFragment);
    }

    /**
     * 隐藏单个的Fragment
     * @param fragmentTransaction
     * @param fragment
     */
    private void hideFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if(fragment!=null){
            fragmentTransaction.hide(fragment);
        }
    }

    /**
     * 开始分享
     * @param share_media 分享媒体
     * @param install 是否安装客户端
     * @param url 分享链接
     * @param invitation 邀请码
     */
    public void share(SHARE_MEDIA share_media, boolean install,String url,String invitation,String type){
        mLinkUrl = url;
        mInvitation = invitation ;
        inviteType = type;

        share(share_media,install);
    }

    @Override
    public void share(SHARE_MEDIA share_media, boolean install) {

        if (!install) { //未安装客户端
            toastShort("你还没有安装客户端,请先安装。");
            return;
        }
        if (inviteType.equals("3")){
            //教师
            new ShareUtil.Build(this).listener(this).url(Constant.SHARE_TEACHER_URL + mRoomId)
                    .title("教师").description("邀请码 : " + mInvitation)
                    .thumb(Constant.SHARE_IMAGE_URL).shareMedia(share_media).build().share();
        }else if (inviteType.equals("1")){
            //学生
            new ShareUtil.Build(this).listener(this).url(Constant.SHARE_CLASS_ROOM_URL + mRoomId)
                    .title("学员").description("邀请码 : " + mInvitation)
                    .thumb(Constant.SHARE_IMAGE_URL).shareMedia(share_media).build().share();

        }

    }

    @Override
    public void shareRoomInvite(ShareBean shareBean) {
        //获取房间邀请码
        Gson gson = new Gson();
        String ss = gson.toJson(shareBean);
        Log.e(TAG_CLASS + "邀请码返回 : " , ss);
        if (shareBean != null){
            for (int i = 0; i < shareBean.getStudent().size(); i++) {
                if (mRoomId.equals(shareBean.getStudent().get(i).getCourseId())){
                    student_invite_code = shareBean.getStudent().get(i).getInviteCode();
                }
            }

            for (int i = 0; i < shareBean.getTeacher().size(); i++) {
                if (mRoomId.equals(shareBean.getTeacher().get(i).getCourseId())){
                    teacher_invite_code = shareBean.getTeacher().get(i).getInviteCode();
                }
            }

            showStudentFragment();
        }


    }


}

