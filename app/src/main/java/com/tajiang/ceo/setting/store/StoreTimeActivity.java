package com.tajiang.ceo.setting.store;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.dialog.CommonDialog;
import com.tajiang.ceo.common.utils.CommandTools;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.model.StoreTime;
import com.tajiang.ceo.setting.dialog.SelectTwoTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 营业时间设置
 */
public class StoreTimeActivity extends BaseActivity {

    @BindView(R.id.lv_public)
    ListView listView_1;
    CommonAdapter<StoreTime> mAdapter_1;
    private List<StoreTime> dataList_1 = new ArrayList<>();

    @BindView(R.id.lv_public_2)
    ListView listView_2;
    CommonAdapter<StoreTime> mAdapter_2;
    private List<StoreTime> dataList_2 = new ArrayList<>();

    @BindView(R.id.lv_public_3)
    ListView listView_3;
    CommonAdapter<StoreTime> mAdapter_3;
    private List<StoreTime> dataList_3 = new ArrayList<>();

    private String store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("配送时间");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_store_time);
    }

    @Override
    protected void initData() {

        store_id = getIntent().getStringExtra("store_id");

        listView_1.setDivider(null);
        listView_1.setAdapter(mAdapter_1 = new CommonAdapter<StoreTime>(this, dataList_1, R.layout.item_store_time) {

            @Override
            public void convert(ViewHolder helper, StoreTime item) {

                helper.setText(R.id.item_store_time_time, item.getStartTime() + "-" + item.getEndTime());
            }
        });
        listView_1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CommonDialog.showDialog(StoreTimeActivity.this, "取消", "确认", "确认删除该条记录", new CommonDialog.DialogCallback() {

                    @Override
                    public void callback(int pos) {

                        if (pos == 1){
                            dataList_1.remove(position);
                            mAdapter_1.notifyDataSetChanged();
                        }
                    }
                });

                return false;
            }
        });

        listView_2.setDivider(null);
        listView_2.setAdapter(mAdapter_2 = new CommonAdapter<StoreTime>(this, dataList_2, R.layout.item_store_time) {

            @Override
            public void convert(ViewHolder helper, StoreTime item) {

                helper.setText(R.id.item_store_time_time, item.getStartTime() + "-" + item.getEndTime());
            }
        });
        listView_2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CommonDialog.showDialog(StoreTimeActivity.this, "取消", "确认", "确认删除该条记录", new CommonDialog.DialogCallback() {

                    @Override
                    public void callback(int pos) {

                        if (pos == 1){
                            dataList_2.remove(position);
                            mAdapter_2.notifyDataSetChanged();
                        }
                    }
                });

                return false;
            }
        });


        listView_3.setDivider(null);
        listView_3.setAdapter(mAdapter_3 = new CommonAdapter<StoreTime>(this, dataList_3, R.layout.item_store_time) {

            @Override
            public void convert(ViewHolder helper, StoreTime item) {

                helper.setText(R.id.item_store_time_time, item.getStartTime() + "-" + item.getEndTime());
            }
        });
        listView_3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CommonDialog.showDialog(StoreTimeActivity.this, "取消", "确认", "确认删除该条记录", new CommonDialog.DialogCallback() {

                    @Override
                    public void callback(int pos) {

                        if (pos == 1){
                            dataList_3.remove(position);
                            mAdapter_3.notifyDataSetChanged();
                        }
                    }
                });

                return false;
            }
        });

    }

    @OnClick(R.id.store_time_add_1)
    public void add_1(){

        SelectTwoTime birth = new SelectTwoTime(this, new SelectTwoTime.TimeCallback() {
            @Override
            public void callback(boolean flag, int startHour, int startMinute, int endHour, int endMinute) {

                StoreTime storeTime = new StoreTime();
                storeTime.setStartTime(startHour + ":" + startMinute + ":" + "00");
                storeTime.setEndTime(endHour + ":" + endMinute + ":" + "00");
                storeTime.setWeekDay("12345");

                dataList_1.add(storeTime);
                mAdapter_1.notifyDataSetChanged();
                CommandTools.setListViewHeightBasedOnChildren(listView_1);
            }
        });
        birth.showAtLocation(this.findViewById(R.id.activity_store_time_root), Gravity.BOTTOM, 0, 0);
    }

    @OnClick(R.id.store_time_add_2)
    public void add_2(){

        SelectTwoTime birth = new SelectTwoTime(this, new SelectTwoTime.TimeCallback() {
            @Override
            public void callback(boolean flag, int startHour, int startMinute, int endHour, int endMinute) {

                StoreTime storeTime = new StoreTime();
                storeTime.setStartTime(startHour + ":" + startMinute + ":" + "00");
                storeTime.setEndTime(endHour + ":" + endMinute + ":" + "00");
                storeTime.setWeekDay("6");

                dataList_2.add(storeTime);
                mAdapter_2.notifyDataSetChanged();
                CommandTools.setListViewHeightBasedOnChildren(listView_2);
            }
        });
        birth.showAtLocation(this.findViewById(R.id.activity_store_time_root), Gravity.BOTTOM, 0, 0);
    }

    @OnClick(R.id.store_time_add_3)
    public void add_3(){

        SelectTwoTime birth = new SelectTwoTime(this, new SelectTwoTime.TimeCallback() {
            @Override
            public void callback(boolean flag, int startHour, int startMinute, int endHour, int endMinute) {

                StoreTime storeTime = new StoreTime();
                storeTime.setStartTime(startHour + ":" + startMinute + ":" + "00");
                storeTime.setEndTime(endHour + ":" + endMinute + ":" + "00");
                storeTime.setWeekDay("7");

                dataList_3.add(storeTime);
                mAdapter_3.notifyDataSetChanged();
                CommandTools.setListViewHeightBasedOnChildren(listView_3);
            }
        });
        birth.showAtLocation(this.findViewById(R.id.activity_store_time_root), Gravity.BOTTOM, 0, 0);
    }
    @OnClick(R.id.store_time_save)
    public void save(){

        JSONArray jsonArray = new JSONArray();

        List<StoreTime> allList = new ArrayList<>();
        allList.addAll(dataList_1);
        allList.addAll(dataList_2);
        allList.addAll(dataList_3);

        for(int i=0; i<allList.size(); i++){

            StoreTime storeTime = allList.get(i);
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("startTime", storeTime.getStartTime());
                jsonObject.put("endTime", storeTime.getEndTime());
                jsonObject.put("weekDay", storeTime.getWeekDay());

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("bussTime", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostDataTools.modify_shop_busstime(this, store_id, jsonObject2, new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {

                if(flag){
                    dataList_1.clear();
                    dataList_2.clear();
                    dataList_3.clear();

                    dataList_1 = null;
                    dataList_2 = null;
                    dataList_3 = null;

                    finish();
                }
            }
        });
    }

    protected void onResume(){
        super.onResume();

        PostDataTools.shop_busstime(this, store_id, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                List<StoreTime> list = (List<StoreTime>) object;
                for(int i=0; i<list.size(); i++){

                    StoreTime storeTime = list.get(i);
                    if (storeTime.getWeekDay().equals("12345")){
                        dataList_1.add(storeTime);
                    }else if(storeTime.getWeekDay().equals("6")){
                        dataList_2.add(storeTime);
                    }else if(storeTime.getWeekDay().equals("7")){
                        dataList_3.add(storeTime);
                    }
                }

                mAdapter_1.notifyDataSetChanged();
                mAdapter_2.notifyDataSetChanged();
                mAdapter_3.notifyDataSetChanged();

                CommandTools.setListViewHeightBasedOnChildren(listView_1);
                CommandTools.setListViewHeightBasedOnChildren(listView_2);
                CommandTools.setListViewHeightBasedOnChildren(listView_3);
            }
        });
    }

}
