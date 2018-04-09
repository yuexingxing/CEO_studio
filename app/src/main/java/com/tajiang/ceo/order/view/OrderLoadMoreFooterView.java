package com.tajiang.ceo.order.view;

import android.content.Context;
import android.util.AttributeSet;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.tajiang.ceo.common.widget.LayoutFooterView;

/**
 * Created by SQL on 2016/7/9.
 */
public class OrderLoadMoreFooterView extends LayoutFooterView implements SwipeTrigger, SwipeLoadMoreTrigger{
    public OrderLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public OrderLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public void onPrepare() {
        textView.setText("上拉加载更多");
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {

    }

    @Override
    public void onRelease() {
        textView.setText("加载中...");
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onComplete() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onReset() {

    }

    public void setFootTextView(String msg) {
        textView.setText(msg);
    }
}
