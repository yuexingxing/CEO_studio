package com.tajiang.ceo.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admins on 2016/10/11.
 */
public class EmptyViewAdapter extends RecyclerView.Adapter<EmptyViewAdapter.MyEmptyViewHolder> {
    private Context context;
    private int resId;

    public EmptyViewAdapter(Context context, int resId) {
        this.context = context;
        this.resId = resId;
    }

    @Override
    public MyEmptyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resId, parent,false);
        MyEmptyViewHolder viewHolder = new MyEmptyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyEmptyViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }


    class MyEmptyViewHolder extends RecyclerView.ViewHolder {

        public MyEmptyViewHolder(View itemView) {
            super(itemView);
        }

        public Context getContext() {
            return itemView.getContext();
        }

    }

}