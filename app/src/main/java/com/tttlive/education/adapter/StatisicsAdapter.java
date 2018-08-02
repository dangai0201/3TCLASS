package com.tttlive.education.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.ui.room.bean.StudentAnswerBean;

import java.util.ArrayList;

/**
 * Created by mr.li on 2018/6/12.
 */

public class StatisicsAdapter extends RecyclerView.Adapter<StatisicsAdapter.ViewHolder> {

    private Context context;

    private ArrayList<StudentAnswerBean> mAnswerData=new ArrayList();
    public StatisicsAdapter(Context context) {

        this.context =context;
    }

    public StatisicsAdapter(Context context, ArrayList<StudentAnswerBean> answerlist) {

        this.context = context;
        this.mAnswerData=answerlist;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView tv_student_answer;
        TextView tv_student_username;


    }

    @Override
    public int getItemCount() {

        return mAnswerData.size();

    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_statisics_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_student_answer = view.findViewById(R.id.tv_student_answer);
        viewHolder.tv_student_username=view.findViewById(R.id.tv_student_username);
        return viewHolder;
    }


    /**
     * 设置值
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.tv_student_answer.setText(mAnswerData.get(i).getData().getReply());
        viewHolder.tv_student_username.setText(mAnswerData.get(i).getData().getNickName());


    }

}