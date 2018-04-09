package com.tajiang.ceo.setting.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.model.Shop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 设置--档口列表
 */
public class StoreListActivity extends BaseActivity {

    @BindView(R.id.lv_public)
    ListView listView;

    private CommonAdapter<Shop> commonAdapter;
    private List<Shop> dataList = new ArrayList<Shop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {

    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_store_list);
    }

    @Override
    protected void initData() {

        setTitle("档口列表");

        listView.setAdapter(commonAdapter = new CommonAdapter<Shop>(this, dataList, R.layout.item_layout_store_list) {

            @Override
            public void convert(ViewHolder helper, final Shop item) {

                helper.setText(R.id.item_store_list_name, item.getShopName());

                if(item.getBussState() == 1){
                    helper.setText(R.id.item_store_list_status, "营业中");
                }else{
                    helper.setText(R.id.item_store_list_status, "停止营业");
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Shop item = dataList.get(arg2);

                Intent intent = new Intent(StoreListActivity.this, StoreMenuActivity.class);
                intent.putExtra("store_name", item.getShopName());
                intent.putExtra("store_id", item.getShopId());
                intent.putExtra("state", item.getBussState());
                startActivity(intent);
            }
        });

    }

    protected void onResume(){
        super.onResume();

        PostDataTools.shop_list(this, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                dataList.clear();
                dataList.addAll((List<Shop>) object);
                commonAdapter.notifyDataSetChanged();
            }
        });
    }

}
