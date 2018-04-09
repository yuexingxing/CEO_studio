package com.tajiang.ceo.statistic.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.mess.adapter.DeliverDataAdapter;
import com.tajiang.ceo.model.Deliver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admins on 2017/3/10.
 */

public class OrderStatisticFragment extends BaseFragment {

    @BindView(R.id.tv_date_start)
    TextView tvDateStart;
    @BindView(R.id.tv_date_end)
    TextView tvDateEnd;
    @BindView(R.id.ll_content_statistic)
    LinearLayout llContentStatistic;
    @BindView(R.id.ll_content_deliver)
    LinearLayout llContentDeliver;
    @BindView(R.id.rv_deliver_data)
    RecyclerView rvDeliverData;

    private int mStartYear, mStartMonth, mStartDay;
    private int mEndYear, mEndMonth, mEndDay;

    private DeliverDataAdapter mDeliverAdapter;

    @Override
    protected void initTopBar() {
        setTitle("全部统计数据");
        setTitleRightImg(R.mipmap.icon_jiantou_r);
        setOnTitleClick(new OnTitleClick() {
            @Override
            public void titleClick() {
                if (UserUtils.getUser().getRoleType() != TJApp.RULES_SCHOOL_CEO) return;
                if (llContentStatistic.getVisibility() == View.VISIBLE) {
                    updateTitleText("配送员数据");
                    llContentStatistic.setVisibility(View.GONE);
                    llContentDeliver.setVisibility(View.VISIBLE);
                } else {
                    updateTitleText("全部统计数据");
                    llContentStatistic.setVisibility(View.VISIBLE);
                    llContentDeliver.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.fragment_order_statistic);
    }

    @Override
    protected void initData() {
        llContentDeliver.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance();
        mStartYear = mEndYear = calendar.get(Calendar.YEAR);
        mStartMonth = mEndMonth = calendar.get(Calendar.MONTH);
        mStartDay = mEndDay = calendar.get(Calendar.DAY_OF_MONTH);
        tvDateStart.setText(mStartYear + "-" + (mStartMonth + 1) + "-" + mStartDay);
        tvDateEnd.setText(mEndYear + "-" + (mEndMonth + 1) + "-" + mEndDay);
        initRecyclerList();
    }

    /**
     * 测试数据
     */
    private void initRecyclerList() {
        List<Deliver> data = new ArrayList<>();
        Deliver deliver;
        for (int i = 0; i < 20; i++) {
            deliver = new Deliver();
            deliver.setName("测试数据");
            deliver.setAccount("187236726");
            deliver.setOrderCount(123);
            deliver.setCopies(12);
            data.add(deliver);
        }
        mDeliverAdapter = new DeliverDataAdapter(data, getActivity().getApplicationContext());
        rvDeliverData.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDeliverData.setAdapter(mDeliverAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    //R.id.tv_date_end
    @OnClick(R.id.tv_date_end)
    public void onTvDateEndClick() {
        new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year < mStartYear || monthOfYear < mStartMonth || dayOfMonth < mStartDay) {
                            ToastUtils.showShort("时间设置有误");
                        } else {
                            mEndYear = year;
                            mEndMonth = monthOfYear;
                            mEndDay = dayOfMonth;
                            tvDateEnd.setText(mEndYear + "-" + (mEndMonth + 1) + "-" + mEndDay);
                            //TODO..HTTP请求..选完时间（已经检查过时间的准确性了）更新所选时间段内的统计数据.......



                        }
                    }
                }, mEndYear, mEndMonth, mEndDay).show();
    }

    @OnClick(R.id.tv_date_start)
    public void onTvDateStartClick() {
        new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year > mEndYear || monthOfYear > mEndMonth || dayOfMonth > mEndDay) {
                            ToastUtils.showShort("时间设置有误");
                        } else {
                            mStartYear = year;
                            mStartMonth = monthOfYear;
                            mStartDay = dayOfMonth;
                            tvDateStart.setText(mStartYear + "-" + (mStartMonth + 1) + "-" + mStartDay);
                            //TODO....HTTP请求....选完时间（已经检查过时间的准确性了）更新所选时间段内的统计数据.......


                        }
                    }
                }, mStartYear, mStartMonth, mStartDay).show();
    }

}
