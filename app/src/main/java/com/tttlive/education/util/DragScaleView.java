package com.tttlive.education.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/5/30/0030.
 * 直播视频窗口
 */
public abstract class DragScaleView extends RelativeLayout implements View.OnTouchListener{

    public static final int ZOOM_MODE_CUT = 0x100;
    public static final int ZOOM_MODE_FULL = 0x101;

    private static final int TOP = 0x15;
    private static final int LEFT = 0x16;
    private static final int BOTTOM = 0x17;
    private static final int RIGHT = 0x18;
    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int CENTER = 0x19;
    private static final int MIN_SIZE = 100;
    private static final int CONTROL_AREA_SIZE = 10;
    private static final String TAG = "DragScaleView";

    private int screenWidth;
    private int screenHeight;
    private int startX;
    private int startY;
    private int lastX;
    private int lastY;
    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;
    private int dragDirection;

    protected int initLeft;
    protected int initRight;
    protected int initTop;
    protected int initBottom;
    protected int initWidth;
    protected int initHeight;

    protected float oldXScalingRatio = 1.0f;
    protected float oldYScalingRatio = 1.0f;
    private int offset = 0;
    private Paint paint = new Paint();
    private Context mContext;
    protected boolean isZoom = false;
    protected boolean isFull = false;
    protected int mZoomMode = ZOOM_MODE_CUT;
    private boolean isDoubleTapListener = false;

    protected int mVideoLayoutType;

    protected boolean touchable = true;

    public DragScaleView(Context context) {
        super(context);
        this.mContext = context;

        init();
    }

    protected void setMyVideoLayoutType(int mVideoLayoutType) {
        this.mVideoLayoutType = mVideoLayoutType;
    }

    public DragScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public DragScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void setZoomMode(int zoomMode) {
        mZoomMode = zoomMode;
    }

    public void setDoubleTapListener(boolean doubleTapListener) {
        isDoubleTapListener = doubleTapListener;
    }

    private void init() {
        setOnTouchListener(this);
        initScreenW_H();
    }

    protected void initScreenW_H() {

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        screenHeight = height;
        screenWidth = width;
    }

    private static final int MAX_INTERVAL_FOR_CLICK = 250;
    private static final int MAX_DISTANCE_FOR_CLICK = 20;
    private static final int MAX_DOUBLE_CLICK_INTERVAL = 500;
    int mDownX = 0;
    int mDownY = 0;
    int mTempX = 0;
    int mTempY = 0;
    boolean mIsWaitUpEvent = false;
    boolean mIsWaitDoubleClick = false;
    Runnable mTimerForUpEvent = new Runnable() {
        @Override
        public void run() {
            if (mIsWaitUpEvent) {
                Log.d(TAG,
                        "The mTimerForUpEvent has executed, so set the mIsWaitUpEvent as false");
                mIsWaitUpEvent = false;
            } else {
                Log.d(TAG,
                        "The mTimerForUpEvent has executed, mIsWaitUpEvent is false,so do nothing");
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!touchable) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mDownX = (int) event.getRawX();
            mDownY = (int) event.getRawY();
            mIsWaitUpEvent = true;
            postDelayed(mTimerForUpEvent, MAX_INTERVAL_FOR_CLICK);

            oriLeft = getLeft();
            oriRight = getRight();
            oriTop = getTop();
            oriBottom = getBottom();
            startX = (int) event.getRawX();
            startY = (int) event.getRawY();
            lastX = (int) event.getRawX();
            lastY = (int) event.getRawY();
            dragDirection = getDirection(v, (int) event.getX(),
                    (int) event.getY());
        }
        // 处理拖动事件
        delDrag(v, event, action);
        invalidate();

        return true;
    }

    boolean flag = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (flag) {
            initLeft = getLeft();
            initRight = getRight();
            initTop = getTop();
            initBottom = getBottom();
            initWidth = getWidth();
            initHeight = getHeight();
            flag = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(123, 123, 123));

