package com.tajiang.ceo.mess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.constant.TJCst;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.CardNumberUtil;
import com.tajiang.ceo.common.utils.ErrorCode;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.model.Bank;
import com.tajiang.ceo.model.WithdrawMethosDetail;
import com.tajiang.ceo.setting.activity.ResetPsdActivity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额
 */
public class BalanceActivity extends BaseActivity implements HttpResponseListener {

    @BindView(R.id.btn_withdraw_cash)
    Button btnWithdrawCash;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;

    private boolean isCardBind = false;   //是否绑定了银行卡

    private Bank bank;
    private String TotalIncome;
    private LoadingDialog loadingDialog;
    private WithdrawMethosDetail withdrawMethosDetail;


    @Override
    protected void initTopBar() {
        setTitle("余额");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCash();   //更新显示余额
    }

    /**
     * 判断今日是否可提现，
     * 获取结算方式
     */
    private void initBottonView() {
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {

                LogUtils.e("onSuccess");

                withdrawMethosDetail = (WithdrawMethosDetail)response.getData();

                if (withdrawMethosDetail != null && withdrawMethosDetail.getState() == true) {
                    btnWithdrawCash.setEnabled(true);
                    btnWithdrawCash.setText("提现");
                    btnWithdrawCash.setBackgroundResource(R.drawable.slt_btn_rect_green);
                } else {
                    btnWithdrawCash.setEnabled(false);
                    btnWithdrawCash.setText(withdrawMethosDetail.getWithdrawMode());
                    btnWithdrawCash.setBackgroundResource(R.drawable.shape_rect_round_gray3);
                }
            }
            @Override
            public void onFailed(BaseResponse response) {
                LogUtils.e("onFailed");
            }
            @Override
            public void onFinish() {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        }).presentInformation();
    }

    /**
     * 更新完银行卡返回刷新页面
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getBoolean("is_update_success") == true) {
                    new HttpHandler(this).getCeoUserIdBank();//是否绑定了银行卡
                }
            }
        }

    }

    /**
     * 获取提现金额
     */
    private void updateCash() {
        loadingDialog.show();
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                TotalIncome = response.getData().toString();
                BalanceActivity.this.tvBalance.setText(TotalIncome);
            }

            @Override
            public void onFailed(BaseResponse response) {

            }

            @Override
            public void onFinish() {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        }).getBalance();
    }

    /**
     * CEO用户每月1号为提现日
     * 非CEO用户每月1号和16号为提现日
     */
