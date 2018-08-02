package com.tttlive.education.ui.course.edit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.CourseListBean;
import com.tttlive.education.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by mrliu on 2018/3/10.
 * 此类用于:编辑课程的页面
 */

public class EditCourseActivity extends BaseActivity implements EditCourseInterface{

    @BindView(R.id.rl_course_num)
    RelativeLayout rlCourseNum ;
    @BindView(R.id.course_name_content)
    EditText mCourseName ;
    @BindView(R.id.course_num_content)
    EditText mCourseNum ;
    @BindView(R.id.course_time_content)
    TextView mCourseStartTime ;
    @BindView(R.id.course_confirm)
    TextView mCourseConfirm ;
    @BindView(R.id.course_end_time_content)
    TextView mCourseTimeEnd ;
    private static final String EXTRA_COURSE = "course_info";
    private String courseInfo;
    private CourseListBean.ItemListBean mItemListBean;
    private EditCoursePresenter mPresenter;
    private String mUserId;
    private String mTitle;
    private String mToplimit;
    private String mTimeStart;
    private String mTimeEnd;
    private String mDuration;
    private String mDurationType;
    private String mCapacity;
    private String mCoverSrc;
    private String mJoinRule;
    private String mDesc;

    private boolean isSucess = false ;
    private TimePickerView pvCustomTime ;
    private String currentTime ;

    private int mCourseType = 0 ;
    private String mType;
    private String mNum;//输入的人数
    private boolean isshowToast = true;
    private boolean numOk = true ;

