package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.WheelView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by work on 2016/7/1.
 */
public class EditMessTimeActivity extends BaseActivity {

    private static final int SPLIT_MINUTE = 5;  //默认的配送时间的minute分割段时长  5 分钟
    private static final int START_TIME = 1;   //当前为设置开始时间状态
    private static final int END_TIME = 0;     //当前为设置结束时间状态

    Bundle mBundle;
    private boolean isAddTimeMode = false;
    private boolean isModifyMode = false;  //判断是否为修改模式
    private String StrMessTime;

    @BindView(R.id.wv_choose_hour)
    WheelView wvChooseHour;
    @BindView(R.id.wv_choose_minute)
    WheelView wvChooseMinute;
    @BindView(R.id.btn_start_time)
    Button btnStartTime;
    @BindView(R.id.btn_end_time)
    Button btnEndTime;
    @BindView(R.id.btn_clear_time)
    Button btnClearTime;
    @BindView(R.id.ll_confirm_time)
    LinearLayout llConfirmTime;
    @BindView(R.id.tv_start_time_hour)
    TextView tvStartTimeHour;
    @BindView(R.id.tv_start_time_minute)
    TextView tvStartTimeMinute;
    @BindView(R.id.tv_end_time_hour)
    TextView tvEndTimeHour;
    @BindView(R.id.tv_end_time_minute)
    TextView tvEndTimeMinute;
    @BindView(R.id.ll_time)
    LinearLayout llTime;

