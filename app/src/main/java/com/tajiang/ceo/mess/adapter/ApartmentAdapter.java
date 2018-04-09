package com.tajiang.ceo.mess.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Dorm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SQL on 2016/10/16.
 */
public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.MyViewHolder> {

    private TextView itemTextView;
    private int selectedItem = 0;
    private Context mContext;
    private List<ApartmentZone> mDatas;

    private OnItemZoneClickListener onItemClickListener;

    public interface OnItemZoneClickListener {
        public void onItemZoneClick(ApartmentZone selectedApartmentZone, int selectedItemPosition);
    }

    public void setOnItemClickListener(OnItemZoneClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ApartmentAdapter(Context context, List<ApartmentZone> mDatas) {
        this.mContext = context;
        if (mDatas == null) {
            this.mDatas = new ArrayList<>();
        } else {
            this.mDatas = mDatas;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layou_title_content_mess_deliver, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ApartmentZone Item = mDatas.get(position);
        holder.contentApartmentZone.setText(Item.getName());
        if (position == selectedItem) {
            itemTextView = holder.contentApartmentZone;
            holder.contentApartmentZone.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.contentApartmentZone.setBackgroundColor(mContext.getResources().getColor(R.color.gray_selected));
        } else {
            holder.contentApartmentZone.setTextColor(mContext.getResources().getColor(R.color.text_black_1));
            holder.contentApartmentZone.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void updateAllDataSetChanged(List<ApartmentZone> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    public void addAllAndUpdateDataSetChanged(List<ApartmentZone> mDatas) {
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public TextView getItemTextView() {
        return itemTextView;
    }
    public void setItemTextView(TextView itemTextView) {
        this.itemTextView = itemTextView;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ll_root_apart_zones)
        LinearLayout llRootApartZones;
        @BindView(R.id.content_apartment_zone)
        TextView contentApartmentZone;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initListener();
        }

        private void initListener() {
            contentApartmentZone.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.content_apartment_zone:
                    if (getLayoutPosition() != selectedItem) {
                        //回调接口，响应外部点击事件更换宿舍区对于楼栋
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemZoneClick(mDatas.get(getLayoutPosition()), getLayoutPosition());
                        }
                        updateItemView(v);
                    }
                    break;
                default:
                    break;
            }
        }
        //更换被点击Item的布局样式
        private void updateItemView(View itemView) {
            TextView textView = ((TextView)itemView);
            textView.setTextColor(mContext.getResources().getColor(R.color.green));
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_selected));
            if (getItemTextView() != null) {
                getItemTextView().setTextColor(mContext.getResources().getColor(R.color.text_black_1));
                getItemTextView().setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
            setItemTextView(textView);
            setSelectedItem(getLayoutPosition());
        }
    }

}
