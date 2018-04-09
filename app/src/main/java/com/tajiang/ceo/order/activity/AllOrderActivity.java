package com.tajiang.ceo.order.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.adapter.EmptyViewAdapter;
import com.tajiang.ceo.common.utils.BottomCallBackInterface;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.Res;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.Pager;
import com.tajiang.ceo.order.adapter.OrderMenuListAdapter;
import com.tajiang.ceo.order.view.OrderLoadMoreFooterView;
import com.tajiang.ceo.order.view.OrderRefreshHeadView;
import com.tajiang.ceo.order.view.popwindow.SelectApartmentPopWindow;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全部订单，CEO查看
 */
public class AllOrderActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener{

    @BindView(R.id.swipe_refresh_header)
    OrderRefreshHeadView swipeRefreshHeader;
    @BindView(R.id.swipe_load_more_footer)
    OrderLoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.ll_show_dorm_list)
    LinearLayout llShowDormList;
    @BindView(R.id.tv_current_building)
    TextView tvCurrentBuilding;
    @BindView(R.id.et_search_input)
    EditText etSearchInput;
    @BindView(R.id.swipe_target)
    RecyclerView rvOrder;
    @BindView(R.id.iv_delete_search)
    ImageView ivDeleteSearch;
    @BindView(R.id.ll_root_content)
    LinearLayout llRootContent;

    @BindView(R.id.tv_order_undelivered)
    TextView tvOrderUndelivered;

    @BindView(R.id.tv_order_delivering)
    TextView tvOrderDelivering;

    @BindView(R.id.tv_order_delivered)
    TextView tvOrderDelivered;

    @BindView(R.id.order_all_order_title)
    TextView tvTitle;

    private String searchCondition = ""; //搜索内容
    private String currentBuilding = "全部宿舍楼";
    private String currentStallId = "";//

    private List<Building> ApartmentListData;  //非CEO用户选择宿舍下拉列表

    private OrderMenuListAdapter mAdapter;
    private EmptyViewAdapter emptyViewAdapter;

    private SelectApartmentPopWindow selectApartmentPopWindow;

    private List<Order> orderList = new ArrayList<Order>();
    private int pageNumber = 1;//当前页

    private String apartmentId = "";//宿舍楼ID
    private String storeName;//档口名称
    private String storeId = "";//档口ID
    private final int SELECT_APARTMENT = 0x0011;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initApartmentList();
        initListener();
    }

    @Override
    protected void initTopBar() {
        disableNav();
    }

    @Override
    protected void initLayout() {

        setContentView(R.layout.fragment_order);
    }

    private void initListener() {

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

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

                searchCondition = charSequence.toString();
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

        //添加搜索框的键盘回车事件
        etSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //TODO.....搜索....订单........
                    searchCondition = etSearchInput.getText().toString();
                    onRefresh();
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_APARTMENT && resultCode == RESULT_OK){

            Bundle bundle = data.getExtras();

            storeName = bundle.getString("store_name");
            storeId = bundle.getString("store_id");
            tvTitle.setText(storeName);
        }
    }

    @OnClick(R.id.order_all_order_left)
    public void onClickLeft(){

        orderList.clear();
        orderList = null;
        finish();
    }

    @OnClick(R.id.order_all_order_title)
    public void onCilckTitle(){

        if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) {
            return;
        }

        currentTime = System.currentTimeMillis();
        Intent intent = new Intent(AllOrderActivity.this, ChooseMessActivity.class);
        startActivityForResult(intent, SELECT_APARTMENT);
    }

    /**
     * 刷新当前的宿舍范围 (全部宿舍楼)
     */
    private void refreshAllOrder() {

        searchCondition = "";
        tvCurrentBuilding.setText("全部宿舍楼");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化请求参数
     */
    @Override
    protected void initData() {

        setCurrentStallId(null);
        onOrderUndeliveredClick();

        mAdapter = new OrderMenuListAdapter(this, orderList, Order.CEO, new BottomCallBackInterface.OnBottomClickListener() {

            @Override
            public void onBottomClick(View v, int position) {

                if(v.getId() == R.id.item_order_menu_confirm){

                }
            }
        });

        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        rvOrder.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.gray)).size(36).build());
        rvOrder.setItemAnimator(new DefaultItemAnimator());
        rvOrder.setAdapter(mAdapter);

        this.emptyViewAdapter = new EmptyViewAdapter(this, R.layout.layout_empty_order_view);
        this.ApartmentListData = new ArrayList<Building>();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRefresh() {

        pageNumber = 1;
        PostDataTools.order_list(this,apartmentId, Order.CEO, searchCondition, Order.CURRENT_ORDER_STATE + "", pageNumber, storeId,swipeToLoadLayout, new PostDataTools.DataCallback() {

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

        PostDataTools.order_list(this, apartmentId, Order.CEO, searchCondition, Order.CURRENT_ORDER_STATE, pageNumber, storeId, swipeToLoadLayout, new PostDataTools.DataCallback() {

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

    //当前订单
    @OnClick(R.id.tv_order_undelivered)
    public void onOrderUndeliveredClick() {

        Order.CURRENT_ORDER_TYPE = 3;
        updateOrderStateColor(Order.GET_ORDER_STATE_DANGQIAN, tvOrderUndelivered, tvOrderDelivered, tvOrderDelivering);
    }

    //预定订单
    @OnClick(R.id.tv_order_delivering)
    public void onOrderDeliveringClick() {

        Order.CURRENT_ORDER_TYPE = 4;
        updateOrderStateColor(Order.GET_ORDER_STATE_YUDING, tvOrderDelivering, tvOrderUndelivered, tvOrderDelivered);
    }

    //送达订单
    @OnClick(R.id.tv_order_delivered)
    public void onOrderDeliveredClick() {

        Order.CURRENT_ORDER_TYPE = 5;
        updateOrderStateColor(Order.GET_ORDER_STATE_SONGDA, tvOrderDelivered, tvOrderDelivering, tvOrderUndelivered);
    }

    private void updateOrderStateColor(String orderState, TextView textView, TextView textView1, TextView textView2) {

        if (Order.CURRENT_ORDER_STATE.equals(orderState)) {
            return;
        }

        Order.CURRENT_ORDER_STATE = orderState;

        onRefresh();
        textView.setTextColor(Res.getColor(R.color.text_black));
        textView1.setTextColor(Res.getColor(R.color.text_loading_gray));
        textView2.setTextColor(Res.getColor(R.color.text_loading_gray));
    }

    @OnClick(R.id.iv_delete_search)
    public void onSearchDeleteClick() {
        etSearchInput.setText("");
        ivDeleteSearch.setVisibility(View.GONE);
        updateOrderList();
    }

    /**
     * 重新刷新订单
     */
    private void updateOrderList() {

        searchCondition = null;
    }

    /**
     * 点击宿舍区，弹出宿舍列表
     */
    @OnClick(R.id.ll_show_dorm_list)
    public void onSelectDormListClick() {

        if (ApartmentListData.size() == 1) {//当前用户如果是非CEO，并且楼栋数量只有1栋则不展开列表
            RemoveDropListTextViewRightImg();
        } else {
            setDropListTextViewRightImg(R.mipmap.order_replace);
            initCommonPopupWindow();
        }
    }

    /**
     * 获取楼栋
     * @return
     */
    private void initCommonPopupWindow() {

        //通过布局注入器，注入布局给View对象
        View myView = getLayoutInflater().inflate(R.layout.layout_pop_dorm_list, null);
        //通过view 和宽·高，构造PopopWindow
        if (selectApartmentPopWindow == null) {

            selectApartmentPopWindow = new SelectApartmentPopWindow(this
                    , myView
                    , ApartmentListData
                    , LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.MATCH_PARENT
                    , true);

            //点击全部宿舍楼，重新加载全部订单
            selectApartmentPopWindow.setOnSelectAllApartmentListener(new SelectApartmentPopWindow.OnSelectAllApartmentListener() {
                @Override
                public void onSelectAllApartment() {

                    tvCurrentBuilding.setText("全部宿舍楼");
                    selectApartmentPopWindow.dismiss();
                }
            });

            selectApartmentPopWindow.setSelectApartmentClickListener(new SelectApartmentPopWindow.OnSelectApartmentListener() {

                @Override
                public void onSelectApartmentClick(String apartmentId1, int position) {

                    currentBuilding = ApartmentListData.get(position).getName();
                    apartmentId = ApartmentListData.get(position).getId();
                    tvCurrentBuilding.setText(currentBuilding);
                }
            });
            selectApartmentPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {

                    setDropListTextViewRightImg(R.drawable.order_replace);
                }
            });
        }

        selectApartmentPopWindow.setCurrentDorm(currentBuilding, apartmentId);
        selectApartmentPopWindow.showAsDropDown(llShowDormList, 0, -(this.getResources().getDimensionPixelSize(R.dimen.com_field_height_44)));
    }

    /**
     * 对订单的一系列操作之后更新当前订单数据
     *
     * @param newOrderList
     */
    private void updateMyRecyclerView(List<Order> newOrderList) {

        if (newOrderList.size() < 1) {
            swipeToLoadLayout.setLoadMoreEnabled(false);
            rvOrder.setAdapter(emptyViewAdapter);
        } else {
            swipeToLoadLayout.setLoadMoreEnabled(true);
            rvOrder.setAdapter(mAdapter);
        }

        //更新数据源
        mAdapter.updateDataSetChanged(newOrderList);
    }

    public void RemoveDropListTextViewRightImg() {
        tvCurrentBuilding.setCompoundDrawables(null, null, null, null);
    }

    public void setDropListTextViewRightImg(int resId) {

        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvCurrentBuilding.setCompoundDrawables(null, null, drawable, null);
    }

    public void setCurrentStallId(String currentStallId) {
        this.currentStallId = currentStallId;
    }

    public String getCurrentStallId() {
        return currentStallId;
    }

    /**
     * 非CEO权限用户获取学校宿舍区和楼栋
     */
    private void initApartmentList() {

        PostDataTools.apartment_list(this, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                List<ApartmentZone> list = new ArrayList<ApartmentZone>();
                list.addAll((List<ApartmentZone>) object);

                //这里面需要对校区进行处理，暂时没改
                ApartmentListData = new ArrayList<Building>();
                for(int i=0; i<list.size(); i++){

                    ArrayList<Building> buildingArrayList = list.get(i).getList();
                    ApartmentListData.addAll(buildingArrayList);
                }

                if (ApartmentListData.size() == 1) {
                    RemoveDropListTextViewRightImg();
                    tvCurrentBuilding.setText(ApartmentListData.get(0).getName());
                }
            }
        });
    }

}
