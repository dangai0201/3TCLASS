package com.tttlive.education.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/3/10/0010.
 */

public abstract class SimpleRecyclerAdapter<Data ,Holder extends SimpleRecyclerHolder<Data>>
        extends RecyclerView.Adapter<Holder>{

    private List<Data> dataList;

    public SimpleRecyclerAdapter(List<Data> dataList) {
        this.dataList = dataList;
    }

    public final void setDataList(List<Data> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public final void appendData(List<Data> appends) {
        this.dataList.addAll(appends);
        notifyDataSetChanged();
    }

    public List<Data> getDataList(){
        return this.dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType),
                parent, false);
        return createHolder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.displayData(dataList.get(position));
    }

    @LayoutRes
    protected abstract int getItemLayoutId(int viewType);

    @NonNull
    protected abstract Holder createHolder(View view);

}
