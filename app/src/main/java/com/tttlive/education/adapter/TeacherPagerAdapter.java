package com.tttlive.education.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.tttlive.basic.education.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7/0007.
 * 教师端课件数据源
 */
public class TeacherPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> listString;
    private PhotoView photoView;

    public TeacherPagerAdapter(Context context , List<String> list){
        this.mContext = context;
        this.listString = list;

    }

    @Override
    public int getCount() {
        return listString != null ? listString.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        RelativeLayout rView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.adapter_courseware , null);
        photoView = rView.findViewById(R.id.courseware_photoView);
        photoView.enable();
        photoView.setScaleType(ImageView.ScaleType.FIT_START);
        ViewGroup parent = (ViewGroup) photoView.getParent();
        if (parent != null){
            parent.removeAllViews();
        }

        Glide.with(mContext)
                .load(listString.get(position))
                .crossFade()
                .into(photoView);

        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }





}
