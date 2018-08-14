package com.tttlive.education.ui.widget;

import android.content.Context;
import android.view.ViewGroup;

import com.wushuangtech.videocore.RemoteSurfaceView;

/**
 * Author: sunny
 * Time: 2018/8/10
 * description:
 */

public class WrapperSurfaceView  extends RemoteSurfaceView {
    public WrapperSurfaceView(Context context) {
        super(context);
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        this.setLayoutParams(layoutParams);
        invalidate();
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        this.setLayoutParams(layoutParams);
        invalidate();

    }
}
