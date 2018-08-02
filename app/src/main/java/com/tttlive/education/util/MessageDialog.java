package com.tttlive.education.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tttlive.basic.education.R;

/**
 * Created by mrliu on 2018/3/10.
 * 此类用于: 此类用于一般通用的dialog
 */

public class MessageDialog extends Dialog implements View.OnClickListener{
    private TextView mContent,mLeftTv,mRightTv;
    private LinearLayout mCancel, mCommit;
    private String mContentStr,mHintStr,mLeftBut,mRightBut;
    private MessageDialogListener listener;
    private TextView mHint;
    private View mViewBg;
    private int mHeight , mWidth;

    public MessageDialog(Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_message);
        findView();
        initLayout();
        init();
    }
    private void initLayout() {

        //获取当前Activity所在的窗体
        Window dialogWindow = this.getWindow();

        dialogWindow.setGravity( Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        if (mWidth != 0 && mHeight != 0){
            lp.width = mWidth;
            lp.height = mHeight;
        }else {
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        }

//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }
    public void setHint(String hint){
        mHintStr = hint ;
    }

    public void setHeight(int height){
        mHeight = height;
    }

    public void setWidth(int width){
        mWidth = width;
    }

    public void setHint(int strId) {
        mHintStr = this.getContext().getResources().getString(strId);
    }


    public void setContent(int strId) {
        mContentStr = this.getContext().getResources().getString(strId);
    }

    public void setContent(String str) {
        mContentStr = str;
    }

    public void setLeftButton(String leftButton){
        mLeftBut = leftButton ;
    }

    public void setLeftButton(int strId){
        mLeftBut = this.getContext().getResources().getString(strId);
    }

    public void setRightButton(String leftButton){
        mRightBut = leftButton ;
    }

    public void setRightButton(int strId){
        mRightBut = this.getContext().getResources().getString(strId);
    }



    public void setMessageDialogListener(MessageDialogListener listener) {
        this.listener = listener;
    }

    private void init() {
        mHint.setText(mHintStr+"");
        mContent.setText(mContentStr + "");
        if(!TextUtils.isEmpty(mLeftBut)){
            mLeftTv.setText(mLeftBut+"");
        }
       if(!TextUtils.isEmpty(mRightBut)){
           mRightTv.setText(mRightBut+"");
       }
        mCancel.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        mViewBg.setOnClickListener(this);
    }

    private void findView() {
        mHint = findViewById(R.id.hint);
        mContent = findViewById(R.id.dialog_message_content);
        mCancel = findViewById(R.id.cancle);
        mCommit = findViewById(R.id.confirm);
        mLeftTv = findViewById(R.id.tv_cancle);
        mRightTv = findViewById(R.id.tv_confirm);
        mViewBg = findViewById(R.id.view_bg);
    }

    @Override
    public void onClick(View v) {
        if (v == mCancel) {
            if (listener != null) {
                listener.onCancelClick(this);
            }
        } else if(v==mViewBg){
            dismiss();
        } else{
            if (listener != null) {
                listener.onCommitClick(this);
            }
        }
    }

    public interface MessageDialogListener {
        void onCancelClick(MessageDialog dialog);

        void onCommitClick(MessageDialog dialog);
    }
}
