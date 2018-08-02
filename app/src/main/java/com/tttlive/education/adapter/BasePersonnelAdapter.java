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
import com.tttlive.education.ui.room.bean.LmBean;
import com.tttlive.education.util.CircleImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/27/0027.
 * 弹窗人员列表
 */
public class BasePersonnelAdapter extends RecyclerView.Adapter<BasePersonnelAdapter.ViewHolder> implements View.OnClickListener{


    private static final String LIVE_LARGE_CLASS = "0"; //大班课
    private static final String LIVE_SMALL_CLASS = "1"; //小班课
    private int[] headArrayList = {R.drawable.avatar_student_boy_02 , R.drawable.avatar_student_boy_04,
                                    R.drawable.avatar_student_girl_01 , R.drawable.avatar_student_girl_03 ,
                                    R.drawable.avatar_student_girl_05 ,R.drawable.avatar_student_girl_06};
    private Context mContext;
    private List<CustomBean> mCustomBeanList;
    private List<String> mLmBeanList;
    private List<String> mNowLmList;
    private String messageType = "0";
    private String mRoomType;
    private ViewHolder mViewHolder;
    private CustomBean mCustomBean;
    private Map<Integer , Integer> mCupMapNum;
    private int mTeacherRole = 3;

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

    public BasePersonnelAdapter(Context context , List<CustomBean> customBeans , Map<Integer , Integer> cupNumMapList){
        this.mContext = context;
        this.mCustomBeanList = customBeans;
        this.mCupMapNum=cupNumMapList;

    }

    public BasePersonnelAdapter(Context context , List<CustomBean> customBeans ,
                                List<String> lmBeanList , Map<Integer , Integer> cupNumMapList){
        this.mContext = context;
        this.mCustomBeanList = customBeans;
        this.mLmBeanList = lmBeanList;
        this.mCupMapNum=cupNumMapList;
    }

    public BasePersonnelAdapter(Context context , List<CustomBean> customBeans ,
                                List<String> lmBeanList ,List<String> nowLmList,Map<Integer , Integer> cupNumMapList ){
        this.mContext = context;
        this.mCustomBeanList = customBeans;
        this.mLmBeanList = lmBeanList;
        this.mNowLmList = nowLmList;
        this.mCupMapNum=cupNumMapList;

    }

