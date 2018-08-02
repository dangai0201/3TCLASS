package com.tttlive.education.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tttlive.basic.education.R;
import com.tttlive.education.constant.Constant;

import java.lang.reflect.Field;


/**
 * Created by Administrator on 2018/7/20/0020.
 */

public class CustomBackToast extends Toast {


    private Toast lastInstance;
    private Context context ;

    public CustomBackToast(Context context) {
        super(context);
        this.context = context.getApplicationContext();
    }


    public static CustomBackToast makeCustomBackText(Context context, CharSequence text,
                                             int duration) {
        context = context.getApplicationContext();
        CustomBackToast toast = new CustomBackToast(context);
        /*设置Toast的位置*/
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        /*让Toast显示为我们自定义的样子*/
        toast.setView(getToastView(context, text));

//        try {
//            Object mTN = getField(toast.getClass().getSuperclass(), toast, "mTN");
//            if (mTN != null) {
//                Object mParams = getField(mTN.getClass(), mTN, "mParams");
//                if (mParams != null
//                        && mParams instanceof WindowManager.LayoutParams) {
//                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
//                    params.windowAnimations = R.style.anim_view;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return toast;
    }

    @Override
    public void show() {
        if (lastInstance != null) {
            lastInstance.cancel();
        }
        super.show();
        lastInstance = this;
    }

    public static CustomBackToast makeCustomBackText(Context context, int text,
                                                 int duration) {
        return makeCustomBackText(context, context.getString(text), duration);
    }


    //获取toast布局文件和位置
    public static View getToastView(Context context, CharSequence msg) {
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.custom_back_toast, null);
        TextView text = (TextView) v.findViewById(R.id.tv_custom_message);
        text.setText(msg);
//        RelativeLayout.LayoutParams paramsToast;
//        if (Constant.EQUIPMENT_WIDTH == 0){
//            paramsToast = new RelativeLayout.LayoutParams(getWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT);
//        }else {
//            paramsToast = new RelativeLayout.LayoutParams(Constant.EQUIPMENT_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//        text.setLayoutParams(paramsToast);
        return v;
    }

    public static int getWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private static Object getField(Class<?> clz, Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = clz.getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }



}
