package com.tttlive.education.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tttlive.basic.education.R;
import com.tttlive.education.base.BaseActivity;
import com.tttlive.education.base.MyApplication;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.util.StatusBarCompat;
import com.tttlive.education.util.Tools;

import butterknife.BindView;

/**
 * Created by Iverson on 2018/3/5 下午3:46
 * 此类用于：
 */

public class HomeActivity extends BaseActivity {
    private String TAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    @BindView(R.id.rb_course_list)
    RadioButton rbCourseList;
    @BindView(R.id.rb_mine)
    RadioButton rbMine;

    //private HomePageFragment mHomeFragment;
    private SwipeHomePageFragment mHomeFragment;
    private MeFragment mMeFragment;

    private long lastTimeTapBack = 0;

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    protected void findView() {
        rgMain.check(R.id.rb_course_list);
        showHomeFragment();
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int itemViewId) {
                if (itemViewId == R.id.rb_course_list) {
                    showHomeFragment();
                } else if (itemViewId == R.id.rb_mine) {
                    showMineFragment();
                }
            }
        });
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarCompat.compat(this, getResources().getColor(R.color.color_FF1093ED));
    }

    public void commitShowFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    public void hideAllFragment(FragmentTransaction fragmentTransaction) {
        hideFragment(fragmentTransaction, mHomeFragment);
        hideFragment(fragmentTransaction, mMeFragment);
    }

    private void hideFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
    }

    /**
     * 显示我的界面
     */
    public void showMineFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        hideAllFragment(fragmentTransaction);
        if (mMeFragment == null) {
            mMeFragment = MeFragment.newInstance();
            fragmentTransaction.add(R.id.fragmentContent, mMeFragment);
        }
        commitShowFragment(fragmentTransaction, mMeFragment);
    }

    /**
     * 显示首页界面
     */
    public void showHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        if (mHomeFragment == null) {
            mHomeFragment = SwipeHomePageFragment.newInstance();
            fragmentTransaction.add(R.id.fragmentContent, mHomeFragment);
        }
        commitShowFragment(fragmentTransaction, mHomeFragment);
    }


    @Override
    public void onBackPressed() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTimeTapBack > 2000) {
            toastShort("再按一次返回桌面");
            lastTimeTapBack = currentTimeMillis;
        } else {
            MyApplication.getInstance().finishAllActivity();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.popupType = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (Constant.popupType){
                return true;
            }else {
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
