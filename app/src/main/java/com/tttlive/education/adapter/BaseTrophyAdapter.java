package com.tttlive.education.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tttlive.education.constant.Constant;
import com.tttlive.basic.education.R;
import com.tttlive.education.ui.room.bean.CustomBean;
import com.tttlive.education.ui.room.bean.JoinQunRoomBean;
import com.tttlive.education.ui.room.bean.JoinRoomBean;
import com.tttlive.education.util.CircleImageView;

import java.text.ParsePosition;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mr.li on 2018/6/6.
 */
public class BaseTrophyAdapter extends RecyclerView.Adapter<BaseTrophyAdapter.ViewHolder> implements View.OnClickListener{


    private Context mContext;
    private List<CustomBean> mCustomBeanList;
    private Map<Integer , Integer> mCupNumMap ;
    private String messageType = "0";
    private ViewHolder mViewHolder;
    private boolean sss = false;

    private tvAcceptSetOnClick mTvAcceptSetOnClick = null;
    private tvRefusedSetOnClick mTvRefusedSetOnClick = null;

    public interface tvAcceptSetOnClick{
        void onTvAcceptClick(View view , int position);
    }
    public interface tvRefusedSetOnClick{
        void onTvRefusedOnClick(View view , int position);
    }
    public void setAcceptOnClickListener(tvAcceptSetOnClick listener){
        this.mTvAcceptSetOnClick = listener;
    }
    public void setRefusedOnClickListener(tvRefusedSetOnClick listener){
        this.mTvRefusedSetOnClick = listener;
    }

    public BaseTrophyAdapter(Context context , List<CustomBean> customBeans , Map<Integer , Integer> cupNumMap , String mType){
        this.mContext = context;
        this.mCustomBeanList = customBeans;
        this.messageType = mType;
        this.mCupNumMap = cupNumMap;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trophy_recycler_item,parent,false);
        ViewHolder vh = new ViewHolder(view);

        vh.tv_trophy_launch.setOnClickListener(this);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position ) {
        mViewHolder = holder;
        holder.tv_trophy_launch.setTag(position);

        if (TextUtils.isEmpty(mCustomBeanList.get(position).getNickName().trim())) {
            holder.personnel_name.setText(mCustomBeanList.get(position).getUserId());
        }else {
            holder.personnel_name.setText( mCustomBeanList.get(position).getNickName());
        }

        Map<Integer, Integer> persionM = mCupNumMap;
        Set<Map.Entry<Integer, Integer>> perEntrySet = persionM.entrySet();
        Iterator<Map.Entry<Integer, Integer>> perMapIt = perEntrySet.iterator();
        while (perMapIt.hasNext()) {
            Map.Entry<Integer, Integer> meNext = perMapIt.next();
            if (mCustomBeanList.get(position).getUserId()==meNext.getKey()){
                if (meNext.getValue() ==0) {
                    holder.tv_x.setVisibility(View.GONE);
                    holder.tv_cup_num.setVisibility(View.GONE);
                }else {
                    holder.tv_x.setVisibility(View.VISIBLE);
                    holder.tv_cup_num.setVisibility(View.VISIBLE);
                    holder.tv_cup_num.setText(String.valueOf(meNext.getValue()));
                }
            }
        }


        if (!TextUtils.isEmpty(mCustomBeanList.get(position).getAvatar())){
            Uri imageUri = Uri.parse(mCustomBeanList.get(position).getAvatar());
            Glide.with(mContext).load(imageUri).into(holder.personnel_image);
        }else {
            holder.personnel_image.setBackground(mContext.getResources().getDrawable(R.drawable.avatar_student));
        }

    }

    @Override
    public int getItemCount() {
        return mCustomBeanList != null ? mCustomBeanList.size() : 0;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_trophy_launch:
                if (mTvAcceptSetOnClick != null){
                    mTvAcceptSetOnClick.onTvAcceptClick((View) v.getParent() , (Integer) v.getTag());
                }

                break;

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView personnel_image;
        private final TextView personnel_name,tv_trophy_launch;
        private final LinearLayout ll_personnel_one;
        private final LinearLayout ll_lm_in;
        private final LinearLayout ll_trophy_recycler_item;
        private final TextView tv_cup_num;
        private final TextView tv_x;

        public ViewHolder(View itemView) {
            super(itemView);
            personnel_image = itemView.findViewById(R.id.trophy_sd_personnel_image);
            personnel_name = itemView.findViewById(R.id.trophy_tv_personnel_name);
            ll_personnel_one = itemView.findViewById(R.id.trophy_ll_personnel_one);
            ll_lm_in = itemView.findViewById(R.id.trophy_ll_live_lm_ing);
            tv_trophy_launch = itemView.findViewById(R.id.tv_trophy_launch);
            ll_trophy_recycler_item = itemView.findViewById(R.id.ll_trophy_recycler_item);
            tv_cup_num = itemView.findViewById(R.id.tv_cup_num);
            tv_x = itemView.findViewById(R.id.tv_x);
        }
    }

}