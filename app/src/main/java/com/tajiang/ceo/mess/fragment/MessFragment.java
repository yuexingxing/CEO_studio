/*
 * Copyright (c) 2016
 * 杭州塔酱科技有限公司 版权所有
 */

package com.tajiang.ceo.mess.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.constant.TJCst;
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.SharedPreferencesUtils;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.common.widget.dialog.CallDialog;
import com.tajiang.ceo.login.activity.LoginActivity;
import com.tajiang.ceo.mess.activity.BalanceActivity;
import com.tajiang.ceo.mess.activity.DeliverManageActivity;
import com.tajiang.ceo.mess.activity.DormInfoActivity;
import com.tajiang.ceo.mess.activity.MessSelectActivity;
import com.tajiang.ceo.mess.activity.SetMessTimeActivity;
import com.tajiang.ceo.mess.adapter.HeaderAndFooterAdapter;
import com.tajiang.ceo.model.SalesStatistics;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 配送管理
 */

public class MessFragment
        extends BaseFragment
        implements HttpResponseListener, HeaderAndFooterAdapter.OnItemClickListener {

    public static final int ITEM_SPAN_COUNT = 3;  //RecyclerView 每一行Item数量

    @BindView(R.id.rv_root_content)
    RecyclerView mRecyclerView;

    private NumberFormat numberFormat = NumberFormat.getInstance();
    private SalesStatistics salesStatistics;

    private LoadingDialog loadingDialog;
    private HeaderAndFooterAdapter mAdapter;

    private View headView;
    private TextView headTvSchoolName;
    private TextView headTvOrderCount;
    private TextView headTvUserName;
    private TextView headTvIncomeToday;
    private TextView headTvIncomeTotal;

    @Override
    protected void initTopBar() {
        setTitle("配送收入");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.fragment_mess);
        initHeadView();
    }

    private void initHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mess_header, null);
        headTvSchoolName = (TextView) headView.findViewById(R.id.tv_school_name);
        headTvUserName = (TextView) headView.findViewById(R.id.tv_user_name);
        headTvOrderCount = (TextView) headView.findViewById(R.id.tv_order);
//        headTvIncomeToday = (TextView) headView.findViewById(R.id.tv_income_today);
//        headTvIncomeTotal = (TextView) headView.findViewById(R.id.tv_income_total);
    }

    @Override
    protected void initData() {
        loadingDialog = new LoadingDialog(getActivity());
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new HeaderAndFooterAdapter(getActivity().getApplicationContext(), headView, null,
                UserUtils.getUser().getRoleType(), ITEM_SPAN_COUNT);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(mAdapter.getLayoutManager());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        this.salesStatistics = (SalesStatistics) response.getData();
        updateStatistics();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    //刷新今日有效订单，今日有效提。。。
    @Override
    public void reFreshCurrentPageData() {
//        loadingDialog.show();
        super.reFreshCurrentPageData();
        new HttpHandler(this).orderToday();
    }

    private void updateStatistics() {
        headTvSchoolName.setText(UserUtils.getUser().getSchoolName());
        headTvUserName.setText(UserUtils.getUser().getRealName());
        headTvOrderCount.setText("今日有效订单: " + String.valueOf(salesStatistics.getOrderCount() == null ? 0 : salesStatistics.getOrderCount()));

//        headTvIncomeToday.setText((salesStatistics.getTodayIncome() == null || salesStatistics.getTodayIncome().doubleValue() == 0) ? "0.00" :
//                String.valueOf(salesStatistics.getTodayIncome().setScale(2, BigDecimal.ROUND_DOWN).toString()));
//
//        headTvIncomeTotal.setText((salesStatistics.getSumMoney() == null || salesStatistics.getSumMoney().doubleValue() == 0) ? "0.00" :
//                String.valueOf(salesStatistics.getSumMoney().setScale(2, BigDecimal.ROUND_DOWN).toString()));
    }

    /**
     * 此处添加自己的九宫格点击事件
     *
     * @param ItemId
     */
    @Override
    public void onItemClickListener(int ItemId) {
        switch (ItemId) {
            case R.string.account_balance:
                intent2Activity(BalanceActivity.class);
                break;
            case R.string.mess_management:
                intent2Activity(MessSelectActivity.class);
                break;
            case R.string.deliver_schedule:
                //TODO........学校配送时间.........
                intent2Activity(SetMessTimeActivity.class);
                break;
            case R.string.dorm_management:
                intent2Activity(DormInfoActivity.class);
                break;
            case R.string.message_notification:
                break;
            case R.string.contact_service:
                CallDialog callDialog = new CallDialog(getActivity(), TJCst.CUSTOMER_SERVICES_PHONE);
                callDialog.show();
                break;
            case R.string.deliver_manage:
                intent2Activity(DeliverManageActivity.class);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.btn_quit)
    public void onQuitClick() {
        SharedPreferencesUtils.remove(SharedPreferencesUtils.USER_LOGIN_INFOR);
        intent2Activity(LoginActivity.class);
        MessFragment.this.getActivity().finish();
    }
}

