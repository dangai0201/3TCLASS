package com.tttlive.education.ui.room.webviewtool;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.tttlive.basic.education.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/30/0030.
 * 横屏白板工具栏
 */
public class WebviewToolPopupWindowLand {


    private LinearLayout rl_brush_tool_view;
    private Context mContext;
    private BasePopupWindow mBasePopupWindow;
    private LinearLayout mTvClose;
    private RecyclerView mRecyclerView;
    private List<WebViewToolBean> mToolBeanList;
    private ToolAdapter mToolAdapter;
    private WebView mWebView;
    private WebViewToolBean mCurrentToolBean;
    private int web_state = 0;

    public WebviewToolPopupWindowLand(Context context, WebView webView , int type) {
        this.mContext = context;
        this.web_state = type;
        mWebView = webView;
        mBasePopupWindow = new BasePopupWindow(context);
        mBasePopupWindow.setAnimationStyle(R.style.AnimBottomIn);
        mBasePopupWindow.setOutsideTouchable(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_webview_tool_land, null);
        mBasePopupWindow.setContentView(contentView);

        rl_brush_tool_view = contentView.findViewById(R.id.brush_tool_view_land);
        mTvClose = (LinearLayout) contentView.findViewById(R.id.tv_close);
        mTvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBasePopupWindow.dismiss();
            }
        });
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rv_tool);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_brush_tool_view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height * 2/5;
        rl_brush_tool_view.setLayoutParams(layoutParams);
        mBasePopupWindow.setHeight(height * 2/5);

        if (web_state == 1){
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, 10));
        }else if (web_state == 3){
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, 9));
        }
        initData();
        ToolAdapter.ItemClickListener listener = new ToolAdapter.ItemClickListener() {
            @Override
            public void onClick(WebViewToolBean bean) {
                mCurrentToolBean = bean;
                int select = -1;
                int oldSelect = -1;

                if (bean.isSelected()) {
                    for (int i = 0; i < mToolBeanList.size(); i++) {
                        if (mToolBeanList.get(i).getId() == bean.getId()){
                            mToolBeanList.get(i).setSelected(false);
                            setWebView("Default");
                        }
                    }
                    mToolAdapter.update(mToolBeanList);
                    return;
                }

                for (int i = 0; i < mToolBeanList.size(); i++) {
                    if (mToolBeanList.get(i).getId() == bean.getId()) {
                        select = i;
                    }
                    if (mToolBeanList.get(i).isSelected()) {
                        oldSelect = i;
                    }
                }

                if (select >= 0) {
                    if (select != 9){
                        mToolBeanList.get(select).setSelected(true);
                    }
                    setWebView(mToolBeanList.get(select).getToolName());
                    if (select == 9){
                        if (mToolBeanList.get(select).getToolName().equals("clearAll")){
                            setWebView("Default");
                        }
                    }
                }

                if (oldSelect >= 0) {
                    mToolBeanList.get(oldSelect).setSelected(false);
                }
                mToolAdapter.update(mToolBeanList);
            }
        };
        mToolAdapter = new ToolAdapter(mContext, mToolBeanList, listener);
        mRecyclerView.setAdapter(mToolAdapter);
    }

    private void setWebView(String tool) {
        if (mWebView != null) {
            String js = "javascript:boardSetTool(\'" + tool + "\')";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.e("webview", value);
                    }
                });
            } else {
                mWebView.loadUrl(js);
            }
        }
    }

    private void initData() {
        if (web_state == 1){
            //老师使用
            mToolBeanList = new ArrayList<>();
            String[] toolsName = mContext.getResources().getStringArray(R.array.webview_tool_names);
            String[] toolsValue = mContext.getResources().getStringArray(R.array.webview_tool_values);
            TypedArray iconN = mContext.getResources().obtainTypedArray(R.array.webview_tool_icon_normal);
            TypedArray iconHovers = mContext.getResources().obtainTypedArray(R.array.webview_tool_icon_hover);
            for (int i = 0; i < toolsName.length; i++) {
                mToolBeanList.add(
                        new WebViewToolBean(i, toolsName[i], toolsValue[i], iconN.getResourceId(i, 0),  iconHovers.getResourceId(i, 0)));
            }

        }else if (web_state == 3){
            //学生使用
            mToolBeanList = new ArrayList<>();
            String[] toolsName = mContext.getResources().getStringArray(R.array.webview_tool_names_student);
            String[] toolsValue = mContext.getResources().getStringArray(R.array.webview_tool_values_student);
            TypedArray iconN = mContext.getResources().obtainTypedArray(R.array.webview_tool_icon_normal_student);
            TypedArray iconHovers = mContext.getResources().obtainTypedArray(R.array.webview_tool_icon_hover_student);
            for (int i = 0; i < toolsName.length; i++) {
                mToolBeanList.add(
                        new WebViewToolBean(i, toolsName[i], toolsValue[i], iconN.getResourceId(i, 0),  iconHovers.getResourceId(i, 0)));
            }

        }

    }

    /**
     * 翻页后设置已经悬着的工具
     */
    public void setCurrentWebViewTool() {
        if (mCurrentToolBean != null) {
            setWebView(mCurrentToolBean.getToolName());
        }
    }

    /**
     * 显示PopupWindow
     * @param view
     */
    public void showPop(View view) {
        if (mBasePopupWindow != null) {
            if (!mBasePopupWindow.isShowing()) {
                mBasePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                mBasePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
                mBasePopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        }
    }

    public void dismissPop() {
        if (mBasePopupWindow != null) {
            if (mBasePopupWindow.isShowing()) {
                mBasePopupWindow.dismiss();
            }
        }
    }

    public void trunPage(){
        for (int i = 0; i < mToolBeanList.size(); i++) {
            if (mToolBeanList.get(i).isSelected()){
                mToolBeanList.get(i).setSelected(false);
                mToolAdapter.update(mToolBeanList);
                break;
            }
        }
    }

    /**
     * 取消绘画
     */
    public void defaultWite(){
        setWebView("Default");
    }

}
