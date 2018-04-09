package com.tajiang.ceo.order.view;

import android.content.Context;
import android.util.AttributeSet;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.widget.LayoutHeaderView;

/**
 * Created by SQL on 2016/7/9.
 */
public class OrderRefreshHeadView extends LayoutHeaderView implements SwipeRefreshTrigger, SwipeTrigger {

    public OrderRefreshHeadView(Context context) {
        this(context, null);
    }

    public OrderRefreshHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderRefreshHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onPrepare() {
        gifImageView.setImageResource(R.mipmap.load_start);

    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {

    }

    @Override
    public void onRelease() {
        gifImageView.setImageResource(R.mipmap.load_ing);

    }

    @Override
    public void onComplete() {
        gifImageView.setImageResource(R.mipmap.load_finish);

    }

    @Override
    public void onReset() {

    }
}