package com.tajiang.ceo.order.view.popwindow;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.model.SchoolApartment;
import com.tajiang.ceo.order.adapter.ApartmentListAdapter;
import com.tajiang.ceo.order.adapter.ApartmentZoneListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by work on 2016/7/13.
 */
public class SelectApartmentZonesPopWindow extends PopupWindow {

    private Context context;

    private LinearLayout viewClickDismiss;  //点击阴影区域退出弹窗

    private View contentView;
    private View rectBgSelectApartmentDialog;

    private TextView tvSelectAllApartment;  //点击选择全部宿舍楼
    private ListView lvRectApartment;
    private ListView lvApartment;

    private ApartmentZoneListAdapter apartmentZoneListAdapter;
    private ApartmentListAdapter apartmentListAdapter;

    private ApartmentZone apartmentZoneSelected;
    private Building buildingSelected;

    private OnSelectApartmentListener onSelectApartmentListener;

    private SchoolApartment schoolApartment;

    public SelectApartmentZonesPopWindow(View contentView,SchoolApartment schoolApartment, int width, int height, Context context, boolean focusable) {
        super(contentView, width, height, focusable);
        this.contentView = contentView;
        this.context = context;
        this.schoolApartment = (schoolApartment == null? initSchoolApartment(schoolApartment): schoolApartment);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        initView();
    }

    public void setSchoolApartment(SchoolApartment schoolApartment) {
        this.schoolApartment = schoolApartment;
    }

    private SchoolApartment initSchoolApartment(SchoolApartment schoolApartment) {

        schoolApartment = new SchoolApartment();
        ApartmentZone apartmentZone = new ApartmentZone();

        List<ApartmentZone> zoneList = new ArrayList<ApartmentZone>();
        ArrayList<Building> buildingList = new ArrayList<Building>();

        apartmentZone.setName("");
        apartmentZone.setList(buildingList);
        zoneList.add(apartmentZone);

        schoolApartment.setName("");
        schoolApartment.setZonesList(zoneList);

        return schoolApartment;
    }


    private void initView() {
//        contentView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//                .inflate(R.layout.dialog_user_adress_add_select_apartment, null);
        tvSelectAllApartment = (TextView) contentView.findViewById(R.id.tv_select_all_apartment);
        lvRectApartment = (ListView) contentView.findViewById(R.id.lvRectApartment);
        lvApartment = (ListView) contentView.findViewById(R.id.lvApartment);
        viewClickDismiss = (LinearLayout) contentView.findViewById(R.id.ll_click_dismiss);
        viewClickDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectApartmentZonesPopWindow.this.dismiss();
            }
        });

//        TextView tvNameSchoolApartmentDialog = (TextView) contentView.findViewById(R.id.tvNameSchoolApartmentDialog);
//        tvNameSchoolApartmentDialog.setText(schoolApartment.getName());

        if(schoolApartment != null && schoolApartment.getZonesList().size() >=1){
            apartmentZoneListAdapter = new ApartmentZoneListAdapter(context, schoolApartment.getZonesList(), R.layout.layout_title_list_title_content);
            lvRectApartment.setAdapter(apartmentZoneListAdapter);

            lvRectApartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    apartmentZoneSelected = (ApartmentZone) parent.getItemAtPosition(position);

                    apartmentZoneListAdapter.setSelected(position);
                    selectApartmentZone(SelectApartmentZonesPopWindow.this.context, SelectApartmentZonesPopWindow.this.schoolApartment.getZonesList().get(position).getList());

                    if (apartmentListAdapter != null) {
                        apartmentListAdapter.cannelSelect();
                    }
                }
            });
            // 默认选中第一个寝室区
            apartmentZoneSelected = schoolApartment.getZonesList().get(0);
            selectApartmentZone(context, schoolApartment.getZonesList().get(0).getList());
        }

    }

    // 选中一个寝室区，刷新右边的寝室楼信息
    private void selectApartmentZone(Context context, List<Building> BuildingList) {
        apartmentListAdapter = new ApartmentListAdapter(context, BuildingList, R.layout.layout_title_list_content);
        lvApartment.setAdapter(apartmentListAdapter);
        lvApartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                buildingSelected = (Building) parent.getItemAtPosition(position);

                if (onSelectApartmentListener != null) {
                    onSelectApartmentListener.onSelectApartment(schoolApartment , apartmentZoneSelected , buildingSelected);
                    dismiss();
                }

                apartmentListAdapter.setSelect(position);

            }
        });
    }

    public OnSelectApartmentListener getOnSelectApartmentListener() {
        return onSelectApartmentListener;
    }

    public void setOnSelectApartmentListener(OnSelectApartmentListener onSelectApartmentListener) {
        this.onSelectApartmentListener = onSelectApartmentListener;
    }

    public void setOnSelectAllApartmentListener(final OnSelectAllApartmentListener listener) {

        tvSelectAllApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onSelectAllApartment();
                if (apartmentListAdapter != null) {
                    apartmentListAdapter.cannelSelect();  //选择全部宿舍楼，重置楼栋区位未选状态
                }
            }
        });

    }

    public interface OnSelectApartmentListener{
        public void onSelectApartment(SchoolApartment schoolApartment, ApartmentZone apartmentZone, Building building);
    }

    public interface OnSelectAllApartmentListener{
        public void onSelectAllApartment();
    }

    public void notifyAllAdapter(SchoolApartment schoolApartment) {
        this.schoolApartment = schoolApartment;
        //TODO..................
    }
}
