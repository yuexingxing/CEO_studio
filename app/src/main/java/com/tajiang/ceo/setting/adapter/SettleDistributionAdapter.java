package com.tajiang.ceo.setting.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.SettleDetailInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-03.
 */
public class SettleDistributionAdapter extends BaseAdapter {

    List<SettleDetailInfo> dataList;
    private Context mContext;
    private ViewHolder holder;

    public SettleDistributionAdapter(Context mContext, List<SettleDetailInfo> dataList, OnBottomClickListener listener) {

        this.mContext = mContext;
        this.dataList = dataList;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = View.inflate(mContext, R.layout.item_settle_distribution, null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.item_settle_dis_name);
            holder.number = (TextView) convertView.findViewById(R.id.item_settle_dis_number);
            holder.fee = (EditText) convertView.findViewById(R.id.item_settle_dis_fee);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        SettleDetailInfo detailInfo = dataList.get(position);
        holder.name.setText(detailInfo.getDelyName());
        holder.number.setText(detailInfo.getTotalOrderQty() + "");
        holder.fee.setText(detailInfo.getSettleMoney() + "");

        holder.fee.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str = s.toString();

                if(TextUtils.isEmpty(s.toString())){
                    str = "0";
                }

                int fee = Integer.parseInt(str);
                dataList.get(position).setSettleMoney(fee);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;
    }

    class ViewHolder {

        public TextView name;
        public TextView number;
        public EditText fee;
    }

    /**
     * 单击事件监听器
     */
    private OnBottomClickListener mListener = null;

    public void setOnBottomClickListener(OnBottomClickListener listener) {
        mListener = listener;
    }

    public interface OnBottomClickListener {
        void onBottomClick(View v, int position);
    }

}

