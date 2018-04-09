package com.tajiang.ceo.setting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.BottomCallBackInterface;
import com.tajiang.ceo.common.utils.CashUtils;
import com.tajiang.ceo.model.AccountWithDrawInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单适配器
 * Created by Administrator on 2017-07-27.
 */
public class BalanceRecordAdapter extends RecyclerView.Adapter<BalanceRecordAdapter.MyViewHolder> {

    private Context context;
    private List<AccountWithDrawInfo> dataList;

    private BottomCallBackInterface.OnBottomClickListener mListener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new BalanceRecordAdapter.MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_balance_record, null));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final AccountWithDrawInfo info = dataList.get(position);

        holder.title.setText("提现");
        holder.time.setText(info.getCreateTime());
        holder.fee.setText(CashUtils.changeF2Y(info.getBalance(), 1));
        holder.time2.setText(info.getCreateTime());
        holder.time2.setVisibility(View.INVISIBLE);

        holder.state.setText(info.getState() == 0 ? "处理中" : "提现成功");

        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(mListener != null){
                    mListener.onBottomClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public BalanceRecordAdapter(Context context, List<AccountWithDrawInfo> dataList, BottomCallBackInterface.OnBottomClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.mListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_balance_record_top)
        LinearLayout layout;

        @BindView(R.id.item_balance_record_title)
        TextView title;

        @BindView(R.id.item_balance_record_time)
        TextView time;
        @BindView(R.id.item_balance_record_time2)
        TextView time2;

        @BindView(R.id.item_balance_record_state)
        TextView state;

        @BindView(R.id.item_balance_record_fee)
        TextView fee;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
