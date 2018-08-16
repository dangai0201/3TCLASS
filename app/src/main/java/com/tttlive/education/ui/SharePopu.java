package com.tttlive.education.ui;

/**
 * Created by feiyue on 2017/12/4.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tttlive.basic.education.R;


public class SharePopu extends PopupWindow {


    //    private String[] titles = {"微信", "微信朋友圈", "QQ", "空间", "链接"};
    private String[] titles = {"微信", "微信朋友圈", "链接"};
    private int[] resIds = {
            R.drawable.icon_share_wechat,
            R.drawable.icon_share_group,
            R.drawable.icon_share_url};

    private View mMenuView;
    private RecyclerView recyclerView;

    public SharePopu(Activity context, OnItemClickListener listener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popu_share_layout, null);
        recyclerView = mMenuView.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(new Adapter(listener));
        mMenuView.findViewById(R.id.share_btn_cancel).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
        this.setContentView(mMenuView);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottomIn);
//        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(new BitmapDrawable());
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private OnItemClickListener listener;

        public Adapter(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShareViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder hl, final int position) {
            final ShareViewHolder holder = (ShareViewHolder) hl;
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

