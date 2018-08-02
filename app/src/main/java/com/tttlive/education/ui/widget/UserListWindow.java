package com.tttlive.education.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.tttlive.basic.education.R;

/**
 * Author: sunny
 * Time: 2018/8/1
 * description:
 */

public class UserListWindow extends PopupWindow {

    public UserListWindow(Context context) {
        super(context);

        initWindow(context);
    }

    private void initWindow(Context context) {
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_personnel_detail_list_land, null);
        setContentView(popView);
        setAnimationStyle(R.style.AnimBottomIn);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
    }
}
