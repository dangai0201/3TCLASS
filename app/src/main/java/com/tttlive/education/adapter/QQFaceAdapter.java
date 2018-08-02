package com.tttlive.education.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tttlive.basic.education.R;

/**
 * Created by mr.li on 2018/4/28.
 */

public class QQFaceAdapter extends BaseAdapter {
    private Context mContext;
    private static int[] mImageIds = new int[] {R.drawable.smile,R.drawable.curlmouth,R.drawable.colour,R.drawable.asleep,R.drawable.lacrimation,R.drawable.shy,
    R.drawable.shutup,R.drawable.sleep,R.drawable.cry,R.drawable.awkward,R.drawable.burnup,R.drawable.naughty,R.drawable.bareteeth,R.drawable.amazed,R.drawable.begrieved,
    R.drawable.coldsweat,R.drawable.gocrazy,R.drawable.spit,R.drawable.titter,R.drawable.loveliness,R.drawable.supercilious,R.drawable.arrogance,R.drawable.hunger,
    R.drawable.tired,R.drawable.terrified,R.drawable.sweat,R.drawable.smilefatuously,R.drawable.commonsoldier,R.drawable.strive,R.drawable.curse,R.drawable.query,
    R.drawable.hush,R.drawable.faint,R.drawable.torment,R.drawable.decline,R.drawable.beat,R.drawable.goodbye,R.drawable.wipeperspiration,R.drawable.nose,
    R.drawable.solidfood,R.drawable.snicker,R.drawable.lefthem,R.drawable.righthem,R.drawable.yawn,R.drawable.contempt,R.drawable.feelwronged,R.drawable.fastcry,
    R.drawable.cattiness,R.drawable.kiss,R.drawable.intimidate,R.drawable.pitiful,R.drawable.hug,R.drawable.moon,R.drawable.sun,R.drawable.bomb,R.drawable.humanskeleton,
    R.drawable.kitchenknife,R.drawable.pighead,R.drawable.watermelon,R.drawable.coffee,R.drawable.meal,R.drawable.lovingheart,R.drawable.strong,R.drawable.weak,
    R.drawable.handshake,R.drawable.victory,R.drawable.holdhand,R.drawable.seduce,R.drawable.ok,R.drawable.no,R.drawable.rose,R.drawable.fade,R.drawable.courtshipdisplay,
    R.drawable.loves,R.drawable.throwkiss};



    public QQFaceAdapter(Context c) {
        mContext = c;
    }

    // 获取图片的总id
    public static int[] getImageIds() {
        return mImageIds;
    }

    // 获取图片的个数
    public int getCount() {
        return mImageIds.length;
    }

    // 获取图片在库中的位置
    public Object getItem(int position) {
        return position;
    }

