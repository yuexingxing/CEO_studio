package com.tajiang.ceo.mess.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.mess.activity.BillDetailActivity;
import com.tajiang.ceo.model.BillRecord;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/7.
 * 账单流水adapter
 */

public class BillRecordListAdapter extends RecyclerView.Adapter<BillRecordListAdapter.RecordListViewHolder>
        implements StickyRecyclerHeadersAdapter<BillRecordListAdapter.HeaderViewHolder> {

    private Context context;
    private List<BillRecord> dataList;
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd", Locale.CHINA);
//    private SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);

    public BillRecordListAdapter(Context context, List<BillRecord> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public RecordListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_bill, null));
    }

    @Override
    public void onBindViewHolder(RecordListViewHolder holder, final int position) {

        BillRecord billRecord = dataList.get(position);
        holder.tvTypeName.setText(billRecord.getChangeDescription());
//        holder.tvTypeName.setText(billRecord.getChangeType() == BillRecord.TRANSFER_OUT ? holder.getContext().getString(R.string.balance_transfer_out) :
//                holder.getContext().getString(R.string.bill));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(billRecord.getChangeDate());

        Calendar calendarSystem = Calendar.getInstance();
        calendarSystem.setTimeInMillis(System.currentTimeMillis());

        holder.tvDayRemain.setText("");
        if (billRecord.getChangeType() == BillRecord.TRANSFER_IN) {
//            //设置到账日期（2天之内到账）
//            if (calendar.get(Calendar.YEAR) == calendarSystem.get(Calendar.YEAR)) {
//                if (calendar.get(Calendar.MONTH) == calendarSystem.get(Calendar.MONTH)) {
//
//                    int deltaDay = calendar.get(Calendar.DAY_OF_MONTH) - calendarSystem.get(Calendar.DAY_OF_MONTH);
//                    deltaDay = Math.abs(deltaDay);
//                    if (deltaDay < 2) {
//                        holder.tvDayRemain.setText("(" + (2 - deltaDay) + "天后到账)");
//                    }
//                }
//            }
            holder.tvTurnOutAmount.setText("+" + String.valueOf(billRecord.getChangeAmount()));
            holder.tvTurnOutAmount.setTextColor(context.getResources().getColor(R.color.red_orange));
        } else {
            holder.tvTurnOutAmount.setText("-" + String.valueOf(billRecord.getChangeAmount()));
            holder.tvTurnOutAmount.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.tvTime.setText(format.format(calendar.getTime()));
        holder.setAmount_record_id(billRecord.getId());

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public long getHeaderId(int position) {
        BillRecord record = dataList.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(record.getChangeDate());
        return String.valueOf(calendar.get(Calendar.MONTH) + 1).charAt(0);
    }


    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_header_view, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        BillRecord record = dataList.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(record.getChangeDate());
        holder.tvMounth.setText(String.valueOf(calendar.get(Calendar.YEAR)) + "年"
                + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "月");
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_mounth)
        TextView tvMounth;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateDataSetChanged(List<BillRecord> billRecordList) {
        this.dataList.clear();
        this.dataList.addAll(billRecordList);
        notifyDataSetChanged();
    }

    public void addAllAndUpdateData(List<BillRecord> newbillRecordList) {
        this.dataList.addAll(newbillRecordList);
        notifyDataSetChanged();
    }

    public List<BillRecord> getData() {
        return dataList;
    }

    class RecordListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_bill_detail)
        RelativeLayout rlBillDetail;
        @BindView(R.id.tv_type_name)
        TextView tvTypeName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_amount)
        TextView tvTurnOutAmount;
        @BindView(R.id.tv_day_remain)
        TextView tvDayRemain;

        private String amount_record_id;

        public void setAmount_record_id(String amount_record_id) {
            this.amount_record_id = amount_record_id;
        }

        public RecordListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initListener();
        }

        private void initListener() {
//            rlBillDetail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent  = new Intent(getContext(), BillDetailActivity.class);
//                    intent.putExtra("amount_record_id", amount_record_id);
//                    getContext().startActivity(intent);
//                }
//            });
        }

        public Context getContext() {
            return itemView.getContext();
        }

    }

}

