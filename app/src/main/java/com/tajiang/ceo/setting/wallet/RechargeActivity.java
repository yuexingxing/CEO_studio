package com.tajiang.ceo.setting.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.model.ToolsInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 充值活动
 */
public class RechargeActivity extends BaseActivity {

    @BindView(R.id.lv_public_setting)
    GridView gridView;

    private CommonAdapter<ToolsInfo> commonAdapter;
    private List<ToolsInfo> dataList = new ArrayList<ToolsInfo>();

    private int currPostion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("充值");

        enableRightText("说明", new OnRightClick() {

            @Override
            public void rightClick() {

            }
        });
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_recharge);
    }

    @Override
    protected void initData() {

        ToolsInfo info = new ToolsInfo();
        info.setPrice(50);
        info.setReward(3);
        info.setState(1);
        dataList.add(info);

        ToolsInfo info2 = new ToolsInfo();
        info2.setPrice(100);
        info2.setReward(8);
        info2.setState(0);
        dataList.add(info2);

        ToolsInfo info3 = new ToolsInfo();
        info3.setPrice(200);
        info3.setReward(20);
        info3.setState(0);
        dataList.add(info3);

        gridView.setNumColumns(2);
        gridView.setAdapter(commonAdapter = new CommonAdapter<ToolsInfo>(this, dataList, R.layout.item_setting_wallet_recharge) {

            @Override
            public void convert(ViewHolder helper, ToolsInfo item) {

                helper.setText(R.id.item_recharge_name, String.format("充%s元\n" + "送%s元", item.getPrice(), item.getReward()));
                if(item.getState() == 1){
                    helper.setTextViewBackground(R.id.item_recharge_name, R.drawable.shape_radius_order_main_color);
                }else{
                    helper.setTextViewBackground(R.id.item_recharge_name, R.drawable.shape_radius_order_gray_color);
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currPostion = position;
                for(int i=0; i<dataList.size(); i++){
                    dataList.get(i).setState(0);
                }

                dataList.get(position).setState(1);
                commonAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.recharge_commit)
    public void submit(){

        Intent intent = new Intent();
        intent.putExtra("price", dataList.get(currPostion).getPrice());
        intent.putExtra("reward", dataList.get(currPostion).getReward());
        setResult(RESULT_OK, intent);
        finish();
    }

}
