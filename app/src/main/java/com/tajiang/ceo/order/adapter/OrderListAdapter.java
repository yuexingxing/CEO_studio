package com.tajiang.ceo.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.widget.dialog.CallDialog;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.OrderGoods;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ciko on 16/6/12.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    public static final int ORDER_TYPE_NORMAL = 0;
    public static final int ORDER_TYPE_SEARCH = 1;
    public static final int ORDER_TYPE_HISTORY = 2;
    private int orderType = ORDER_TYPE_NORMAL;

    private static final String NULL_STRING = "null";

    private SimpleDateFormat format;
    private NumberFormat numberFormat = NumberFormat.getInstance();

    private Context context;

    private String LastOrderStallName = NULL_STRING;  //记录上一个菜品所属的档口名称
    private boolean isAllChoosed;  //当前是否全选
    private boolean isChoosedButtonVisiable;
//    private boolean AllChoosedPriority;  //是否 优先使用全选参数来判断当前UI上每一个订单的选中状态

    private List<Order> dataList;
    private List<String> MultiOrderIds;  //订单批量确认送达(或取餐)的数据类型 (保存订单ID)

    private OnOrderConfirmListener onOrderConfirmListener;
    private OnAllOrderConfirmedListener onAllOrderConfirmedListener;
    private View viewBottomSheet;

    public void setOnAllOrderConfirmedListener(OnAllOrderConfirmedListener listener) {
        this.onAllOrderConfirmedListener = listener;
    }

    /**
     * 检查是否清空了订单列表
     *
     * @param listener
     */
    public void setOnOrderConfirmListener(OnOrderConfirmListener listener) {
        this.onOrderConfirmListener = listener;
    }

    /**
     * 更新局部 显示在屏幕上的 订单为选中状态
     * @param firstVisibleItem
     * @param lastVisibleItem
     */
    public void updateOrderChoosed(boolean isAllChoosed, int firstVisibleItem, int lastVisibleItem) {
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setChoosed(isAllChoosed);
        }
        ((TextView)viewBottomSheet.findViewById(R.id.tv_order_count)).setText(isAllChoosed ? "" + dataList.size() : "0");
        this.isAllChoosed = isAllChoosed;
        notifyDataSetChanged();
    }

    public List<String> getChoosedOrderIds() {
        List<String> list = new LinkedList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isChoosed())
            list.add(dataList.get(i).getDelyId());
        }
        return list;
    }

    public void setSingleChooseButtonVisibility(boolean visibility) {
        this.isChoosedButtonVisiable = visibility;
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setChoosed(false);
        }
        notifyDataSetChanged();
    }

    public void setBottomView(View viewBottomSheet) {
        this.viewBottomSheet = viewBottomSheet;
    }


    //订单进行批量全选监听器
    public interface OnAllOrderConfirmedListener {
        void OnAllOrderConfirmed();
    }

    public interface OnOrderConfirmListener {
        void OnSingleOrderConfirmed(List<Order> dataList, int position);
    }

    public OrderListAdapter(Context context, List<Order> dataList, int orderType) {
        this.context = context;
        this.dataList = dataList;
        this.orderType = orderType;
        initData();
    }

    public void initData() {
        this.isAllChoosed = false;
        this.isChoosedButtonVisiable = false;
        this.MultiOrderIds = new ArrayList<>();
        this.format = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_order, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Order order = dataList.get(position);
        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(order.getCreateDate().toString());
//        if (orderType == ORDER_TYPE_NORMAL&& ! DateUtils.getCurrentDayMonthYearByMills(esTime).equals(DateUtils.getCurrentDayMonthYear())) {   //预订单
//        if (orderType == ORDER_TYPE_NORMAL && order.getEstimatedTimev2().contains("明天")) {   //预订单
//            holder.ivFlag.setImageResource(R.mipmap.icon_flag_pre_order);
//        } else {
            holder.ivFlag.setImageResource(R.mipmap.icon_flag_order);
//        }

        //初始化当前Item订单是否被选中状态
        holder.ivConfirmOrder.setImageResource(order.isChoosed() ? R.mipmap.icon_order_confirmed : R.mipmap.icon_order_unconfirmed);
        holder.ivConfirmOrder.setVisibility(isChoosedButtonVisiable ? View.VISIBLE : View.GONE);
        holder.btnConfirmOrder.setVisibility(isChoosedButtonVisiable ? View.GONE : View.VISIBLE);
//        holder.tvEstimedTime.setText(order.getEstimatedTimev2());
        holder.tvOrderTime.setText(format.format(calendar.getTime()) + "下单");
//        holder.tvApartment.setText(order.getOrderAddress());
//        holder.tvName.setText(order.getBuyerName());
//        holder.tvPhone.setText(order.getBuyerPhone());
//        holder.phone = order.getBuyerPhone();
//        holder.tvOrder.setText("订单号:" + order.getOrderSn());
//        holder.tvDistributionFee.setText("￥" + numberFormat.format(order.getShippingFee()));

//        holder.tvVoucher.setText("-￥" + numberFormat.format(order.getVoucherPrice()));
//        if (order.getVoucherPrice() != null) {
//            holder.tvVoucher.setText("-￥" + numberFormat.format(order.getVoucherPrice()));
//        } else {
            holder.tvVoucher.setText("-￥0");
//        }

        holder.tvOrderPrice.setText("￥" + numberFormat.format(order.getFinalMoney()));
        holder.llGoodsList.removeAllViews();
        if (order.getOrderItemList() != null) {
            for (OrderGoods goods : order.getOrderItemList()) {

                //------------档口--------------
                String StallsName = goods.getGoodsName() == null ? NULL_STRING : goods.getGoodsName();

                if (LastOrderStallName.equals(StallsName) != true) {
                    LinearLayout linearLayout_1 = (LinearLayout) LayoutInflater.from(holder.getContext()).inflate(R.layout.item_order_stalls, holder.llGoodsList, false);
                    LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    linearLayout_1.setLayoutParams(params_1);

                    TextView tvStallName = ButterKnife.findById(linearLayout_1, R.id.tv_stall_name);
                    tvStallName.setText(StallsName + " (档口)");
                    holder.llGoodsList.addView(linearLayout_1);
                    LastOrderStallName = StallsName;
                }

                //------------菜品------------
                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(holder.getContext()).inflate(R.layout.item_order_goods, holder.llGoodsList, false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(params);

                TextView tvGoodsName = ButterKnife.findById(linearLayout, R.id.tv_goods_name);
                TextView tvGoodsNum = ButterKnife.findById(linearLayout, R.id.tv_goods_num);
                TextView tvGoodsPrice = ButterKnife.findById(linearLayout, R.id.tv_goods_price);
                tvGoodsName.setText(goods.getGoodsName());
                tvGoodsNum.setText("x " + numberFormat.format(goods.getGoodsQty()));
                tvGoodsPrice.setText("￥" + numberFormat.format(goods.getGoodsQty() * goods.getGoodsPrice()));
                holder.llGoodsList.addView(linearLayout);
            }
            LastOrderStallName = NULL_STRING;
        }

        switch (order.getOrderState()) {
            case 0:
                holder.tvOrderStatue.setText("未取餐");
                holder.btnConfirmOrder.setText("确认取餐");
                break;

            case 1:
                holder.tvOrderStatue.setText("配送中");
                holder.btnConfirmOrder.setText("确认送达");
                break;

            case 2:
            default:
                holder.tvOrderStatue.setText("已送达");
                holder.btnConfirmOrder.setVisibility(View.GONE);
                break;
        }

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public int removeItem(int position) {
        this.dataList.remove(position);
        notifyItemRemoved(position);//Attention!
        return this.dataList.size();
    }

    public int getDataSize() {
        return dataList.size();
    }

    public void updateDataSetChanged(List<Order> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAllAndUpdateData(List<Order> newDataList) {
        this.dataList.addAll(newDataList);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_flag)
        ImageView ivFlag;
        @BindView(R.id.tv_apartment)
        TextView tvApartment;
        @BindView(R.id.tv_estimated_time)
        TextView tvEstimedTime;
        @BindView(R.id.tv_name)
        TextView tvName;
        //        @BindView(R.id.tv_phone)
//        TextView tvPhone;
        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.ll_goods_list)
        LinearLayout llGoodsList;
        @BindView(R.id.tv_distribution_fee)
        TextView tvDistributionFee;
        @BindView(R.id.tv_voucher)
        TextView tvVoucher;
        @BindView(R.id.tv_order_price)
        TextView tvOrderPrice;
        @BindView(R.id.ll_order_detail)
        LinearLayout llOrderDetail;
        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;
        @BindView(R.id.tv_order_statue)
        TextView tvOrderStatue;
        @BindView(R.id.iv_dialog_phone)
        ImageView ivDialogPhone;
        @BindView(R.id.btn_confirm_order)
        Button btnConfirmOrder;
        @BindView(R.id.iv_confirm_order)
        ImageView ivConfirmOrder;

        private String phone;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initListener();
        }

        private void initListener() {
            btnConfirmOrder.setOnClickListener(this);
            /**
             * 拨打下单用户电话
             */
            ivDialogPhone.setOnClickListener(this);
            /**
             * 点击选中作为候选批量确认的订单
             */
            ivConfirmOrder.setOnClickListener(this);
        }

        public Context getContext() {
            return itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm_order:
                    onOrderConfirmListener.OnSingleOrderConfirmed(dataList, getLayoutPosition());
                    break;

                case R.id.iv_dialog_phone:
                    new CallDialog(getContext(), phone).show();
                    break;

                case R.id.iv_confirm_order:
                    int choosedOrderCount = Integer.valueOf((String) ((TextView)viewBottomSheet.findViewById(R.id.tv_order_count)).getText());
                    boolean choosed = !dataList.get(getLayoutPosition()).isChoosed();
                    ivConfirmOrder.setImageResource(choosed ? R.mipmap.icon_order_confirmed : R.mipmap.icon_order_unconfirmed);
                    dataList.get(getLayoutPosition()).setChoosed(choosed);
                    if (choosed) {
                        ((TextView)viewBottomSheet.findViewById(R.id.tv_order_count)).setText("" + (choosedOrderCount + 1));
                    } else {
                        ((TextView)viewBottomSheet.findViewById(R.id.tv_order_count)).setText("" + (choosedOrderCount - 1));
                    }
                    break;
            }
        }
    }

}
