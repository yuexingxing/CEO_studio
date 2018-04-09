package com.tajiang.ceo.order.view.popwindow;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.order.adapter.PopDormListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by work on 2016/7/13.
 */
public class SelectApartmentPopWindow extends PopupWindow {

    private View contentView;
    private Context context;

    private LinearLayout viewClickDismiss;  //点击阴影区域退出弹窗
    private TextView tvSelectAllApartment;
    private RecyclerView recyclerViewApartment; //当前送餐范围内的楼栋列表

    private PopDormListAdapter popDormListAdapter;

    private List<Building> DormListData;

    private OnSelectApartmentListener listener;

    public void setCurrentDorm(String name, String id){
        tvSelectAllApartment.setText(name+"");
    }

    public void setDormListData(List<Building> dormListData) {
        DormListData = dormListData;
    }

    public void setSelectApartmentClickListener(OnSelectApartmentListener listener) {
        this.listener = listener;
    }

    public SelectApartmentPopWindow(Context context, View contentView, List<Building> ApartmentListData, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        this.context = context;
        this.contentView = contentView;
        this.DormListData = (ApartmentListData == null? new ArrayList<Building>(): ApartmentListData);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        initView();
    }

    private void initView() {

        tvSelectAllApartment = (TextView) contentView.findViewById(R.id.tv_select_all_apartment);
        recyclerViewApartment = (RecyclerView) contentView.findViewById(R.id.rv_pop_dorm_list);
        viewClickDismiss = (LinearLayout) contentView.findViewById(R.id.ll_click_dismiss);
        viewClickDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectApartmentPopWindow.this.dismiss();
            }
        });

        recyclerViewApartment.setLayoutManager(new LinearLayoutManager(context));

        popDormListAdapter = new PopDormListAdapter(DormListData
                , new PopDormListAdapter.OnApartmentClickListener() {
            @Override
            public void onSelectApartment(String apartmentId, int position) {
                SelectApartmentPopWindow.this.dismiss();
                //TODO.........
                //重新选择送餐楼栋，刷新订单列表)
                listener.onSelectApartmentClick(apartmentId, position);
            }
        });
        recyclerViewApartment.setAdapter(popDormListAdapter);
    }

    public interface OnSelectApartmentListener{
        public void onSelectApartmentClick(String apartmentId, int position);
    }

    public interface OnSelectAllApartmentListener {
        public void onSelectAllApartment();
    }

    public void notifyAllAdapter(List<Building> datas) {
        this.DormListData = datas;
        popDormListAdapter.updateDataSet(DormListData);
    }

    public void setOnSelectAllApartmentListener(final OnSelectAllApartmentListener listener) {
        tvSelectAllApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDormListAdapter.cancelSelectedItem();
                listener.onSelectAllApartment();
            }
        });
    }

}
