package com.tajiang.ceo.setting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.mess.view.LayoutEditBuilding;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/28.
 */
public class UpdateApartmentStateDialog extends Dialog {

    @BindView(R.id.tv_show_msg)
    TextView tvShowMsg;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_ensure)
    TextView btnEnsure;

    UpdateStateListener updateStateListener;
    private LayoutEditBuilding mView;

    public void setUpdateStateListener(UpdateStateListener updateStateListener) {
        this.updateStateListener = updateStateListener;
    }

    public interface UpdateStateListener {
        public void updateState();
    }

    public UpdateApartmentStateDialog(Context context, LayoutEditBuilding mView) {
        super(context, R.style.dialog_operate);
        this.mView = mView;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_popup_change_apartment_statue, null);
        setContentView(view);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        this.setCancelable(false);
        if (mView.getCurrentButtonState() == true){
            tvShowMsg.setText("寝室楼关闭后不可配送，确定关闭吗？");
            btnEnsure.setText("关闭");
            btnEnsure.setTextColor(getContext().getResources().getColor(R.color.red_orange));
        } else {
            tvShowMsg.setText("寝室楼开启后可配送，确定开启吗？");
            btnEnsure.setText("开启");
            btnEnsure.setTextColor(getContext().getResources().getColor(R.color.green_light));
        }
    }

    public void setTextView(String msg) {
        tvShowMsg.setText(msg);
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick() {
        this.dismiss();
    }

    @OnClick(R.id.btn_ensure)
    public void onConfirmClick() {
        mView.changeButtonStatues();
        this.dismiss();
    }
}
