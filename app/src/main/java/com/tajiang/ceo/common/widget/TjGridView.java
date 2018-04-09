package com.tajiang.ceo.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

import com.tajiang.ceo.common.utils.LogUtils;

/**
 * Created by Admins on 2016/10/11.
 */
public class TjGridView extends GridView {

    public TjGridView(Context context) {
        this(context, null);
    }

    public TjGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TjGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //截取GridView的自动获取焦点的能力，以此保留切换页面返回停留在原始位置
        this.setFocusable(false);
        // TODO 自动生成的构造函数存根
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO 自动生成的方法存根
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}