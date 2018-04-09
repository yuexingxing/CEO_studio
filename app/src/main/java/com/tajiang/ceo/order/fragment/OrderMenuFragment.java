package com.tajiang.ceo.order.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.utils.BottomCallBackInterface;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.model.Apartment;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.order.activity.ChooseMessActivity;
import com.tajiang.ceo.order.adapter.OrderMenuListAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单栏主界面
 * Created by Administrator on 2017-07-27.
 */

public class OrderMenuFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_current_building)
    TextView tvCurrentBuilding;

    @BindView(R.id.swipe_target)
    RecyclerView listView;

    @BindView(R.id.et_search_input)
    EditText etSearchInput;

    @BindView(R.id.iv_delete_search)
    ImageView ivDeleteSearch;

    @BindView(R.id.tv_order_undelivered)
    TextView tvOrderUndelivered;
    @BindView(R.id.tv_order_delivering)
    TextView tvOrderDelivering;
    @BindView(R.id.tv_order_delivered)
    TextView tvOrderDelivered;

    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout swipeToLoadLayout;

    private List<Apartment> ApartmentListData;
    private OrderMenuListAdapter mAdapter;

    private int currentOrderState = 0; //默认获取的订单类型
    private String searchCondition = null; //搜索内容

    private List<Order> orderList = new ArrayList<Order>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initListener();
        return rootView;
    }

    @Override
    protected void initTopBar() {

    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.fragment_order);
    }

    @Override
    protected void initData() {

        setTitle("全部档口");
        initApartmentList();

        mAdapter = new OrderMenuListAdapter(getActivity(), orderList, "1", new BottomCallBackInterface.OnBottomClickListener() {
            @Override
            public void onBottomClick(View v, int position) {

            }
        });

        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.gray)).size(36).build());
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {


    }

    private void initListener() {

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        //点击页面上方 (食堂档口名字) 标题选择餐厅
        setOnTitleClick(new OnTitleClick() {
            @Override
            public void titleClick() {
                if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
                currentTime = System.currentTimeMillis();
                intent2ActivityForResult(ChooseMessActivity.class, Activity.RESULT_FIRST_USER);
            }
        });
        etSearchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchInput.setCursorVisible(true);
            }
        });
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
//                    updateOrderList();
                } else {
                    ivDeleteSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        //添加搜索框的键盘回车事件
        etSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //TODO.....搜索....订单........
                    searchCondition = etSearchInput.getText().toString();

                    return true;
                }
                return false;
            }
        });
    }

    //未确认订单
    @OnClick(R.id.tv_order_no_confirm)
    public void onOrderUndeliveredClick() {
        updateOrderStateColor(true, 0,
                tvOrderUndelivered, tvOrderDelivered, tvOrderDelivering);
    }

    //已确认订单
    @OnClick(R.id.tv_order_ok_confirm)
    public void onOrderDeliveringClick() {
        updateOrderStateColor(true, 1,
                tvOrderDelivering, tvOrderUndelivered, tvOrderDelivered);
    }

    private void initApartmentList() {

        PostDataTools.apartment_list(getActivity(), new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {

                List<Apartment> list = new ArrayList<Apartment>();
                list.addAll((List<Apartment>) object);

                OrderMenuFragment.this.ApartmentListData = list;
                if (ApartmentListData.size() == 1) {
                    tvCurrentBuilding.setText(ApartmentListData.get(0).getName());
                }
            }
        });

    }

    private void updateOrderStateColor(boolean isEnableMultiConfirm, int orderState, TextView textView, TextView textView1, TextView textView2) {
        if (this.currentOrderState == orderState) return;
//        mOrderListAdapter.initData();
//        refreshAllData(orderState);
        textView.setTextColor(getActivity().getResources().getColor(R.color.green));
        textView1.setTextColor(getActivity().getResources().getColor(R.color.text_black_bank));
        textView2.setTextColor(getActivity().getResources().getColor(R.color.text_black_bank));

        onRefresh();
    }
}
