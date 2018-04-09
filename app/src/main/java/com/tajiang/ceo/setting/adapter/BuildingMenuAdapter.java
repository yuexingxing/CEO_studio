package com.tajiang.ceo.setting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.widget.TjGridView;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-03.
 */
public class BuildingMenuAdapter extends BaseAdapter {

    List<ApartmentZone> dataList = new ArrayList<ApartmentZone>();
    private Context mContext;
    private ViewHolder holder;

    public BuildingMenuAdapter(Context mContext, List<ApartmentZone> dataList, OnBottomClickListener listener) {

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

            convertView = View.inflate(mContext, R.layout.item_setting_building_menu_list, null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.item_setting_building_menu_school);
            holder.edit = (LinearLayout) convertView.findViewById(R.id.item_layout_setting_building_menu_edit);
            holder.gridView = (TjGridView) convertView.findViewById(R.id.lv_public_setting);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ApartmentZone apartmentZone = dataList.get(position);
        holder.name.setText(apartmentZone.getName());

        List<Building> buildingList = new ArrayList<Building>();
        buildingList.addAll(dataList.get(position).getList());

        //移除掉关闭状态的宿舍楼
        for(int i=0; i<buildingList.size(); i++){
            if(buildingList.get(i).getState() != 0){
                buildingList.remove(i);
                --i;
            }
        }

        CommonAdapter<Building> buildingCommonAdapter;
        holder.gridView.setAdapter(buildingCommonAdapter = new CommonAdapter<Building>(mContext, buildingList, R.layout.item_setting_building_button) {
            @Override
            public void convert(com.tajiang.ceo.common.adapter.ViewHolder helper, Building item) {

                helper.setText(R.id.item_layout_setting_building_button, item.getName());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {

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
        public LinearLayout edit;
        public GridView gridView;
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

