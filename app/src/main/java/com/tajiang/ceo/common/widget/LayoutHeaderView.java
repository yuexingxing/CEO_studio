package com.tajiang.ceo.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tajiang.ceo.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by SQL on 2016/7/11.
 */

public class LayoutHeaderView extends LinearLayout {

    private Context mContext;
    private LayoutInflater mInflater;
    private LayoutParams mLayoutParams;

    public GifImageView gifImageView;
    public TextView textView;
    public ProgressBar progressBar;

    public LayoutHeaderView(Context context) {
        this(context, null);
    }

    public LayoutHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayoutHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
        initView();
    }

    private void initView() {
        this.setLayoutParams(mLayoutParams);
        this.setOrientation(HORIZONTAL);

        View view = mInflater.inflate(R.layout.layout_header_loading, this, false);
        gifImageView = (GifImageView) view.findViewById(R.id.gif_loading);
        this.addView(view);

    }

    private void initData(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
    }


}