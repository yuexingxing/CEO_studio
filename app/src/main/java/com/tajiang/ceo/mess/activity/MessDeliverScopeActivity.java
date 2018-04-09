package com.tajiang.ceo.mess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.mess.adapter.ApartmentAdapter;
import com.tajiang.ceo.mess.adapter.ApartmentBuildingAdapter;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.model.Dorm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 食堂配送范围设置(选择需要配送的楼栋，保存)
 * Created by Admins on 2016/10/14.
 */
public class MessDeliverScopeActivity
        extends
            BaseActivity
        implements
            ApartmentAdapter.OnItemZoneClickListener,
            ApartmentBuildingAdapter.OnItemBuildingClickListener {

    private static final int MY_RESULT_CODE = 1;

    @BindView(R.id.tv_add_deliver_dorm)
    TextView tvAddDeliverDorm;
    @BindView(R.id.rv_apartment)
    RecyclerView rvApartment;
    @BindView(R.id.rv_building_apartment)
    RecyclerView rvBuildingApartment;
    @BindView(R.id.ll_root_content)
    LinearLayout llRootContent;

    private boolean isGetApartmentFinished = false;
    private String storeId;

    private ApartmentAdapter mZonesAdapter;     //宿舍区
    private ApartmentBuildingAdapter mBuildingAdapter;  //对于楼栋
    private LoadingDialog loadingDialog;
    private List<String> buildingIdList;      //存放该食堂配送范围内的楼栋ID

    @Override
    protected void initTopBar() {
        setTitle("配送范围");
        enableRightText("保存", new OnRightClick() {
            @Override
            public void rightClick() {
                requestUpdateStoreApartment();
            }
        });
    }



    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_mess_deliver_scope_layout);
    }

    @Override
    protected void initData() {
        storeId        = getIntent().getStringExtra("store_id");
        buildingIdList = new ArrayList<>();
        loadingDialog  = new LoadingDialog(this);
        initAdapterAndList();
        initSchoolApartment();
    }

    /**
     * 更新食堂配送范围（楼栋）
     */
    private void requestUpdateStoreApartment() {
        loadingDialog.show();
        String strJson = new Gson().toJson(buildingIdList);
        LogUtils.e("配送范围ID: json ---> " + strJson);
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                if ((Boolean)response.getData() == true) {
                    ToastUtils.showShort("保存成功");
                    MessDeliverScopeActivity.this.finish();
                } else {
                    ToastUtils.showShort("保存失败");
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
        }).updateStoreAparment(storeId, strJson);
    }

    private void initAdapterAndList() {
        mZonesAdapter    = new ApartmentAdapter(this, new ArrayList<ApartmentZone>());
        mBuildingAdapter = new ApartmentBuildingAdapter(this, new ArrayList<Building>(), buildingIdList);

        mZonesAdapter.setOnItemClickListener(this);
        mBuildingAdapter.setOnItemBuildingClickListener(this);
        //宿舍区列表
        rvApartment.setLayoutManager(new LinearLayoutManager(this));
        rvApartment.setAdapter(mZonesAdapter);
        //楼栋列表
        rvBuildingApartment.setLayoutManager(new LinearLayoutManager(this));
        rvBuildingApartment.setAdapter(mBuildingAdapter);
    }

    /**
     * 获取学校宿舍区和楼栋
     */
    private void initSchoolApartment() {
        loadingDialog.show();
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                List<ApartmentZone> list = (List<ApartmentZone>) response.getData();
                if (list != null && list.size() < 1) {
                    MessDeliverScopeActivity.this.llRootContent.setVisibility(View.GONE);
                } else {
                    MessDeliverScopeActivity.this.llRootContent.setVisibility(View.VISIBLE);
                    mZonesAdapter.updateAllDataSetChanged(list);
                    mBuildingAdapter.updateAllDataSetChanged(list.get(0).getList());
                    isGetApartmentFinished = true;
                }
            }

            @Override
            public void onFailed(BaseResponse response) {

            }

            @Override
            public void onFinish() {
                if (isGetApartmentFinished) {
                    isGetApartmentFinished = false;
                    getDeliveredBuilding();
                } else {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
            }
        }).getSchoolZonesAparment();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle.getBoolean("is_editing_finished") == true) {
                // 并通知首页订单列表的下拉楼栋数据进行重新刷新楼栋数据
                Intent intent = new Intent("modify_apartment_info");
                intent.putExtra("new_message", "modify_apartment_info");
                this.sendBroadcast(intent);
                // 编辑成功后返回刷新整个List数据,
                initSchoolApartment();
            }
        }
    }

    @OnClick(R.id.tv_add_deliver_dorm)
    public void onClick() {
        intent2ActivityForResult(AddDormActivity.class, MY_RESULT_CODE);
    }

    /**
     * 响应左边宿舍区域，刷新 右边楼栋数据
     *
     * @param selectedApartmentZone
     * @param selectedItemPosition
     */
    @Override
    public void onItemZoneClick(ApartmentZone selectedApartmentZone, int selectedItemPosition) {
        mBuildingAdapter.updateAllDataSetChanged(selectedApartmentZone.getList());
    }

    /**
     * @param selectedBuilding
     * @param selectedItemPosition
     */
    @Override
    public void onItemBuildingClick(Building selectedBuilding, int selectedItemPosition) {
        if (buildingIdList != null) {
            if (buildingIdList.contains(selectedBuilding.getId())) {
                buildingIdList.remove(selectedBuilding.getId());
            } else {
                buildingIdList.add(selectedBuilding.getId());
            }
        }
    }

    /**
     * 获取当前食堂配送楼栋请求
     * 需要更新的局部变量： buildingIdList； mBuildingAdapter
     */
    public void getDeliveredBuilding() {
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                List<String> list = (List<String>) response.getData();
                if (list.size() > 0) {
                    buildingIdList.clear();
                    buildingIdList.addAll(list);
                    mBuildingAdapter.notifyDataSetChanged();
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
        }).getStoreAparment(storeId);
    }
}
