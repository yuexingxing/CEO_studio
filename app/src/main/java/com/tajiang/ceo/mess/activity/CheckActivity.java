package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.mess.adapter.BillRecordListAdapter;
import com.tajiang.ceo.mess.view.CheckBillLoadMoreFooterView;
import com.tajiang.ceo.mess.view.CheckBillRefreshHeadView;
import com.tajiang.ceo.model.BillRecord;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.Pager;
import com.tajiang.ceo.model.User;
import com.tajiang.ceo.order.adapter.OrderListAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 账单流水
 */
public class CheckActivity extends BaseActivity implements HttpResponseListener, OnRefreshListener, OnLoadMoreListener {

    public static final String EXTRA_BALANCE = "extra_balance";

    @BindView(R.id.swipe_target)
    RecyclerView listBillRecord;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.swipe_refresh_header)
    CheckBillRefreshHeadView swipeRefreshHeader;
    @BindView(R.id.swipe_load_more_footer)
    CheckBillLoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout swipeToLoadLayout;

    private static final int DEFAULT_FIRST_PAGE = 1;  //首次加载首页当前Page

    private LoadingDialog dialog;

    private BillRecordListAdapter billListAdapter;
    private int currentPage;
    private User user;

    @Override
    protected void initTopBar() {
        setTitle("账单流水");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_check);
        initMyView();
        initListener();
    }

    private void initMyView() {
        tvBalance.setText(getIntent().getStringExtra(EXTRA_BALANCE));
    }

    private void initListener() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        this.dialog = new LoadingDialog(this);
        this.currentPage = DEFAULT_FIRST_PAGE;
        this.user = UserUtils.getUser();
        this.dialog.show();

//        new HttpHandler(this).getStoreOrder("26o0f", "7c", null, null, 1, 10, 1);
        new HttpHandler(this).getAmountRecord(currentPage, 15);
    }


    @Override
    public void onSuccess(BaseResponse response) {
//        Pager<BillRecord> pager = (Pager<BillRecord>) response.getData();
//        List<BillRecord> newOrderList = pager.getList();
//
//        billListAdapter = new BillRecordListAdapter(CheckActivity.this, newOrderList);
//        listBillRecord.setLayoutManager(new LinearLayoutManager(this));
//        listBillRecord.setAdapter(billListAdapter);

        Pager<BillRecord> pager = (Pager<BillRecord>) response.getData();
        List<BillRecord> list = pager.getList();

        checkAmountRecord(list);

        billListAdapter = new BillRecordListAdapter(CheckActivity.this, list);
        listBillRecord.setLayoutManager(new LinearLayoutManager(this));
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(billListAdapter);
        listBillRecord.addItemDecoration(headersDecor);
        listBillRecord.setAdapter(billListAdapter);
    }

    @Override
    public void onFailed(BaseResponse response) {

    }

    @Override
    public void onFinish() {
        dialog.dismiss();
    }

    @Override
    public void onLoadMore() {
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                Pager<BillRecord> pager = (Pager<BillRecord>) response.getData();
                List<BillRecord> newOrderList = pager.getList();
                if (newOrderList.size() > 0) {
                    setCurrentPage(currentPage + 1); //更新当前页数
                    billListAdapter.addAllAndUpdateData(newOrderList);
                    swipeLoadMoreFooter.setFootTextView("加载完毕！");
                } else {
                    swipeLoadMoreFooter.setFootTextView("已经到底咯！");
                }
            }

            @Override
            public void onFailed(BaseResponse response) {

            }

            @Override
            public void onFinish() {
                if (swipeToLoadLayout.isLoadingMore()) {
                    swipeToLoadLayout.setLoadingMore(false);
                }
            }
        }).getAmountRecord((currentPage + 1), 15);
    }

    @Override
    public void onRefresh() {
        setCurrentPage(DEFAULT_FIRST_PAGE);
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                Pager<BillRecord> pager = (Pager<BillRecord>) response.getData();
                List<BillRecord> list = pager.getList();
                checkAmountRecord(list);
                billListAdapter.updateDataSetChanged(list);
            }

            @Override
            public void onFailed(BaseResponse response) {

            }

            @Override
            public void onFinish() {
                if (swipeToLoadLayout.isRefreshing()) {
                    swipeToLoadLayout.setRefreshing(false);
                }
            }
        }).getAmountRecord(currentPage, 15);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void checkAmountRecord(List<BillRecord> newOrderList){
        if (newOrderList.size() < 1) {
            swipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            swipeToLoadLayout.setLoadMoreEnabled(true);
        }
    }
}
