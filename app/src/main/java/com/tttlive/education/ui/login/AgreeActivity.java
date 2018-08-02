package com.tttlive.education.ui.login;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AgreeActivity extends BaseActivity {

    private static final java.lang.String URL = "http://dashboard.3ttech.cn/index/server-clause";

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_agree;
    }

    @Override
    protected void findView() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.color_FF1093ED));
    }

    @Override
    protected void initView() {
        showLoadingDialog();
        webView.loadUrl(Constant.SERVER_CLAUSE_URL);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);  //加上这一行网页为响应式的
        settings.setUseWideViewPort(true);//这里需要设置为true，才能让WebView支持<meta>标签的viewport属性
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;   //返回true， 立即跳转，返回false,打开网页有延时
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoadingDialog();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

}
