package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/26.
 */
public class TodayIncomeDetailActivity extends BaseActivity {


    @BindView(R.id.tv_today_profit)
    TextView tvTodayProfit;
    @BindView(R.id.tv_today_reward)
    TextView tvTodayReward;

    @Override
    protected void initTopBar() {
        setTitle("今日配送收入");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_today_income_detail);
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("profit")) {
            tvTodayProfit.setText("+" + getIntent().getStringExtra("profit"));
        }
        if (getIntent().hasExtra("reward")) {
            tvTodayReward.setText("+" + getIntent().getStringExtra("reward"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_today_reward_rules)
    public void onClick() {
        intent2Activity(OrderRewardActivity.class);
    }
}
