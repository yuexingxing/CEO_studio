package com.tajiang.ceo.mess.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.DensityUtils;
import com.tajiang.ceo.common.utils.ToastUtils;

/**
 * Created by SQL on 2016/8/6.
 */
public class MessTimeView extends TextView {

    private String text;
    private Context context;

    public MessTimeView(Context context, String text) {
        this(context, null, text);
    }

    public MessTimeView(Context context, AttributeSet attrs, String text) {
        super(context, attrs);
        this.context = context;
        this.text = text;
        initParams();
    }

    private void initParams() {
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setText(text);
        this.setPadding(DensityUtils.dp2px(context, 15), 0, DensityUtils.dp2px(context, 15), 0);
        this.setLayoutParams(mLayoutParams);
        this.setHeight(DensityUtils.dp2px(context, 50));
        this.setClickable(true);
        this.setBackgroundDrawable(getResources().getDrawable(R.drawable.slt_layout_rect_gray));
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setTextSize(16);
        this.setTextColor(getResources().getColor(R.color.text_black_1));
        Drawable drawable= getResources().getDrawable(R.mipmap.icon_jiantou_r);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        this.setCompoundDrawables(null,null,drawable,null);

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(text);
            }
        });
    }

}