    public BasePersonnelAdapter(Context context , String type , List<CustomBean> customBean , String roomType){
        this.mContext = context;
        this.messageType =type;
        this.mRoomType = roomType;
        this.mCustomBeanList = customBean;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.personnel_recycler_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        vh.tv_personnel_accept.setOnClickListener(this);
        vh.tv_personnel_refused.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mViewHolder = holder;
        holder.tv_personnel_accept.setTag(position);
        holder.tv_personnel_refused.setTag(position);
        Random random = new Random();
        int headNum = random.nextInt(5)%(5-0+1) + 0;

        if (messageType.equals("1")){
            if (mCustomBeanList.get(position).getRole() == 3 ) {
                holder.ll_cup_item.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(mCustomBeanList.get(position).getAvatar())){
                    Uri imageUri = Uri.parse(mCustomBeanList.get(position).getAvatar());
                    Glide.with(mContext).load(imageUri).into(holder.personnel_image);
                }else {
                    holder.personnel_image.setBackground(mContext.getResources().getDrawable(R.drawable.avatar_teacher_o1));
                }
            }else {
                if (mRoomType.equals(LIVE_LARGE_CLASS)){
                    holder.ll_cup_item.setVisibility(View.GONE);
                }else if (mRoomType.equals(LIVE_SMALL_CLASS)){
                    holder.ll_cup_item.setVisibility(View.VISIBLE);
                }
                if (mCustomBeanList.get(position).getTrophyCount() ==0){
                    holder.tv_cup_num.setVisibility(View.GONE);
                    holder.tv_x_personal.setVisibility(View.GONE);
                }else {
                    holder.tv_cup_num.setVisibility(View.VISIBLE);
                    holder.tv_x_personal.setVisibility(View.VISIBLE);
                    holder.tv_cup_num.setText(String.valueOf(mCustomBeanList.get(position).getTrophyCount()));
                }
                if (mCustomBeanList.get(position).isWhiteBoardAccess()){
                    holder.ll_white_board.setVisibility(View.VISIBLE);
                }else {
                    holder.ll_white_board.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(mCustomBeanList.get(position).getAvatar())){
                    Uri imageUri = Uri.parse(mCustomBeanList.get(position).getAvatar());
                    Glide.with(mContext).load(imageUri).into(holder.personnel_image);
                }else {
                    holder.personnel_image.setBackground(mContext.getResources().getDrawable(headArrayList[headNum]));
                }
            }

            if (TextUtils.isEmpty(mCustomBeanList.get(position).getNickName())){
                holder.personnel_name.setText(mCustomBeanList.get(position).getUserId());
            }else {
                holder.personnel_name.setText( mCustomBeanList.get(position).getNickName());
            }


        }else {
            if (mCustomBeanList.get(position).getRole() == mTeacherRole) {
                holder.personnel_name.setText(mCustomBeanList.get(position).getNickName());
                holder.ll_cup_item.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(mCustomBeanList.get(position).getAvatar())){
                    Uri imageUri = Uri.parse(mCustomBeanList.get(position).getAvatar());
                    Glide.with(mContext).load(imageUri).into(holder.personnel_image);
                }else {
                    holder.personnel_image.setBackground(mContext.getResources().getDrawable(R.drawable.avatar_teacher_o1));
                }
            }else {
                holder.personnel_name.setText(mCustomBeanList.get(position).getNickName());
                holder.ll_cup_item.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mCustomBeanList.get(position).getAvatar())) {
                    Uri imageUri = Uri.parse(mCustomBeanList.get(position).getAvatar());
                    Glide.with(mContext).load(imageUri).into(holder.personnel_image);
                }else {
//                    holder.personnel_image.setBackground(mContext.getResources().getDrawable(R.drawable.avatar_student));
                    holder.personnel_image.setBackground(mContext.getResources().getDrawable(headArrayList[headNum]));
                }

                if (mCupMapNum != null && mCupMapNum.size()>0){
                    Map<Integer, Integer> cupMap = mCupMapNum;
                    Set<Map.Entry<Integer, Integer>> entrySet = cupMap.entrySet();
                    Iterator<Map.Entry<Integer, Integer>> mapIt = entrySet.iterator();
                    while (mapIt.hasNext()) {
                        Map.Entry<Integer, Integer> me = mapIt.next();
                        if (me.getKey()== mCustomBeanList.get(position).getUserId()) {
                            if (me.getValue() ==0){
                                holder.tv_x_personal.setVisibility(View.GONE);
                                holder.tv_cup_num.setVisibility(View.GONE);
                            }else {
                                holder.tv_cup_num.setVisibility(View.VISIBLE);
                                holder.tv_x_personal.setVisibility(View.VISIBLE);
                                holder.tv_cup_num.setText(String.valueOf(me.getValue()));
                            }
                        }
                    }

                }

                if (mLmBeanList != null && mLmBeanList.size()>0){
                    for (int i = 0; i < mLmBeanList.size(); i++) {
                        if (mLmBeanList.get(i).equals(String.valueOf(mCustomBeanList.get(position).getUserId()))){
                            holder.ll_personnel_two.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (mNowLmList != null && mNowLmList.size()>0){
                    for (int i = 0; i < mNowLmList.size(); i++) {
                        if (mNowLmList.get(i).equals(String.valueOf(mCustomBeanList.get(position).getUserId()))){
                            holder.ll_lm_in.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCustomBeanList != null ? mCustomBeanList.size() : 0;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_personnel_accept:
                if (mTvAcceptSetOnClick != null){
                    mTvAcceptSetOnClick.onTvAcceptClick((View) v.getParent() , (Integer) v.getTag());
                }

                break;
            case R.id.tv_personnel_refused:
                if (mTvRefusedSetOnClick != null){
                    mTvRefusedSetOnClick.onTvRefusedOnClick((View) v.getParent() , (Integer) v.getTag());
                }
                break;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView personnel_image;
        private final TextView personnel_name;
        private final TextView tv_personnel_refused;
        private final TextView tv_personnel_accept;
        private final LinearLayout ll_personnel_one;
        private final LinearLayout ll_personnel_two;
        private final LinearLayout ll_lm_in;
        private final TextView tv_x_personal;
        private final TextView tv_cup_num;
        private final LinearLayout ll_cup_item;
        private final LinearLayout ll_white_board;

        public ViewHolder(View itemView) {
            super(itemView);
            personnel_image = itemView.findViewById(R.id.sd_personnel_image);
            personnel_name = itemView.findViewById(R.id.tv_personnel_name);
            tv_personnel_refused = itemView.findViewById(R.id.tv_personnel_refused);
            tv_personnel_accept = itemView.findViewById(R.id.tv_personnel_accept);
            ll_personnel_one = itemView.findViewById(R.id.ll_personnel_one);
            ll_personnel_two = itemView.findViewById(R.id.ll_personnel_two);
            ll_lm_in = itemView.findViewById(R.id.ll_live_lm_ing);
            tv_x_personal = itemView.findViewById(R.id.tv_x_personnel);
            tv_cup_num = itemView.findViewById(R.id.tv_cup_num_personnel);
            ll_cup_item = itemView.findViewById(R.id.ll_cup_item);
            ll_white_board = itemView.findViewById(R.id.ll_authorization_white_board);
        }
    }

}
