package com.tajiang.ceo.scan;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.OrderGoods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确认取件
 */
public class GetGoodsActivity extends BaseActivity{

    @BindView(R.id.item_order_menu_time)
    TextView tvTime;

    @BindView(R.id.item_order_menu_address)
    TextView tvAddress;

    @BindView(R.id.item_order_menu_rec_name)
    TextView tvRecName;

    @BindView(R.id.item_order_menu_sort)
    LinearLayout sort;

    @BindView(R.id.item_order_menu_goods_layout)
    LinearLayout layoutGoods;

    @BindView(R.id.lv_public)
    ListView listView;

    @BindView(R.id.item_order_menu_confirm)
    Button btnConfirm;

    private CommonAdapter<OrderGoods> commomAdapter;
    private List<OrderGoods> goodsList = new ArrayList<OrderGoods>();
    private Order order;

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {

        setTitle("扫码");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_get_goods);

        btnConfirm.setVisibility(View.INVISIBLE);

        commomAdapter = new CommonAdapter<OrderGoods>(this, goodsList, R.layout.item_layout_order_menu_goods) {

            @Override
            public void convert(ViewHolder helper, OrderGoods item) {

                if (commomAdapter.getPosition() == goodsList.size() - 1) {

                    helper.setText(R.id.item_order_menu_goods_name, item.getGoodsName());
                    helper.setText(R.id.item_order_menu_goods_count, "X" + item.getGoodsQty());
                    helper.setText(R.id.item_order_menu_goods_fee, item.getGoodsPrice() + "");

                    helper.hideView(R.id.item_order_menu_goods_send_fee_layout, false);
                    helper.setText(R.id.item_order_menu_goods_send_fee, order.getVoucherMoney() + "");
                } else {

                    helper.setText(R.id.item_order_menu_goods_name, item.getGoodsName());
                    helper.setText(R.id.item_order_menu_goods_count, "X" + item.getGoodsQty());
                    helper.setText(R.id.item_order_menu_goods_fee, item.getGoodsPrice() + "");

                    helper.hideView(R.id.item_order_menu_goods_send_fee_layout, true);
                }
            }
        };

        listView.setDivider(null);
        listView.setAdapter(commomAdapter);

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(order.isHidenGoods()){
                    order.setHidenGoods(false);
                    layoutGoods.setVisibility(View.GONE);
                }else{
                    order.setHidenGoods(true);
                    layoutGoods.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void initData() {

        orderId = getIntent().getStringExtra("orderId");

        PostDataTools.order_detail(this, orderId, new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {

                order = (Order) object;

                tvTime.setText(order.getArriveDate());
                tvRecName.setText(order.getReceiverName());
                tvAddress.setText(order.getReceiverAddr());

                goodsList.addAll(order.getOrderItemList());
                commomAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.get_goods_rescan)
    public void reScan() {

        goodsList.clear();
        goodsList = null;
        finish();
    }

    @OnClick(R.id.get_goods_get)
    public void getGoods() {

        if(TextUtils.isEmpty(orderId)){
            ToastUtils.showShort("订单号不能为空");
            return;
        }

        PostDataTools.order_take(this, orderId, new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {

                ToastUtils.showLong(message);
                if(flag){
                    goodsList.clear();
                    goodsList = null;
                    finish();
                }
            }
        });
    }

}
