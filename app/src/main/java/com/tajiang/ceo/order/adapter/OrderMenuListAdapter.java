package com.tajiang.ceo.order.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.utils.BottomCallBackInterface;
import com.tajiang.ceo.common.utils.CashUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.utils.Utility;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.OrderGoods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单适配器
 * Created by Administrator on 2017-07-27.
 */
public class OrderMenuListAdapter extends RecyclerView.Adapter<OrderMenuListAdapter.MyViewHolder> {

    private Context context;
    private List<Order> dataList;
    private String orderType;
    private CommonAdapter<OrderGoods> commomAdapter;

    private BottomCallBackInterface.OnBottomClickListener mListener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new OrderMenuListAdapter.MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_layout_order_menu, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Order order = dataList.get(position);

        holder.storeName.setText(order.getShopName());
        holder.time.setText(order.getDelyDate());
        holder.address.setText(order.getReceiverAddr());
        holder.recName.setText(order.getReceiverName());

        holder.totalFee.setText(CashUtils.changeF2Y(order.getFinalMoney(), -1));
        holder.orderNumber.setText(order.getOrderId());

        holder.btnConfirm.setVisibility(View.GONE);
        holder.imgTime.setBackgroundResource(R.drawable.order_time);

        //未送达
        if(Order.CURRENT_ORDER_TYPE == 1){

            if(order.isChecked()){
                holder.imgTime.setBackgroundResource(R.drawable.order_select);
            }else{
                holder.imgTime.setBackgroundResource(R.mipmap.order_unselected);
            }

            holder.orderState.setText("未送达");
            holder.btnConfirm.setVisibility(View.VISIBLE);
        }
        //已送达
        else if(Order.CURRENT_ORDER_TYPE == 2){

            holder.imgTime.setBackgroundResource(R.drawable.order_time);
            holder.orderState.setText("已送达");
        }

        //当前
        else if(Order.CURRENT_ORDER_TYPE == 3){

            holder.orderState.setText("未送达");
        }
        //预定订单
        else if(Order.CURRENT_ORDER_TYPE == 4){

            holder.orderState.setText("未送达");
        }
        //已完成
        else if(Order.CURRENT_ORDER_TYPE == 5){

            holder.orderState.setText("已送达");
        }

        final List<OrderGoods> goodsList = order.getOrderItemList();
        if(goodsList != null && goodsList.size() > 0) {

            commomAdapter = new CommonAdapter<OrderGoods>(context, goodsList, R.layout.item_layout_order_menu_goods) {

                @Override
                public void convert(ViewHolder helper, OrderGoods item) {

                    System.out.println("数量" + commomAdapter.getPosition() + "," + goodsList.size() + "," + position + "," + item.getGoodsName());
                    if (commomAdapter.getPosition() == goodsList.size() - 1) {

                        helper.setText(R.id.item_order_menu_goods_name, item.getGoodsName());
                        helper.setText(R.id.item_order_menu_goods_count, "X" + item.getGoodsQty());
                        helper.setText(R.id.item_order_menu_goods_fee, CashUtils.changeF2Y(item.getGoodsPrice(), -1));

                        helper.hideView(R.id.item_order_menu_goods_send_fee_layout, false);
                        helper.setText(R.id.item_order_menu_goods_send_fee, CashUtils.changeF2Y(order.getVoucherMoney(), -1));
                    } else {

                        helper.setText(R.id.item_order_menu_goods_name, item.getGoodsName());
                        helper.setText(R.id.item_order_menu_goods_count, "X" + item.getGoodsQty());
                        helper.setText(R.id.item_order_menu_goods_fee, CashUtils.changeF2Y(item.getGoodsPrice(), -1));

                        helper.hideView(R.id.item_order_menu_goods_send_fee_layout, true);
                    }

                }

            };

            holder.listView.setDivider(null);
            holder.listView.setAdapter(commomAdapter);
            Utility.setListViewHeightBasedOnChildren(holder.listView);
        }

        holder.imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(order.getOrderStateExtra() == 0 || order.getOrderStateExtra() == 1){
                    if(mListener != null){
                        mListener.onBottomClick(v, position);
                    }
                }
            }
        });

        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showShort(position + "");
            }
        });

        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){
                    mListener.onBottomClick(v, position);
                }
            }
        });

        holder.sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(order.isHidenGoods()){
                    order.setHidenGoods(false);
                    holder.layoutGoods.setVisibility(View.GONE);
                }else{
                    order.setHidenGoods(true);
                    holder.layoutGoods.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void notify(int position){

        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateDataSetChanged(List<Order> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public OrderMenuListAdapter(Context context, List<Order> dataList, String orderType, BottomCallBackInterface.OnBottomClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.orderType = orderType;
        this.mListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_order_menu_store_name)
        TextView storeName;

        @BindView(R.id.item_order_menu_img_time)
        ImageView imgTime;

        @BindView(R.id.item_order_menu_time)
        TextView time;

        @BindView(R.id.item_order_menu_order_state)
        TextView orderState;

        @BindView(R.id.item_order_menu_sort)
        LinearLayout sort;

        //地址
        @BindView(R.id.item_order_menu_address)
        TextView address;

        //收货人名称
        @BindView(R.id.item_order_menu_rec_name)
        TextView recName;

        //合计
        @BindView(R.id.item_order_menu_goods_total_fee)
        TextView totalFee;

        @BindView(R.id.item_order_menu_goods_layout)
        LinearLayout layoutGoods;

        @BindView(R.id.item_order_menu_ordernum)
        TextView orderNumber;

        @BindView(R.id.lv_public)
        ListView listView;

        //确认送达
        @BindView(R.id.item_order_menu_confirm)
        Button btnConfirm;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
