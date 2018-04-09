package com.tajiang.ceo.mess.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputFilter;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.CashInputFilter;
import com.tajiang.ceo.common.utils.CashUtils;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.MD5Utils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.mess.dialog.WithDrawCashDialog;
import com.tajiang.ceo.model.Bank;
import com.tajiang.ceo.setting.activity.ConfirmPsdActivity;
import com.tajiang.ceo.setting.activity.ResetPsdActivity;

import java.math.BigDecimal;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现
 * Created by work on 2016/6/24.
 */
public class WithdrawBalanceActivity extends BaseActivity {

    public static final BigDecimal DefaultCashFee = new BigDecimal(2.0);  //提现银行收取的默认手续费
    public static final BigDecimal MIN_WITHDRAW_CASH = new BigDecimal(5.0);   //5元为最低提现金额

    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.et_withdraw_money)
    EditText etWithdrawMoney;
    @BindView(R.id.et_withdraw_password)
    EditText etWithdrawPassword;
    @BindView(R.id.tv_withdraw_money_limited)
    TextView tvWithdrawMoneyLimited;
    @BindView(R.id.tv_open_bank)
    TextView tvOpenBank;

    private BigDecimal withdrawCash;

    private Double limitedCash = 0.0;//提现金额上限
    private LoadingDialog loadingDialog;

    private void initMyView() {
        loadingDialog = new LoadingDialog(this);
        if (getIntent().hasExtra("bank_name")) {
            tvOpenBank.setText(getIntent().getStringExtra("bank_name"));
        }
        if (getIntent().hasExtra("balance_cash")) {
            String money = getIntent().getStringExtra("balance_cash");
            tvWithdrawMoneyLimited.setText(money);
            limitedCash = Double.valueOf(money);
        }
    }

    @Override
    protected void initTopBar() {
        setTitle("提现");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_withdraw_balance);
        ButterKnife.bind(this);
        initMyView();
        initListener();
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmClick() {
        // 补足金额格式
        etWithdrawMoney.setText(CashUtils.CheckCashNumber(etWithdrawMoney.getText().toString()));
        if (isWithdrawCashEnable() == true) {
            checkPasswordEnable();
        }
    }
    private void initListener() {
        /**
         * 限制小数点后面输入两位小数
         */
        InputFilter[] filters = { new CashInputFilter() };
        etWithdrawMoney.setFilters(filters);

        etWithdrawMoney.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    etWithdrawMoney.setText(CashUtils.CheckCashNumber(etWithdrawMoney.getText().toString()));
                }
            }
        });
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
                    WithdrawBalanceActivity.this.withdrawCash = new BigDecimal(
                            Double.valueOf(etWithdrawMoney.getText().toString()));
                    showCashDialog();
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

    private boolean isWithdrawCashEnable() {
        try {
            if (etWithdrawMoney.getText().toString().equals("")) {
                ToastUtils.showShort("请输入提现金额");
                return false;
            }
            Double cash = Double.valueOf(etWithdrawMoney.getText().toString());
            if (cash > limitedCash) {
                ToastUtils.showShort("余额不足，请重新输入");
                return false;
            } else if (cash < MIN_WITHDRAW_CASH.doubleValue()) {
                ToastUtils.showShort("5元为最低提现金额");
                return false;
            }
            if (etWithdrawPassword.getText().toString().equals("")) {
                ToastUtils.showShort("请输入提现密码");
                return false;
            }
        } catch (NumberFormatException e) {
            LogUtils.e(e.toString());
            ToastUtils.showShort("请输入正确金额格式！");
            return false;
        }
        return true;
    }

    /**
     *
     * 显示确认对话框
     * */
    private void showCashDialog() {
        WithDrawCashDialog dialog = new WithDrawCashDialog(this);
        dialog.setWithdrawCash(CashUtils.getCashWith2Point(
                new BigDecimal(withdrawCash.doubleValue() - DefaultCashFee.doubleValue())));
        dialog.setRequestListener(new WithDrawCashDialog.RequestListener() {
            @Override
            public void doPostRequest() {
                PostCashRequest();  //提现请求
            }
        });
        dialog.show();
    }

    private void PostCashRequest() {
        loadingDialog.show();
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                if ((Boolean) response.getData() == true) {
                    Intent intent = new Intent();
                    intent.putExtra("withdraw_cash",CashUtils.getCashWith2Point(
                            new BigDecimal(withdrawCash.doubleValue() - DefaultCashFee.doubleValue())));
                    intent2ActivityWithExtras(intent, WithdrawProgressActivity.class);
                    WithdrawBalanceActivity.this.finish();
                } else {
                    ToastUtils.showShort(response.getMoreInfo());
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
        }).addApply(Float.valueOf(CashUtils.getCashWith2Point(
                new BigDecimal(withdrawCash.doubleValue()))),
                MD5Utils.StringMD5(etWithdrawPassword.getText().toString()));
    }

}
