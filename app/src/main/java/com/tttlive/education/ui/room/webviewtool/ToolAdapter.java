package com.tttlive.education.ui.room.webviewtool;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tttlive.basic.education.R;

import java.util.List;

public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.MyHolder> {

    private Context mContext;
    private List<WebViewToolBean> mList;
    private ItemClickListener mListener;

    public ToolAdapter(Context context, List<WebViewToolBean> list, ItemClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tool_recycle_item, parent, false);
        return new MyHolder(view);
    }

    public void update(List<WebViewToolBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if (mList.get(position).isSelected()) {
            holder.mImageView.setImageDrawable(mContext.getResources().getDrawable(mList.get(position).getIconHover()));
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.color_FF1093ED));
        } else {
            holder.mImageView.setImageDrawable(mContext.getResources().getDrawable(mList.get(position).getIconNormal()));
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.color_FF666666));
        }
        holder.mTextView.setText(mList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ImageView mImageView;

        public MyHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_text);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    public interface ItemClickListener {
        void onClick(WebViewToolBean bean);
    }
}
