package com.tajiang.ceo.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Admins on 2016/10/12.
 */
public class ObserveScrollView extends ScrollView {

    private ScrollListener mListener;

    //声明接口，用于向外部传递位置数据
    public static interface ScrollListener {
        public void scrollOritention(int l, int t, int oldl, int oldt);
    }

    public ObserveScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public ObserveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ObserveScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.scrollOritention(l, t, oldl, oldt);
        }
    }

    public void setScrollListener(ScrollListener listener) {
        this.mListener = listener;
    }

}