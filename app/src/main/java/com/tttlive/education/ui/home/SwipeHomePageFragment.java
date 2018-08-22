package com.tttlive.education.ui.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tttlive.basic.education.R;
import com.tttlive.education.adapter.CourseListAdapter;
import com.tttlive.education.base.BaseFragment;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.data.AccountLoginBean;
import com.tttlive.education.data.CourseListBean;
import com.tttlive.education.data.InviteCodeLoginBean;
import com.tttlive.education.ui.course.CreateCourseActivity;
import com.tttlive.education.ui.login.LoginInterface;
import com.tttlive.education.ui.login.LoginPresenter;
import com.tttlive.education.ui.room.BaseLiveActivity;
import com.tttlive.education.ui.room.PopupWindowSyles;
import com.tttlive.education.ui.share.ShareCourseActivity;
import com.tttlive.education.util.MessageDialog;
import com.tttlive.education.util.SPTools;
import com.tttlive.education.util.StatusBarUtil;
import com.tttlive.education.util.Tools;
import com.wushuangtech.room.core.TTTSDK;

import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by mrliu on 2018/4/16.
 * 此类用于:首页包括侧滑删除
 */

public class SwipeHomePageFragment extends BaseFragment implements HomePageUIinterface,
        BGARefreshLayout.BGARefreshLayoutDelegate,
        BGAOnItemChildClickListener, BGAOnRVItemClickListener ,LoginInterface{

    private static final String TAG_CLASS = SwipeHomePageFragment.class.getSimpleName();

    @BindView(R.id.load_recycler)
    RecyclerView loadRecycler;
    @BindView(R.id.ptr_load_data)
    BGARefreshLayout refreshLayout;
    @BindView(R.id.main)
    RelativeLayout main;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;

    private HomePresenter presenter;
    private LoginPresenter loginPresenter;
    private int pageSize = 20;
    private int mPage = 1;
    private CourseListAdapter mAdapter;
    private String mUserId;
    private final int roomType = 3;//代表老师
    private PopupWindowSyles popupWindowSyles;

    //标记是否还有更多数据
    private boolean hasMoreData = true;

    //标题
    private static final String TITLE = "3T <font color='#2DE9FC'>C</font>lass";
    private String stuPublicode;
    private String userId;
    private String code;

    public static SwipeHomePageFragment newInstance() {
        Bundle args = new Bundle();
        SwipeHomePageFragment fragment = new SwipeHomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_for_swipe_homepage;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.CREATCOURSELIVE.equals("1")) {
            presenter.getCourseList(mUserId, 1 + "", String.valueOf(pageSize));
        }

    }

    @Override
    protected void initViews(View view) {

        tv_title.setText(Html.fromHtml(TITLE));
        presenter = new HomePresenter(this);
        loginPresenter  = new LoginPresenter(this);
        popupWindowSyles = new PopupWindowSyles(mContext , mContext);
        StatusBarUtil.setStatusBarColor(mContext, getResources().getColor(R.color.color_FF1093ED));
        AccountLoginBean loginInfo = SPTools.getInstance(getContext()).getLoginInfo();

        if (loginInfo != null) {
            mUserId = loginInfo.getUserId() + "";
        }
        view.findViewById(R.id.iv_create_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CreateCourseActivity.creatIntent(mContext));
            }
        });

        loadRecycler.setLayoutManager(new GridLayoutManager(mContext, 1));

        BGANormalRefreshViewHolder bgaNormalRefreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);
        refreshLayout.setRefreshViewHolder(bgaNormalRefreshViewHolder);
        refreshLayout.setIsShowLoadingMoreView(true);
        refreshLayout.setDelegate(this);
        mAdapter = new CourseListAdapter(loadRecycler);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnRVItemClickListener(this);
        loadRecycler.setAdapter(mAdapter);
        loadRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    mAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
            }
        });

        presenter.getCourseList(mUserId, String.valueOf(mPage), String.valueOf(pageSize));


    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mPage = 1;
        presenter.getCourseList(mUserId, String.valueOf(mPage), String.valueOf(pageSize));
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mPage++;
        presenter.getCourseList(mUserId, String.valueOf(mPage), String.valueOf(pageSize));
        return hasMoreData;
    }


    @Override
    public void getCourseListSuccess(CourseListBean courseListBean) {

        if (courseListBean == null || courseListBean.getItemList() == null || courseListBean.getItemList().size() == 0) {
            if (mPage == 1) {
                ll_empty.setVisibility(View.VISIBLE);
            }
            return;
        }
        ll_empty.setVisibility(View.GONE);
        List<CourseListBean.ItemListBean> itemList = courseListBean.getItemList();

        if (mPage == 1) {
            refreshLayout.endRefreshing();
            mAdapter.addNewData(itemList);

        } else {
            refreshLayout.endLoadingMore();
            mAdapter.addMoreData(itemList);

        }
        hasMoreData = courseListBean.getTotalCount() > mPage * pageSize;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {

        String id = mAdapter.getItem(position).getId();
        String timeStart = mAdapter.getItem(position).getTimeStart();
        String timeEnd = mAdapter.getItem(position).getTimeEnd();
        String title = mAdapter.getItem(position).getTitle();
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            //调用删除接口
            presenter.delCourse(mUserId, id, position);
        } else if (childView.getId() == R.id.iv_course_share) {
            //分享
            startActivity(ShareCourseActivity.creatIntent(mContext, timeStart, timeEnd, title, id));
        } else if (childView.getId() == R.id.tv_delete_course) {
            //弹出删除框
            showDelDialog(id, position);
        }
    }

    private long lastClickTime;
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

        long curTime = System.currentTimeMillis();
        if(curTime - lastClickTime > 2000) {
            String roomId = mAdapter.getItem(position).getId();
            userId = mAdapter.getItem(position).getUserId();
            String pushRtmp = mAdapter.getItem(position).getPushRtmp();
            String teacherName = mAdapter.getItem(position).getTeacherName();
            String type = mAdapter.getItem(position).getType();
            String appId = mAdapter.getItem(position).getAppId();
            String title = mAdapter.getItem(position).getTitle();
            String timeStart = mAdapter.getItem(position).getTimeStart();
            code = mAdapter.getItem(position).getTeacherInviteCode();
            stuPublicode = mAdapter.getItem(position).getPublicCode();
            Constant.app_id = appId;
            SPTools.getInstance(getContext()).save(Constant.app_id,appId);

            TTTSDK.init(mContext.getApplicationContext(), Constant.app_id);
            Log.e("appId   = ", appId);
//            showLoadingDialog();

            if (!TextUtils.isEmpty(mAdapter.getItem(position).getIsLive())){
                if ("0".equals(mAdapter.getItem(position).getIsLive())){
                    //未开始课程
                    showConfilmDialog(userId, roomId, teacherName, pushRtmp, type, title, timeStart);
                }else if ("3".equals(mAdapter.getItem(position).getIsLive())){
                    //已经失效的课程
                    toastShort("直播已失效");
                    dismissLoadingDialog();
                }else {
                    //直播中或者已经结束
                    if (Tools.isNetworkAvailable(mContext)){
//                        loginPresenter.inviteCodeLogin(stuPublicode, "");
                    Constant.exitRoom = false;
                    Constant.popupType = true;
                    BaseLiveActivity.getWsIpLocationReq(appId, Integer.parseInt(userId));
                    popupWindowSyles.popupWindowStylesTeacher(getContext(), code,roomType, userId, roomId, teacherName, pushRtmp, type, title, timeStart, "");
                    popupWindowSyles.showProgressBar(main, getContext());

                    }else {
                        toastBackShort(getResources().getString(R.string.msg_network_error));
                        dismissLoadingDialog();
                    }

                }
            }
            lastClickTime = curTime;
        }


    }


    /**
     * 未开始点击进入直播前提示是否进入直播
     *
     * @param userId      用户ID
     * @param roomId      房间ID
     * @param teacherName 老师昵称
     * @param pushRtmp    推流地址
     * @param type
     */
    private void showConfilmDialog(final String userId, final String roomId,
                                   final String teacherName, final String pushRtmp,
                                   final String type, final String title, final String timeStart) {


        MessageDialog dialog = new MessageDialog(mContext);
        dialog.setHint(R.string.mes_hint);
        dialog.setContent(R.string.is_enter_live);
        dialog.setLeftButton(R.string.waiter_enter);
        dialog.setRightButton(R.string.enter_confirm);

        dismissLoadingDialog();
        dialog.setMessageDialogListener(new MessageDialog.MessageDialogListener() {
            @Override
            public void onCancelClick(MessageDialog dialog) {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }

            @Override
            public void onCommitClick(MessageDialog dialog) {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                if (Tools.isNetworkAvailable(mContext)){
                    Constant.exitRoom = false;
                    Constant.popupType = true;
                    BaseLiveActivity.getWsIpLocationReq(Constant.app_id, Integer.parseInt(userId));
                    popupWindowSyles.popupWindowStylesTeacher(getContext(),code, roomType, userId, roomId, teacherName, pushRtmp, type, title, timeStart, "");
                    popupWindowSyles.showProgressBar(main, getContext());
//                    showLoadingDialog();
//                    loginPresenter.inviteCodeLogin(stuPublicode, "");
                }else {
                    toastBackShort(getResources().getString(R.string.msg_network_error));
                }
            }
        });
        dialog.show();

    }

    //删除课件的dialog
    private void showDelDialog(final String id, final int layoutPosition) {
        MessageDialog dialog = new MessageDialog(mContext);
        dialog.setHint(R.string.del_hint);
        dialog.setContent(R.string.confirm_del_course);

        dialog.setMessageDialogListener(new MessageDialog.MessageDialogListener() {
            @Override
            public void onCancelClick(MessageDialog dialog) {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }

            @Override
            public void onCommitClick(MessageDialog dialog) {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                presenter.delCourse(mUserId, id, layoutPosition);
            }
        });
        dialog.show();
    }

    @Override
    public void delSucess(String msg, int layoutPosition) {
        toastShort(msg);
        mAdapter.removeItem(layoutPosition);
        List<CourseListBean.ItemListBean> data = mAdapter.getData();
        if (data == null || data.size() == 0) {
            ll_empty.setVisibility(View.VISIBLE);
        } else {
            ll_empty.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribeTasks();
    }


    @Override
    public void showLoadingComplete() {
        super.showLoadingComplete();
        if (refreshLayout != null) {
            refreshLayout.endLoadingMore();
            refreshLayout.endRefreshing();
        }
    }

    @Override
    public void inviteCodeLoginSuccess(InviteCodeLoginBean inviteCodeLoginBean) {
        dismissLoadingDialog();
        String role = inviteCodeLoginBean.getRole();
        String userId = inviteCodeLoginBean.getUserId();
        String appId = inviteCodeLoginBean.getAppId();
        if (role.equals("1")){
            Constant.exitRoom = false;
            Constant.popupType = true;
            popupWindowSyles.popupWindowStylesStudent(mContext, inviteCodeLoginBean,"");
            popupWindowSyles.showProgressBar(main, getContext());
            BaseLiveActivity.getWsIpLocationReq(appId, Integer.parseInt(userId));
        }else {
            toastBackShort(getResources().getString(R.string.msg_network_error));
        }

    }

    @Override
    public void inviteCodeLoginFailure(BaseResponse<Object> response) {
        dismissLoadingDialog();
        toastBackShort(response.getMessage());
    }

    @Override
    public void accountLoginSuccess(AccountLoginBean accountLoginBean) {

    }

    @Override
    public void accountLoginFailure(BaseResponse<Object> response) {

    }

    @Override
    public void verifyAccount(BaseResponse<Object> baceBean) {

    }

    @Override
    public void getSMSVerificationCode(BaseResponse<Object> VerificationCode) {

    }

    @Override
    public void getCheckSMSCode(BaseResponse<Object> checkCode) {

    }

    @Override
    public void getForgetPassword(BaseResponse<Object> frogetPassword) {

    }

    @Override
    public void getRegisterAccount(BaseResponse<Object> accountBaen) {

    }

    @Override
    public void loginError() {

    }
}
