package com.tajiang.ceo.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.model.Shop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择食堂
 * Created by work on 2016/6/24.
 */
public class ChooseMessActivity extends BaseActivity {

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

        setTitle(R.string.msg_choose_mess);
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_choose_mess);
    }

    @Override
    protected void initData() {

        listView.setAdapter(commonAdapter = new CommonAdapter<Shop>(ChooseMessActivity.this, dataList, R.layout.item_layout_choose_mess) {

            @Override
            public void convert(ViewHolder helper, final Shop item) {

                ImageView imgIcon = helper.getView(R.id.choose_mess_img);
                TJApp.getImageLoader().displayImage(item.getShopImage(), imgIcon, TJApp.getInstance().getOptions(), null);
                helper.setText(R.id.choose_mess_name, item.getShopName());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Shop item = dataList.get(arg2);

                Intent intent = new Intent();
                intent.putExtra("store_name", item.getShopName());
                intent.putExtra("store_id", item.getShopId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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
