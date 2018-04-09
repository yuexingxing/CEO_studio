package com.tajiang.ceo.setting.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jungly.gridpasswordview.GridPasswordView;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 修改密码
 * Created by Administrator on 2016/5/10.
 */
public class ChangePsdActivity extends BaseActivity implements GridPasswordView.OnPasswordChangedListener {

    @BindView(R.id.gpv_normal)
    GridPasswordView gpvNormal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        gpvNormal.setOnPasswordChangedListener(this);
    }

    @Override
    protected void initTopBar() {

        setTitle("修改支付密码");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_change_password);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onChanged(String psw) {

    }

    @Override
    public void onMaxLength(String psw) {

        Intent intent = new Intent(ChangePsdActivity.this, ResetPsdActivity.class);
        intent.putExtra("mode", ConfirmPsdActivity.STATE_MODE_HAVE_PAY_PWD);
        intent.putExtra("oldPassword", psw);
        startActivity(intent);
        finish();
    }
}
