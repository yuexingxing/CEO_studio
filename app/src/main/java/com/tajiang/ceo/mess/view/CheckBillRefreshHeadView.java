package com.tajiang.ceo.mess.view;

import android.content.Context;
import android.util.AttributeSet;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.widget.LayoutHeaderView;

/**
 * Created by work on 2016/7/12.
 */

public class CheckBillRefreshHeadView extends LayoutHeaderView implements SwipeRefreshTrigger, SwipeTrigger {

    public CheckBillRefreshHeadView(Context context) {
        this(context, null);
    }

    public CheckBillRefreshHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckBillRefreshHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
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