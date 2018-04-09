package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.model.BillDetail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by work on 2016/7/12.
 */
public class BillDetailActivity extends BaseActivity implements HttpResponseListener {
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.tv_income_date)
    TextView tvIncomeDate;
    @BindView(R.id.tv_income_money)
    TextView tvIncomeMoney;
    @BindView(R.id.tv_reward)
    TextView tvReward;
    @BindView(R.id.tv_reward_date)
    TextView tvRewardDate;
    @BindView(R.id.tv_reward_money)
    TextView tvRewardMoney;
    @BindView(R.id.ll_content_1)
    LinearLayout llContent1;
    @BindView(R.id.ll_content_2)
    LinearLayout llContent2;

    private String amountRecordId;  //当前账单id
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd hh:mm", Locale.CHINA);
    private LoadingDialog loadingDialog;

    @Override
    protected void initTopBar() {
        setTitle("账单明细");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_bill_detail);
        llContent1.setVisibility(View.GONE);
        llContent2.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        this.loadingDialog = new LoadingDialog(this);
        this.amountRecordId = getIntent().getStringExtra("amount_record_id");
        //TODO....获取账单明细.......
        if (amountRecordId != null) {
            loadingDialog.show();
            new HttpHandler(this).getMoneyRecord(amountRecordId);
        }
    }

    private void initBillView(List<BillDetail> list) {
        if (list.size() > 0) {
            try {
                llContent1.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                BillDetail BillIncome = list.get(0);
                tvIncome.setText(BillIncome.getDescribe() == null?"":BillIncome.getDescribe());
                tvIncomeMoney.setText(String.valueOf(BillIncome.getMoney() == null ? 0 : BillIncome.getMoney()) + "元");
                calendar.setTimeInMillis(BillIncome.getCreatedAt());
                tvIncomeDate.setText(format.format(calendar.getTime()));

                if (list.size() > 1) {
                    llContent2.setVisibility(View.VISIBLE);
                    BillDetail BillReward = list.get(1);
                    tvReward.setText(BillReward.getDescribe() == null?"":BillReward.getDescribe());
                    tvRewardMoney.setText(String.valueOf(BillReward.getMoney() == null ? 0 : BillReward.getMoney()) + "元");
                    calendar.setTimeInMillis(BillReward.getCreatedAt());
                    tvIncomeDate.setText(format.format(calendar.getTime()));
                }
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        List<BillDetail> list = (List<BillDetail>) response.getData();
        initBillView(list);
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
}
