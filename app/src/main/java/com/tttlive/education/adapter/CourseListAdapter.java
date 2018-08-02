package com.tttlive.education.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tttlive.basic.education.R;
import com.tttlive.education.data.CourseListBean;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewHolder;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;

/**
 * Author: sunny
 * Time: 2018/7/13
 * description:课程列表适配器
 */

public class CourseListAdapter extends BGARecyclerViewAdapter<CourseListBean.ItemListBean> {

    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String THREE = "3";


    public CourseListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_course_list_swipe);

    }

    @Override
    public BGARecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper, int viewType) {
        BGASwipeItemLayout swipeItemLayout = viewHolderHelper.getView(R.id.sil_item_swipe_root);
        swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
            @Override
            public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_swipe_delete);
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, CourseListBean.ItemListBean model) {

        String title = model.getTitle();
        String isLive = model.getIsLive();
        String timeStart = model.getTimeStart();
        String timeEnd = model.getTimeEnd();
        String publicCode = model.getPublicCode();
        if ("永久".equals(timeEnd)) {
            timeEnd = "长期";
        }
        String type = model.getType();
        helper.setText(R.id.tv_title, title);
        helper.setText(R.id.tv_isLive, getLiveText(isLive));
        helper.setImageResource(R.id.iv_isLive, getLiveIcon(isLive));
        helper.setText(R.id.tv_timeStart, timeStart);
        helper.setText(R.id.tv_timeEnd, timeEnd);
        helper.setImageResource(R.id.iv_type, getTypeIcon(type));
        helper.setText(R.id.tv_public_code, publicCode);

    }

    private String getLiveText(String isLive) {
        String text = "";
        switch (isLive) {
            case ZERO:
                text = mContext.getString(R.string.live_unStart);
                break;
            case ONE:
                text = mContext.getString(R.string.running);
                break;
            case TWO:
                text = mContext.getString(R.string.live_end);
                break;
            case THREE:
                text = mContext.getString(R.string.live_overdue);
                break;
        }

        return text;
    }

    private int getLiveIcon(String isLive) {
        int resId = R.drawable.home_stauts_unstar_icon;
        switch (isLive) {
            case ZERO:
                resId = R.drawable.home_stauts_unstar_icon;
                break;
            case ONE:
                resId = R.drawable.home_stauts_living_icon;
                break;
            case TWO:
                resId = R.drawable.home_stauts_stop_icon;
                break;
            case THREE:
                resId = R.drawable.home_stauts_error_icon;
                break;
        }
        return resId;
    }

    private int getTypeIcon(String type) {
        int resId = R.drawable.home_big_class_icon;
        switch (type) {
            case ZERO:
                resId = R.drawable.home_big_class_icon;
                break;
            case ONE:
                resId = R.drawable.home_small_class_icon;
                break;
            case TWO:

                break;
        }
        return resId;
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    @Override
    public void addNewData(List<CourseListBean.ItemListBean> data) {
        if (data != null && data.size() >= 0) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addMoreData(List<CourseListBean.ItemListBean> data) {
        if (data != null && data.size() >= 0) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void setOnRVItemClickListener(BGAOnRVItemClickListener onRVItemClickListener) {
        super.setOnRVItemClickListener(onRVItemClickListener);
    }
}
