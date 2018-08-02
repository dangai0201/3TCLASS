package com.tttlive.education.ui.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.util.DateUtils;
import com.tttlive.education.util.SPTools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by mrliu on 2018/3/16.
 * 此类用于：创建课程页面
 */

public class CreateCourseActivity extends BaseActivity implements CourseUI {

    @BindView(R.id.big_class)
    LinearLayout bigClass;
    @BindView(R.id.tv_big_class)
    TextView tvBigClass ;
    @BindView(R.id.tv_big_num_lim)
    TextView tvBigNumLim ;
    @BindView(R.id.middle_class)
    LinearLayout middleClass;
    @BindView(R.id.tv_middle_class)
    TextView tvMilldeClass ;
    @BindView(R.id.tv_middle_num_lim)
    TextView tvMiddleNumLim ;
    @BindView(R.id.small_class)
    LinearLayout smallClass;
    @BindView(R.id.tv_small_class)
    TextView tvSmallClass ;
    @BindView(R.id.tv_small_num_lim)
    TextView tvSmallNumLim ;
    @BindView(R.id.course_name_content)
    EditText mCourseName;
    @BindView(R.id.course_num_content)
    TextView mCourseNum;
    @BindView(R.id.course_time_content)
    TextView mCourseStartTime;
    @BindView(R.id.course_end_time_content)
    TextView mCourseTimeEnd;
    @BindView(R.id.rl_course_num)
    RelativeLayout mCourseNumRl;
    @BindView(R.id.bt_enter_room)
    Button mCourseConfirm;

    private TimePickerView pvCustomTime;
    private int mCourseType = 0;
    private String currentTime;
    private String mNum;//输入的人数

    private int numOk = -1; //人数是否输入正确
    private AccountLoginBean mLoginInfo;
    private CoursePresenter mPresenter;

    private boolean isshowToast = true;

    public static Intent creatIntent(Context context) {
        Intent intent = new Intent(context, CreateCourseActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_create_course;
    }

    @Override
    protected void findView() {
        setBtGlobalLeft(getResources().getDrawable(R.drawable.icon_back));
        setTvGlobalTitleName(R.string.creat_course);
        mLoginInfo = SPTools.getInstance(this).getLoginInfo();
        if (mLoginInfo == null) return;
        mPresenter = new CoursePresenter(this);
        initSelector(middleClass);

        currentTime = getCurrentTime();//获取当前时间

    }

    @Override
    protected void initView() {
        onClick(mBtTitleLeft, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackPressed();
            }
        });

