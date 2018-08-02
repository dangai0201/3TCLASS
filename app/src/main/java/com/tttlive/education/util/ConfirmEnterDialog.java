package com.tttlive.education.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tttlive.basic.education.R;


/**
 * Created by mrliu on 2018/3/21.
 * 此类用于:
 */

public class ConfirmEnterDialog extends Dialog implements View.OnClickListener{

    private TextView mContent;
    private String mContentStr;
    private LinearLayout mWaiter, mConfirm;

    private EnterDialogListener listener;

    public ConfirmEnterDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_confirm);
        findView();
        initLayout();
        init();
    }

    private void init() {

        mContent.setText(mContentStr + "");
        mWaiter.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    private void initLayout() {
        //获取当前Activity所在的窗体
        Window dialogWindow = this.getWindow();

        dialogWindow.setGravity( Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    public void setContent(int strId) {
        mContentStr = this.getContext().getResources().getString(strId);
    }

    public void setContent(String str) {
        mContentStr = str;
    }

    public void setEnterDialogListener(EnterDialogListener listener) {
        this.listener = listener;
    }
    private void findView() {

        mContent = findViewById(R.id.dialog_message_content);
        mWaiter = findViewById(R.id.cancle);
        mConfirm = findViewById(R.id.confirm);

    }

    @Override
    public void onClick(View v) {
        if (v == mWaiter) {
            if (listener != null) {
                listener.onCancelClick(this);
            }
        } else {
            if (listener != null) {
                listener.onCommitClick(this);
            }
        }
    }

    public interface EnterDialogListener {
        void onCancelClick(ConfirmEnterDialog dialog);

        void onCommitClick(ConfirmEnterDialog dialog);
    }
}
