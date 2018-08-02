package com.tttlive.education.util;

import android.app.Activity;
import android.graphics.Bitmap;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;



public class ShareUtil {

    private ShareAction shareAction;

    private ShareUtil(ShareAction shareAction) {
        this.shareAction = shareAction;
    }

    public void share() {
        shareAction.share();
    }

    public static class Build {
        private Activity mActivity;
        private UMShareListener listener;
        private String url;
        private String title;
        private String description;
        private UMImage thumb;
        private SHARE_MEDIA share_media;
        private ShareAction shareAction;

        public Build(Activity activity) {
            this.mActivity = activity;
        }

        public Build shareMedia(SHARE_MEDIA share_media) {
            this.share_media = share_media;
            return this;
        }

        public Build listener(UMShareListener listener) {
            this.listener = listener;
            return this;
        }

        public Build url(String url) {
            this.url = url;
            return this;
        }

        public Build title(String title) {
            this.title = title;
            return this;
        }

        public Build description(String descrption) {
            this.description = descrption;
            return this;
        }

        public Build thumb(String imageurl) {
            this.thumb = new UMImage(mActivity, imageurl);//网络图片
            return this;
        }

        public Build thumb(File file) {
            this.thumb = new UMImage(mActivity, file);//文件
            return this;
        }

        public Build thumb(int resId) {
            this.thumb = new UMImage(mActivity, resId);//资源ID
            return this;
        }

        public Build thumb(Bitmap bitmap) {
            this.thumb = new UMImage(mActivity, bitmap);//bitmap
            return this;
        }

        public Build thumb(byte[] bytes) {
            this.thumb = new UMImage(mActivity, bytes);//字节流
            return this;
        }

        public ShareUtil build() {
            UMWeb web = new UMWeb(url);
            web.setTitle(this.title);//标题
            web.setThumb(this.thumb);  //缩略图
            web.setDescription(this.description);//描述
            this.shareAction = new ShareAction(this.mActivity)
                    .setPlatform(this.share_media).setCallback(this.listener).withMedia(web);

            return new ShareUtil(this.shareAction);
        }
    }
}
