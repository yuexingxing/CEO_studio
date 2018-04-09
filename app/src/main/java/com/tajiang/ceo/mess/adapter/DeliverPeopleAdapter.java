package com.tajiang.ceo.mess.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.mess.model.BankInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admins on 2017/3/20.
 */

public class DeliverPeopleAdapter extends RecyclerView.Adapter<DeliverPeopleAdapter.DeliverPeopleHolder> {

    private List<String> mDatas;
    private ImageView currentStatues;

    private OnChooseBankListener onChooseBankListener;

    public interface OnChooseBankListener {
        public void ChooseBankListener(BankInfo bankInfo);
    }

    public void setOnChooseBankListener(OnChooseBankListener listener) {
        this.onChooseBankListener = listener;
    }

    public DeliverPeopleAdapter(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public DeliverPeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deliver_people, parent, false);
        DeliverPeopleHolder holder = new DeliverPeopleHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DeliverPeopleHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void notifyDatas(List<String> datas) {
        this.mDatas = datas;
        this.notifyDataSetChanged();
    }

    class DeliverPeopleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public DeliverPeopleHolder(View view) {
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
