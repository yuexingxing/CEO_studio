package com.tajiang.ceo.setting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.setting.wallet.ModifyPsdActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付设置
 * Created by SQL on 2016/6/25.
 */
public class PaySettingActivity extends BaseActivity{

    @BindView(R.id.ll_change_password)
    LinearLayout llChangePassword;

    @BindView(R.id.ll_find_password)
    LinearLayout llFindPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_setting);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {

        setTitle("支付设置");
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.ll_change_password)
    public void onChangePasswordClick() {

        Intent intent = new Intent(this, ModifyPsdActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_find_password)
    public void onFindPasswordClick() {

        Intent intent = new Intent(this, FindPsdActivity.class);
        startActivity(intent);
    }

}
