package com.tajiang.ceo.setting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jungly.gridpasswordview.GridPasswordView;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 重置密码
 * Created by work on 2016/6/23.
 */
public class ResetPsdActivity extends BaseActivity implements GridPasswordView.OnPasswordChangedListener {

    private int currentState;
    private String oldPassword;

    @BindView(R.id.tv_reset_password)
    TextView tvResetPassword;
    @BindView(R.id.gpv_reset_password)
    GridPasswordView gpvResetPassword;

    private String smsCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        gpvResetPassword.setOnPasswordChangedListener(this);
        Intent intent = getIntent();
        currentState = intent.getIntExtra("mode", ConfirmPsdActivity.STATE_MODE_NO_PAY_PWD);
        if (currentState == ConfirmPsdActivity.STATE_MODE_HAVE_PAY_PWD) {
            oldPassword = intent.getStringExtra("oldPassword");
        }else if(currentState == ConfirmPsdActivity.STATE_MODE_FIND_PAY_PWD){
            smsCode = getIntent().getStringExtra("sms_code");
        }
    }

    @Override
    protected void initTopBar() {
        setTitle("设置支付密码");
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onChanged(String psw) {

    }

    @Override
    public void onMaxLength(String psw) {
        Intent intent = new Intent();
        intent.putExtra("mode",currentState);
        if (currentState == ConfirmPsdActivity.STATE_MODE_HAVE_PAY_PWD) {
            intent.putExtra("oldPassword", oldPassword);
            intent.putExtra("newPassword", psw);
        } else if(currentState == ConfirmPsdActivity.STATE_MODE_FIND_PAY_PWD){
            intent.putExtra("sms_code", smsCode);
            intent.putExtra("newPassword", psw);
        }else{
            intent.putExtra("newPassword", psw);
        }

        intent.setClass(ResetPsdActivity.this, ConfirmPsdActivity.class);
        startActivity(intent);
        finish();
    }
}
