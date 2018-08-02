package com.tttlive.education.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.tttlive.education.constant.Constant;
import com.tttlive.basic.education.R;

/**
 * Created by Administrator on 2018/5/9/0009.
 */

public class PicturePreviewPopWindowUtil {
    private Context mContext;
    private PopupWindow popupWindow;
    private TextView tv_picture_back;
    private PhotoView pictur_courseware_photoView;

    private BackOnClickListener backOnClickListener = new BackOnClickListener();


    public void showPicturePreviewUtil(Context context , View view  , String data){
        this.mContext = context;
        View pContenView = LayoutInflater.from(context).inflate(R.layout.picture_preview , null);
        popupWindow = new PopupWindow(pContenView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        tv_picture_back = pContenView.findViewById(R.id.tv_picture_back);
        pictur_courseware_photoView = pContenView.findViewById(R.id.pictur_courseware_photoView);
        tv_picture_back.setOnClickListener(backOnClickListener);

        pictur_courseware_photoView.enable();
        pictur_courseware_photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(mContext)
                .load(data)
                .crossFade()
                .into(pictur_courseware_photoView);


        popupWindow.setAnimationStyle(R.style.AnimBottomIn);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置PopupWindow显示的位置
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);



    }


    /**
     * 返回
     */
    private class BackOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (popupWindow != null && popupWindow.isShowing()){
                popupWindow.dismiss();
            }
        }
    }

}
