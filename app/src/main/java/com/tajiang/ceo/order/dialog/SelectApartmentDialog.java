package com.tajiang.ceo.order.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.model.ApartmentZone;

import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.model.SchoolApartment;
import com.tajiang.ceo.order.adapter.ApartmentListAdapter;
import com.tajiang.ceo.order.adapter.ApartmentZoneListAdapter;




import java.util.List;

public class SelectApartmentDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private View contentView;
    private View rectBgSelectApartmentDialog;

    private ListView lvRectApartment;
    private ListView lvApartment;

    private ApartmentZoneListAdapter apartmentZoneListAdapter;
    private ApartmentListAdapter apartmentListAdapter;

    private ApartmentZone apartmentZoneSelected;
    private Building buildingSelected;

    private OnSelectApartmentListener onSelectApartmentListener;

    private SchoolApartment schoolApartment;

    public SelectApartmentDialog(Context context, SchoolApartment schoolApartment) {
        super(context);
        this.context = context;
        this.schoolApartment = schoolApartment;

        contentView = getLayoutInflater().inflate(R.layout.dialog_user_adress_add_select_apartment, null);
//        rectBgSelectApartmentDialog = contentView.findViewById(R.id.rectBgSelectApartmentDialog);

        lvRectApartment = (ListView) contentView.findViewById(R.id.lvRectApartment);
        lvApartment = (ListView) contentView.findViewById(R.id.lvApartment);

        if(schoolApartment != null && schoolApartment.getZonesList().size() >=1){
            apartmentZoneListAdapter = new ApartmentZoneListAdapter(context, schoolApartment.getZonesList(), R.layout.layout_title_list_content);
            lvRectApartment.setAdapter(apartmentZoneListAdapter);

            lvRectApartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    apartmentZoneSelected = (ApartmentZone) parent.getItemAtPosition(position);

                    apartmentZoneListAdapter.setSelected(position);
                    selectApartmentZone(SelectApartmentDialog.this.context, SelectApartmentDialog.this.schoolApartment.getZonesList().get(position).getList());

                    apartmentListAdapter.cannelSelect();
                }
            });
            // 默认选中第一个寝室区
            apartmentZoneSelected = schoolApartment.getZonesList().get(0);
            selectApartmentZone(context, schoolApartment.getZonesList().get(0).getList());
        }

//        rectBgSelectApartmentDialog.setOnClickListener(this);
        setContentView(contentView);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.rectBgSelectApartmentDialog:
//                dismiss();
//                break;
        }
    }

    public interface OnSelectApartmentListener{
        public void onSelectApartment(SchoolApartment schoolApartment, ApartmentZone apartmentZone, Building building);
    }

}
