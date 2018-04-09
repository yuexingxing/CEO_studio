package com.tajiang.ceo.common.widget.dialog;

/**
 * Created by Administrator on 2016/8/19.
 */

import com.tajiang.ceo.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CallDialog extends Dialog implements OnClickListener{

    private View contentView;

    private TextView tv_phoneOrderTakeAway;

    private View view_cancelCallDialog;
    private View view_callCallDialog;

    private String phone;

    public CallDialog(Context context , String phone) {
        super(context, R.style.dialog_operate);

        this.phone = phone;
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        contentView = getLayoutInflater().inflate(R.layout.dialog_call, null);

        tv_phoneOrderTakeAway = (TextView) contentView.findViewById(R.id.tv_phoneOrderTakeAway);
        view_cancelCallDialog = contentView.findViewById(R.id.view_cancelCallDialog);
        view_callCallDialog = contentView.findViewById(R.id.view_callCallDialog);

        tv_phoneOrderTakeAway.setText(phone);

        view_cancelCallDialog.setOnClickListener(this);
        view_callCallDialog.setOnClickListener(this);

        setContentView(contentView);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.view_cancelCallDialog:
                break;
            case R.id.view_callCallDialog:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + phone)); // num为电话号码
                getContext().startActivity(intent);
                break;
        }

        dismiss();

    }

}
