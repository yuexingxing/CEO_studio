package com.tajiang.ceo.common.widget;

/**
 * Created by Administrator on 2016/9/8.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Scroller;

public class ScrollViewByY extends ViewGroup {
    private static final String TAG = "CustomScrollViewByY";
    private static final int DURATION_TIME = 1000;  //粘性滚动持续时间毫秒
    private static final int MIN_VELOCITY = 400;
    private static final int SNAP_UP = 1;
    private static final int SNAP_DOWN = -1;
    private static final float STICKY_SCROLL_HEIGHT_RATE = 1/4;  //触发粘性滑动所占总控件高度比

    private int mChiildHeight;
    private int mChildWidth;
    private int currentPage;
    private int mTouchSlop;
    private int mLastY;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mSnapDirection;

    private boolean mStartStickeyScroll = false;

    private VelocityTracker mVelocityTracker = null;
    private Scroller mScroller;
    private Context mContext;

    public ScrollViewByY(Context context) {
        this(context, null);
    }

    public ScrollViewByY(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewByY(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mScroller = new Scroller(mContext);

        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity() + MIN_VELOCITY;
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        currentPage = 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {   //遍历通知所有子View对自身测量
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        mChiildHeight = b - t;
        mChildWidth = r - l;

        //设置ViewGroup高度

        MarginLayoutParams mLayoutParams = (MarginLayoutParams) getLayoutParams();
        mLayoutParams.height = mChiildHeight * childCount;
        setLayoutParams(mLayoutParams);
        for (int i = 0; i < childCount; i++) {  //设置子View的放置位置
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.layout(l, i * (b - t), r, (i + 1) * (b - t));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ScrollY;    //相对于ViewGroup控件Y坐标子控件移动距离 getScrollY()
        int currentY = (int) event.getY();    //相对于ViewGroup控件Y坐标位置（0 - mScreenHeight）
        obtainVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartStickeyScroll = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastY - currentY;
                mLastY = currentY;
                ScrollY = getScrollY();
                //获取滑动手势的方向
                mSnapDirection = deltaY > 0?SNAP_UP:SNAP_DOWN;
                //预测判定移动后是否会超出顶末端界限
                if (deltaY < 0) {
                    if (ScrollY + deltaY > 0) {
                        scrollBy(0,deltaY);
                    }
                } else {
                    if (ScrollY + deltaY <= (getChildCount() - 1) * mChiildHeight) {
                        scrollBy(0,deltaY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000,mMaximumVelocity);  //速度单位：1000毫秒内移动的像素
                int initialVelocity = (int) mVelocityTracker.getYVelocity();
                if ((Math.abs(initialVelocity) > mMinimumVelocity)
                        && getChildCount() > 0) {
                    mStartStickeyScroll = true; //标志位：当结束惯性滑动时触发粘性滑动
                    fling(-initialVelocity,mChiildHeight * (getChildCount() - 1));
                } else {  //手势离开时，触发粘性滑动
                    StartStickyScroll();
                }
                recycleVelocityTracker();
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        } else {
            //结束惯性滑动，触发粘性滑动
            if (mStartStickeyScroll) {
                StartStickyScroll();
                postInvalidate();
                mStartStickeyScroll = false;
            }
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    private void fling(int velocityY, int maxYScrollEdge) {
        if (getChildCount() > 0) {
            mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0,
                    maxYScrollEdge);
            awakenScrollBars(0);
        }
    }

    /**
     *  根据手势离开或者View停止滚动时的Y偏移量，进行粘性滚动
     */
    private void StartStickyScroll() {
        int mEndY = getScrollY();
        int dScrollY = mEndY%mChiildHeight;
        if (mSnapDirection == SNAP_UP) {  //上滑
            if (dScrollY <= mChiildHeight * STICKY_SCROLL_HEIGHT_RATE) {
                mScroller.startScroll(
                        0,getScrollY(),
                        0,-dScrollY,
                        DURATION_TIME);
            } else {
                mScroller.startScroll(
                        0,getScrollY(),
                        0,mChiildHeight - dScrollY,
                        DURATION_TIME);
            }
        } else {
            dScrollY = mChiildHeight - dScrollY;
            if (dScrollY <= mChiildHeight * STICKY_SCROLL_HEIGHT_RATE) {
                mScroller.startScroll(
                        0,getScrollY(),
                        0,dScrollY,
                        DURATION_TIME);
            } else {
                mScroller.startScroll(
                        0,getScrollY(),
                        0,-mChiildHeight + dScrollY,
                        DURATION_TIME);
            }
        }
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}

