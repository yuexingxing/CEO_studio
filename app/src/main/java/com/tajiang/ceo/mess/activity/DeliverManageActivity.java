package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.mess.adapter.DeliverPeopleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admins on 2017/3/20.
 */

public class DeliverManageActivity extends BaseActivity {

    @BindView(R.id.rv_deliver_people)
    RecyclerView rvDeliverPeople;

    private DeliverPeopleAdapter adapter;

    @Override
    protected void initTopBar() {
        setTitle("配送员管理");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_deliver_manage);
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        list.add("asd");
        list.add("a2sd");
        list.add("as3d");
        adapter = new DeliverPeopleAdapter(list);
        rvDeliverPeople.setLayoutManager(new LinearLayoutManager(this));
        rvDeliverPeople.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
