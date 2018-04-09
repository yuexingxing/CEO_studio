package com.tajiang.ceo.mess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.mess.adapter.DormListAdapter;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Dorm;
import com.tajiang.ceo.model.User;

import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 送餐寝室
 * Created by Administrator on 2016/5/10.
 */
public class DormInfoActivity extends BaseActivity implements HttpResponseListener{

    @BindView(R.id.list_dorm_selected)
    RecyclerView listDormSelected;
    @BindView(R.id.btn_add_dorm)
    Button btnAddDorm;

    private DormListAdapter mDormListAdapter;
    private LoadingDialog dialog;


    @Override
    protected void initTopBar() {
        setTitle("送餐楼栋");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_dorminfo);
    }

    @Override
    protected void initData() {
        dialog = new LoadingDialog(this);
        initMessList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();
            if (bundle.getBoolean("is_editing_finished") == true) {

                // 并通知首页订单列表的下拉楼栋数据进行重新刷新楼栋数据
                Intent intent = new Intent("modify_apartment_info");
                intent.putExtra("new_message","modify_apartment_info");
                this.sendBroadcast(intent);

                // 编辑成功后返回刷新整个List数据,
                new HttpHandler(new HttpResponseListener() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        List<ApartmentZone>  dormList = (List<ApartmentZone>) response.getData();
                        //通知刷新整个列表数据
                        mDormListAdapter.notifyAllDataChanged(dormList);
                    }

                    @Override
                    public void onFailed(BaseResponse response) {

                    }

                    @Override
                    public void onFinish() {

                    }
                }).getSchoolZonesAparment();
            }
        }
    }

    @OnClick(R.id.btn_add_dorm)
    void onClick() {
        if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
        currentTime = System.currentTimeMillis();
        intent2ActivityForResult(AddDormActivity.class, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void initMessList() {
        //TODO...获取食堂列表请求...
        dialog.show();
        new HttpHandler(this).getSchoolZonesAparment();
    }

    @Override
    public void onSuccess(BaseResponse response) {
        List<ApartmentZone>  dormList = (List<ApartmentZone>) response.getData();
        mDormListAdapter = new DormListAdapter(dormList,DormInfoActivity.this);
        listDormSelected.setLayoutManager(new LinearLayoutManager(DormInfoActivity.this));
        listDormSelected.setAdapter(mDormListAdapter);

    }

    @Override
    public void onFailed(BaseResponse response) {

    }

    @Override
    public void onFinish() {
        dialog.dismiss();
    }
}