    private int mCurrentTimeState = START_TIME;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mess_time);
        ButterKnife.bind(this);
        initWheelView();

        if (getIntent().hasExtra("is_add_time")) {
            isAddTimeMode = getIntent().getBooleanExtra("is_add_time", false);
            btnClearTime.setVisibility(View.GONE);
            initDefaultTimeToDisplay();
        }
        if (getIntent().hasExtra("is_modify_mode")) {
            isModifyMode = getIntent().getBooleanExtra("is_modify_mode", false);
            StrMessTime = getIntent().getStringExtra("mess_time");
            parseMessTime(StrMessTime);
        }
    }

    /**
     * 显示默认的营业时间
     */
    private void initDefaultTimeToDisplay() {
        wvChooseHour.setSeletion(10);
        wvChooseMinute.setSeletion(6);
        tvStartTimeHour.setText("10");
        tvStartTimeMinute.setText("30");
        tvEndTimeHour.setText("12");
        tvEndTimeMinute.setText("30");
    }

    /**
     * 解析时间字符串类型
     * 时间格式统一为：12:12-12:12
     * @param strMessTime
     */
    private void parseMessTime(String strMessTime) {
        String[] list = strMessTime.split("-");

        String[] strStartTimeList = list[0].split(":");
        String[] strEndTimeList = list[1].split(":");

        try {
            wvChooseHour.setSeletion(Integer.valueOf(strStartTimeList[0]).intValue());
            wvChooseMinute.setSeletion(Integer.valueOf(strStartTimeList[1]).intValue() / SPLIT_MINUTE);
        } catch(NumberFormatException e) {
            LogUtils.e(e.toString());
        }

        tvStartTimeHour.setText(strStartTimeList[0]);
        tvStartTimeMinute.setText(strStartTimeList[1]);

        tvEndTimeHour.setText(strEndTimeList[0]);
        tvEndTimeMinute.setText(strEndTimeList[1]);

        llTime.setVisibility(LinearLayout.VISIBLE);
    }

    private void initWheelView() {
        wvChooseHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {

                if (llTime.getVisibility() == LinearLayout.GONE){
                    llTime.setVisibility(LinearLayout.VISIBLE);
                }

                if (mCurrentTimeState == START_TIME) {
                    tvStartTimeHour.setText(item);
                } else {
                    tvEndTimeHour.setText(item);
                }
            }
        });

        wvChooseMinute.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {

                if (llTime.getVisibility() == LinearLayout.GONE){
                    llTime.setVisibility(LinearLayout.VISIBLE);
                }

                if (mCurrentTimeState == START_TIME) {
                    tvStartTimeMinute.setText(item);
                } else {
                    tvEndTimeMinute.setText(item);
                }
            }
        });

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("0" + String.valueOf(i));
        }
        for (int i = 10; i < 24; i++) {
            list.add(String.valueOf(i));
        }
        wvChooseHour.setOffset(1);
        wvChooseHour.setItems(list);

        list.clear();
        list.add("00");
        list.add("05");
        for (int i = 10; i <= 55; i += 5) {
            list.add(String.valueOf(i));
        }
        wvChooseMinute.setOffset(1);
        wvChooseMinute.setItems(list);
    }

    @Override
    protected void initTopBar() {
        setTitle("配送时间");
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_start_time)
    public void onTimeStartClick() {
        try {
            wvChooseHour.setSeletion(Integer.valueOf(tvStartTimeHour.getText().toString()));
            wvChooseMinute.setSeletion(Integer.valueOf(tvStartTimeMinute.getText().toString()) / SPLIT_MINUTE);
        } catch(NumberFormatException e) {
            LogUtils.e(e.toString());
        }
        mCurrentTimeState = START_TIME;

        btnStartTime.setBackgroundResource(R.drawable.shape_line_backround_green);
        btnEndTime.setBackgroundResource(R.drawable.shape_line_round_green);

        btnEndTime.setTextColor(EditMessTimeActivity.this.getResources()
                .getColor(R.color.green_light));
        btnStartTime.setTextColor(EditMessTimeActivity.this.getResources()
                .getColor(R.color.white));

    }

    @OnClick(R.id.btn_end_time)
    public void onTimeEndClick() {
        try {
            wvChooseHour.setSeletion(Integer.valueOf(tvEndTimeHour.getText().toString()));
            wvChooseMinute.setSeletion(Integer.valueOf(tvEndTimeMinute.getText().toString()) / SPLIT_MINUTE);
        } catch(NumberFormatException e) {
            LogUtils.e(e.toString());
        }
        mCurrentTimeState = END_TIME;

        btnEndTime.setBackgroundResource(R.drawable.shape_line_backround_green);
        btnStartTime.setBackgroundResource(R.drawable.shape_line_round_green);

        btnEndTime.setTextColor(EditMessTimeActivity.this.getResources()
                .getColor(R.color.white));
        btnStartTime.setTextColor(EditMessTimeActivity.this.getResources()
                .getColor(R.color.green_light));
    }

    @OnClick(R.id.btn_clear_time)
    public void onTimeClearClick() {
        //实例化 Bundle, 设置需要传递的营业时间参数
        mBundle = new Bundle();
        if (isModifyMode == true) {
            mBundle.putBoolean("is_delete_mode", true);
        }

        EditMessTimeActivity.this.setResult(RESULT_OK, EditMessTimeActivity.this.getIntent().putExtras(mBundle));

        EditMessTimeActivity.this.finish();
    }

    @OnClick(R.id.ll_confirm_time)
    public void onConfirmTimeClick() {

        if (CheckFinish() == true) { //检查时间设置是否完成

            if (CheckTime() == true) {//检查时间设置是否正确

                //实例化 Bundle, 设置需要传递的营业时间参数
                mBundle = new Bundle();
                if (isModifyMode == true) {
                    mBundle.putBoolean("is_modify_mode", true);
                }
                mBundle.putString("start_hour", tvStartTimeHour.getText().toString());
                mBundle.putString("start_minute", tvStartTimeMinute.getText().toString());
                mBundle.putString("end_hour", tvEndTimeHour.getText().toString());
                mBundle.putString("end_minute", tvEndTimeMinute.getText().toString());

                EditMessTimeActivity.this.setResult(RESULT_OK, EditMessTimeActivity.this.getIntent().putExtras(mBundle));

                EditMessTimeActivity.this.finish();
            } else {
                ToastUtils.showShort("时间设置不正确！");
            }

        } else {
            ToastUtils.showShort("您还未设置完毕营业时间！");
        }
    }

    private Boolean CheckFinish() {
        if (tvStartTimeHour.getText().toString().equals("") || tvStartTimeHour.getText() == null) {
            return false;
        } else if (tvStartTimeMinute.getText().toString().equals("") || tvStartTimeMinute.getText() == null){
            return false;
        } else if (tvEndTimeHour.getText().toString().equals("") || tvEndTimeHour.getText() == null){
            return false;
        } else if (tvEndTimeMinute.getText().toString().equals("") || tvEndTimeMinute.getText() == null){
            return false;
        }
        return true;
    }

    //对时间设置的正确性进行校验
    private boolean CheckTime() {

        int StartHour = Integer.parseInt(tvStartTimeHour.getText().toString());
        int StartMinute = Integer.parseInt(tvStartTimeMinute.getText().toString());
        int EndHour = Integer.parseInt(tvEndTimeHour.getText().toString());
        int EndMinute = Integer.parseInt(tvEndTimeMinute.getText().toString());

        if (EndHour < StartHour) {
            return false;
        }

        if (EndHour == StartHour) {
            if (EndMinute < StartMinute) {
                return false;
            }
        }
        return true;
    }
}
