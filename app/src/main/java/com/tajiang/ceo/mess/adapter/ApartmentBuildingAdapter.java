package com.tajiang.ceo.mess.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.Building;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admins on 2016/10/17.
 */
public class ApartmentBuildingAdapter extends RecyclerView.Adapter<ApartmentBuildingAdapter.MyViewHolder> {
    private Context mContext;
    private List<Building> mDatas;
    private List<String> mDeliveredDatas;
    private OnItemBuildingClickListener mItemClickListener;

    public void setOnItemBuildingClickListener(OnItemBuildingClickListener onItemBuildingClickListener) {
        this.mItemClickListener = onItemBuildingClickListener;
    }

    public interface OnItemBuildingClickListener {
        public void onItemBuildingClick(Building selectedBuilding, int selectedItemPosition);
    }

    public ApartmentBuildingAdapter(Context context, List<Building> mDatas, List<String> mDeliveredDatas) {
        this.mContext = context;
        if (mDatas == null) {
            this.mDatas = new ArrayList<>();
        } else {
            this.mDatas = mDatas;
        }
        if (mDeliveredDatas == null) {
            this.mDeliveredDatas = new ArrayList<>();
        } else {
            this.mDeliveredDatas = mDeliveredDatas;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_building_content_mess_deliver, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Building Item = mDatas.get(position);
        holder.contentApartmentZone.setText(Item.getName());
        if (mDeliveredDatas.contains(Item.getId())) {
            holder.ivHockDelivered.setVisibility(View.VISIBLE);
            holder.contentApartmentZone.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.contentApartmentZone.setBackgroundColor(mContext.getResources().getColor(R.color.gray_selected));
        } else {
            holder.ivHockDelivered.setVisibility(View.INVISIBLE);
            holder.contentApartmentZone.setTextColor(mContext.getResources().getColor(R.color.text_black_1));
            holder.contentApartmentZone.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void updateAllDataSetChanged(List<Building> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    public void addAllAndUpdateDataSetChanged(List<Building> mDatas) {
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    public void updateAllDeliveredBuildingId(List<String> mDatas) {
        this.mDeliveredDatas.clear();
        this.mDeliveredDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ll_root_apart_zones)
        LinearLayout llRootApartZones;
        @BindView(R.id.content_apartment_zone)
        TextView contentApartmentZone;
        @BindView(R.id.iv_hock_delivered)
        ImageView ivHockDelivered;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initListener();
        }

        private void initListener() {
            llRootApartZones.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_root_apart_zones:
                    Building building = mDatas.get(getLayoutPosition());
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemBuildingClick(building, getLayoutPosition());
                    }
                    updateItemView(v, building);
                    break;
                default:
                    break;
            }
        }

        //更换被点击Item的布局样式,更新Item的配送状态
        private void updateItemView(View itemView, Building building) {
            TextView textView = (TextView) itemView.findViewById(R.id.content_apartment_zone);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_hock_delivered);

//            building.setMessDelivered(!building.isMessDelivered());

            if (mDeliveredDatas.contains(building.getId())) {
                imageView.setVisibility(View.VISIBLE);
                textView.setTextColor(mContext.getResources().getColor(R.color.green));
                textView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_selected));
            } else {
                imageView.setVisibility(View.INVISIBLE);
                textView.setTextColor(mContext.getResources().getColor(R.color.text_black_1));
                textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }

    }

}
