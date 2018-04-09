package com.tajiang.ceo.setting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jungly.gridpasswordview.GridPasswordView;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 确认密码，涉及到的接口在这里调用
 * Created by work on 2016/6/28.
 */
public class ConfirmPsdActivity extends BaseActivity implements GridPasswordView.OnPasswordChangedListener{

    public static final int STATE_MODE_HAVE_PAY_PWD = 1;   //设置过支付密码,需要进入输入原密码页面

    public static final int STATE_MODE_NO_PAY_PWD = 2; //没有设置过支付密码，直接进入设置新密码页面/

    public static final int STATE_MODE_FIND_PAY_PWD = 3; //通过验证码方式找回支付密码

    public static final int STATE_MODE_WITHDRAW_PWD = 4;//提现时输入密码

    private int CurrentState;

    private String oldPassword;
    private String confirmPassword;

    @BindView(R.id.gpv_reset_password)
    GridPasswordView gpvResetPassword;

    private String smsCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        ButterKnife.bind(this);

        gpvResetPassword.setOnPasswordChangedListener(this);

        Intent intent = getIntent();
        CurrentState = intent.getIntExtra("mode", STATE_MODE_NO_PAY_PWD);

        if (CurrentState == STATE_MODE_HAVE_PAY_PWD) {

            oldPassword = intent.getStringExtra("oldPassword");
        } else  if (CurrentState == STATE_MODE_FIND_PAY_PWD) {

            smsCode = intent.getStringExtra("sms_code");
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
        confirmPassword = psw;

        if (!TextUtils.isEmpty(oldPassword) && !oldPassword.equals(confirmPassword)) {
            ToastUtils.showShort("两次密码不一致");
            return;
        }

        //修改密码
        if (CurrentState == STATE_MODE_HAVE_PAY_PWD) {

            PostDataTools.acct_updatepwd(this, confirmPassword, oldPassword, new PostDataTools.DataCallback() {

                @Override
                public void callback(boolean flag, String message, Object object) {

                    if(flag){
                        finish();
                    }
                }
            });
        }
        //找回密码
        else if (CurrentState == STATE_MODE_FIND_PAY_PWD) {

            PostDataTools.acct_resetpwd(this, confirmPassword, smsCode, new PostDataTools.DataCallback() {

                @Override
                public void callback(boolean flag, String message, Object object) {

                    if(flag){
                        finish();
                    }
                }
            });
        }
        //第一次设置支付密码
        else if(CurrentState == STATE_MODE_NO_PAY_PWD){

            PostDataTools.acct_setpwd(this, confirmPassword, new PostDataTools.DataCallback() {

                @Override
                public void callback(boolean flag, String message, Object object) {

                    if(flag){

                        PostDataTools.getUserInfo(ConfirmPsdActivity.this, null);
                        finish();
                    }
                }
            });
        }
        //提现时输入密码
        else if(CurrentState == STATE_MODE_WITHDRAW_PWD){

            Intent intent = new Intent();
            intent.putExtra("payPwd", confirmPassword);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
