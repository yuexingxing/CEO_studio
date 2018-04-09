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
import com.tajiang.ceo.mess.model.BankInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/20.
 */

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankListViewHolder> {

    private List<BankInfo> mDatas;
    private String selectedBank;
    private ImageView currentStatues;

    private OnChooseBankListener onChooseBankListener;

    public interface OnChooseBankListener {
        public void ChooseBankListener(BankInfo bankInfo);
    }

    public void setOnChooseBankListener(OnChooseBankListener listener) {
        this.onChooseBankListener = listener;
    }

    public BankAdapter(List<BankInfo> mDatas, String selectedBank) {
        this.mDatas = mDatas;
        this.selectedBank = selectedBank;
    }

    @Override
    public BankListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_bank, parent, false);
        BankListViewHolder holder = new BankListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BankListViewHolder holder, int position) {
        BankInfo mBankInfo = mDatas.get(position);

        holder.ivBank.setBackgroundResource(mBankInfo.getImageResId());
        holder.tvBankName.setText(mBankInfo.getName());
        holder.tvBankNotice.setText(mBankInfo.getDescribe());

        if (selectedBank!= null && mBankInfo.getName().equals(selectedBank)) {
            currentStatues = holder.ivChooseStatues;
            holder.ivChooseStatues.setImageResource(R.mipmap.hook);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void notifyDatas(List<BankInfo> datas) {
        this.mDatas = datas;
        this.notifyDataSetChanged();
    }

    class BankListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_bank)
        ImageView ivBank;
        @BindView(R.id.tv_bank_name)
        TextView tvBankName;
        @BindView(R.id.tv_bank_notice)
        TextView tvBankNotice;
        @BindView(R.id.iv_choose_statues)
        ImageView ivChooseStatues;
        @BindView(R.id.rl_choose_bank)
        RelativeLayout rlChooseBank;

        public BankListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            initListener();
        }

        private void initListener() {
            rlChooseBank.setOnClickListener(this);
        }

        public Context getContext() {
            return itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_choose_bank:
                    if (currentStatues != null) {
                        currentStatues.setImageResource(0);
                    }
                    ivChooseStatues.setImageResource(R.mipmap.hook);
                    onChooseBankListener.ChooseBankListener(mDatas.get(getLayoutPosition()));
                    break;
                default:
                    break;
            }
        }
    }

}