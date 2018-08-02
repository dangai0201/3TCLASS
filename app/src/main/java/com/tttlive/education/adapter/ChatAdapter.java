package com.tttlive.education.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tttlive.basic.education.R;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.ui.room.bean.SendTextMessageBean;
import com.tttlive.education.util.CircleImageView;
import com.tttlive.education.util.EmojiSpan;
import com.tttlive.education.util.ExpressionUtil;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mr.li on 2018/3/14.
 * 聊天界面
 *
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<SendTextMessageBean> smglist;

    private PersonneViewItemClickListener mPersonneViewItemClickListener = null;

    public void setOnItemClickListener(PersonneViewItemClickListener listener){
        this.mPersonneViewItemClickListener = listener;

    }

    public interface PersonneViewItemClickListener{
        void onPersonneItemClick(View view , int position);
    }

    public ChatAdapter(Context context, ArrayList<SendTextMessageBean> msglist) {
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

        if (!TextUtils.isEmpty(smglist.get(position).getData().getAvatar())){
            Uri imageUrl = Uri.parse(smglist.get(position).getData().getAvatar());
            Glide.with(mContext).load(imageUrl).into(holder.sd_image);
        }else{
            if (Constant.LIVE_TYPE_ID.equals(smglist.get(position).getData().getUserId())){
                holder.tv_nickname_teacher.setVisibility(View.VISIBLE);
                holder.mTvnickname.setText(smglist.get(position).getData().getNickName());
                holder.sd_image.setBackground(mContext.getResources().getDrawable(R.drawable.avatar_teacher));
                holder.mTvnickname.setTextColor(mContext.getResources().getColor(R.color.one_level_black));
                holder.ll_taxt_image_view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_chat));
            }else {
                holder.tv_nickname_teacher.setVisibility(View.GONE);
                holder.mTvnickname.setText(smglist.get(position).getData().getNickName());
                holder.sd_image.setBackground(mContext.getResources().getDrawable(R.drawable.avatar_student));
                holder.ll_taxt_image_view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_bg_chat_student));
            }
        }


        String str = smglist.get(position).getData().getMessage(); //消息具体内容

        String zhengze = "\\[([^\\[\\]]+)\\]"; //正则表达式，用来判断消息内是否有表情

        try {

            if (smglist.get(position).getData().getType() == Constant.SEND_MESSAGE_TYPE_IMAGE){
                holder.iv_chat_image.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(smglist.get(position).getData().getMessage()).placeholder(R.drawable.thumb_images).into(holder.iv_chat_image);
            }else {
                SpannableString spannableString = ExpressionUtil.getExpressionString(mContext, str, zhengze);
                holder.mTvmsg.setVisibility(View.VISIBLE);
                holder.mTvmsg.setText(spannableString);

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
            case R.id.ll_text_image_view:
                if (mPersonneViewItemClickListener != null) {
                    mPersonneViewItemClickListener.onPersonneItemClick((View) v.getParent(), (Integer) v.getTag());
                }
                break;

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nickname_teacher;
        private LinearLayout ll_taxt_image_view;
        private ImageView iv_chat_image;
        private CircleImageView sd_image;
        private TextView mTvmsg,mTvnickname;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvmsg=  itemView.findViewById(R.id.tv_msg);
            mTvnickname= itemView.findViewById(R.id.tv_nickname);
            sd_image = itemView.findViewById(R.id.sd_user_image);
            iv_chat_image = itemView.findViewById(R.id.iv_chat_image);
            ll_taxt_image_view = itemView.findViewById(R.id.ll_text_image_view);
            tv_nickname_teacher = itemView.findViewById(R.id.tv_nickname_teacher);


        }
    }


}