        onClick(bigClass, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                initSelector(bigClass);
            }
        });

        onClick(middleClass, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                initSelector(middleClass);
            }
        });

        onClick(smallClass, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                initSelector(smallClass);
            }
        });

        onClick(mCourseStartTime, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //起始时间
                showTimePicker(mCourseStartTime, true);
            }
        });


        onClick(mCourseTimeEnd, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //结束时间
                showTimePicker(mCourseTimeEnd, false);
            }
        });

        onLongClick(mCourseConfirm, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                submit();
            }
        });

    }

    /**
     * 提交服务器
     */
    private void submit() {
        String courseName = mCourseName.getText().toString();
        String courseNum = "10";
        String courseStarttime = mCourseStartTime.getText().toString();
        String coursetimeend = mCourseTimeEnd.getText().toString();
        String appId = mLoginInfo.getAppId();//应用ID
        String userName = mLoginInfo.getUserName();//用户名称
        int userId = mLoginInfo.getUserId();//用户ID


        if (isOK(1, courseName) && isOK(2, courseStarttime)) {

            long startTimeStamp = DateUtils.getStringToDate(courseStarttime);//课程开始时间
            long endTimeStamp;//结束时间
            long dur;//时长

            if (0 == mCourseType) {
                //有时长类型,结束时间不能为空
                if (isOK(3, coursetimeend)) {
                    endTimeStamp = DateUtils.getStringToDate(coursetimeend);//课程结束时间
                    dur = endTimeStamp - startTimeStamp;//时长

                    if (smallClass.isSelected()) {
                        //一对一课程
                        mPresenter.createCourse(userId + "", courseName, startTimeStamp + "",
                                Constant.SMALLCOURSE, dur + "", mCourseType + "", 1 + "", appId, userName,endTimeStamp+"");

                    } else {
                        if (isOK(4, courseNum)) {

                            if (bigClass.isSelected()) {
                                //大班课
                                if (0 == numOk) {
                                    showError(5);
                                } else {
                                    mPresenter.createCourse(userId + "", courseName, startTimeStamp + "",
                                            Constant.BIGCOURSE, dur + "", mCourseType + "", courseNum, appId, userName,endTimeStamp+"");
                                }

                            } else {
                                //小班课
                                if (0 == numOk) {
                                    showError(6);
                                } else {
                                    Log.e("submit","numok=="+numOk+"");
                                    mPresenter.createCourse(userId + "", courseName, startTimeStamp + "",
                                            Constant.MIDDLECOURSE, dur + "", mCourseType + "", courseNum, appId, userName,endTimeStamp+"");
                                }

                            }


                        }
                    }
                }

            } else {


                if (smallClass.isSelected()) {
                    //一对一课程

                    mPresenter.createCourse(userId + "", courseName, startTimeStamp + "",
                            Constant.SMALLCOURSE, "", mCourseType + "", 1 + "", appId, userName,"");
                } else {
                    if (isOK(4, courseNum)) {

                        if (bigClass.isSelected()) {
                            //大班课
                            if (0 == numOk) {
                                showError(5);
                            } else {
                                mPresenter.createCourse(userId + "", courseName, startTimeStamp + "",
                                        Constant.BIGCOURSE, "", mCourseType + "", courseNum, appId, userName,"");
                            }

                        } else {
                            //小班课
                            if (0 == numOk) {
                                showError(6);
                            } else {
                                mPresenter.createCourse(userId + "", courseName, startTimeStamp + "",
                                        Constant.MIDDLECOURSE, "", mCourseType + "", courseNum, appId, userName,"");
                            }

                        }


                    }
                }
            }


        }


    }

    private boolean isOK(int code, String text) {
        if (!TextUtils.isEmpty(text)) {
            return true;
        } else {
            showError(code);
            return false;
        }
    }

    private void showError(int code) {
        switch (code) {
            case 1:
                toastShort(R.string.msg_creat_course_empty_error);
                break;
            case 2:
                toastShort(R.string.msg_creat_course_start_time_empty_error);
                break;
            case 3:
                toastShort(R.string.msg_creat_course_end_time_empty_error);
                break;
            case 4:
                toastShort(R.string.msg_creat_course_num_empty_error);
                break;
            case 5:
                toastShort(R.string.msg_creat_course_greater_num);
                break;
            case 6:
                toastShort(R.string.msg_creat_course_ten_to_one);
                break;
        }
    }

    /**
     * 显示时间选择器
     *
     * @param courseTime 控件
     * @param isStart    点击的是否是开始时间
     */
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

                if (compareTime(time, currentTime)) {
                    toastShort(R.string.msg_creat_now_time);
                    return;
                } else if (!isStart && mCourseType == 0 && compareTime(time, mCourseStartTime.getText().toString())) {
                    toastShort(R.string.msg_creat_end_time);
                    return;
                } else {
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

                        if (isStart) {
                            mRlForever.setVisibility(View.GONE);
                        } else {
                            mRlForever.setVisibility(View.VISIBLE);
                            if (1 == mCourseType) {
                                mRlForever.setSelected(true);
                            } else {
                                mRlForever.setSelected(false);
                            }

                        }

                        mRlForever.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mRlForever.isSelected()) {
                                    mRlForever.setSelected(false);
                                    mCourseType = 0;
                                } else {
                                    mRlForever.setSelected(true);
                                    mCourseType = 1;
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


    /**
     * 初始化选择器
     *
     * @param view
     */
    private void initSelector(LinearLayout view) {
        bigClass.setSelected(false);
        middleClass.setSelected(false);
        smallClass.setSelected(false);
        tvBigClass.setSelected(false);
        tvMilldeClass.setSelected(false);
        tvSmallClass.setSelected(false);
        tvBigNumLim.setSelected(false);
        tvMiddleNumLim.setSelected(false);
        tvSmallNumLim.setSelected(false);
        switch (view.getId()) {
            case R.id.big_class:
                bigClass.setSelected(true);
                tvBigClass.setSelected(true);
                tvBigNumLim.setSelected(true);

                break;
            case R.id.middle_class:
                middleClass.setSelected(true);
                tvMilldeClass.setSelected(true);
                tvMiddleNumLim.setSelected(true);
                break;
            case R.id.small_class:
                smallClass.setSelected(true);
                tvSmallClass.setSelected(true);
                tvSmallNumLim.setSelected(true);
                break;
        }
        initData();

    }


    /**
     * 初始化数据
     */
    private void initData() {
        if (smallClass.isSelected()) {
            mCourseNumRl.setVisibility(View.GONE);
        } else {
            mCourseNumRl.setVisibility(View.VISIBLE);
        }
        numOk = -1;//表示未输入
        mCourseType = 0;//默认有时长类型
        isshowToast = true ;//一开始默认可以弹toast
        mCourseName.setText("");

        mCourseStartTime.setText("");
        mCourseTimeEnd.setText("");


//        mCourseNum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                mNum = s.toString();
//                if (!TextUtils.isEmpty(mNum)) {
//                    if (middleClass.isSelected()) {
//                        //小班课人数在1到10人
//                        if (Integer.parseInt(mNum) > 10 && isshowToast) {
//                            toastShort("人数必须在1到10之间");
//                            isshowToast = false;
//                            numOk = 0;
//                        } else if(Integer.parseInt(mNum) <=10){
//                            isshowToast = true;
//                            numOk = 1;
//                        }
//
//                    }else if(bigClass.isSelected()){
//                        if(Integer.parseInt(mNum) > 10){
//                            numOk = 1;
//                        }else {
//                            numOk = 0;
//                        }
//                    }
//                }
//            }
//        });

//        mCourseNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (bigClass.isSelected()) {
//                    if (!hasFocus) {
//                        if (!TextUtils.isEmpty(mNum)&&Integer.parseInt(mNum) <= 10) {
//                            toastShort("人数必须大于10人");
//                            numOk = 0;
//                        } else {
//                            numOk = 1;
//                        }
//                    }
//                }
//            }
//        });


    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return format.format(date);
    }

    //获取当前时间
    private String getCurrentTime() {
        Date currentTime = new Date();
        String time = getTime(currentTime);
        return time;
    }

    //比较时间大小,如果第一个参数比第二个参数小,返回true ,否则返回false
    private boolean compareTime(String data1, String data2) {
        if (data1.compareTo(data2) < 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void createSuccess() {
        toastShort(R.string.msg_creat_successful);
        Constant.CREATCOURSELIVE = "1";
        finish();
    }
}
