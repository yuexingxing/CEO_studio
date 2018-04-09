package com.tajiang.ceo.mess.adapter;

import android.content.Intent;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.widget.FlowLayout;
import com.tajiang.ceo.mess.activity.DormInfoActivity;
import com.tajiang.ceo.mess.view.LayoutAddBuildingNumber;
import com.tajiang.ceo.mess.activity.SetApartmentActivity;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.model.Dorm;

import java.util.List;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by work on 2016/6/30.
 */
public class DormListAdapter extends RecyclerView.Adapter<DormListAdapter.DormListViewHolder> {

    private  List<Integer> Binedlist;
    private Context context;
    private List<ApartmentZone> mDatas;
    private LayoutInflater layoutInflater;
    LinearLayout.LayoutParams mLayoutParams;

    public DormListAdapter(List<ApartmentZone> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.Binedlist = new ArrayList<Integer>();
    }

    public void notifyAllDataChanged(List<ApartmentZone> Data) {
        this.mDatas.clear();
        this.mDatas.addAll(Data);
        notifyDataSetChanged();
    }

    public void addAllAndnotifyDataChanged(List<ApartmentZone> Data) {
        this.mDatas.addAll(Data);
        notifyDataSetChanged();
    }

    @Override
    public DormListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dorm_selected, parent, false);
        DormListViewHolder holder = new DormListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DormListViewHolder holder, int position) {
        ApartmentZone dorm = mDatas.get(position);
        holder.tvDormId.setText(dorm.getName());
        List<Building> list = dorm.getList();//当前宿舍区楼栋号列表
        /**
         * 清楚上一次Item中残留的楼栋号View
         * 防止当前的Item中被被重复addView楼栋号
         */
        holder.flRootViewBuilding.removeAllViews();

        if (list != null) {
            if (list.size() != 0 && holder.flRootViewBuilding.getChildCount() == 0) {
                for (int i = 0; i < list.size(); i++) {
                    holder.flRootViewBuilding.addView(createBuildingView(context, list.get(i), i));
                }
            }
        }
    }

    private View createBuildingView(Context context, Building building, int i) {

        final LayoutAddBuildingNumber linearLayout = new LayoutAddBuildingNumber(context);
        linearLayout.setNameEditTextBuilding(building.getName());
        linearLayout.setBuildingEditTextEnable(false);
        if (building.getState() == Building.BUILDING_STATE_OPENED) {
            linearLayout.setEditBuildingBackground(R.drawable.shape_rect_round_green_state);
        }
        return linearLayout;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void notifyDatas(List<ApartmentZone> datas) {
        this.mDatas = datas;
        this.notifyDataSetChanged();
    }

    class DormListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_dorm_id)
        TextView tvDormId;
        @BindView(R.id.fl_root_view_building)
        FlowLayout flRootViewBuilding;
        @BindView(R.id.tv_click_to_edit)
        TextView tvClickToEdit;

        private long currentTime = 0;  //用户600ms内只能响应用户一次点击事件，此方式仅解决用户手抖造成点开2次activity

        public DormListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            initListener();
        }

        private void initListener() {
            tvClickToEdit.setOnClickListener(this);
        }

        public Context getContext() {
            return itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_click_to_edit:
                    if ((System.currentTimeMillis() - currentTime) < 600) return;
                    currentTime = System.currentTimeMillis();

                    ApartmentZone currentDorm = mDatas.get(getLayoutPosition()); //获取当前被点击的Dorm对象
                    Intent intent = new Intent(getContext(), SetApartmentActivity.class);

                    ArrayList<Building> list = currentDorm.getList();

                    intent.putExtra("id", currentDorm.getId());
                    intent.putExtra("schoolId", currentDorm.getSchoolId());
                    intent.putExtra("dorm_name", currentDorm.getName());
                    intent.putParcelableArrayListExtra("building_list", list);

                    ((DormInfoActivity)getContext()).startActivityForResult(intent, 0);
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

