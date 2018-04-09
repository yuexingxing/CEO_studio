package com.tajiang.ceo.setting.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jungly.gridpasswordview.GridPasswordView;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import butterknife.BindView;

/**
 * 第一次设置支付密码
 */
public class FirstSetPayPsdActivity extends BaseActivity implements GridPasswordView.OnPasswordChangedListener{

    @BindView(R.id.gpv_first_set_password)
    GridPasswordView gpvResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gpvResetPassword.setOnPasswordChangedListener(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("设置支付密码");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_first_set_pay_psd);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onChanged(String psw) {

    }

    @Override
    public void onMaxLength(String psw) {

        Intent intent = new Intent(this, ConfirmPsdActivity.class);
        intent.putExtra("mode", ConfirmPsdActivity.STATE_MODE_NO_PAY_PWD);
        intent.putExtra("newPassword", psw);
        startActivity(intent);
        finish();
    }
}
