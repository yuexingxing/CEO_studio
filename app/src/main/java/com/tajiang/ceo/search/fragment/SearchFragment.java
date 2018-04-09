package com.tajiang.ceo.search.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.adapter.EmptyViewAdapter;
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.Pager;
import com.tajiang.ceo.model.SalesStatistics;
import com.tajiang.ceo.order.adapter.OrderListAdapter;
import com.tajiang.ceo.order.view.OrderLoadMoreFooterView;
import com.tajiang.ceo.order.view.OrderRefreshHeadView;
import com.tajiang.ceo.search.activity.OrderHistoryActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单查询
 * Created by Administrator on 2016/5/10.
 */
public class SearchFragment extends BaseFragment implements HttpResponseListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.et_search_input)
    EditText etSearchInput;
    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_refresh_header)
    OrderRefreshHeadView swipeRefreshHeader;
    @BindView(R.id.swipe_load_more_footer)
    OrderLoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipe_target)
    RecyclerView rvOrder;
    @BindView(R.id.iv_delete_search)
    ImageView ivDeleteSearch;

    private static final int                DEFAULT_FIRST_PAGE = 1;  //首次加载首页当前Page
    private int                             currentPage = DEFAULT_FIRST_PAGE;

    private String                          storeId;
    private String                          schoolId;

    private String                          searchContent = null; //搜索订单
    private EmptyViewAdapter emptyViewAdapter;  //当没有数据的时候，暂时通过设置列表的空AdapterView 来替代空布局
    private OrderListAdapter                mOrderListAdapter;
    private LoadingDialog                   loadingDialog;

    private List<Order>                     mSearchOrderList = new ArrayList<>();

    @Override
    protected void initTopBar() {
        setTitle("今日送达订单(0)");
        setRightByString("历史订单");
        setOnRightClick(new OnRightClick() {
            @Override
            public void rightClick() {
                Intent intent = new Intent();
                intent.putExtra("storeId", storeId);
                intent.putExtra("schoolId", schoolId);
                intent2ActivityWidthExtras(intent, OrderHistoryActivity.class);
            }
        });
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.fragment_search);
        //添加搜索框清空时候事件
        etSearchInput.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (temp.length() == 0) { //搜索框内容清空后刷新数据
                    ivDeleteSearch.setVisibility(View.GONE);
                    updateOrderList();
                } else {
                    ivDeleteSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        //支持搜索框上弹出软键盘回车搜索
        etSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //TODO.....搜索....订单........
                    loadingDialog.show();
                    searchContent = etSearchInput.getText().toString();

                    new HttpHandler(new HttpResponseListener() {
                        @Override
                        public void onSuccess(BaseResponse response) {
                            Pager<Order> pager = (Pager<Order>) response.getData();
                            List<Order> newOrderList = pager.getList();
                            if (newOrderList.size() < 1) {
                                ToastUtils.showShort("该订单号/手机号没有记录！");
                            }
                            updateMyRecyclerView(newOrderList);
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
                    }).getStoreOrder(schoolId, storeId
                            , null, searchContent, DEFAULT_FIRST_PAGE, 10, 1);
                    //TODO.....搜索....订单........
                    return true;
                }
                return false;
            }
        });
    }

    private void initListener() {
        etSearchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchInput.setCursorVisible(true);
            }
        });
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);
    }

    private void initMyView() {
        loadingDialog = new LoadingDialog(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvOrder.setLayoutManager(manager);
        rvOrder.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.gray)).size(36).build());
        rvOrder.setAdapter(emptyViewAdapter);
    }


    @Override
    protected void initData() {
        this.emptyViewAdapter = new EmptyViewAdapter(getContext(), R.layout.layout_empty_order_finished_view);
        this.mOrderListAdapter = new OrderListAdapter(getActivity(), new ArrayList<Order>(), OrderListAdapter.ORDER_TYPE_SEARCH);
        initMyView();
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initListener();
        return rootView;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                currentPage = DEFAULT_FIRST_PAGE;
                Pager<Order> pager = (Pager<Order>) response.getData();
                List<Order> newOrderList = pager.getList();
                updateMyRecyclerView(newOrderList);
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
        }).getStoreOrder(schoolId, storeId
                , null, searchContent, DEFAULT_FIRST_PAGE, 10, 1);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                Pager<Order> pager = (Pager<Order>) response.getData();
                List<Order> newOrderList = pager.getList();
                if (newOrderList.size() > 0) {
                    setCurrentPage(currentPage + 1);
                    mOrderListAdapter.addAllAndUpdateData(newOrderList);
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
        }).getStoreOrder(schoolId, storeId
                , null, searchContent, (currentPage + 1), 10, 1);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        SalesStatistics salesStatistics = (SalesStatistics) response.getData();
        updateTitleText("今日送达订单("
                + String.valueOf(salesStatistics.getOrderCount() == null ? 0 : salesStatistics.getOrderCount())
                + ")");
    }

    @Override
    public void onFailed(BaseResponse response) {

    }

    @Override
    public void onFinish() {
        if (searchContent == null || searchContent.equals("")) {
            updateOrderList();
        } else {
            loadingDialog.dismiss();
        }
    }

//    /**
//     * 查看前7天的订单
//     */
//    @OnClick(R.id.btn_show_all_order)
//    public void onShowAllOrderClick() {
//        //TODO.......Click To Load All Order Here.......
//    }


    @OnClick(R.id.iv_delete_search)
    public void onDeleteSearchClick() {
        etSearchInput.setText("");
        ivDeleteSearch.setVisibility(View.GONE);
        updateOrderList();
    }

    /**
     * 重新刷新订单
     */
    private void updateOrderList() {
        setCurrentPage(DEFAULT_FIRST_PAGE);
        searchContent = null;
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                Pager<Order> pager = (Pager<Order>) response.getData();
                List<Order> newOrderList = pager.getList();
                updateMyRecyclerView(newOrderList);
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
        }).getStoreOrder(schoolId, storeId
                , null, searchContent, DEFAULT_FIRST_PAGE, 10, 1);
    }

    //刷新确认订单。和有效订单
    private void updateOrderTodayAndOrderList() {
        loadingDialog.show();
        new HttpHandler(this).orderToday();
    }

    /**
     * 此处在判断的时候需要注意：
     * 当没有数据的时候，暂时通过设置列表的空AdapterView 来替代空布局
     *
     * @param newOrderList
     */
    private void updateMyRecyclerView(List<Order> newOrderList) {
        if (newOrderList.size() < 1) {
            swipeToLoadLayout.setLoadMoreEnabled(false);
            rvOrder.setAdapter(emptyViewAdapter);
        } else {
            swipeToLoadLayout.setLoadMoreEnabled(true);
            rvOrder.setAdapter(mOrderListAdapter);
            mOrderListAdapter.updateDataSetChanged(newOrderList);
        }
    }

    //刷新今日确认订单。和有效订单
    @Override
    public void reFreshCurrentPageData() {
        super.reFreshCurrentPageData();
        updateOrderTodayAndOrderList();
    }
}
