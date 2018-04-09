package com.tajiang.ceo.setting.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.CashUtils;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.model.AccountInfo;
import com.tajiang.ceo.model.User;
import com.tajiang.ceo.setting.activity.FirstSetPayPsdActivity;
import com.tajiang.ceo.setting.activity.PaySettingActivity;
import com.tajiang.ceo.setting.wallet.certification.CertificationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包主界面
 * 2 用户使用余额支付时，判断acct_user.active_state=0未激活时（或判断pay_password=空）时，提示设置支付密码
 * 3 设置支付密码成功时， 置acct_user.active_state=1已激活
 * 4 提现时，判断是否实名user_base.certified，如未实名，跳转到实名认证页面
 * 5 提现时，如果已实名，再判断是否激活（或交易密码是否为空），只有已实名+已激活（或已设置交易密码）才可以提现
 */
public class WalletMenuActivity extends BaseActivity {

    @BindView(R.id.wallet_menu_balance)
    TextView tvBalance;

    private AccountInfo accountInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("我的钱包");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_wallet_menu);
    }

    @Override
    protected void initData() {

    }

    protected void onResume(){
        super.onResume();

        PostDataTools.acct_info(this, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                accountInfo = (AccountInfo) object;
                tvBalance.setText(CashUtils.changeF2Y(accountInfo.getBalance(), 1));
            }
        });
    }

    //支付设置
    @OnClick(R.id.wallet_menu_pay_setting)
    public void pay_setting(){

        Intent intent = new Intent(this, PaySettingActivity.class);
        startActivity(intent);
    }

    //充值
    @OnClick(R.id.wallet_menu_recharge)
    public void recharge(){

        Intent intent = new Intent(this, RechargeMenuActivity.class);
        startActivity(intent);
    }

    //提现
    @OnClick(R.id.wallet_menu_withdraw)
    public void withdraw(){

        User user = UserUtils.getUser();
        if(user.getCertified() == 0){
            Intent intent = new Intent(this, CertificationActivity.class);
            startActivity(intent);
            return;
        }

        if(accountInfo.getActiveState() != 1){

            Intent intent = new Intent(this, FirstSetPayPsdActivity.class);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(this, WithDrawMenuActivity.class);
        intent.putExtra("accountId", accountInfo.getAcctId());
        intent.putExtra("payPwd", accountInfo.getPayPassword());
        intent.putExtra("balance", accountInfo.getBalance());
        startActivity(intent);
    }
}
