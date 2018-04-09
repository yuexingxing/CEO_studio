package com.tajiang.ceo.order.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.utils.BottomCallBackInterface;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.Res;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.model.Apartment;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.Pager;
import com.tajiang.ceo.order.activity.AllOrderActivity;
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

public class MyOrderMenuFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener{

    @BindView(R.id.swipe_target)
    RecyclerView listView;

    @BindView(R.id.tv_order_no_confirm)
    TextView tvOrderNoConfirm;

    @BindView(R.id.tv_order_ok_confirm)
    TextView tvOrderOkConfirm;

    @BindView(R.id.view_order_not_confirm)
    View viewOrderNoConfirm;

    @BindView(R.id.view_order_ok_confirm)
    View viewOrderOKConfirm;

    @BindView(R.id.checkBox_selall_fragment)
    CheckBox checkBox;

    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout swipeToLoadLayout;

    @BindView(R.id.my_order_layout_bottom)
    LinearLayout layoutBottom;

    private List<Apartment> ApartmentListData;
    private OrderMenuListAdapter mAdapter;

    private List<Order> orderList = new ArrayList<Order>();
    private int pageNumber = 1;//当前页
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mView = rootView;
        ButterKnife.bind(this, rootView);
        initListener();
        return rootView;
    }

    @Override
    protected void initTopBar() {

    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.fragment_my_order);
    }

    @Override
    protected void initData() {

        setTitle("我的订单");
        setRightByString("全部订单");

        mAdapter = new OrderMenuListAdapter(getActivity(), orderList, Order.RIDER, new BottomCallBackInterface.OnBottomClickListener() {

            @Override
            public void onBottomClick(View v, int position) {

                Order order = orderList.get(position);
                if(v.getId() == R.id.item_order_menu_img_time){

                    if(order.isChecked()){
                        order.setChecked(false);
                    }else{
                        order.setChecked(true);
                    }

                    checkSingleChoice();
                }else if(v.getId() == R.id.item_order_menu_confirm){

                    confirmOrderSingle(orderList.get(position).getOrderId(), position);
                }
            }
        });

        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.gray)).size(36).build());
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(mAdapter);

        onRefresh();
    }

    private void initListener() {

        checkBox.setOnClickListener(this);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        setOnRightClick(new OnRightClick() {

            @Override
            public void rightClick() {

                Intent intent = new Intent(getActivity(), AllOrderActivity.class);
                startActivity(intent);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                selectAll();
            }
        });
    }

    @Override
    public void onRefresh() {

        pageNumber = 1;
        PostDataTools.order_list(getActivity(), "", Order.RIDER, "", Order.CURRENT_ORDER_STATE, pageNumber, "", swipeToLoadLayout, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                Pager<Order> pager = (Pager<Order>) object;
                List<Order> newOrderList = pager.getList();

                orderList.clear();
                orderList.addAll(newOrderList);
                mAdapter.notifyDataSetChanged();

                if (newOrderList.size() >= Order.PAGE_SIZE){
                    pageNumber = 2;
                    swipeToLoadLayout.setLoadingMore(true);
                }
            }
        });
    }

    @Override
    public void onLoadMore() {

        PostDataTools.order_list(getActivity(), "", Order.RIDER, "", Order.CURRENT_ORDER_STATE, pageNumber, "", swipeToLoadLayout, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                Pager<Order> pager = (Pager<Order>) object;
                List<Order> newOrderList = pager.getList();

                orderList.addAll(newOrderList);
                mAdapter.notifyDataSetChanged();

                if (newOrderList.size() >= Order.PAGE_SIZE){
                    ++pageNumber;
                    swipeToLoadLayout.setLoadingMore(true);
                }
            }
        });
    }

    //未确认订单
    @OnClick(R.id.tv_order_no_confirm)
    public void onOrderNoConfirmClick() {

        Order.CURRENT_ORDER_TYPE = 1;
        viewOrderNoConfirm.setVisibility(View.VISIBLE);
        viewOrderOKConfirm.setVisibility(View.INVISIBLE);

        layoutBottom.setVisibility(View.VISIBLE);
        updateOrderStateColor(Order.GET_ORDER_STATE_NO_CONFIRM, tvOrderNoConfirm, tvOrderOkConfirm);
    }

    //已确认订单
    @OnClick(R.id.tv_order_ok_confirm)
    public void onOrderOkConfirmClick() {

        Order.CURRENT_ORDER_TYPE = 2;
        viewOrderNoConfirm.setVisibility(View.INVISIBLE);
        viewOrderOKConfirm.setVisibility(View.VISIBLE);

        layoutBottom.setVisibility(View.GONE);
        updateOrderStateColor(Order.GET_ORDER_STATE_OK_CONFIRM, tvOrderOkConfirm, tvOrderNoConfirm);
    }

    private void updateOrderStateColor(String orderState, TextView textView, TextView textView1) {

        if (Order.CURRENT_ORDER_STATE == orderState) {
            return;
        }

        Order.CURRENT_ORDER_STATE = orderState;
        textView.setTextColor(Res.getColor(R.color.text_black));
        textView1.setTextColor(Res.getColor(R.color.text_loading_gray));

        onRefresh();
    }

    /**
     * 订单确认--单个确认
     * @param orderId
     * @param position
     */
    public void confirmOrderSingle(String orderId, final int position){

        PostDataTools.order_arrive(getActivity(), orderId, new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {

                orderList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 订单确认--批量确认
     * @param orderIds
     */
    public void confirmOrderMulty(final String orderIds){

        if(TextUtils.isEmpty(orderIds)){
            ToastUtils.showShort("请选择数据");
            return;
        }

        PostDataTools.order_arrive(getActivity(), orderIds, new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {

                String[] strOrderIds = orderIds.split(",");

                for(int i=0; i<orderList.size(); i++){

                    Order order = orderList.get(i);
                    for(int k=0; k<strOrderIds.length; k++){
                        if(order.getOrderId().equals(strOrderIds[k])){
                            orderList.remove(i);
                            --i;
                            break;
                        }
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 判断单选状态
     */
    private void checkSingleChoice(){

        int checkCount = 0;
        int notCheckCount = 0;

        int len = orderList.size();
        for(int i=0; i<len; i++){

            Order order = orderList.get(i);

            if(order.isChecked()){
                checkCount++;
            }else{
                notCheckCount++;
            }
        }

        if(notCheckCount > 0){
            checkBox.setChecked(false);
        }

        if(checkCount == orderList.size() && orderList.size() != 0){
            checkBox.setChecked(true);
        }

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 全选、反选
     */
    private void selectAll(){

        boolean flag = checkBox.isChecked();

        int len = orderList.size();
        if(len == 0){
            checkBox.setSelected(false);
            return;
        }

        for(int i=0; i<len; i++){

            Order info = orderList.get(i);
            if(flag == true){
                info.setChecked(true);
            }else{
                info.setChecked(false);
            }
        }

        checkBox.setSelected(!flag);

        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.my_order_confirm_multy)
    public void confirmMulty() {

        StringBuffer sb  = new StringBuffer();
        int len = orderList.size();
        for(int i=0; i<len; i++){

            Order order = orderList.get(i);
            if(order.isChecked()){

                if (TextUtils.isEmpty(sb.toString())){
                    sb.append(order.getOrderId());
                }else{
                    sb.append(",").append(order.getOrderId());
                }
            }
        }

        confirmOrderMulty(sb.toString());
    }

}
