package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by work on 2016/7/12.
 */
public class WithdrawProgressActivity extends BaseActivity {

    @BindView(R.id.tv_withdraw_money)
    TextView tvWithdrawMoney;
    @BindView(R.id.tv_date_withdraw_money)
    TextView tvDateWithdrawMoney;
    @BindView(R.id.tv_date_transfer)
    TextView tvDateTransfer;

    @Override
    protected void initTopBar() {
        setTitle("提现");
        enableRightText("完成", new OnRightClick() {
            @Override
            public void rightClick() {
                WithdrawProgressActivity.this.finish();
            }
        });
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_withdraw_progress);
        ButterKnife.bind(this);
        initMyView();
    }

    private void initMyView() {
        tvWithdrawMoney.setText(getIntent().getStringExtra("withdraw_cash"));
        //获取当前时间
        SimpleDateFormat formatter  =  new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        tvDateWithdrawMoney.setText(formatter.format(curDate));
        //设置预计到账时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.HOUR, 48);
        Date date =  new Date(calendar.getTimeInMillis());
        tvDateTransfer.setText(formatter.format(date));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
