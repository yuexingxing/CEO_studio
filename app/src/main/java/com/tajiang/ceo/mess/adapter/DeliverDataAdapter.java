package com.tajiang.ceo.mess.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.Deliver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 配送员数据列表 适配器
 * Created by Admins on 2017/3/21.
 */

public class DeliverDataAdapter extends RecyclerView.Adapter<DeliverDataAdapter.DeliverDataHolder> {

    private List<Deliver> data;
    private Context context;

    public DeliverDataAdapter(List<Deliver> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public DeliverDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DeliverDataHolder holder = new DeliverDataHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_deliver_data, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(DeliverDataHolder holder, final int position) {
        Deliver deliver = data.get(position);
        holder.llItemRoot.setBackgroundColor(context.getResources().getColor(position % 2 == 0 ? R.color.item_bg_light_green : R.color.white));
        holder.tvDeliverName.setText(deliver.getName());
        holder.tvDeliverAccount.setText(deliver.getAccount());
        holder.tvDeliverOrderCount.setText(String.valueOf(deliver.getOrderCount()));
        holder.tvDeliverCopies.setText(String.valueOf(deliver.getCopies()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DeliverDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ll_item_root)
        LinearLayout llItemRoot;
        @BindView(R.id.tv_deliver_name)
        TextView tvDeliverName;
        @BindView(R.id.tv_deliver_account)
        TextView tvDeliverAccount;
        @BindView(R.id.tv_deliver_order_count)
        TextView tvDeliverOrderCount;
        @BindView(R.id.tv_deliver_copies)
        TextView tvDeliverCopies;

        public DeliverDataHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            initListener();
        }

        private void initListener() {

        }

        public Context getContext() {
            return itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
            }
        }
    }

}
