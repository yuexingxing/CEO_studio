package com.tajiang.ceo.common.widget.dialog;

import android.content.Context;

import com.tajiang.ceo.R;

/**
 * Created by 12154 on 2015/12/16.
 */
public class DownAppDialog extends CDialog {

    public DownAppDialog(Context context) {
        super(context, R.layout.dialog_down_app);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {
    }
}
