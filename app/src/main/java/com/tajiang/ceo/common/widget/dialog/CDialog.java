package com.tajiang.ceo.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.tajiang.ceo.R;

public class CDialog extends Dialog {

    private View contentView;

    public CDialog(Context context, int layoutId) {
        super(context, R.style.loading_dialog);
        init(layoutId);
    }


    public CDialog(Context context, int layoutId, int style) {
        super(context, style);
        init(layoutId);
    }

    private void init(int layoutId) {
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        contentView = getLayoutInflater().inflate(layoutId, null);

        setContentView(contentView);
    }

    public <T extends View> T findChildViewById(int id) {
        return (T) contentView.findViewById(id);
    }

    public View getContentView() {
        return contentView;
    }

}
