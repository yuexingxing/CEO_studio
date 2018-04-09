package com.tajiang.ceo.order.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.Store;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/29.
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private List<Store> storeList;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        public void onItemClick(List<Store> storeList, int position);
    }

    public StoreAdapter(List<Store> storeList, OnItemClickListener listener) {
        this.storeList = storeList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.tvStoreName.setText(store.getName());
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_store_name)
        TextView tvStoreName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_store_name)
        public void onItemClick() {
            listener.onItemClick(storeList, getLayoutPosition());
        }

    }
}
