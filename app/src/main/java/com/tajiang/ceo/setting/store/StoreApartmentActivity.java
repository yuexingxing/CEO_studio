package com.tajiang.ceo.setting.store;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商家配送范围
 */
public class StoreApartmentActivity extends BaseActivity {

    @BindView(R.id.lv_public)
    ListView listViewZone;

    @BindView(R.id.lv_public_2)
    ListView listViewBuilding;

    private List<ApartmentZone> zoneList = new ArrayList<ApartmentZone>();
    private ArrayList<Building> buildingList = new ArrayList<Building>();

    private CommonAdapter<ApartmentZone> zoneAdapter;
    private CommonAdapter<Building> buildingAdapter;

    private int currentPos = 0;
    private String store_id;

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
        setContentView(R.layout.activity_store_apartment);
        setTitle("配送范围");

        enableRightText("保存", new OnRightClick() {

            @Override
            public void rightClick() {

                save();
            }
        });
    }

    @Override
    protected void initData() {

        store_id = getIntent().getStringExtra("store_id");

        listViewZone.setDivider(null);
        listViewZone.setAdapter(zoneAdapter = new CommonAdapter<ApartmentZone>(this, zoneList, R.layout.item_setting_zone) {
            @Override
            public void convert(ViewHolder helper, ApartmentZone item) {

                helper.setText(R.id.item_setting_zone_name, item.getName());
            }
        });

        listViewBuilding.setAdapter(buildingAdapter = new CommonAdapter<Building>(this, buildingList, R.layout.item_setting_building) {
            @Override
            public void convert(ViewHolder helper, Building item) {

                helper.setText(R.id.item_setting_building_name, item.getName());

                if(item.getState() == 1){
                    helper.setImageResource(R.id.item_setting_building_img, R.drawable.set_up_select);
                }else{
                    helper.setImageResource(R.id.item_setting_building_img, 0);
                }
            }
        });

        listViewZone.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(currentPos == position){
                    return;
                }

                //对前面修改的数据重新赋值
                ArrayList<Building> list = new ArrayList<Building>();
                list.addAll(buildingList);
                zoneList.get(currentPos).setList(list);

                currentPos = position;
                buildingList.clear();
                buildingList.addAll(zoneList.get(position).getList());
                buildingAdapter.notifyDataSetChanged();
            }
        });

        listViewBuilding.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Building building = buildingList.get(position);
                if (building.getState() == 1){
                    building.setState(0);
                }else{
                    building.setState(1);
                }

                buildingAdapter.notifyDataSetChanged();
            }
        });
    }

    protected  void onResume(){
        super.onResume();

        String store_id = getIntent().getStringExtra("store_id");
        PostDataTools.shop_apartment(this, store_id, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                zoneList.addAll((Collection<? extends ApartmentZone>) object);
                if(zoneList.size() >= 1){
                    currentPos = 0;
                    buildingList.addAll(zoneList.get(0).getList());
                    buildingAdapter.notifyDataSetChanged();
                }

                zoneAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 保存
     */
    private void save(){

        int len = zoneList.size();

        if(len == 0){
            ToastUtils.showShort("当前没有数据");
            return;
        }

        StringBuffer sb = new StringBuffer();
        for(int i=0; i<len; i++){

            ArrayList<Building> buildingList = new ArrayList<Building>();
            buildingList = zoneList.get(i).getList();
            for(int k=0; k<buildingList.size(); k++){

                Building building = buildingList.get(k);
                if(building.getState() == 1){

                    if(TextUtils.isEmpty(sb.toString())){
                        sb.append(building.getId());
                    }else{
                        sb.append(",").append(building.getId());
                    }
                }
            }
        }

        if (TextUtils.isEmpty(sb.toString())){
            sb.append("-1");
        }

        PostDataTools.modify_shop_apartment(this, store_id, sb.toString(), new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                if (flag){
                    zoneList.clear();
                    buildingList.clear();
                    zoneList = null;
                    buildingList = null;
                    finish();
                }
            }
        });

    }

}