    // 获取图片ID
    public long getItemId(int position) {
        return mImageIds[position];
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 150));

            imageView.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mImageIds[position]);
        switch (position){
            case 0:
                imageView.setTag("[smile]");
                 break;
            case 1:
                imageView.setTag("[curlmouth]");
                 break;
            case 2:
                imageView.setTag("[colour]");
                break;
            case 3:
                imageView.setTag("[asleep]");
                break;
            case 4:
                imageView.setTag("[lacrimation]");
                break;
            case 5:
                imageView.setTag("[shy]");
                break;
            case 6:
                imageView.setTag("[shutup]");
                break;
            case 7:
                imageView.setTag("[sleep]");
                break;
            case 8:
                imageView.setTag("[cry]");
                break;

            case 9:
                imageView.setTag("[awkward]");
                break;
            case 10:
                imageView.setTag("[burnup]");
                break;
            case 11:
                imageView.setTag("[naughty]");
                break;
            case 12:
                imageView.setTag("[bareteeth]");
                break;
            case 13:
                imageView.setTag("[amazed]");
                break;
            case 14:
                imageView.setTag("[begrieved]");
                break;
            case 15:
                imageView.setTag("[coldsweat]");
                break;
            case 16:
                imageView.setTag("[gocrazy]");
                break;
            case 17:
                imageView.setTag("[spit]");
                break;
            case 18:
                imageView.setTag("[titter]");
                break;
            case 19:
                imageView.setTag("[loveliness]");
                break;
            case 20:
                imageView.setTag("[supercilious]");
                break;
            case 21:
                imageView.setTag("[arrogance]");
                break;

            case 22:
                imageView.setTag("[hunger]");
                break;
            case 23:
                imageView.setTag("[tired]");
                break;
            case 24:
                imageView.setTag("[terrified]");
                break;
            case 25:
                imageView.setTag("[sweat]");
                break;
            case 26:
                imageView.setTag("[smilefatuously]");
                break;
            case 27:
                imageView.setTag("[commonsoldier]");
                break;
            case 28:
                imageView.setTag("[strive]");
                break;
            case 29:
                imageView.setTag("[curse]");
                break;
            case 30:
                imageView.setTag("[query]");
                break;
            case 31:
                imageView.setTag("[hush]");
                break;
            case 32:
                imageView.setTag("[faint]");
                break;
            case 33:
                imageView.setTag("[torment]");
                break;
            case 34:
                imageView.setTag("[decline]");
                break;
            case 35:
                imageView.setTag("[beat]");
                break;
            case 36:
                imageView.setTag("[goodbye]");
                break;
            case 37:
                imageView.setTag("[wipeperspiration]");
                break;
            case 38:
                imageView.setTag("[nose]");
                break;
            case 39:
                imageView.setTag("[solidfood]");
                break;
            case 40:
                imageView.setTag("[snicker]");
                break;
            case 41:
                imageView.setTag("[lefthem]");
                break;
            case 42:
                imageView.setTag("[righthem]");

                break;
            case 43:
                imageView.setTag("[yawn]");
                break;
            case 44:
                imageView.setTag("[contempt]");
                break;
            case 45:
                imageView.setTag("[feelwronged]");
                break;
            case 46:
                imageView.setTag("[fastcry]");
                break;
            case 47:
                imageView.setTag("[cattiness]");
                break;
            case 48:
                imageView.setTag("[kiss]");
                break;
            case 49:
                imageView.setTag("[intimidate]");
                break;
            case 50:
                imageView.setTag("[pitiful]");
                break;
            case 51:
                imageView.setTag("[hug]");
                break;
            case 52:
                imageView.setTag("[moon]");
                break;
            case 53:
                imageView.setTag("[sun]");
                break;
            case 54:
                imageView.setTag("[bomb]");
                break;
            case 55:
                imageView.setTag("[humanskeleton]");
                break;
            case 56:
                imageView.setTag("[kitchenknife]");
                break;
            case 57:
                imageView.setTag("[pighead]");
                break;
            case 58:
                imageView.setTag("[watermelon]");
                break;
            case 59:
                imageView.setTag("[coffee]");
                break;
            case 60:
                imageView.setTag("[meal]");
                break;
            case 61:
                imageView.setTag("[lovingheart]");
                break;
            case 62:
                imageView.setTag("[strong]");
                break;
            case 63:
                imageView.setTag("[weak]");
                break;
            case 64:
                imageView.setTag("[handshake]");
                break;
            case 65:
                imageView.setTag("[victory]");
                break;
            case 66:
                imageView.setTag("[holdhand]");
                break;
            case 67:
                imageView.setTag("[seduce]");
                break;
            case 68:
                imageView.setTag("[ok]");
                break;
            case 69:
                imageView.setTag("[no]");
                break;
            case 70:
                imageView.setTag("[rose]");
                break;
            case 71:
                imageView.setTag("[fade]");
                break;
            case 72:
                imageView.setTag("[courtshipdisplay]");
                break;
            case 73:
                imageView.setTag("[loves]");
                break;
            case 74:
                imageView.setTag("[throwkiss]");
                break;


        }
//        if (position < 75) {
//            imageView.setTag("[" + position + "]");
//        } else if (position < 100) {
//            imageView.setTag("[" + (position + 1) + "]");
//        } else {
//            imageView.setTag("[" + (position + 2) + "]");
//        }

      // Glide.with(mContext).load(mImageIds[position]) .centerCrop().placeholder(R.drawable.gif1).crossFade().into(imageView);//有预加载

        return imageView;
    }
}
