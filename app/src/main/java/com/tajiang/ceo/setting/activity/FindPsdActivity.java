package com.tajiang.ceo.setting.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找回支付密码
 * Created by work on 2016/6/20.
 */
public class FindPsdActivity extends BaseActivity{

    private static final int TIME_LIMITED_SEND_CODE = 60000;  //发送校验码的时间（毫秒）
    private static final int TIME_LIMITED_PER_SECOND = 1000;  //（毫秒）


    @BindView(R.id.tv_input_phone)
    TextView tvInputPhone;
    @BindView(R.id.tv_code_verify)
    TextView tvCodeVerify;
    @BindView(R.id.tv_reset_password)
    TextView tvResetPassword;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.rl_code)
    RelativeLayout rlCode;
    @BindView(R.id.btn_verify_code)
    Button btnVerifyCode;

    @Override
    protected void initTopBar() {

        setTitle("找回支付密码");
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get_code)
    public void onGetCodeClick() {

        String phone = etPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.showShort("手机号不能为空");
            return;
        }

        PostDataTools.msg_code(this, "PAY_PWD_CODE", phone, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                if(flag){

                }
            }
        });
    }

    @OnClick(R.id.btn_verify_code)
    public void onCheckCodeClick() {

        if(TextUtils.isEmpty(etCode.getText())) {
            ToastUtils.showShort("验证码不能为空");
            return;
        }

        Intent intent = new Intent(FindPsdActivity.this, ResetPsdActivity.class);
        intent.putExtra("mode", ConfirmPsdActivity.STATE_MODE_FIND_PAY_PWD);
        intent.putExtra("sms_code", etCode.getText().toString());
        startActivity(intent);
        finish();
    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture
         *      表示以毫秒为单位 倒计时的总数
         *      例如 millisInFuture=1000 表示1秒
         * @param countDownInterval
         *      表示 间隔 多少微秒 调用一次 onTick 方法
         *      例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            btnGetCode.setClickable(false);//发送验证码期间停用发送按钮
        }

        @Override
        public void onFinish() {
            btnGetCode.setText(getResourcesString(R.string.msg_send_reg_code));
            btnGetCode.setBackgroundColor(FindPsdActivity.this.getResources()
                    .getColor(R.color.green));
            btnGetCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnGetCode.setText("" + millisUntilFinished / 1000 + "s");
        }
    }

}
