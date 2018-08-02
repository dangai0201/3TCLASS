package com.tttlive.education.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tttlive.basic.education.R;
import com.tttlive.education.ui.room.bean.TrophyAwardBean;

/**
 * Created by Administrator on 2018/6/5/0005.
 */

public class FireworksView extends SurfaceView implements SurfaceHolder.Callback {

    // 监听
    private SurfaceHolder holder;
    private int[] imgList;
    private Activity context;
    private MrlThread mrlThread;
    private Bitmap bitmap = null, resizeBmp = null;
    private Resources resources;
    private float mWidth, mHeight;
    private boolean isruning;
    private GitfSpecialsStop animsopt;
    private float mLeft;
    private float mGiftWidthOld, mGiftWidthNew;
    private boolean surfaceDesy = false;

    public FireworksView(Activity context) {
        super(context);
        this.context = context;
        init();
    }

    public FireworksView(Activity context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    public void init() {
       setZOrderOnTop(true);//使surfaceview放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度
        imgList = new int[]{R.drawable.cup_01, R.drawable.cup_02, R.drawable.cup_03, R.drawable.cup_04, R.drawable.cup_05, R.drawable.cup_06, R.drawable.cup_07, R.drawable.cup_08, R.drawable.cup_09,
                R.drawable.cup_10, R.drawable.cup_11, R.drawable.cup_12, R.drawable.cup_13, R.drawable.cup_14, R.drawable.cup_15, R.drawable.cup_16, R.drawable.cup_17,
                R.drawable.cup_18, R.drawable.cup_19, R.drawable.cup_20};
        //        这里得到holder啦 我们继承了这个surfaceview  里面就有holder的
        //        SurfaceView.getHolder()获得SurfaceHolder对象
        holder = this.getHolder();
        // 给SurfaceView当前的持有者一个回调对象,也就是下面那几个回调监听啦
        holder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //在创建时激发，一般在这里调用画图的线程。
        mrlThread = new MrlThread(holder);
        mrlThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //        /在surface的大小发生改变时激发
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //销毁时激发，一般在这里将画图的线程停止、释放。
        surfaceDesy = true;
        bitmap = null;
        resizeBmp = null;
        holder.removeCallback(this);
    }

    public void startAnim() {
        if (!isruning) {
            this.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() / 1f;
        mHeight = getMeasuredHeight() / 1f;
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    public void setAnimsopt(GitfSpecialsStop animsopt) {
        this.animsopt = animsopt;
    }

    class MrlThread extends Thread {
        SurfaceHolder surfaceHolder;
        Paint paint;

        public MrlThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            paint = new Paint();
        }

        @Override
        public void run() {
            isruning = true;
            int i = 0;
            bitmap = null;
            resources = context.getResources();
            while (i < imgList.length) {
                if (surfaceDesy) {
                    return;
                }
                Canvas c = null;
                try {
                    synchronized (surfaceHolder) {
                        //                        获得Canvas对象并锁定画布
                        c = surfaceHolder.lockCanvas();
                        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        bitmap = BitmapFactory.decodeResource(resources, imgList[i]);
                        c.drawBitmap(small(bitmap), mLeft, 0, paint);
                        //通过它来控制帧数执行一次绘制后休息50ms
                        if (i == imgList.length - 1) {
                            Thread.sleep(2000);
                        } else {
                            Thread.sleep(18);
                        }
                        i++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 结束锁定画图，并提交改变，将图形显示
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    } else {
                        return;
                    }
                }
            }
            isruning = false;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FireworksView.this.setVisibility(GONE);
                    animsopt.animend();
                }
            });
        }

        private Bitmap small(Bitmap bitmap) {
            Matrix matrix = new Matrix();
            matrix.postScale(bitmap.getWidth() * mHeight / bitmap.getHeight() / bitmap.getWidth(), mHeight / bitmap.getHeight()); //长和宽放大缩小的比例
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            mLeft = (mWidth - resizeBmp.getWidth()) / 2;
            return resizeBmp;
        }
    }
}
