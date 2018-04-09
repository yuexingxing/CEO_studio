package com.tajiang.ceo.order.adapter;

import android.content.Context;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.utils.Res;
import com.tajiang.ceo.model.Building;

import java.util.List;

/**
 * Created by work on 2016/7/13.
 */
public class ApartmentListAdapter extends CommonAdapter<Building>{

    private int selectPosition = -1;

    public ApartmentListAdapter(Context context, List<Building> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Building item) {

        TextView textView = helper.getView(R.id.content_apartment);
        textView.setText(item.getName());

//        if (helper.getPosition() % 2 == 0) {
//            textView.setBackgroundColor(Color.parseColor("#fbfbfb"));
//        } else {
//            textView.setBackgroundColor(Color.parseColor("#ffffff"));
//        }

        if(helper.getPosition() == selectPosition){
            textView.setTextColor(Res.getColor(R.color.green));
            textView.setBackgroundColor(Res.getColor(R.color.gray_selected));
        }else {
            textView.setTextColor(Res.getColor(R.color.text_black_1));
            textView.setBackgroundColor(Res.getColor(R.color.transparent));
        }

    }

    public void setSelect(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }

    public  void cannelSelect() {
        selectPosition = -1;
        notifyDataSetChanged();
    }

}

