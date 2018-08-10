package com.tttlive.education.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tttlive.basic.education.R;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/5/30/0030.
 */

public class VideoView extends RelativeLayout {
    private String mId;
    private setOnClickListener mOnClickLien;
    private View video_utile_land;
    private String flagUserId;
    private RelativeLayout rl_video_land;
    private ImageView live_stauts_authorization;
    private ImageView live_stauts_phone;
    private ImageView live_stauts_camera;
    private RelativeLayout land_rl_live_microphone_one;

    private static final String W = "width";
    private static final String H = "height";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String TRANSLATION_X = "translationX";
    private static final String TRANSLATION_Y = "translationY";
    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";
    private static final long DURATION = 2000L;

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
        this.rl_video_land = (RelativeLayout) video_utile_land;
    }

//    @Override
//    protected void childOnLayout() {
//
//    }
//
//    @Override
//    protected void onLayoutMove() {
//
//    }
//
//    @Override
//    protected void onLayoutClick() {
//        //单击点击事件
//        Log.e(" ----  ", "单击点击事件");
//        mOnClickLien.OnClickListener(mId);
//    }
//
//    @Override
//    protected void onLayoutZoom() {
//
//    }
//
//    @Override
//    protected void onLayoutDoubleTap() {
//
//    }
//
//    @Override
//    protected void resetVideo() {
//
//    }

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

    private void setWidth(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        this.setLayoutParams(layoutParams);
        invalidate();
    }

    private void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        this.setLayoutParams(layoutParams);
        invalidate();
    }



    public AnimatorSet getLayoutChangeAnimator(int width, int height, float x, float y) {
        WeakReference<VideoView> videoViewWeakReference = new WeakReference<>(this);
        VideoView videoView = videoViewWeakReference.get();
        ObjectAnimator widthAnimator = ObjectAnimator.ofInt(videoView, W, width);
        ObjectAnimator heightAnimator = ObjectAnimator.ofInt(videoView, H, height);
        setWidth(width);
        setHeight(height);
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(videoView, X, x);
        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(videoView, Y, y);
        AnimatorSet set = new AnimatorSet();
//        set.playTogether( xAnimator, yAnimator);
        set.playTogether(widthAnimator, heightAnimator, xAnimator, yAnimator);
        set.setDuration(DURATION);
        return set;

    }

//    public AnimatorSet getLayoutChangeAnimatorSet(int width, int height, int translationX, int translationY) {
//        WeakReference<VideoView> videoViewWeakReference = new WeakReference<>(this);
//        VideoView videoView = videoViewWeakReference.get();
//        ObjectAnimator widthAnimator = ObjectAnimator.ofInt(videoView, W, width);
//        ObjectAnimator heightAnimator = ObjectAnimator.ofInt(videoView, H, height);
//        ObjectAnimator xAnimator = ObjectAnimator.ofInt(videoView, TRANSLATION_X, translationX);
//        ObjectAnimator yAnimator = ObjectAnimator.ofInt(videoView, TRANSLATION_Y, translationY);
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(widthAnimator, heightAnimator, xAnimator, yAnimator);
//        set.setDuration(DURATION);
//        return set;
//
//    }
    public ValueAnimator getLayoutChangeAnimatorSet(int width, int height, float translationX, float translationY) {
        WeakReference<VideoView> videoViewWeakReference = new WeakReference<>(this);
        VideoView videoView = videoViewWeakReference.get();
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofInt(W, width);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofInt(H, height);
        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat(TRANSLATION_X, translationX);
        PropertyValuesHolder holder4 = PropertyValuesHolder.ofFloat(TRANSLATION_Y, translationY);
        return ObjectAnimator.ofPropertyValuesHolder(videoView, holder1, holder2, holder3, holder4);

    }

    private ViewPropertyAnimator getPropertyAnimator(float scaleX, float scaleY, float translationX, float translationY) {

        this.setPivotX(0);
        this.setPivotY(0);

        return this.animate()
                .scaleX(scaleX)
                .scaleY(scaleY)
                .translationX(translationX)
                .translationY(translationY)
                .setDuration(DURATION)
                .setInterpolator(new LinearInterpolator());

    }

    public ValueAnimator getPropertyAnimatorByCount(int scaleX, int scaleY, int translationX, int translationY) {
        this.setPivotX(0);
        this.setPivotY(0);
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat(SCALE_X, scaleX);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat(SCALE_Y, scaleY);
        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat(TRANSLATION_X, translationX);
        PropertyValuesHolder holder4 = PropertyValuesHolder.ofFloat(TRANSLATION_Y, translationY);


        return ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2, holder3, holder4);
    }

}
