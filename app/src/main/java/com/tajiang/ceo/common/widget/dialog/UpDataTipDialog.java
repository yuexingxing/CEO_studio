package com.tajiang.ceo.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tajiang.ceo.R;

/**
 * Created by 12154 on 2015/12/16.
 */
public class UpDataTipDialog extends Dialog implements View.OnClickListener {

    private OnClickUpdataListener onClickUpdataListener;

    private View tv_negetiveHintDialog;
    private View tv_positiveHintDialog;

    private TextView tvDescriptionVision;

    private boolean isForce = false;

    public UpDataTipDialog(Context context, boolean isForce , String descriptionVision) {
        super(context, R.style.dialog_operate);
        this.isForce = isForce;

        setContentView(R.layout.dialog_updata_tip);
        setCancelable(!isForce);
        setCanceledOnTouchOutside(false);

        tv_positiveHintDialog = findViewById(R.id.tv_positiveHintDialog);
        tv_negetiveHintDialog = findViewById(R.id.tv_negetiveHintDialog);

        tvDescriptionVision = (TextView) findViewById(R.id.tvDescriptionVision);

        tv_negetiveHintDialog.setVisibility(isForce ? View.GONE : View.VISIBLE);

        tv_positiveHintDialog.setOnClickListener(this);
        tv_negetiveHintDialog.setOnClickListener(this);

        tvDescriptionVision.setText(descriptionVision);

    }

    @Override
    public void onBackPressed() {
        if (!isForce) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_negetiveHintDialog:
                dismiss();
                break;
        }
    }

    public interface OnClickUpdataListener {
        void onClickUpdata();
    }

    public void setOnUpDataClickListener(View.OnClickListener listener) {
        if (listener != null) {
            tv_positiveHintDialog.setOnClickListener(listener);
        }
    }
}