        //        paint.setColor(Color.RED);
        //        paint.setStrokeWidth(4.0f);
        //        paint.setStyle(Paint.Style.STROKE);
        //        canvas.drawRect(offset, offset, getWidth() - offset, getHeight()
        //                - offset, paint);
    }

    protected void delDrag(View v, MotionEvent event, int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                mTempX = (int) event.getRawX();
                mTempY = (int) event.getRawY();
                if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                        || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                    Log.d(TAG, "The move distance too far:cancel the click");
                }

                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                switch (dragDirection) {
                    case LEFT: // 左边缘
                        left(v, dx);
                        break;
                    case RIGHT: // 右边缘
                        right(v, dx);
                        break;
                    case BOTTOM: // 下边缘
                        bottom(v, dy);
                        break;
                    case TOP: // 上边缘
                        top(v, dy);
                        break;
                    case CENTER: // 点击中心-->>移动
                        center(v, dx, dy);
                        break;
                    case LEFT_BOTTOM: // 左下
                        left(v, dx);
                        bottom(v, dy);
                        break;
                    case LEFT_TOP: // 左上
                        left(v, dx);
                        top(v, dy);
                        break;
                    case RIGHT_BOTTOM: // 右下
                        right(v, dx);
                        bottom(v, dy);
                        break;
                    case RIGHT_TOP: // 右上
                        right(v, dx);
                        top(v, dy);
                        break;
                    default:
                        break;
                }
                if (dragDirection != CENTER && isZoom && !isFull) {
                    ViewGroup.LayoutParams lp = v.getLayoutParams();
                    lp.width = oriRight - oriLeft;
                    lp.height = oriBottom - oriTop;
                    v.setLayoutParams(lp);

                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                mTempX = (int) event.getRawX();
                mTempY = (int) event.getRawY();
                if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                        || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                } else {
                    mIsWaitUpEvent = false;
                    removeCallbacks(mTimerForUpEvent);
                    onSingleClick();
                }

                if (dragDirection > 0 && dragDirection != CENTER) {
                    //                    childOnLayout();
                }

                // 移动位移小于视频高度时，做归位操作
