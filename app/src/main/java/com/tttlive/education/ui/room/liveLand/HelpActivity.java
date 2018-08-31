package com.tttlive.education.ui.room.liveLand;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.QueryString;
import com.tttlive.education.ui.widget.LevelPickDialog;
import com.tttlive.education.util.StatusBarCompat;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity implements HelpInterface, TextWatcher, LevelPickDialog.OnSelectListener {

    @BindView(R.id.et_question_description)
    TextView et_question_description;

    @BindView(R.id.tv_length)
    TextView tv_length;

    @BindView(R.id.tv_level)
    TextView tv_level;

    private int limit = 100; // 字数限制
    private CharSequence beforeSeq; // 保存修改前的值

    private int afterStart;
    private int afterCount;

    private LevelPickDialog levelPickDialog;

    private HelpPresenter presenter;


    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_help;
    }

    @Override
    protected void findView() {

        StatusBarCompat.compat(this, getResources().getColor(R.color.color_FF1093ED));
    }

    @Override
    protected void initView() {
        et_question_description.addTextChangedListener(this);
        presenter = new HelpPresenter(this);

        initData();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s.length() + (after - count) > limit) {
            beforeSeq = s.subSequence(start, start + count);
            toastShort("不能超过" + limit + "字！");
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > before && s.length() > limit) { //如果字符数增加时，且当前字符数超过限制了, 保存原串用于还原
            afterStart = start;
            afterCount = count;
        }
        tv_length.setText(String.format("%d/" + limit, s.length()));
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > limit) {
            s.replace(afterStart, afterStart + afterCount, beforeSeq);
        }
    }

    @OnClick({R.id.iv_close, R.id.tv_level, R.id.tv_send})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_level:
                showSelectDialog();
                break;
            case R.id.tv_send:
                send();
                toastShort("发送");
                break;
        }
    }

    //弹出底部等级选择框
    private void showSelectDialog() {

        if (levelPickDialog == null) {
            levelPickDialog = new LevelPickDialog(this);
            levelPickDialog.setSelectListener(this);
        }
        levelPickDialog.show();
    }

    @Override
    public void selectLevel(String level, int index) {
        tv_level.setText(level);
    }

    //发送
    private void send() {
        String questionContent = et_question_description.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put(QueryString.CONTENT, questionContent);
        presenter.sendQuestion(params);
    }


    @Override
    public void onSendSuccess(BaseResponse<Object> response) {
         if(response.getCode() == 0) {
             finish();
         }

    }

    @Override
    public void onSendFailed(BaseResponse<Object> response) {
        toastShort(response.getMessage());
    }

    public static void actionStart(Context context, long lessonId, long courseId, long teacherId,
                                    long SID, long SubscribeId) {
        Intent intent = new Intent(context, HelpActivity.class);
        intent.putExtra(QueryString.LESSON_ID, lessonId);
        intent.putExtra(QueryString.COURSE_ID, courseId);
        intent.putExtra(QueryString.TEACHER_ID, teacherId);
        intent.putExtra(QueryString.SID, SID);
        intent.putExtra(QueryString.SUBSCRIBE_ID, SubscribeId);
        context.startActivity(intent);
    }

    private void initData() {
        Intent intent = getIntent();
        intent.getLongExtra(QueryString.LESSON_ID, 0);
        intent.getLongExtra(QueryString.COURSE_ID, 0);
        intent.getLongExtra(QueryString.TEACHER_ID, 0);
        intent.getLongExtra(QueryString.SID, 0);
        intent.getLongExtra(QueryString.SUBSCRIBE_ID, 0);
    }

}
