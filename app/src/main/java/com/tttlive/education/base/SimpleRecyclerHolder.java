package com.tttlive.education.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Iverson on 2017/10/17 下午3:19
 * 此类用于：用于适配器holder
 */

public abstract class SimpleRecyclerHolder<DataType> extends RecyclerView.ViewHolder {

    public SimpleRecyclerHolder(View itemView) {
        super(itemView);
    }

    public abstract void displayData(DataType data);


}