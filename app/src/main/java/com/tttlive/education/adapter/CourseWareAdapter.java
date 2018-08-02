package com.tttlive.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.ui.room.DocsListbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/10/0010.
 * 课件
 */

public class CourseWareAdapter extends RecyclerView.Adapter<CourseWareAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<DocsListbean.ListBean> mList;
    private Context mContext;
    private int currentCheckedItemPosition;
    private boolean isChecked = false;

    private CourseWareViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(CourseWareViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface CourseWareViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public CourseWareAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    public CourseWareAdapter(Context context, ArrayList<DocsListbean.ListBean> list) {
        this.mContext = context;
        this.mList = list;
        currentCheckedItemPosition = 0;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_courseware_utile, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_name.setText(mList.get(position).getOriginalName());
        holder.item_size.setText(mList.get(position).getPage() + " 页");
        holder.itemView.setTag(position);
        if (isChecked) {
            if (currentCheckedItemPosition == position) {
                holder.item_name.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_FF1093ED));
                holder.item_size.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                holder.item_name.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_FFF6F6F6));
                holder.item_size.setTextColor(mContext.getResources().getColor(R.color.black));
            }

        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }

    }

    public ArrayList<DocsListbean.ListBean> getList() {
        return mList;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }

    }

    /**
     * 修改点击背景
     *
     * @param position
     * @param checked
     */
    public void check(int position, boolean checked) {
        isChecked = checked;
        setDefaultCheckedItemPosition(position);
        notifyDataSetChanged();
    }

    public void setDefaultCheckedItemPosition(int position) {
        currentCheckedItemPosition = position;
    }

    /**
     * 刷新课件列表
     *
     * @param courseWareList
     */
    public void refresh(List<DocsListbean.ListBean> courseWareList) {
        if (courseWareList != null && courseWareList.size() >= 0) {
            mList.clear();
            mList.addAll(courseWareList);
            notifyDataSetChanged();
        }
    }

    /**
     * 加载更多课件
     *
     * @param courseWareList
     */
    public void loadMore(List<DocsListbean.ListBean> courseWareList) {
        if (courseWareList != null && courseWareList.size() >= 0) {
            mList.addAll(courseWareList);
            notifyDataSetChanged();
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_name;
        public TextView item_size;

        public ViewHolder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_course_name);
            item_size = itemView.findViewById(R.id.item_course_size);
        }
    }


}