//    private void initButtonEnable() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//        int integer_roles = UserUtils.getUser().getRoles();
//
//        switch (integer_roles) {
//            case TJApp.RULES_SCHOOL_CEO:    //CEO用户每月1号为提现日
//                if (day != 1) {
//                    btnWithdrawCash.setClickable(false);
//                    btnWithdrawCash.setText("每月1号为提现日");
//                    btnWithdrawCash.setBackgroundResource(R.drawable.shape_rect_round_gray3);
//                } else {
//                    btnWithdrawCash.setClickable(true);
//                    btnWithdrawCash.setText("提现");
//                    btnWithdrawCash.setBackgroundResource(R.drawable.slt_btn_rect_green);
//                }
//                break;
//            default: //非CEO用户每月1号和16号为提现日
//                if (day != 1 && day != 16) {
//                    btnWithdrawCash.setClickable(false);
//                    btnWithdrawCash.setText("每月1号和16号为提现日");
//                    btnWithdrawCash.setBackgroundResource(R.drawable.shape_rect_round_gray3);
//                } else {
//                    btnWithdrawCash.setClickable(true);
//                    btnWithdrawCash.setText("提现");
//                    btnWithdrawCash.setBackgroundResource(R.drawable.slt_btn_rect_green);
//                }
//                break;
//        }
//    }


    @Override
    protected void initData() {
        loadingDialog = new LoadingDialog(this);
        new HttpHandler(this).getCeoUserIdBank();//是否绑定了银行卡
        initBottonView();//是否可提现
    }

    //进入编辑账户界面
    @OnClick(R.id.rl_check_account)
    public void onCheckAccountClick() {

        if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
        currentTime = System.currentTimeMillis();

        Intent intent = new Intent();
        intent.putExtra("is_card_bind", isCardBind);
        intent.putExtra("withdraw_date", withdrawMethosDetail == null ? "" : withdrawMethosDetail.getSettlementMode());
        intent.putExtra("min_withdraw_cash", withdrawMethosDetail == null ? "5" : String.valueOf(withdrawMethosDetail.getMinmoney()));

        intent2ActivityWidthExtrasAndForResult(intent,AccountActivity.class,1);    }

    //查看账户明细
    @OnClick(R.id.rl_balance)
    void click() {
        Intent intent = new Intent(this, CheckActivity.class);
        intent.putExtra(CheckActivity.EXTRA_BALANCE, TotalIncome);
        startActivity(intent);
    }

    //进入提现界面(检查余额是否超过最低提现额度)
    @OnClick(R.id.btn_withdraw_cash)
    public void onWithdrawCashClick() {

        if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
        currentTime = System.currentTimeMillis();

        if (!checkCash()) {
            ToastUtils.showShort("5元为最低提现金额");
        } else {
            if (isCardBind == false) {
                ToastUtils.showShort("请先绑定银行卡");
                Intent intent = new Intent();
                intent.putExtra("is_card_bind", isCardBind);
                intent.putExtra("withdraw_date", withdrawMethosDetail == null ? "" : withdrawMethosDetail.getSettlementMode());
                intent.putExtra("min_withdraw_cash", withdrawMethosDetail == null ? "5" : String.valueOf(withdrawMethosDetail.getMinmoney()));

                intent2ActivityWidthExtrasAndForResult(intent, AccountActivity.class, 1);
            } else {
                checkPasswordEnable();
            }

        }
    }

    private boolean checkCash() {
        double minCash = 0;
        double currentCash = 0;
        try {
            minCash = WithdrawBalanceActivity.MIN_WITHDRAW_CASH.doubleValue();
            currentCash = Double.valueOf(TotalIncome);
        } catch (NumberFormatException e) {
            LogUtils.e(e.toString());
            return false;
        }
        if (currentCash < minCash) {
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(BaseResponse response) {
        Bank bank = (Bank) response.getData();
        if (bank != null) {
            this.bank = bank;
            if (bank.getCardNo() != null && bank.getUserName() != null) {
                tvCardNumber.setText(CardNumberUtil.formatCardNumber(bank.getCardNo()));
                isCardBind = true;
            } else {
                tvCardNumber.setText("未绑定");
                isCardBind = false;
            }
        }
    }

    @Override
    public void onFailed(BaseResponse response) {
//        if (response.getErrorCode() == ErrorCode.CODE_NO_BANK) {   //没有绑定银行卡的错误码
//            tvCardNumber.setText("未绑定");
//            isCardBind = false;
//        }
    }

    @Override
    public void onFinish() {

    }

    /**
     * 判断是否设置过提现密码
     */
    private void checkPasswordEnable() {
        loadingDialog.show();
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                Bank bank = (Bank)response.getData();
                if (bank.getPassword().equals("true")){
                    Intent intent = new Intent();
                    intent.putExtra("balance_cash", TotalIncome == null ? "0.0" : TotalIncome);
                    intent.putExtra("bank_name", bank == null ? "" : bank.getOpenBank());
                    intent2ActivityWithExtras(intent, WithdrawBalanceActivity.class);
                } else {
                    //跳转到设置提现密码的页面
                    ToastUtils.showShort("请先设置提现密码");
                    intent2Activity(ResetPsdActivity.class);
                }
            }

            @Override
            public void onFailed(BaseResponse response) {

            }

            @Override
            public void onFinish() {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        }).getCeoUserIdBank();
    }

}
