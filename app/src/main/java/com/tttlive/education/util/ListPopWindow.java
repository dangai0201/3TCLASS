package com.tttlive.education.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tttlive.basic.education.R;
import com.tttlive.education.adapter.PopListAdapter;
import com.tttlive.education.data.AccountBean;

import java.util.List;

/**
 * Author: sunny
 * Time: 2018/7/12
 * description:
 */

public class ListPopWindow extends PopupWindow {

    private Context mContext;
    private RecyclerView rv_account;
    private PopListAdapter mAdapter;
    private PopListAdapter.Listener listener;

    public ListPopWindow(Context context) {
        super(context);

        this.mContext = context;

        initPop();

    }

    private void initPop() {
        View view = View.inflate(mContext, R.layout.pop_account_list, null);
        rv_account = view.findViewById(R.id.rv_account);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.color_FFFFFFFF)));
//        this.setAnimationStyle(R.style.popwin_anim_style);
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        update();

        rv_account.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new PopListAdapter(mContext);
        rv_account.setAdapter(mAdapter);
    }


    public void setData(List<AccountBean> accountBeanList){
        mAdapter.refresh(accountBeanList);
    }

    public List<AccountBean> getAccountList(){

        return mAdapter.getAccountList();
    }

    public void setListener(PopListAdapter.Listener listener) {
        this.listener = listener;
        mAdapter.setListener(listener);
    }

    public void remove(int position) {
        mAdapter.remove(position);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(listener != null) {
            listener.onDismiss();
        }
    }
}