    public static Intent creatIntent(Context context, @NonNull String data){
        Intent intent = new Intent(context,EditCourseActivity.class);
        intent.putExtra(EXTRA_COURSE,data);
        return intent ;
    }

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_edit_course;
    }

    @Override
    protected void findView() {
        mPresenter = new EditCoursePresenter(this);

        setTvGlobalTitleName(getString(R.string.edit_course));
        mTvTitleName.setTextColor(getResources().getColor(R.color.color_FF666666));
        setBtGlobalLeft(R.drawable.icon_back);
        Intent intent = getIntent();
        if(intent!=null){
            courseInfo = intent.getStringExtra(EXTRA_COURSE);
            mItemListBean = new Gson().fromJson(courseInfo, CourseListBean.ItemListBean.class);
            mUserId = mItemListBean.getUserId();
        }
    }

    @Override
    protected void initView() {
        final String id = mItemListBean.getId();//课程ID
        mTitle = mItemListBean.getTitle();//课程标题
        mToplimit = mItemListBean.getToplimit();//人数限制
        //开始时间
        mTimeStart = mItemListBean.getTimeStart();
        //结束时间
        mTimeEnd = mItemListBean.getTimeEnd();
        //班级类型
        mType = mItemListBean.getType();
        //课程时长
        mDuration = mItemListBean.getDuration();
        //课程时长类型
        mDurationType = mItemListBean.getDurationType();
        //教师容量
        mCapacity = mItemListBean.getCapacity();
        //封面图
        mCoverSrc = mItemListBean.getCoverSrc();
        //观看规则
        mJoinRule = mItemListBean.getJoinRule();
        //课程描述
        mDesc = mItemListBean.getDesc();

        currentTime = getCurrentTime();//获取当前时间
        if("2".equals(mType)){
            rlCourseNum.setVisibility(View.GONE);
        }else {
            rlCourseNum.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(mTitle)){
            mCourseName.setText(mTitle);
            mCourseName.setSelection(mTitle.length());
        }
        if(!TextUtils.isEmpty(mCapacity)){
            mCourseNum.setText(mCapacity);
            mCourseNum.setSelection(mCapacity.length());
        }

        if(!TextUtils.isEmpty(mTimeStart)){
            mCourseStartTime.setText(mTimeStart);
        }

        if(!TextUtils.isEmpty(mTimeEnd)){
            mCourseTimeEnd.setText(mTimeEnd);
        }


        if("0".equals(mDurationType)){
            //有时长类型的结束时间可以点击
            mCourseTimeEnd.setBackground(getResources().getDrawable(R.drawable.shape_item_course));
            mCourseTimeEnd.setTextColor(getResources().getColor(R.color.color_FF333333));
            mCourseTimeEnd.setEnabled(true);
            mCourseType = 0 ;
        }else if("1".equals(mDurationType)){
            //无时长类型的结束时间不可以点击
            mCourseTimeEnd.setBackground(getResources().getDrawable(R.drawable.shape_login_button_bg));
            mCourseTimeEnd.setTextColor(getResources().getColor(R.color.color_FFC4C4C4));
            mCourseTimeEnd.setEnabled(false);
            mCourseType = 1 ;
        }

        limitNum();
        onClick(mBtTitleLeft, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBack();
            }
        });

        onClick(mCourseStartTime, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //起始时间
                showTimePicker(mCourseStartTime,true);
            }
        });


        onClick(mCourseTimeEnd, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //结束时间
                showTimePicker(mCourseTimeEnd,false);
            }
        });

        onClick(mCourseConfirm, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onSaveCourse(id, mType);
            }
        });


    }
    //人数的限制
    private void limitNum() {
        mCourseNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mNum = s.toString();
                if (!TextUtils.isEmpty(mNum)) {
                    if ("1".equals(mType)) {//小班课
                        if (Integer.parseInt(mNum) > 10 && isshowToast) {
                            toastShort(R.string.msg_creat_course_ten_to_one);
                            isshowToast = false;
                            numOk = false ;
                        }else if(Integer.parseInt(mNum) <=10){
                            numOk = true ;
                        }

                    }else if("0".equals(mType)){//大班课
                        if(Integer.parseInt(mNum)>10){
                            isshowToast = true;
                            numOk = true ;
                        }else {
                            numOk = false ;
                        }
                    }
                }
            }
        });

        mCourseNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ("0".equals(mType)) {//大班课
                    if (!hasFocus) {
                        isshowToast = true ;
                        if (!TextUtils.isEmpty(mNum)&&Integer.parseInt(mNum) <= 10) {
                            toastShort(R.string.msg_creat_course_greater_num);
                            numOk =false ;
                        }else {
                            numOk = true ;
                        }
                    }
                }
            }
        });
    }


    //时间选择
    private void showTimePicker(final TextView courseTime, final boolean isStart) {

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2050, 3, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String time = getTime(date);

                if(compareTime(time,currentTime)){
                    toastShort("选择时间不能小于当前时间");
                    return;
                }else if(!isStart&&mCourseType==0&&compareTime(time,mCourseStartTime.getText().toString())){
                    toastShort("结束时间不能小于起始时间");
                    return;
                }else {
                    if(!isStart&& 1 == mCourseType ){
                        courseTime.setText("永久有效");
                    }else {
                        courseTime.setText(time);
                    }
                }

            }
        })
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.sel_course_time, new CustomListener() {

                    private RelativeLayout mRlForever;

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        TextView ivCancel = v.findViewById(R.id.tv_cancle);
                        mRlForever = v.findViewById(R.id.rl_is_forever);

                        if(isStart){
                            mRlForever.setVisibility(View.GONE);
                        }else {
                            mRlForever.setVisibility(View.VISIBLE);
                            if(1==mCourseType){
                                mRlForever.setSelected(true);
                            }else {
                                mRlForever.setSelected(false);
                            }

                        }

                        mRlForever.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(mRlForever.isSelected()){
                                    mRlForever.setSelected(false);
                                    mCourseType = 0 ;
                                }else {
                                    mRlForever.setSelected(true);
                                    mCourseType = 1 ;
                                }
                            }
                        });

                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentSize(18)
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.6f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

        pvCustomTime.show();

    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return format.format(date);
    }

    //获取当前时间
    private String getCurrentTime(){
        Date currentTime = new Date();
        String time = getTime(currentTime);
        return time;
    }

    //比较时间大小,如果第一个参数比第二个参数小,返回true ,否则返回false
    private boolean compareTime(String data1,String data2){
        if(data1.compareTo(data2)<0){
            return true ;
        }else {
            return false ;
        }
    }
    private void onBack() {
        if(isSucess) {
            Intent intent = new Intent();
            intent.putExtra(Constant.COURSETITLE,mCourseName.getText().toString());
            intent.putExtra(Constant.COURSESTARTTIME,mCourseStartTime.getText().toString());
            if(0==mCourseType){
                //有时长类型
                intent.putExtra(Constant.COURSESENDTIME,mCourseTimeEnd.getText().toString());
            }
            setResult(RESULT_OK,intent);
        }
        finish();
    }

    private String endTime ;
    private void onSaveCourse(String id, String type) {
        if(isOK(1,mCourseName.getText().toString())&&
                isOK(3,mCourseStartTime.getText().toString())){
            String coursenum = mCourseNum.getText().toString();
            //编辑课程
            String startTime = mCourseStartTime.getText().toString();
            long startTimeStamp = DateUtils.getStringToDate(startTime);
            if(0==mCourseType){
                //有时长类型的
                if(!TextUtils.isEmpty(mCourseTimeEnd.getText().toString())){
                    endTime = mCourseTimeEnd.getText().toString();
                }

                if(isOK(4,endTime)){

                    long endTimeStamp = DateUtils.getStringToDate(endTime);
                    long dur = endTimeStamp - startTimeStamp ;//时长
                    String s = mCourseName.getText().toString();
                    if(s.equals(mTitle)&&coursenum.equals(mCapacity)&&mTimeStart.equals(startTime)&&mTimeEnd.equals(endTime)){
                        this.finish();
                    }else {
                        if(numOk){
                            mPresenter.editCourse(id,type, s, startTimeStamp+"", dur+"", mCourseType+"", coursenum, mCoverSrc, mJoinRule, mDesc,mUserId,endTimeStamp+"");
                        }else {
                            toastShort("请输入正确的人数");
                        }
                    }


                }

            }else {
                //无时长类型的
                if(mCourseName.getText().toString().equals(mTitle)&&coursenum.equals(mCapacity)&&mTimeStart.equals(startTime)){
                    this.finish();
                }else {
                    if(numOk){
                        mPresenter.editCourse(id,type, mTitle, startTimeStamp+"", "", mCourseType+"", coursenum, mCoverSrc, mJoinRule, mDesc,mUserId,"");
                    }else {
                        toastShort("请输入正确的人数");
                    }

                }


            }

        }
    }

    private boolean isOK(int code,String text){
        if(!TextUtils.isEmpty(text)){
            return true ;
        }else {
            showError(code);
            return false ;
        }
    }

    private void showError(int code) {
        switch (code){
            case 1:
                toastShort(R.string.msg_creat_course_empty_error);
                break;
            case 2:
                toastShort(R.string.msg_creat_course_num_empty_error);
                break;
            case 3:
                toastShort(R.string.msg_creat_course_start_time_empty_error);
                break;
            case 4:
                toastShort(R.string.msg_creat_course_end_time_empty_error);
                break;
        }
    }

    @Override
    public void editSucess() {
        isSucess = true ;
        toastShort("更改成功");
        onBack();
    }

}
