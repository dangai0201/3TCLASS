package com.tttlive.education.ui.room.webviewtool;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tttlive.basic.education.R;

import java.util.ArrayList;
import java.util.List;

public class WebviewToolPopupWindow  {

    private Context mContext;
    private BasePopupWindow mBasePopupWindow;
    private LinearLayout mTvClose;
    private RecyclerView mRecyclerView;
    private List<WebViewToolBean> mToolBeanList;
    private ToolAdapter mToolAdapter;
    private WebView mWebView;
    private WebViewToolBean mCurrentToolBean;

    public WebviewToolPopupWindow(Context context, WebView webView) {
        this.mContext = context;
        mWebView = webView;
        mBasePopupWindow = new BasePopupWindow(context);
        mBasePopupWindow.setAnimationStyle(R.style.AnimBottomIn);
        mBasePopupWindow.setOutsideTouchable(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_webview_tool, null);
        mBasePopupWindow.setContentView(contentView);

        mTvClose = (LinearLayout) contentView.findViewById(R.id.tv_close);
        mTvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBasePopupWindow.dismiss();
            }
        });
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rv_tool);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        initData();
        ToolAdapter.ItemClickListener listener = new ToolAdapter.ItemClickListener() {
            @Override
            public void onClick(WebViewToolBean bean) {
                mCurrentToolBean = bean;
                if (bean.isSelected()) {
                    return;
                }

                int select = -1;
                int oldSelect = -1;
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
        mToolBeanList = new ArrayList<>();
        String[] toolsName = mContext.getResources().getStringArray(R.array.webview_tool_names);
        String[] toolsValue = mContext.getResources().getStringArray(R.array.webview_tool_values);
        TypedArray iconN = mContext.getResources().obtainTypedArray(R.array.webview_tool_icon_normal);
        TypedArray iconHovers = mContext.getResources().obtainTypedArray(R.array.webview_tool_icon_hover);
        for (int i = 0; i < toolsName.length; i++) {
            mToolBeanList.add(
                    new WebViewToolBean(i, toolsName[i], toolsValue[i], iconN.getResourceId(i, 0),  iconHovers.getResourceId(i, 0)));
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
}
