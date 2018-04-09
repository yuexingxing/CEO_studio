package com.tajiang.ceo.setting.settle;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.dialog.CommonDialog;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.model.SettleDetailInfo;
import com.tajiang.ceo.model.SettleInfo;
import com.tajiang.ceo.setting.adapter.SettleDistributionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 业绩分配
 */
public class SettleDistributionActivity extends BaseActivity {

    @BindView(R.id.settle_dis_date)
    TextView tvDate;

    @BindView(R.id.settle_dis_order_number)
    TextView tvOrderNumber;

    @BindView(R.id.settle_dis_fee)
    TextView tvFee;

    @BindView(R.id.lv_public)
    ListView listView;

    SettleDistributionAdapter mAdapter;
    List<SettleDetailInfo> dataList = new ArrayList<SettleDetailInfo>();

    private SettleInfo settleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("业绩分配");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_settle_distribution);
    }

    @Override
    protected void initData() {

        listView.setDivider(null);
        mAdapter = new SettleDistributionAdapter(this, dataList, null);
        listView.setAdapter(mAdapter);

        PostDataTools.settle_list(this, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                settleInfo = (SettleInfo) object;

                if(settleInfo != null){

                    tvDate.setText(settleInfo.getSettleCycleStr());
                    tvOrderNumber.setText(settleInfo.getValidOrderQty() + "");
                    tvFee.setText(settleInfo.getDelayTotalMoney() + "");

                    dataList.clear();
                    dataList.addAll(settleInfo.getDelaySettleList());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick(R.id.settle_dis_icon)
    public void tips(){

        ToastUtils.showShort("tips");
    }

    @OnClick(R.id.settle_dis_save)
    public void save(){

        if(settleInfo == null){
            return;
        }

        CommonDialog.showDialog(this, "等等", "确认上传", "上传后不可更改，请核对后再上传", new CommonDialog.DialogCallback() {

            @Override
            public void callback(int position) {

                if (position == 1){

                    JSONArray jsonArray = new JSONArray();
                    for(int i=0; i<dataList.size(); i++){

                        SettleDetailInfo detailInfo = dataList.get(i);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("settleId", detailInfo.getSettleId());
                            jsonObject.put("payedMoney", detailInfo.getSettleMoney());

                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    PostDataTools.settle_upload(SettleDistributionActivity.this,jsonArray, settleInfo.getAssignId(), new PostDataTools.DataCallback() {

                        @Override
                        public void callback(boolean flag, String message, Object object) {

                            if(flag){
                                dataList.clear();
                                dataList = null;
                                finish();
                            }
                        }
                    });
                }
            }
        });


    }
}
