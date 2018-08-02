package com.tttlive.education.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/7/17/0017.
 * 广播服务
 */
public class BaseReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,
                "接收到的Intent的Action为：" + intent.getAction() + "\n 消息内容是：" + intent.getStringExtra("msg"),
                Toast.LENGTH_LONG).show();



    }

}
