package com.tttlive.education.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tttlive.basic.education.R;

/**
 * Created by Administrator on 2018/5/30/0030.
 */

public class VideoView extends DragScaleView {
    private String mId;
    private setOnClickListener mOnClickLien;
    private View video_utile_land;
    private String flagUserId;
    private RelativeLayout rl_video_land;
    private ImageView live_stauts_authorization;
    private ImageView live_stauts_phone;
    private ImageView live_stauts_camera;
    private RelativeLayout land_rl_live_microphone_one;

    public RelativeLayout getRl_video_land() {
        return rl_video_land;
    }

    public void setRl_video_land(RelativeLayout rl_video_land) {
        this.rl_video_land = rl_video_land;
    }

    public String getFlagUserId() {
        return flagUserId;
    }

    public void setFlagUserId(String flagUserId) {
        this.flagUserId = flagUserId;
    }

    public VideoView(Context context, String id, setOnClickListener onClickLien) {
        super(context);
        this.mId = id;
        this.mOnClickLien = onClickLien;
        initView(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        video_utile_land = inflate(context, R.layout.live_video_utile_land, this);
        live_stauts_authorization = video_utile_land.findViewById(R.id.live_video_stauts_authorization);
        live_stauts_phone = video_utile_land.findViewById(R.id.live_video_stauts_phone);
        live_stauts_camera = video_utile_land.findViewById(R.id.live_video_stauts_camera);
        land_rl_live_microphone_one = video_utile_land.findViewById(R.id.land_rl_live_microphone_one);
        live_stauts_authorization.setVisibility(GONE);
        live_stauts_camera.setVisibility(GONE);
        live_stauts_phone.setVisibility(GONE);
        land_rl_live_microphone_one.setVisibility(GONE);
        this.rl_video_land = (RelativeLayout)video_utile_land;
    }

    @Override
    protected void childOnLayout() {

    }

    @Override
    protected void onLayoutMove() {

    }

    @Override
    protected void onLayoutClick() {
        //单击点击事件
        Log.e(" ----  ", "单击点击事件");
        mOnClickLien.OnClickListener(mId);
    }

    @Override
    protected void onLayoutZoom() {

    }

    @Override
    protected void onLayoutDoubleTap() {

    }

    @Override
    protected void resetVideo() {

    }

    public interface setOnClickListener {
        void OnClickListener(String id);
    }

    public ImageView getLive_stauts_authorization() {
        return live_stauts_authorization;
    }

    public void setLive_stauts_authorization(ImageView live_stauts_authorization) {
        this.live_stauts_authorization = live_stauts_authorization;
    }

    public ImageView getLive_stauts_phone() {
        return live_stauts_phone;
    }

    public void setLive_stauts_phone(ImageView live_stauts_phone) {
        this.live_stauts_phone = live_stauts_phone;
    }

    public ImageView getLive_stauts_camera() {
        return live_stauts_camera;
    }

    public void setLive_stauts_camera(ImageView live_stauts_camera) {
        this.live_stauts_camera = live_stauts_camera;
    }

    public RelativeLayout getLand_rl_live_microphone_one() {
        return land_rl_live_microphone_one;
    }

    public void setLand_rl_live_microphone_one(RelativeLayout land_rl_live_microphone_one) {
        this.land_rl_live_microphone_one = land_rl_live_microphone_one;
    }
}
