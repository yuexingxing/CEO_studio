package com.tajiang.ceo.mess.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.mess.activity.WithdrawBalanceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/27.
 */
public class WithDrawCashDialog extends Dialog {

    @BindView(R.id.tv_withdraw_money)
    TextView tvWithdrawMoney;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_ensure)
    TextView btnEnsure;

    RequestListener requestListener;

    public void setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public interface RequestListener {
        public void doPostRequest();
    }

    public WithDrawCashDialog(Context context) {
        super(context, R.style.default_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_withdrawbalance_dialog, null);
        setContentView(view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        this.setCancelable(false);
    }

    public void setWithdrawCash(String cash) {
        tvWithdrawMoney.setText(cash);
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick() {
        this.dismiss();
    }

    @OnClick(R.id.btn_ensure)
    public void onConfirmClick() {
        if (requestListener != null) {
            requestListener.doPostRequest();
        }
        this.dismiss();
    }

}
