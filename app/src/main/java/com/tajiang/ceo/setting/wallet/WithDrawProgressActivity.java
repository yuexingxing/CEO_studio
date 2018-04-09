package com.tajiang.ceo.setting.wallet;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.CashUtils;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.model.AccountWithDrawInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * 提现进度
 */
public class WithDrawProgressActivity extends BaseActivity {

    @BindView(R.id.withdraw_progress_time)
    TextView tvTime1;

    @BindView(R.id.withdraw_progress_time2)
    TextView tvTime2;

    @BindView(R.id.withdraw_progress_time3)
    TextView tvTime3;

    @BindView(R.id.withdraw_progress_state)
    TextView tvState;

    @BindView(R.id.withdraw_progress_fee)
    TextView tvFee;

    @BindView(R.id.withdraw_progress_content)
    TextView tvContent;

    @BindView(R.id.withdraw_progress_icon)
    ImageView imgSuccess;

    private AccountWithDrawInfo accountWithDrawInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initTopBar() {
        setTitle("提现进度");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_with_draw_progress);
    }

    @Override
    protected void initData() {

        accountWithDrawInfo = WithdrawRecordMenuActivity.mAccountWithDrawInfo;

        tvTime1.setText(accountWithDrawInfo.getCreateTime());
        tvTime2.setText(accountWithDrawInfo.getCreateTime());
        tvFee.setText(CashUtils.changeF2Y(accountWithDrawInfo.getBalance(), -1));
        tvState.setText(accountWithDrawInfo.getState() == 0 ? "处理中" : "处理成功");

        String strContent = String.format("提现到 %s (%s) %s", accountWithDrawInfo.getBankName(), accountWithDrawInfo.getAccountNo(), accountWithDrawInfo.getAccountOwner());
        tvContent.setText(strContent);

        PostDataTools.acct_withdrawstate(this, accountWithDrawInfo.getExchangeId(), new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                JSONArray jsonArray = ((JSONObject) object).optJSONArray("data");
                if(jsonArray == null){
                    return;
                }

                JSONObject jsonObject = jsonArray.optJSONObject(0);
                int state = jsonObject.optInt("state");
                if(state == 0){
                    imgSuccess.setBackgroundResource(R.drawable.mine_withdraw);
                    tvState.setText("处理中");
                }else{
                    imgSuccess.setBackgroundResource(R.drawable.mine_withdraw_success);
                    tvState.setText("已完成");
                }
            }
        });
    }

}
