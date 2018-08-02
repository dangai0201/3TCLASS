package com.tttlive.education.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.tttlive.basic.education.R;
import com.tttlive.education.adapter.CourseWareAdapter;
import com.tttlive.education.base.BaseResponse;
import com.tttlive.education.ui.room.DocsListbean;
import com.tttlive.education.ui.room.DocsListDetailsBean;
import com.tttlive.education.ui.room.TeacherPresenter;
import com.tttlive.education.ui.room.TeacherUIinterface;
import com.tttlive.education.ui.room.bean.UploadImageBean;
import com.tttlive.education.ui.room.custom.CustomDialog;
import com.tttlive.education.util.BaseTools;
import com.tttlive.education.util.CustomToast;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author: sunny
 * Time: 2018/7/31
 * description:课件弹窗
 */

public class CourseWareWindow extends PopupWindow implements TeacherUIinterface, View.OnClickListener {

    private Activity activity;
    private ImageView iv_doc_dropdown;
    private PtrClassicFrameLayout ptr_layout;
    private RecyclerView rv_course_ware;
    private CourseWareAdapter courseWareAdapter;
    private TeacherPresenter teacherPresenter;
    private CustomDialog dialog;
    private String teacher_user_id;
    private int page = 1;
    private int size = 4;
    private int currentCheckedPosition = -1;//当前选中的课件索引
    private CourseWareAdapter.CourseWareViewItemClickListener onCourseItemClickListener;//课件列表的点击监听

    public CourseWareWindow(Activity activity, String teacher_user_id) {
        super(activity);

        this.activity = activity;
        this.teacher_user_id = teacher_user_id;

        initWindow(activity);

        showDialog();

        getCourseWareList();
    }

    private void initWindow(Activity activity) {
        View courseView = LayoutInflater.from(activity).inflate(R.layout.window_course_ware, null);
        setContentView(courseView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(BaseTools.getWindowsHeight(activity) * 7 / 10);
        setAnimationStyle(R.style.AnimBottomIn);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);

        findViews(courseView);

    }

    private void findViews(View courseView) {
        iv_doc_dropdown = courseView.findViewById(R.id.iv_doc_dropdown);
        ptr_layout = courseView.findViewById(R.id.ptr_layout);
        ptr_layout.disableWhenHorizontalMove(true);
        ptr_layout.setPtrHandler(new DocsPtrHandler());
        rv_course_ware = courseView.findViewById(R.id.rv_course_ware);
        rv_course_ware.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        courseWareAdapter = new CourseWareAdapter(courseView.getContext());
        courseWareAdapter.setDefaultCheckedItemPosition(currentCheckedPosition);
        rv_course_ware.setAdapter(courseWareAdapter);
        iv_doc_dropdown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_doc_dropdown) {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        disLoadDialog();
    }

    /**
     * 刷新选中状态
     *
     * @param position  点击的索引
     * @param isChecked 是否选中
     */
    public void onItemClick(int position, boolean isChecked) {
        courseWareAdapter.check(position, isChecked);
    }

    /**
     * 获取课件列表
     *
     * @return 课件列表
     */
    public ArrayList<DocsListbean.ListBean> getCourseListBean() {
        return courseWareAdapter.getList();
    }

    /**
     * 获取课件列表
     */
    private void getCourseWareList() {

        if (teacherPresenter == null) {
            teacherPresenter = new TeacherPresenter(this);
        }
        teacherPresenter.getDocsLists(teacher_user_id, String.valueOf(page), String.valueOf(size), "");
    }


    /**
     * 获取课件列表成功回调
     *
     * @param docsListbean 请求课件列表返回结果
     */
    @Override
    public void docSucess(DocsListbean docsListbean) {
        disLoadDialog();
        if (docsListbean == null || docsListbean.getList() == null) {
            return;
        }
        int totalCount = docsListbean.getCount();
        List<DocsListbean.ListBean> courseWareList = docsListbean.getList();
        if (page == 1) {//下拉刷新
            courseWareAdapter.refresh(courseWareList);
            if (courseWareList.size() == 0) {
                toastShort("当前课程暂时没有课件");
            }
        } else {//上拉加载更多
            courseWareAdapter.loadMore(courseWareList);
            if (totalCount <= page * size) {
                toastShort("已是最后一页了");
            }
        }
        ptr_layout.refreshComplete();
    }


    private void showDialog() {
        if (dialog == null) {
            dialog = new CustomDialog(activity, R.style.CustomDialog);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }

    }
    private void disLoadDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    /**
     * 自定义吐司
     *
     * @param msg
     */
    private void toastShort(String msg) {
        CustomToast.makeCustomText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 设置课件列表的点击事件监听
     *
     * @param onCourseItemClickListener 点击监听
     */
    public void setOnCourseItemClickListener(CourseWareAdapter.CourseWareViewItemClickListener onCourseItemClickListener) {
        this.onCourseItemClickListener = onCourseItemClickListener;
        courseWareAdapter.setOnItemClickListener(onCourseItemClickListener);
    }


    /**
     * 课件分页加载
     */
    private class DocsPtrHandler extends PtrDefaultHandler2 {

        @Override
        public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
            return super.checkCanDoLoadMore(frame, rv_course_ware, footer);
        }

        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
            return super.checkCanDoRefresh(frame, rv_course_ware, header);
        }

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            //加载更多
            page++;
            getCourseWareList();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            //刷新
            page = 1;
            getCourseWareList();

        }
    }


    @Override
    public void showNetworkException() {
          disLoadDialog();
    }

    @Override
    public void showUnknownException() {

    }

    @Override
    public void showDataException(String msg) {

    }

    @Override
    public void showLoadingComplete() {

    }

    @Override
    public Dialog showLoadingDialog() {
        return null;
    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void docDetailsSucess(DocsListDetailsBean docDetail) {

    }

    @Override
    public void imageUploadSucess(BaseResponse<UploadImageBean> uploadBean) {

    }


}
