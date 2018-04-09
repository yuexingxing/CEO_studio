package com.tajiang.ceo.setting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.Building;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-03.
 */
public class BuildingEditAdapter extends BaseAdapter {

    List<Building> dataList = new ArrayList<Building>();
    private Context mContext;
    private ViewHolder holder;

    public BuildingEditAdapter(Context mContext, List<Building> dataList, OnBottomClickListener listener) {

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

            convertView = View.inflate(mContext, R.layout.item_setting_building_edit, null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.item_setting_building_edit_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_setting_building_edit_icon);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Building apartmentZone = dataList.get(position);
        holder.name.setText(apartmentZone.getName());
        if(apartmentZone.getState() == 0){
            holder.icon.setBackgroundResource(R.drawable.set_up_open);
        }else{
            holder.icon.setBackgroundResource(R.drawable.set_up_turn_off);
        }

        holder.icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(mListener != null){
                    mListener.onBottomClick(arg0, position);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {

        public TextView name;
        public ImageView icon;
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

