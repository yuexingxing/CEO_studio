package com.tajiang.ceo.setting.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.dialog.WithdrawTipDialog;
import com.tajiang.ceo.common.utils.CashUtils;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.model.AccountWithDrawInfo;
import com.tajiang.ceo.setting.activity.ConfirmPsdActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithDrawMenuActivity extends BaseActivity {

    @BindView(R.id.with_draw_fee)
    EditText edtFee1;

    @BindView(R.id.with_draw_fee2)
    TextView tvFee2;

    @BindView(R.id.with_draw_zhifubao)
    ImageView imgAli;

    @BindView(R.id.with_draw_weixin)
    ImageView imgWeixin;

    private int withDrawType = 2;//默认1为支付宝提现
    private int balance;//可提现金额

    private int withdrawAmount;

    private AccountWithDrawInfo accountWeiXin;//微信提现账户信息
    private AccountWithDrawInfo accountAli;//支付宝提现账户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    protected void onResume(){
        super.onResume();

        PostDataTools.acct_withdrawacct(WithDrawMenuActivity.this, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                List<AccountWithDrawInfo> list = (List<AccountWithDrawInfo>) object;
                for(int i=0; i<list.size(); i++){

                    AccountWithDrawInfo info = list.get(i);
                    if(info.getBankId() == 1){
                        accountWeiXin = info;
                    }else if(info.getBankId() == 2){
                        accountAli = info;
                    }
                }
            }
        });
    }

    @Override
    protected void initTopBar() {

        setTitle("提现");
        enableRightText("提现记录", new OnRightClick() {

            @Override
            public void rightClick() {

                Intent intent = new Intent(WithDrawMenuActivity.this, WithdrawRecordMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_with_draw);
    }

    @Override
    protected void initData() {

        zhifubao();

        Intent intent = getIntent();
        balance = intent.getIntExtra("balance", 0);
        tvFee2.setText(CashUtils.changeF2Y(balance, 1));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0x0011 && resultCode == RESULT_OK) {

            String payPwd = data.getStringExtra("payPwd");
            int accountId = withDrawType == 1 ? accountWeiXin.getAccountId() : accountAli.getAccountId();
            PostDataTools.acct_withdraw(this, accountId, payPwd, withdrawAmount, new PostDataTools.DataCallback() {

                @Override
                public void callback(boolean flag, String message, Object object) {

                    if(flag){
                        finish();
                    }
                }
            });
        }
    }

    //选择支付宝
    @OnClick(R.id.with_draw_zhifubao_layout)
    public void zhifubao(){

        withDrawType = 2;
        imgAli.setBackgroundResource(R.drawable.order_select);
        imgWeixin.setBackgroundResource(R.drawable.order_unselected);
    }

    //选择微信
    @OnClick(R.id.with_draw_weixin_layout)
    public void weixin(){

        withDrawType = 1;
        imgWeixin.setBackgroundResource(R.drawable.order_select);
        imgAli.setBackgroundResource(R.drawable.order_unselected);
    }

    @OnClick(R.id.with_draw_zhifubao_layout_top)
    public void zhifubao_top(){

       Intent intent = new Intent(this, AddWithDrawAccountActivity.class);
        intent.putExtra("bankId", 2);
        startActivity(intent);
    }

    @OnClick(R.id.with_draw_weixin_layout_top)
    public void weixin_top(){

        Intent intent = new Intent(this, AddWithDrawAccountActivity.class);
        intent.putExtra("bankId", 1);
        startActivity(intent);
    }

    //确认提现
    @OnClick(R.id.with_draw_commit)
    public void commit(){

        if(TextUtils.isEmpty(edtFee1.getText().toString())){
            ToastUtils.showShort("请输入提现金额");
            return;
        }

        withdrawAmount = Integer.parseInt(edtFee1.getText().toString()) * 100;
        if(withdrawAmount == 0){
            ToastUtils.showShort("提现金额不能为0");
            return;
        }

        if(withdrawAmount > balance){
            ToastUtils.showShort("超过可提现金额");
            return;
        }

        if(withDrawType == 1 && accountWeiXin == null){
            ToastUtils.showShort("当前没有绑定微信提现账户，请点击添加");
            return;
        }

        if(withDrawType == 2 && accountAli == null){
            ToastUtils.showShort("当前没有绑定支付宝提现账户，请点击添加");
            return;
        }

        Intent intent = new Intent(WithDrawMenuActivity.this, ConfirmPsdActivity.class);
        intent.putExtra("mode", ConfirmPsdActivity.STATE_MODE_WITHDRAW_PWD);
        startActivityForResult(intent, 0x0011);
    }

    @OnClick(R.id.with_draw_tip)
    public void tip(){

        WithdrawTipDialog.showDialog(this);
    }


}
