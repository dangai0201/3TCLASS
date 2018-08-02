package com.tttlive.education.ui.home;

import android.view.View;

/**
 * Author: sunny
 * Time: 2018/7/31
 * description:
 */


public abstract class SingleClickListener implements View.OnClickListener {
    private static long lastTimeMillis;
    private static final long MIN_CLICK_INTERVAL = 1000;

    protected boolean isTimeEnabled() {
        long currentTimeMillis = System.currentTimeMillis();
        if ((currentTimeMillis - lastTimeMillis) > MIN_CLICK_INTERVAL) {
            lastTimeMillis = currentTimeMillis;
            return true;
        }
        return false;
    }
}
