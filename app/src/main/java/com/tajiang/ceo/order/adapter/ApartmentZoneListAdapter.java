package com.tajiang.ceo.order.adapter;

import android.content.Context;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.utils.Res;
import com.tajiang.ceo.model.ApartmentZone;

import java.util.List;

/**
 * Created by work on 2016/7/13.
 */
public class ApartmentZoneListAdapter extends CommonAdapter<ApartmentZone> {

    private int selectedItem = 0 ;

    public ApartmentZoneListAdapter(Context context, List<ApartmentZone> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, ApartmentZone item) {

        TextView textView = helper.getView(R.id.content_apartment_zone);
        textView.setText(item.getName());

        if(helper.getPosition() == selectedItem){
            textView.setTextColor(Res.getColor(R.color.green));
            textView.setBackgroundColor(Res.getColor(R.color.gray_selected));
        }else {
            textView.setTextColor(Res.getColor(R.color.text_black_1));
            textView.setBackgroundColor(Res.getColor(R.color.transparent));
        }

    }

    public void setSelected(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }

}
