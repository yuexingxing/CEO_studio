package com.tajiang.ceo.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tajiang.ceo.R;

/**
 * Created by SQL on 2016/7/9.
 */
public class LayoutFooterView extends LinearLayout {

    private Context mContext;
    private LayoutInflater mInflater;
    private LayoutParams mLayoutParams;

    public TextView textView;
    public ProgressBar progressBar;

    public LayoutFooterView(Context context) {
        this(context, null);
    }

    public LayoutFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayoutFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
        initView();
    }

    private void initView() {
        this.setLayoutParams(mLayoutParams);
        this.setOrientation(HORIZONTAL);

        View view = mInflater.inflate(R.layout.layout_footer_loading, this, false);
        textView = (TextView) view.findViewById(R.id.pull_to_load_footer_hint_text_view);
        progressBar = (ProgressBar) view.findViewById(R.id.pull_to_load_footer_progressbar);
        this.addView(view);

    }

    private void initData(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
    }


}