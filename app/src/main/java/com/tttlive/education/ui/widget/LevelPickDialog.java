package com.tttlive.education.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.tttlive.basic.education.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sunny on 2017/11/30.
 */

public class LevelPickDialog extends Dialog {

    private List<String> mOptionsItems = new ArrayList<>();

    public LevelPickDialog(Context context) {

        // 在构造方法里, 传入主题
        super(context, R.style.BottomDialogStyle);

        initWindow();

    }

    private void initWindow() {
        // 拿到Dialog的Window, 修改Window的属性
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_level_picker);

        initView();

        setEvent();

        initData();
    }

    private void setEvent() {

    }

    private void initView() {

        WheelView wheelView = findViewById(R.id.wheelview);

        wheelView.setCyclic(false);

        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public int getItemsCount() {
                return mOptionsItems == null ? 0 : mOptionsItems.size();
            }

            @Override
            public Object getItem(int index) {
                return mOptionsItems.get(index);
            }

            @Override
            public int indexOf(Object o) {
                return mOptionsItems.indexOf(o);
            }
        });
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if(listener != null) {
                    listener.selectLevel(mOptionsItems.get(index),index);
                }
            }
        });
    }

    private void initData() {
        mOptionsItems.add("特急");
        mOptionsItems.add("加急");
        mOptionsItems.add("平急");
    }

    private OnSelectListener listener;

    public void setSelectListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public interface OnSelectListener{
        void selectLevel(String level, int index);
    }

}