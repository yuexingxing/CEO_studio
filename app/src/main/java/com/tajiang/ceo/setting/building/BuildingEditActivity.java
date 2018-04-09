package com.tajiang.ceo.setting.building;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.setting.adapter.BuildingEditAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑宿舍楼
 */
public class BuildingEditActivity extends BaseActivity {

    @BindView(R.id.building_edit_name)
    TextView tvName;

    @BindView(R.id.lv_public)
    ListView listView;

    BuildingEditAdapter mAdapter;
    List<Building> dataList = new ArrayList<Building>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {

        setTitle("编辑楼栋");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_building_edit);
    }

    @Override
    protected void initData() {

        ApartmentZone apartmentZone = BuildingMenuActivity.mAparentZone;
        tvName.setText(apartmentZone.getName());
        dataList.addAll(apartmentZone.getList());

        mAdapter = new BuildingEditAdapter(this, dataList, new BuildingEditAdapter.OnBottomClickListener(){

            @Override
            public void onBottomClick(View v, int position) {

                Building building = dataList.get(position);
                if(v.getId() == R.id.item_setting_building_edit_icon){

                    if (building.getState() == 0){
                        building.setState(1);
                    }else{
                        building.setState(0);
                    }

                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        listView.setDivider(null);
        listView.setAdapter(mAdapter);
    }

    @OnClick(R.id.building_edit_save)
    public void save(){


        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<dataList.size(); i++){

            Building building = dataList.get(i);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("apartmentId", building.getId());
                jsonObject.put("state", building.getState() == 0 ? 0 : 1);

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("apartment", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostDataTools.apartment_state(this, jsonObject, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                if (flag){
                    dataList.clear();
                    dataList = null;
                    finish();
                }
            }
        });
    }

}
