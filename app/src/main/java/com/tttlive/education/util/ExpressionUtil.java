package com.tttlive.education.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.tttlive.basic.education.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mr.li on 2018/4/28.
 */

public class ExpressionUtil {
    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
        Log.e("expression","spannableString:"+spannableString);
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            Log.e("expression","key"+key);
            if (matcher.start() < start) {
                continue;
            }
           key=key.replace("[","").replace("]","");
            Log.e("expression","截掉[ ]后的：key："+key);
            Field field = R.drawable.class.getDeclaredField(key);
            int resId = Integer.parseInt(field.get(null).toString()); //通过上面匹配得到的字符串来生成图片资源id
            if (resId != 0) {
                Drawable drawable = context.getResources().getDrawable(
                        resId);
                drawable.setBounds(0, 0, 70, 70);

                ImageSpan imageSpan = new ImageSpan(drawable);
                int end = matcher.start() + key.length()+2; //计算该图片名字的长度，也就是要替换的字符串的长度
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); //将该图片替换字符串中规定的位置中
                if (end < spannableString.length()) { //如果整个字符串还未验证完，则继续。。
                    dealExpression(context,spannableString, patten, end);
                }
                break;
            }
        }
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     * @param context
     * @param str
     * @return
     */
    public static SpannableString getExpressionString(Context context,String str,String zhengze){
        SpannableString spannableString = new SpannableString(str);
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);  //通过传入的正则表达式来生成一个pattern
        try {
            dealExpression(context,spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }

        return spannableString;
    }
}