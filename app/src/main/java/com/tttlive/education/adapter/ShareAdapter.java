package com.tttlive.education.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tttlive.basic.education.R;

/**
 * Author: sunny
 * Time: 2018/7/31
 * description:
 */

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private String[] titles = {"微信", "微信朋友圈", "链接"};
    private int[] resIds = {
            R.drawable.icon_share_wechat,
            R.drawable.icon_share_group,
            R.drawable.icon_share_url};

    @Override
    public ShareAdapter.ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false));
    }

    @Override
    public void onBindViewHolder(ShareAdapter.ShareViewHolder holder, final int position) {
        holder.title.setText(titles[position]);
        holder.icon.setImageResource(resIds[position]);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;
        public LinearLayout layout;

        public ShareViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
            layout = itemView.findViewById(R.id.layout);
        }
}


}
