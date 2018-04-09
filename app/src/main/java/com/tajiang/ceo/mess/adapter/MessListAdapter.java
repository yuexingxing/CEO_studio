package com.tajiang.ceo.mess.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.mess.activity.MessInfoActivity;
import com.tajiang.ceo.mess.activity.MessSelectActivity;
import com.tajiang.ceo.model.Store;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by work on 2016/6/30.
 */
public class MessListAdapter extends RecyclerView.Adapter<MessListAdapter.MessListViewHolder> {

    private List<Store> mDatas;

    public MessListAdapter(List<Store> mDatas) {
        if (mDatas == null) {
            this.mDatas = new ArrayList<>();
        } else {
            this.mDatas = mDatas;
        }
    }

    @Override
    public MessListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mess_selected, parent, false);
        MessListViewHolder holder = new MessListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MessListViewHolder holder, int position) {
        Store mStore = mDatas.get(position);
        holder.tvMessName.setText(mStore.getName());
        if (mStore.getState() == 1) {
            holder.tvBusinessState.setText("营业中");
            holder.tvBusinessState.setTextColor(holder.getContext()
                    .getResources()
                    .getColor(R.color.green));
        } else {
            holder.tvBusinessState.setText("停止营业");
            holder.tvBusinessState.setTextColor(holder.getContext()
                    .getResources()
                    .getColor(R.color.gray_dark));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updateDatas(List<Store> datas) {
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    class MessListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.rl_item_mess)
        RelativeLayout rlItemMess;
        @BindView(R.id.tv_mess_name)
        TextView tvMessName;
        @BindView(R.id.tv_business_state)
        TextView tvBusinessState;

        protected long    currentTime = 0;      //用户600ms内只能响应用户一次点击事件，此方式仅解决用户手抖造成点开2次activity

        public MessListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            initListener();
        }

        private void initListener() {
            rlItemMess.setOnClickListener(this);
        }

        public Context getContext() {
            return itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_item_mess:
                    if ((System.currentTimeMillis() - currentTime) < BaseActivity.TIME_IN_MILLS) return;
                    currentTime = System.currentTimeMillis();

                    //通过getLayoutPosition()方法获取当前位置的Store对象信息，传送到食堂信息页面
                    Intent intent = new Intent(getContext(), MessInfoActivity.class);
                    intent.putExtra("current_store", mDatas.get(getLayoutPosition()));
                    ((Activity)getContext()).startActivityForResult(intent, 0);
//                    TJApp.getInstance().finishActivity((MessSelectActivity)getContext());
                    break;
                default:
                    break;
            }
        }
    }

    public interface MessListClickListener {
        public void MessItemClick();
    }

}
