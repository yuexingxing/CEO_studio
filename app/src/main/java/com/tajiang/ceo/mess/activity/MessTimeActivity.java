package com.tajiang.ceo.mess.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.DensityUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.mess.view.LayoutAddMessTime;
import com.tajiang.ceo.mess.view.MessTimeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 营业时间
 * Created by Administrator on 2016/5/10.
 */
public class MessTimeActivity extends BaseActivity {

    private static final int REQUEST_CODE_MON_2_FRI = 0;
    private static final int REQUEST_CODE_SATURDAY = 1;
    private static final int REQUEST_CODE_SUNDAY = 2;

    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private boolean TimeMonToFri = false;
    private boolean TimeSaturday = false;
    private boolean TimeSunday = false;

    private List<LayoutAddMessTime> mLayoutList;  //索引为requestCode， 用于区分动态添加的不同LayoutMessTime

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_time);
        ButterKnife.bind(this);

        initMessTimeView();
    }

    @Override
    protected void initTopBar() {
        setTitle("营业时间");
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {
        if (mLayoutList == null) {
            mLayoutList = new ArrayList<LayoutAddMessTime>();
        }
    }

    private void initMessTimeView() {
        llRoot = (LinearLayout) findViewById(R.id.ll_root);

        addLayoutMessTime("周一到周五", REQUEST_CODE_MON_2_FRI);
        addLayoutMessTime("周六", REQUEST_CODE_SATURDAY);
        addLayoutMessTime("周日", REQUEST_CODE_SUNDAY);
    }

    private void addLayoutMessTime(String messDate, int requestCode) {
        final LayoutAddMessTime mLayout = new LayoutAddMessTime(this, requestCode);

        mLayout.setMessDate(messDate);
        mLayout.setTextListener(new LayoutAddMessTime.onTextViewClickListener() {
            @Override
            public void onTextClick() {
                //跳转到设置营业时间页面
                int reqCode = mLayout.getRequestCode();

                Intent intent = new Intent();
                intent.setClass(MessTimeActivity.this, EditMessTimeActivity.class);

                //根据requestCode区分不同LayoutMessTime
                switch (reqCode) {
                    case REQUEST_CODE_MON_2_FRI:
                        startActivityForResult(intent, REQUEST_CODE_MON_2_FRI);
                        break;
                    case REQUEST_CODE_SATURDAY:
                        startActivityForResult(intent, REQUEST_CODE_SATURDAY);
                        break;
                    case REQUEST_CODE_SUNDAY:
                        startActivityForResult(intent, REQUEST_CODE_SUNDAY);
                        break;
                    default:
                        break;
                }

            }
        });

        mLayoutList.add(requestCode, mLayout);  //索引为requestCode， 用于区分动态添加的不同LayoutMessTime
        llRoot.addView(mLayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //获取设置完毕后返回的时间数据
            Bundle bundle = data.getExtras();
            LayoutAddMessTime mSelectedLayout = mLayoutList.get(requestCode);

            if (mSelectedLayout != null) {
//                mSelectedLayout.addMessTimeTextView(createTextView(ParseTimeToString(bundle)));
                mSelectedLayout.addMessTimeTextView(new MessTimeView(this, ParseTimeToString(bundle)));
                //设置属性为已经设置
                switch (requestCode) {
                    case REQUEST_CODE_MON_2_FRI:
                        TimeMonToFri = true;
                        break;
                    case REQUEST_CODE_SATURDAY:
                        TimeSaturday = true;
                        break;
                    case REQUEST_CODE_SUNDAY:
                        TimeSunday = true;
                        break;
                    default:
                        break;
                }
            }
        }

    }

    public String ParseTimeToString(Bundle mBundle) {
        String mTimeString = mBundle.getString("start_hour")
                + ":"
                + mBundle.getString("start_minute")
                + "-"
                + mBundle.getString("end_hour")
                + ":"
                + mBundle.getString("end_minute");

        return mTimeString;
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmClick() {
        //判断时候已经设置完所有营业时间段（三个时间段）
        if (TimeMonToFri && TimeSaturday && TimeSunday) {
            //实例化 Bundle, 设置需要传递的是否已经设置参数
            Bundle mBundle = new Bundle();
            mBundle.putBoolean("is_finish_setting", true);

            MessTimeActivity.this.setResult(RESULT_OK, MessTimeActivity.this.getIntent().putExtras(mBundle));

            MessTimeActivity.this.finish();
        } else {
            ToastUtils.showShort("请设置完所有营业时间段");
        }
    }


//    //动态创建显示营业时间的TextView
//    private TextView createTextView(String messTime){
//        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        final TextView mTextViewShowTime = new TextView(getApplicationContext());
//        mTextViewShowTime.setText(messTime);
//        mTextViewShowTime.setPadding(DensityUtils.dp2px(getApplicationContext(), 15), 0, DensityUtils.dp2px(getApplicationContext(), 15), 0);
//        mTextViewShowTime.setLayoutParams(mLayoutParams);
//        mTextViewShowTime.setHeight(DensityUtils.dp2px(getApplicationContext(), 50));
//        mTextViewShowTime.setClickable(true);
//        mTextViewShowTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.slt_layout_rect_gray));
//        mTextViewShowTime.setGravity(Gravity.CENTER_VERTICAL);
//        mTextViewShowTime.setTextSize(16);
//        mTextViewShowTime.setTextColor(getResources().getColor(R.color.text_black_1));
//        Drawable drawable= getResources().getDrawable(R.mipmap.icon_jiantou_r);
//        /// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        mTextViewShowTime.setCompoundDrawables(null,null,drawable,null);
//
//        mTextViewShowTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.showShort("" + mTextViewShowTime.getText().toString());
//            }
//        });
//        return mTextViewShowTime;
//    }

}
