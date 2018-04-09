package com.tajiang.ceo.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.Building;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SQL on 2016/7/10.
 */
public class PopDormListAdapter extends RecyclerView.Adapter<PopDormListAdapter.PopDormListViewHolder> {

    private int selectedItem = -1;

    private List<Building> mDatas;

    private OnApartmentClickListener listener;

    public void updateDataSet(List<Building> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public interface OnApartmentClickListener{
        public void onSelectApartment(String apartmentId, int position);
    }

    public PopDormListAdapter(List<Building> mDatas, OnApartmentClickListener listener) {
        this.mDatas = mDatas;
        this.listener = listener;
    }

    @Override
    public PopDormListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pop_dorm_list_item, parent, false);
        PopDormListViewHolder holder = new PopDormListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PopDormListViewHolder holder, int position) {
//        if (position == selectedItem) {
//            holder.tvChildDormName.setTextColor(holder.getContext().getResources().getColor(R.color.green));
//            holder.tvChildDormName.setBackgroundColor(holder.getContext().getResources().getColor(R.color.gray_selected));
//        } else {
//            holder.tvChildDormName.setTextColor(holder.getContext().getResources().getColor(R.color.text_black_1));
//            holder.tvChildDormName.setBackgroundColor(holder.getContext().getResources().getColor(R.color.white));
//        }
        holder.tvChildDormName.setText(mDatas.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void ChangeSelectedItem(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }

    public void cancelSelectedItem() {
        selectedItem = -1;
        notifyDataSetChanged();
    }

    class PopDormListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_child_dorm_name)
        TextView tvChildDormName;

        public PopDormListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            initListener();
        }

        private void initListener() {
            tvChildDormName.setOnClickListener(this);
        }

        public Context getContext() {
            return itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_child_dorm_name:
                    ChangeSelectedItem(getLayoutPosition());
                    listener.onSelectApartment(mDatas.get(getLayoutPosition()).getId(), getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
    }
}