//                if (mZoomMode == ZOOM_MODE_CUT && dragDirection == CENTER && getTop() < defaultVideoHeight && !isFull) {
//                    if (isResetVideo()) {
//                        layout(initLeft, initTop, initRight, initBottom);
//                        resetVideo();
//                        return;
//                    }
//                }

                int x = Math.abs((int) event.getRawX() - startX);
                int y = Math.abs((int) event.getRawY() - startY);

                if (dragDirection == CENTER && !isFull) {
                    if (x <= MAX_DISTANCE_FOR_CLICK && y <= MAX_DISTANCE_FOR_CLICK) {
                        Log.e(TAG, "移动过小 不触发" + " x=" + x + "; y=" + y);
                    } else {
                        if (getLayoutParams() instanceof AbsoluteLayout.LayoutParams){
                            AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) getLayoutParams();
                            layoutParams.x = getLeft();
                            layoutParams.y = getTop();
                            setLayoutParams(layoutParams);
                        }
                        if(getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                            layoutParams.leftMargin = getLeft();
                            layoutParams.topMargin = getTop();
                            setLayoutParams(layoutParams);
                        }

                        onLayoutMove();
                    }
                } else if (isZoom) {
                    onLayoutZoom();
                }

                dragDirection = 0;
                break;
            default:
                break;
        }
    }


    Runnable mTimerForSecondClick = new Runnable() {
        @Override
        public void run() {
            if (mIsWaitDoubleClick) {
                Log.d(TAG,
                        "The mTimerForSecondClick has executed,so as a singleClick");
                mIsWaitDoubleClick = false;
            } else {
                Log.d(TAG,
                        "The mTimerForSecondClick has executed, the doubleclick has executed ,so do thing");
            }
        }
    };

    private void onSingleClick() {
        if (mIsWaitDoubleClick) {
            onLayoutDoubleTap();
            mIsWaitDoubleClick = false;
            removeCallbacks(mTimerForSecondClick);
        } else {
            onLayoutClick();
            mIsWaitDoubleClick = true;
            postDelayed(mTimerForSecondClick, MAX_DOUBLE_CLICK_INTERVAL);
        }
    }

    private void center(View v, int dx, int dy) {
        int left = v.getLeft() + dx;
        int top = v.getTop() + dy;
        int right = v.getRight() + dx;
        int bottom = v.getBottom() + dy;
        if (left < -offset) {
            left = -offset;
            right = left + v.getWidth();
        }
        if (right > screenWidth + offset) {
            right = screenWidth + offset;
            left = right - v.getWidth();
        }
        if (top < -offset) {
            top = -offset;
            bottom = top + v.getHeight();
        }
        if (bottom > screenHeight + offset) {
            bottom = screenHeight + offset;
            top = bottom - v.getHeight();
        }
        if (!isFull) {
            v.layout(left, top, right, bottom);
        }
    }

    private void top(View v, int dy) {
        oriTop += dy;
        if (oriTop < -offset) {
            oriTop = -offset;
        }
        if (oriBottom - oriTop - 2 * offset < MIN_SIZE) {
            oriTop = oriBottom - 2 * offset - MIN_SIZE;
        }
    }

    private void bottom(View v, int dy) {
        oriBottom += dy;
        if (oriBottom > screenHeight + offset) {
            oriBottom = screenHeight + offset;
        }
        if (oriBottom - oriTop - 2 * offset < MIN_SIZE) {
            oriBottom = MIN_SIZE + oriTop + 2 * offset;
        }
    }

    private void right(View v, int dx) {
        oriRight += dx;
        if (oriRight > screenWidth + offset) {
            oriRight = screenWidth + offset;
        }
        if (oriRight - oriLeft - 2 * offset < MIN_SIZE) {
            oriRight = oriLeft + 2 * offset + MIN_SIZE;
        }
    }

    private void left(View v, int dx) {
        oriLeft += dx;
        if (oriLeft < -offset) {
            oriLeft = -offset;
        }
        if (oriRight - oriLeft - 2 * offset < MIN_SIZE) {
            oriLeft = oriRight - 2 * offset - MIN_SIZE;
        }
    }

    protected int getDirection(View v, int x, int y) {
        int left = v.getLeft();
        int right = v.getRight();
        int bottom = v.getBottom();
        int top = v.getTop();
        if (x < CONTROL_AREA_SIZE && y < CONTROL_AREA_SIZE) {
            return LEFT_TOP;
        }
        if (y < CONTROL_AREA_SIZE && right - left - x < CONTROL_AREA_SIZE) {
            return RIGHT_TOP;
        }
        if (x < CONTROL_AREA_SIZE && bottom - top - y < CONTROL_AREA_SIZE) {
            return LEFT_BOTTOM;
        }
        if (right - left - x < CONTROL_AREA_SIZE && bottom - top - y < CONTROL_AREA_SIZE) {
            return RIGHT_BOTTOM;
        }
        if (x < CONTROL_AREA_SIZE) {
            return LEFT;
        }
        if (y < CONTROL_AREA_SIZE) {
            //            return TOP;
            return CENTER;
        }
        if (right - left - x < CONTROL_AREA_SIZE) {
            return RIGHT;
        }
        if (bottom - top - y < CONTROL_AREA_SIZE) {
            return BOTTOM;
        }
        return CENTER;
    }

    protected void zoom(View view, int x, int y, float zoomX, float zoomY) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", oldXScalingRatio, zoomX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", oldYScalingRatio, zoomY);
        ObjectAnimator animatorTx = ObjectAnimator.ofFloat(view, "translationX", 0, x);
        ObjectAnimator animatorTy = ObjectAnimator.ofFloat(view, "translationY", 0, y);

        animatorSet.play(animatorX).with(animatorY).with(animatorTx).with(animatorTy);
        animatorSet.setDuration(500);
        view.setPivotX(0);
        view.setPivotY(0);
        animatorSet.setTarget(view);
        animatorSet.start();
    }


    protected abstract void childOnLayout();

    /**
     * 移动、放大缩小文档后，文档的当前大小和位置
     */
    protected abstract void onLayoutMove();

    /**
     * 单击事件
     */
    protected abstract void onLayoutClick();

    protected abstract void onLayoutZoom();

    /**
     * 双击事件
     */
    protected abstract void onLayoutDoubleTap();


    /**
     * 视频挪动的时候，自动复位监听函数
     */
    protected abstract void resetVideo();


    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }
}
