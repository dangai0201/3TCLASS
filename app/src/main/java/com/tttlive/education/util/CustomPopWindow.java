package com.tttlive.education.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tttlive.basic.education.R;


/**
 * Author: sunny
 * Time: 2018/7/11
 * description:
 */

public class CustomPopWindow extends PopupWindow {


    private static final long TIME_DISMISS = 1000;//弹窗自动消失时间，单位毫秒
    private View window;
    private final View contentView;
    private TextView tv_message;
    private String message;
    private Handler handler = new Handler();

    public CustomPopWindow(Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        window = inflater.inflate(R.layout.custom_popwindow, null);

        contentView = window.findViewById(R.id.pop_layout);
        tv_message = window.findViewById(R.id.tv_message);

        //设置SelectPicPopupWindow的View
        this.setContentView(window);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为不透明度20%
        ColorDrawable dw = new ColorDrawable(0x33000000);
        //设置弹出窗体的背景
        this.setBackgroundDrawable(dw);


        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        window.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int top = contentView.getTop();
                int left = contentView.getLeft();
                int height = contentView.getHeight();
                int width = contentView.getWidth();

                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top || y > top + height || x < left || x > left + width) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    //设置popupWindow标题
    public void setMessage(String message) {
        this.message = message;
        if(tv_message != null) {
            tv_message.setText(message);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        handler.postDelayed(new MyRunnable(),TIME_DISMISS);
    }

    class MyRunnable implements Runnable{

        @Override
        public void run() {
            if(CustomPopWindow.this.isShowing()) {
                CustomPopWindow.this.dismiss();
            }

        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
