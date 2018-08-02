package com.tttlive.education.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tttlive.basic.education.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: sunny
 * Time: 2018/7/27
 * description:网络状态视图
 */

public class NetStatusView extends RelativeLayout {

    /**
     * 丢包率
     */
    private String lineLost;

    /**
     * 网络延时
     */
    private String delay;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程状态
     */
    private String courseStatus;

    /**
     * 时间
     */
    private String time;

    private Unbinder unbinder;

    @BindView(R.id.iv_netStatus)
    ImageView iv_netStatus;

    @BindView(R.id.tv_netDelay)
    TextView tv_netDelay;

    @BindView(R.id.tv_lostLine_percent)
    TextView tv_lostLine_percent;

    @BindView(R.id.tv_netStatus)
    TextView tv_netStatus;

    @BindView(R.id.tv_courseName)
    TextView tv_courseName;

    @BindView(R.id.tv_isLive)
    TextView tv_isLive;

    @BindView(R.id.tv_liveTime)
    TextView tv_liveTime;


    public NetStatusView(Context context) {
        this(context, null);

    }

    public NetStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_net_status, this);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void setLineLost(String lineLost) {
        if (!TextUtils.isEmpty(lineLost)){
            this.lineLost = lineLost;
            if (tv_lostLine_percent != null){
                tv_lostLine_percent.setText(lineLost);
            }
        }
    }

    public void setDelay(String delay) {
        if (!TextUtils.isEmpty(delay)){
            this.delay = delay;
            if (iv_netStatus != null && tv_netDelay != null && tv_lostLine_percent != null && tv_netStatus != null){
                iv_netStatus.setBackground(getDrawable(delay));
                tv_netDelay.setText(String.format("%s%s", delay.trim(), getResources().getString(R.string.millisecond)));
                tv_netDelay.setTextColor(getColorRes(delay));
                tv_lostLine_percent.setTextColor(getColorRes(delay));
                tv_netStatus.setText(getDescription(delay));
                tv_netStatus.setTextColor(getColorRes(delay));
            }
        }
    }

    /**
     * 根据网络延时获取对应颜色
     *
     * @param delayStr 网络延时
     */
    private int getColorRes(String delayStr) {
        int delayInt = Integer.parseInt(delayStr.trim());
        if (delayInt <= 5) {
            return getResources().getColor(R.color.color_43EB73);
        } else if (delayInt > 5 && delayInt <= 10) {
            return getResources().getColor(R.color.color_FFB500);
        } else {
            return getResources().getColor(R.color.color_FF3B30);
        }
    }

    /**
     * 根据网络延时获取对应的图标
     *
     * @param delayStr 网络延时
     * @return 网络状态图标
     */
    private Drawable getDrawable(String delayStr) {
        int delayInt = Integer.parseInt(delayStr.trim());
        if (delayInt <= 5) {
            return getResources().getDrawable(R.drawable.living_network_stauts_excellent_icon);
        } else if (delayInt > 5 && delayInt <= 10) {
            return getResources().getDrawable(R.drawable.living_network_stauts_general_icon);
        } else {
            return getResources().getDrawable(R.drawable.living_network_stauts_difference_icon);
        }
    }

    /**
     * 根据网络延时获取对应的描述
     *
     * @param delayStr 网络延时
     * @return 网络状态描述
     */
    private String getDescription(String delayStr) {
        int delayInt = Integer.parseInt(delayStr.trim());
        if (delayInt <= 5) {
            return getResources().getString(R.string.network_stauts_optimal);
        } else if (delayInt > 5 && delayInt <= 10) {
            return getResources().getString(R.string.network_stauts_good);
        } else {
            return getResources().getString(R.string.network_stauts_poor);
        }
    }

    /**
     * 设置课程名称
     *
     * @param courseName 课程名称
     */
    public void setCourseName(String courseName) {
        if (!TextUtils.isEmpty(courseName)){
            this.courseName = courseName;
            if (tv_courseName != null){
                tv_courseName.setText(courseName);
            }
        }
    }

    /**
     * 设置直播状态
     *
     * @param courseStatus 直播状态
     */
    public void setCourseStatus(String courseStatus) {
        if (!TextUtils.isEmpty(courseStatus)){
            this.courseStatus = courseStatus;
            if (tv_isLive != null){
                tv_isLive.setText(courseStatus);
            }
        }
    }

    /**
     * 设置时间
     * @param time 时间
     */
    public void setTime(String time) {
        this.time = time;
        tv_liveTime.setText(time);
    }

    public String getTime() {
        return time;
    }

    public String getLineLost() {
        return lineLost;
    }

    public String getDelay() {
        return delay;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseStatus() {
        return courseStatus;
    }
}
