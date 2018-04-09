package com.tajiang.ceo.setting.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置提现账号
 */
public class AddWithDrawAccountActivity extends BaseActivity {

    @BindView(R.id.add_withdraw_name)
    EditText edtName;

    @BindView(R.id.add_withdraw_type)
    TextView tvType;

    @BindView(R.id.add_withdraw_id)
    EditText edtId;

    private int bankId;//  1-微信 2-支付宝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initTopBar() {
        setTitle("添加");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_add_with_draw_account);
    }

    @Override
    protected void initData() {

        bankId = getIntent().getIntExtra("bankId", 0);
        if(bankId == 1){
            tvType.setText("微信");
        }else{
            tvType.setText("支付宝");
        }
    }

    @OnClick(R.id.add_withdraw_commit)
    public void commit(){

        String name = edtName.getText().toString();
        String id = edtId.getText().toString();

        if(TextUtils.isEmpty(name)){
            ToastUtils.showShort("姓名不能为空");
            return;
        }

        if(TextUtils.isEmpty(id)){
            ToastUtils.showShort("账号不能为空");
            return;
        }

        PostDataTools.acct_setwithdraw(this, name, id, bankId, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                if(flag){
                    finish();
                }
            }
        });
    }
}
