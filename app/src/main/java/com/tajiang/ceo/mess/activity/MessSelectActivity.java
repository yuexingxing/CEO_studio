package com.tajiang.ceo.mess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import java.util.*;

import com.google.gson.Gson;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.SharedPreferencesUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.mess.adapter.MessListAdapter;
import com.tajiang.ceo.model.Store;
import com.tajiang.ceo.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 食堂列表
 * Created by Administrator on 2016/5/10.
 */
public class MessSelectActivity extends BaseActivity implements HttpResponseListener{

    @BindView(R.id.list_mess_selected)
    RecyclerView listMessSelected;

    private MessListAdapter mAdapter;
    private LoadingDialog loadingDialog;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mess_select);
//        ButterKnife.bind(this);
//    }

    @Override
    protected void initTopBar() {
        setTitle("食堂");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_mess_select);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        loadingDialog = new LoadingDialog(this);
        updateMessList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getBoolean("is_mess_info_changed") == true) {
                    updateMessList();
                }
            }
        }
    }

    /**
     * 刷新食堂列表
     */
    private void updateMessList() {
        mAdapter = new MessListAdapter(new ArrayList<Store>());
        listMessSelected.setLayoutManager(new LinearLayoutManager(MessSelectActivity.this));
        listMessSelected.setAdapter(mAdapter);
        loadingDialog.show();
        new HttpHandler(this).getStores(UserUtils.getUser().getSchoolId());
    }



    @Override
    public void onSuccess(BaseResponse response) {
        List<Store> storeList = (List<Store>) response.getData();
        mAdapter.updateDatas(storeList);
//        mAdapter = new MessListAdapter(storeList);
//        listMessSelected.setLayoutManager(new LinearLayoutManager(MessSelectActivity.this));
//        listMessSelected.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
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
}
