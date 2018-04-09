package com.tajiang.ceo.setting.building;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.setting.adapter.BuildingMenuAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 楼栋管理
 */
public class BuildingMenuActivity extends BaseActivity {

    @BindView(R.id.lv_public)
    ListView listView;

    BuildingMenuAdapter buildingAdapter;
    List<ApartmentZone> dataList = new ArrayList<ApartmentZone>();

    public static ApartmentZone mAparentZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {

        setTitle("楼栋管理");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_building_menu);
    }

    @Override
    protected void initData() {

        listView.setDivider(null);
        buildingAdapter = new BuildingMenuAdapter(this, dataList, new BuildingMenuAdapter.OnBottomClickListener() {

            @Override
            public void onBottomClick(View v, int position) {

                mAparentZone = dataList.get(position);
                if(v.getId() == R.id.item_layout_setting_building_menu_edit){

                    Intent intent = new Intent(BuildingMenuActivity.this, BuildingEditActivity.class);
                    startActivity(intent);
                }

            }
        });
        listView.setAdapter(buildingAdapter);
    }

    protected void onResume(){
        super.onResume();

        PostDataTools.apartment_list(this, new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {

                dataList.clear();
                dataList.addAll((Collection<? extends ApartmentZone>) object);
                buildingAdapter.notifyDataSetChanged();
            }
        });
    }

}
