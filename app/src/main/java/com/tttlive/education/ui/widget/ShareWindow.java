package com.tttlive.education.ui.widget;

/**
 * Author: sunny
 * Time: 2018/7/27
 * description:分享弹窗
 */

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.tttlive.basic.education.R;
import com.tttlive.education.adapter.ShareAdapter;
import com.tttlive.education.util.CustomToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


public class ShareWindow extends PopupWindow implements View.OnClickListener, ShareAdapter.OnItemClickListener {


    private Button btn_cancel;
    private Activity activity;
    private String shareUrl;
    private String iconUrl;
    private String title;
    private String description;
    private String copyUrl;

    private ShareWindow(Activity activity, String shareUrl, String iconUrl, String title, String description, String copyUrl) {
        super(activity);
        this.activity = activity;
        this.shareUrl = shareUrl;
        this.iconUrl = iconUrl;
        this.title = title;
        this.description = description;
        this.copyUrl = copyUrl;
        initPop(activity);
    }

    private void initPop(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popu_share_layout, null);
        this.setContentView(contentView);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottomIn);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);

        RecyclerView recyclerView = contentView.findViewById(R.id.rv_list);
        btn_cancel = contentView.findViewById(R.id.share_btn_cancel);
        btn_cancel.setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(new ShareAdapter());

    }

    /**
     * 设置屏幕的背景透明度
     *
     * @param bgAlpha 透明度[0f,1f]
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_cancel) {
            dismiss();
        }

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setBackgroundAlpha(0.7f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setBackgroundAlpha(1f);
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                share(SHARE_MEDIA.WEIXIN, UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN));
                break;
            case 1:
                share(SHARE_MEDIA.WEIXIN_CIRCLE, UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN_CIRCLE));
                break;
            case 2:
                copyShareLink(copyUrl);
                break;
        }
        this.dismiss();
    }

    /**
     * 分享方法
     *
     * @param share_media 分享渠道
     * @param install     是否安装了客户端
     */
    private void share(SHARE_MEDIA share_media, boolean install) {

        if (share_media == null) {//share_media null 复制链接
            return;
        }
        if (!install) { //未安装客户端
            toastShort("你还没有安装客户端,请先安装。");
            return;
        }

        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(this.title);//标题
        web.setThumb(new UMImage(activity, iconUrl));  //缩略图
        web.setDescription(this.description);//描述
        ShareAction shareAction = new ShareAction(activity)
                .setPlatform(share_media)
                .withMedia(web);
        shareAction.share();

    }

    /**
     * 复制链接
     *
     * @param copyUrl 链接url
     */
    private void copyShareLink(String copyUrl) {
        android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newRawUri("Label", Uri.parse(copyUrl));
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
            toastShort("复制成功");
        }
    }

    private void toastShort(String msg) {
        CustomToast.makeCustomText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static class Builder {
        private Activity mActivity;
        private String shareUrl;
        private String iconUrl;
        private String title;
        private String description;
        private String copyUrl;

        public Builder(Activity activity) {
            this.mActivity = activity;
        }


        public Builder shareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
            return this;
        }

        public Builder iconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder copyUrl(String copyUrl) {
            this.copyUrl = copyUrl;
            return this;
        }

        public ShareWindow build() {
            return new ShareWindow(mActivity, shareUrl, iconUrl, title, description, copyUrl);
        }
    }
}

