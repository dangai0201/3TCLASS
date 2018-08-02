package com.tttlive.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.data.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: sunny
 * Time: 2018/7/12
 * description:
 */

public class PopListAdapter extends RecyclerView.Adapter<PopListAdapter.MyHolder> implements View.OnClickListener {

    private Context mContext;
    private List<AccountBean> mList;

    public PopListAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_account, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_mobileNo.setText(mList.get(position).getMobile());
        holder.itemView.setTag(position);
        holder.iv_delete.setTag(position);
        holder.itemView.setOnClickListener(this);
        holder.iv_delete.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 刷新数据
     *
     * @param accountBeanList
     */
    public void refresh(List<AccountBean> accountBeanList) {
        if (accountBeanList == null) {
            return;
        }
        mList.clear();
        mList.addAll(accountBeanList);
        notifyDataSetChanged();
    }

    /**
     * 删除指定位置数据
     *
     * @param position
     */
    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }


    /**
     * 获取当前数据列表
     *
     * @return
     */
    public List<AccountBean> getAccountList() {
        return mList;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }
        int position = (int) v.getTag();
        if (v.getId() == R.id.iv_delete) {
            listener.onDeleteClick(position);
        } else {
            listener.onItemClick(position);
        }

    }

    class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_mobileNo;
        private ImageView iv_delete;

        public MyHolder(View itemView) {
            super(itemView);

            tv_mobileNo = itemView.findViewById(R.id.tv_mobileNo);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {

        void onItemClick(int position);

        void onDeleteClick(int position);

        void onDismiss();
    }

}
