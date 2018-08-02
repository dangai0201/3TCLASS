package com.tttlive.education.ui.course.playback;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.base.SimpleRecyclerHolder;
import com.tttlive.education.data.PlayBackListBean;
import com.tttlive.education.util.MessageDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.functions.Action1;

public class PlayBackListActivity extends BaseActivity implements PlayBackListInterface{

    @BindView(R.id.load_recycler)
    RecyclerView loadRecycler;
    @BindView(R.id.ptr_load_data)
    PtrClassicFrameLayout ptrLoadData;

    private static final String EXTRA_COURSE_PLAYBACK = "course_playback";
    private String pageSize = "20";
    private int mPage = 1;
    private boolean isRefresh = true;
    private List<PlayBackListBean.ItemListBean> mItemListBeans = new ArrayList<>();
    private PlayBackListPresenter mPresenter;
    private String mId;
    private playBackListAdapter mAdapter;

    public static Intent creatIntent (Context context,String id){
        Intent intent = new Intent(context,PlayBackListActivity.class);
        intent.putExtra(EXTRA_COURSE_PLAYBACK,id);
        return intent ;
    }
    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_play_back_list;
    }

    @Override
    protected void findView() {
        setBtGlobalLeft(getResources().getDrawable(R.drawable.icon_back));
        setTvGlobalTitleName(R.string.play_back);

        mPresenter = new PlayBackListPresenter(this);
        loadRecycler.setLayoutManager(new GridLayoutManager(this,1));
        ptrLoadData.disableWhenHorizontalMove(true);
        Intent intent = getIntent();
//        if(intent!=null){
//            mId = intent.getStringExtra(EXTRA_COURSE_PLAYBACK);
//        }
        mId = "200001";

        onClick(mBtTitleLeft, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initView() {
        mPresenter.getPlayBackList(mId,mPage+"",pageSize);
        ptrLoadData.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, loadRecycler, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, loadRecycler, header);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                isRefresh = false ;
                mPresenter.getPlayBackList(mId,mPage+"",pageSize);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true ;
                mPage = 1 ;
                mPresenter.getPlayBackList(mId,mPage+"",pageSize);
            }

        });
    }

    @Override
    public void showLoadingComplete() {
        super.showLoadingComplete();
        ptrLoadData.refreshComplete();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribeTasks();
    }

    @Override
    public void getPlayBackList(List<PlayBackListBean.ItemListBean> data) {
        if(isRefresh)mItemListBeans.clear();
        mItemListBeans.addAll(data) ;
        if(mAdapter==null){
            mAdapter = new playBackListAdapter(data);
            loadRecycler.setAdapter(mAdapter);
        }else {
            mAdapter.set(mItemListBeans);
        }
    }

    @Override
    public void getDataIsSucess(boolean flag) {
        if(flag) mPage++ ;
    }


    private class playBackListAdapter extends RecyclerView.Adapter<PlayBackListHolder> {

        private List<PlayBackListBean.ItemListBean> mDatas ;
        public playBackListAdapter(List<PlayBackListBean.ItemListBean> itemListBeans) {
            mDatas = itemListBeans ;
        }

        public void set(List<PlayBackListBean.ItemListBean> itemListBeans) {
            mDatas = itemListBeans ;
            notifyDataSetChanged();
        }

        public void remove(int position){
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public PlayBackListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(mContext, R.layout.item_play_back_list, null);
            return new PlayBackListHolder(view);
        }

        @Override
        public void onBindViewHolder(PlayBackListHolder holder, int position) {
            holder.displayData(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }


    }

    private class PlayBackListHolder extends SimpleRecyclerHolder<PlayBackListBean.ItemListBean> {

        private TextView mCourseName,mCourseDuration;
        private LinearLayout mDelItem,mDelPlay;

        public PlayBackListHolder(View itemView) {
            super(itemView);
            mCourseName = itemView.findViewById(R.id.course_item_name);
            mCourseDuration = itemView.findViewById(R.id.course_item_duration);
            mDelItem = itemView.findViewById(R.id.course_item_del);
            mDelPlay = itemView.findViewById(R.id.course_item_play);
        }

        @Override
        public void displayData(final PlayBackListBean.ItemListBean data) {
            String title = data.getTitle();
            String duration = data.getDuration();
            String videoSrc = data.getVideoSrc();

            if(getLayoutPosition()%2==0){
                itemView.setBackgroundColor(getResources().getColor(R.color.white));
            }else {
                itemView.setBackgroundColor(getResources().getColor(R.color.bg_play_back));
            }

            if(!TextUtils.isEmpty(title)){
                mCourseName.setText(title);
            }
            if(TextUtils.isEmpty(duration)){
                mCourseDuration.setText(duration);
            }


            if(!TextUtils.isEmpty(videoSrc)){
                onLongClick(mDelPlay, new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //点击跳转到回放播放页面

                    }
                });
            }

            onLongClick(mDelItem, new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    showDelDialog(data.getId(),getLayoutPosition());
                }
            });
        }
    }
    //删除回放
    private void showDelDialog(final String id, final int position) {
        MessageDialog dialog = new MessageDialog(mContext);
        dialog.setHint(R.string.del_hint);
        dialog.setContent(R.string.confirm_del);
        dialog.setMessageDialogListener(new MessageDialog.MessageDialogListener() {
            @Override
            public void onCancelClick(MessageDialog dialog) {
                if(dialog!=null&&dialog.isShowing())dialog.dismiss();
            }

            @Override
            public void onCommitClick(MessageDialog dialog) {
                if(dialog!=null&&dialog.isShowing())dialog.dismiss();
                mPresenter.delPlayBack(id,position);
            }
        });
        dialog.show();
    }

    @Override
    public void delSucess(String msg, int position) {
        toastShort(msg);
        mAdapter.remove(position);
    }

}
