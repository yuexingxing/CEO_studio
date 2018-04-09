package com.tajiang.ceo.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.activity.HomeActivity;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.constant.TJCst;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.Res;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.dialog.ChooseHostDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class
LoginActivity extends BaseActivity {

    @BindView(R.id.login_phone)
    EditText edtPhone;

    @BindView(R.id.login_psd)
    EditText edtPwd;

    @BindView(R.id.login_commit)
    Button btnLogin;

    @Override
    protected void initTopBar() {

        setTitle("登录");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void initData() {

        checkStatus();
        setEditableListener(edtPhone);
        setEditableListener(edtPwd);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TJApp.getInstance().exit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnLongClick(R.id.login_commit)
    boolean btnLongClick() {
        if (TJCst.IS_DEBUG) {
            new ChooseHostDialog(this).show();
            return true;
        } else {
            return false;
        }
    }

    @OnClick(R.id.login_commit)
    public void click() {

        String phone = edtPhone.getText().toString();
        String password = edtPwd.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showLong("请输入密码");
            return ;
        }

        PostDataTools.login(this, phone, password, new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {

                Intent intent = new Intent();
                intent2ActivityWithExtras(intent, HomeActivity.class);
                finish();
            }
        });
    }

    /**
     * 对EditText进行录入监听
     * @param edt
     */
    private void setEditableListener(EditText edt){

        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                checkStatus();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
    }

    public void checkStatus(){

        if(edtPhone.getText().toString().length() == 11 && edtPwd.getText().toString().length() >= 6){
            btnLogin.setBackgroundResource(R.drawable.shape_radius_order_main_color);
            btnLogin.setTextColor(Res.getColor(R.color.text_black));
        }else{
            btnLogin.setBackgroundResource(R.drawable.shape_radius_order_gray_color);
            btnLogin.setTextColor(Res.getColor(R.color.text_loading_gray));
        }
    }

}
