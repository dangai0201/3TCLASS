package com.tttlive.education.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tttlive.basic.education.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.li on 2018/5/29.
 */

public class ProfessionRecyclerViewAdapter extends RecyclerView.Adapter<ProfessionRecyclerViewAdapter.ViewHolder> {


    private List<String> mList = new ArrayList<>();


    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private boolean mIsSelectable = false;
    private String content;

    public ProfessionRecyclerViewAdapter(List<String> list) {
        if (list == null) {
            throw new IllegalArgumentException("model Data must not be null");
        }
        mList = list;
    }

    private LayoutInflater mInflater;
    private int  mNum ;
    private  String  arr[] = {"A","B","C","D","E","F","G","H"};

    public ProfessionRecyclerViewAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
    }

    public ProfessionRecyclerViewAdapter(Context context, int num) {

        mInflater = LayoutInflater.from(context);
        mNum = num;
    }

    //设置给定位置条目的选择状态
    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }

    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    //根据位置判断条目是否可选
    private boolean isSelectable() {
        return mIsSelectable;
    }


    //设置给定位置条目的可选与否的状态
    private void setSelectable(boolean selectable) {
        mIsSelectable = selectable;
    }

    public List<String> getSelectContent(){



        return mList;
    }

    public void setNum(int num){
        mNum = num;
        notifyDataSetChanged();

    }
//
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView itemView;


    }

    @Override
    public int getItemCount() {

        return mNum;

    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recyclerview_profession_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView = view.findViewById(R.id.tv_answer_option);
        return viewHolder;
    }


    /**
     * 设置值
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.itemView.setText(arr[i]);

            //是否选中
            if (isItemChecked(i)) {
                  viewHolder.itemView.setSelected(true);
                  viewHolder.itemView.setTextIsSelectable(true);
            } else {
                  viewHolder.itemView.setSelected(false);
                  viewHolder.itemView.setTextIsSelectable(false);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //点击的时候判断是否选中
                    if (isItemChecked(i)) {

                        viewHolder.itemView.setSelected(false);
                        viewHolder.itemView.setTextIsSelectable(false);
                        setItemChecked(i, false);

                        for (int j=0;j<mList.size();j++){
                            if (arr[i].equals(mList.get(j))){
                                mList.remove(j);
                            }
                        }
                    } else {
                        viewHolder.itemView.setSelected(true);
                        viewHolder.itemView.setTextIsSelectable(true);
                        setItemChecked(i, true);

                        mList.add(arr[i]);
                    }
                    notifyDataSetChanged();

                }
            });



    }

}