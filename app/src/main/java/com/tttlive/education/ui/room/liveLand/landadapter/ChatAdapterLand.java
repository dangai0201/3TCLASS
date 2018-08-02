package com.tttlive.education.ui.room.liveLand.landadapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tttlive.education.constant.Constant;
import com.tttlive.basic.education.R;
import com.tttlive.education.ui.room.bean.SendTextMessageBean;
import com.tttlive.education.util.CircleImageView;
import com.tttlive.education.util.ExpressionUtil;

import java.util.ArrayList;

//import pl.droidsonroids.gif.GifImageView;

/**
 * Created by mr.li on 2018/5/30.
 */

public class ChatAdapterLand  extends RecyclerView.Adapter<ChatAdapterLand.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<SendTextMessageBean> smglist;

    private PersonneViewItemClickListener mPersonneViewItemClickListener = null;

    public void setOnItemClickListener(PersonneViewItemClickListener listener){
        this.mPersonneViewItemClickListener = listener;

    }
//
    public interface PersonneViewItemClickListener{
        void onPersonneItemClick(View view , int position);
    }

    public ChatAdapterLand(Context context, ArrayList<SendTextMessageBean> msglist) {
        this.mContext = context;
        this.smglist = msglist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        vh.ll_taxt_image_view.setOnClickListener(this);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ll_taxt_image_view.setTag(position);
            if (Constant.LIVE_TYPE_ID.equals(smglist.get(position).getData().getUserId())){
                holder.tv_nickname_teacher.setVisibility(View.VISIBLE);
                holder.tv_colon.setVisibility(View.VISIBLE);
                holder.iv_live_zoom.setVisibility(View.GONE);
                holder.mTvnickname.setText(smglist.get(position).getData().getNickName());
                holder.mTvnickname.setTextColor(mContext.getResources().getColor(R.color.white));
//                holder.ll_taxt_image_view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_chat));
            }else {
                holder.tv_nickname_teacher.setVisibility(View.GONE);
                holder.tv_colon.setVisibility(View.GONE);
                holder.iv_live_zoom.setVisibility(View.GONE);
                holder.mTvnickname.setText(smglist.get(position).getData().getNickName()  + " : ");
//                holder.ll_taxt_image_view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_chat_student));
            }

        String str = smglist.get(position).getData().getMessage(); //消息具体内容

        String zhengze = "\\[([^\\[\\]]+)\\]"; //正则表达式，用来判断消息内是否有表情

        try {
            if (smglist.get(position).getData().getType() == Constant.SEND_MESSAGE_TYPE_IMAGE){
                holder.iv_chat_image.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(smglist.get(position).getData().getMessage()).placeholder(R.drawable.thumb_images).into(holder.iv_chat_image);
                holder.iv_live_zoom.setVisibility(View.VISIBLE);
            }else {
                SpannableString spannableString = ExpressionUtil.getExpressionString(mContext, str, zhengze);
                holder.mTvmsg.setVisibility(View.VISIBLE);

                holder.mTvmsg.setText(spannableString);

//                holder.mTvmsg.setImageResource(spannableString);
                holder.iv_live_zoom.setVisibility(View.GONE);

            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    //
    @Override
    public int getItemCount() {
        return smglist.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_taxt_image_view:
                if (mPersonneViewItemClickListener != null) {
                    mPersonneViewItemClickListener.onPersonneItemClick((View) v.getParent(), (Integer) v.getTag());
                }
                break;

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_live_zoom;
        private TextView tv_colon;
        private TextView tv_nickname_teacher;

        private ImageView iv_chat_image;

        private TextView mTvnickname;
        private TextView mTvmsg;

        private LinearLayout ll_taxt_image_view;


        public ViewHolder(View itemView) {
            super(itemView);
            mTvmsg=  itemView.findViewById(R.id.tv_msg);
            mTvnickname= itemView.findViewById(R.id.tv_nickname);

            iv_chat_image = itemView.findViewById(R.id.iv_chat_image);

            tv_nickname_teacher = itemView.findViewById(R.id.tv_nickname_teacher);
            ll_taxt_image_view= itemView.findViewById(R.id.ll_taxt_image_view);
            tv_colon = itemView.findViewById(R.id.tv_colon);
            iv_live_zoom = itemView.findViewById(R.id.iv_live_zoom);


        }
    }


}
