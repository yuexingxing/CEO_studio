package com.tajiang.ceo.mess.activity;

/**
 * Created by work on 2016/7/5.
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.DensityUtils;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.mess.view.LayoutAddMessTime;
import com.tajiang.ceo.mess.view.MessTimeView;
import com.tajiang.ceo.model.MessTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 营业时间
 * Created by Administrator on 2016/5/10.
 */
public class SetMessTimeActivity extends BaseActivity implements HttpResponseListener {

    private static final int REQUEST_CODE_MON_2_FRI = 0;
    private static final int REQUEST_CODE_SATURDAY = 1;
    private static final int REQUEST_CODE_SUNDAY = 2;

    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private LoadingDialog loadingDialog;

    private List<MessTime> messTimelist;

    private String schoolId;
    private TextView currentTextView;
    private List<LayoutAddMessTime> mLayoutList;  //索引为requestCode， 用于区分动态添加的不同LayoutMessTime

    //存储从后台获取的三个营业时间列表
    private List<MessTime.Time> timeListMon;
    private List<MessTime.Time> timeListSat;
    private List<MessTime.Time> timeListSun;

    //存储(手动添加)和(来自后台)的营业时间显示View
    private List<TextView> textViewListMon;
    private List<TextView> textViewListSat;
    private List<TextView> textViewListSun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_time);
        ButterKnife.bind(this);

        initMessTimeView();
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
        this.loadingDialog = new LoadingDialog(this);

        if (mLayoutList == null) {
            mLayoutList = new ArrayList<LayoutAddMessTime>();
            timeListMon = new ArrayList<MessTime.Time>();
            timeListSat = new ArrayList<MessTime.Time>();
            timeListSun = new ArrayList<MessTime.Time>();

            textViewListMon = new ArrayList<TextView>();
            textViewListSat = new ArrayList<TextView>();
            textViewListSun = new ArrayList<TextView>();
        }
    }

    /**
     * 发送营业时间的Json数据
     */
    @OnClick(R.id.btn_confirm)
    public void onConfirmClick() {
        Gson gson = new Gson();
        List<MessTime> list = new ArrayList<MessTime>();

        MessTime messTime = new MessTime();
        messTime.setWeekDay(1);
        messTime.setList(timeListMon);
        list.add(messTime);

        messTime = new MessTime();
        messTime.setWeekDay(6);
        messTime.setList(timeListSat);
        list.add(messTime);

        messTime = new MessTime();
        messTime.setWeekDay(0);
        messTime.setList(timeListSun);
        list.add(messTime);

        LogUtils.e(gson.toJson(list));
        loadingDialog.show();
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                if ((Boolean)response.getData() == true) {
                    ToastUtils.showShort("保存成功！");
                    SetMessTimeActivity.this.finish();
                } else {
                    ToastUtils.showShort(response.getMoreInfo());
                }
            }

            @Override
            public void onFailed(BaseResponse response) {

            }

            @Override
            public void onFinish() {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        }).updateSchoolSuchedule(gson.toJson(list), schoolId);
    }

    private void initMessTimeView() {
        schoolId = UserUtils.getUser().getSchoolId();
        llRoot = (LinearLayout) findViewById(R.id.ll_root);

        addLayoutMessTime("周一到周五", REQUEST_CODE_MON_2_FRI);
        addLayoutMessTime("周六", REQUEST_CODE_SATURDAY);
        addLayoutMessTime("周日", REQUEST_CODE_SUNDAY);

        //获取食堂营业时间请求
        loadingDialog.show();
        new HttpHandler(this).getSchoolTime(schoolId);
    }

    private void addLayoutMessTime(String messDate, int requestCode) {
        final LayoutAddMessTime mLayout = new LayoutAddMessTime(this, requestCode);

        mLayout.setMessDate(messDate);
        mLayout.setTextListener(new LayoutAddMessTime.onTextViewClickListener() {
            @Override
            public void onTextClick() {
                if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
                currentTime = System.currentTimeMillis();
                //跳转到设置营业时间页面
                int reqCode = mLayout.getRequestCode();
                Intent intent = new Intent();
                intent.putExtra("is_add_time",true);
                intent.setClass(SetMessTimeActivity.this, EditMessTimeActivity.class);
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

    /**
     * 更新当前被删除元素之后的所有元素Tag位置往前移一位(position - 1)
     * @param tag
     * @param textViewList
     * @param timeList
     */
    private void updateAllTextViewPosition(MyTextViewTag tag, LayoutAddMessTime LayoutMessTime,
                                           List<TextView> textViewList, List<MessTime.Time> timeList) {
        for (int i = tag.getmPosition() + 1; i < textViewList.size(); i++) {
            TextView textView = textViewList.get(i);

            MyTextViewTag mTag = (MyTextViewTag) textView.getTag();
            mTag.updatemPosition(i - 1);
            textView.setTag(mTag);

            textViewList.set(i, textView);
        }

        LayoutMessTime.removeMessTimeTextView(textViewList.get(tag.getmPosition()), tag.getmPosition());
        timeList.remove(tag.getmPosition());
        textViewList.remove(tag.getmPosition());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //获取设置完毕后返回的时间数据
            Bundle bundle = data.getExtras();
            MyTextViewTag tag;
            LayoutAddMessTime LayoutMessTime = mLayoutList.get(requestCode);

            if (bundle.getBoolean("is_delete_mode")) {  //删除此营业时间
                tag = (MyTextViewTag) currentTextView.getTag();
                switch (requestCode) {  //更新当前被删除元素之后的所有元素索引position
                    case REQUEST_CODE_MON_2_FRI:
                        updateAllTextViewPosition(tag, LayoutMessTime, textViewListMon, timeListMon);
                        break;
                    case REQUEST_CODE_SATURDAY:
                        updateAllTextViewPosition(tag, LayoutMessTime, textViewListSat, timeListSat);
                        break;
                    case REQUEST_CODE_SUNDAY:
                        updateAllTextViewPosition(tag, LayoutMessTime, textViewListSun, timeListSun);
                        break;
                    default:
                        break;
                }
                return;
            }

            if (bundle.getBoolean("is_modify_mode")) {
                tag = (MyTextViewTag) currentTextView.getTag();
                currentTextView.setText(ParseTimeToString(true,bundle, requestCode));
                //将修改后的时间放回List中
                switch (requestCode) {
                    case REQUEST_CODE_MON_2_FRI:
                        modifyMessTime(bundle, timeListMon, tag.getmPosition());
                        break;
                    case REQUEST_CODE_SATURDAY:
                        modifyMessTime(bundle, timeListSat, tag.getmPosition());
                        break;
                    case REQUEST_CODE_SUNDAY:
                        modifyMessTime(bundle, timeListSun, tag.getmPosition());
                        break;
                    default:
                        break;
                }
            } else {
                LayoutAddMessTime mSelectedLayout = mLayoutList.get(requestCode);
                //添加一个显示时间的TextView
                if (mSelectedLayout != null) {
                    TextView textView;
                    switch (requestCode) {
                        case REQUEST_CODE_MON_2_FRI:
                            textView = createTextView(timeListMon.size(), requestCode, ParseTimeToString(false, bundle, requestCode));
                            textViewListMon.add(textView);
                            mSelectedLayout.addMessTimeTextView(textView);
                            break;
                        case REQUEST_CODE_SATURDAY:
                            textView = createTextView(timeListSat.size(), requestCode, ParseTimeToString(false, bundle, requestCode));
                            textViewListSat.add(textView);
                            mSelectedLayout.addMessTimeTextView(textView);
                            break;
                        case REQUEST_CODE_SUNDAY:
                            textView = createTextView(timeListSun.size(), requestCode, ParseTimeToString(false, bundle, requestCode));
                            textViewListSun.add(textView);
                            mSelectedLayout.addMessTimeTextView(textView);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void modifyMessTime(Bundle bundle, List<MessTime.Time> CurrentTimeList, int currentTVPosition) {

        MessTime.Time time = CurrentTimeList.get(currentTVPosition);
        time.setStartTime(bundle.getString("start_hour")
                + ":"
                + bundle.getString("start_minute"));
        time.setEndTime(bundle.getString("end_hour")
                + ":"
                + bundle.getString("end_minute"));
        CurrentTimeList.set(currentTVPosition, time);
    }

    private String ParseTimeToString(boolean isModify, Bundle mBundle, int requestCode) {

        String strStartTime = mBundle.getString("start_hour")
                + ":"
                + mBundle.getString("start_minute")
                ;
        String strEndTime = mBundle.getString("end_hour")
                + ":"
                + mBundle.getString("end_minute");
        if (isModify == true) {

        } else {
            MessTime messTime = new MessTime();
            MessTime.Time time = messTime.new Time();

            time.setStartTime(strStartTime);
            time.setEndTime(strEndTime);

            switch (requestCode) {
                case REQUEST_CODE_MON_2_FRI:
                    timeListMon.add(time);
                    break;
                case REQUEST_CODE_SATURDAY:
                    timeListSat.add(time);
                    break;
                case REQUEST_CODE_SUNDAY:
                    timeListSun.add(time);
                    break;
                default:
                    break;
            }
        }

        return strStartTime + "-" + strEndTime;
    }

    /**
     *  解析开始和结束时间
     *  后台weekDay 取值为( 1, 6, 0 )分别代表一周内三个营业时间段
     * @param messTimelist
     */
    private void parseMessTime(List<MessTime> messTimelist) {

        for (int i= 0 ; i < messTimelist.size(); i++) {
            MessTime currentMessTime = messTimelist.get(i);

            switch (currentMessTime.getWeekDay()) {
                case 1:
                    timeListMon = currentMessTime.getList();
                    createTimeToShow(textViewListMon, currentMessTime.getList(), REQUEST_CODE_MON_2_FRI);
                    break;
                case 6:
                    timeListSat = currentMessTime.getList();
                    createTimeToShow(textViewListSat, currentMessTime.getList(), REQUEST_CODE_SATURDAY);
                    break;
                case 0:
                    timeListSun = currentMessTime.getList();
                    createTimeToShow(textViewListSun, currentMessTime.getList(), REQUEST_CODE_SUNDAY);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *  组装开始和结束时间
     *
     * @param list   营业时间列表
     * @param tag    所处的营业时间短TAG
     */
    private void createTimeToShow(List<TextView> textViewList, List<MessTime.Time> list, int tag) {

        for (int current = 0; current < list.size(); current++) {
            MessTime.Time time = list.get(current);

            //添加一个显示时间的TextView到指定营业时间段中
            LayoutAddMessTime mSelectedLayout = mLayoutList.get(tag);

            if (mSelectedLayout != null) {

                //TODO....格式化获取的后台时间......
                getFormatTime(time);

                TextView textView = createTextView(current, tag, getFormatTime(time));
                textViewList.add(textView);    //将已有的营业时间短存储到指定营业段内
                mSelectedLayout.addMessTimeTextView(textView);
            }
        }

    }

    private String getFormatTime(MessTime.Time time) {

        try{

            String[] strStartTimeList = time.getStartTime().split(":");
            String[] strEndTimeList = time.getEndTime().split(":");

            int startHour = Integer.valueOf(strStartTimeList[0]).intValue();
            int startMinute = Integer.valueOf(strStartTimeList[1]).intValue();

            int endHour = Integer.valueOf(strEndTimeList[0]).intValue();
            int endMinute = Integer.valueOf(strEndTimeList[1]).intValue();

            String startTime = ((startHour <10)? "0" + strStartTimeList[0]:strStartTimeList[0])
                    + ":"
                    +  ((startMinute <10)? "0" + strStartTimeList[1]:strStartTimeList[1]);

            String endTime = ((endHour <10)? "0" + strEndTimeList[0]:strEndTimeList[0])
                    + ":"
                    +  ((endMinute <10)? "0" + strEndTimeList[1]:strEndTimeList[1]);

            return startTime + "-" + endTime;
        } catch(Exception e) {
            LogUtils.e(e.toString());
        }

        return "";
    }

    @Override
    public void onSuccess(BaseResponse response) {
        messTimelist = ( List<MessTime>) response.getData();
    }

    @Override
    public void onFailed(BaseResponse response) {

    }

    @Override
    public void onFinish() {
        if (messTimelist != null) {  //获取到营业时间后，显示到TextView中去
            parseMessTime(messTimelist);
        }
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 动态创建显示营业时间的TextView
     *
     * @param position
     * @param requestCode
     * @param messTime
     * @return
     */
    private TextView createTextView(int position, final int requestCode, String messTime){
//        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        final TextView mTextViewShowTime = new TextView(getApplicationContext());
//        mTextViewShowTime.setText(messTime);
//        mTextViewShowTime.setPadding(DensityUtils.dp2px(getApplicationContext(), 15), 0, DensityUtils.dp2px(getApplicationContext(), 15), 0);
//        mTextViewShowTime.setLayoutParams(mLayoutParams);
//        mTextViewShowTime.setHeight(DensityUtils.dp2px(getApplicationContext(), 50));
//        mTextViewShowTime.setClickable(true);
//        mTextViewShowTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.slt_layout_rect_gray));
////        mTextViewShowTime.setBackgroundColor(getResources().getColor(R.color.white));
//        mTextViewShowTime.setGravity(Gravity.CENTER_VERTICAL);
//        mTextViewShowTime.setTextSize(16);
//        mTextViewShowTime.setTextColor(getResources().getColor(R.color.text_black_1));
//
//        Drawable drawable= getResources().getDrawable(R.mipmap.icon_jiantou_r);
//        /// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        mTextViewShowTime.setCompoundDrawables(null,null,drawable,null);

        final TextView tvShowTime = new MessTimeView(this, messTime);
        MyTextViewTag tag = new MyTextViewTag();
        tag.setmRequestCode(requestCode);//Tag标记属于一周内哪一个时间段
        tag.updatemPosition(position);//Tag标记列表位置
        tvShowTime.setTag(tag);

        tvShowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
                currentTime = System.currentTimeMillis();

                MyTextViewTag tag = (MyTextViewTag) tvShowTime.getTag();
                currentTextView = tvShowTime;//指定当前被点击的营业时间，防止修改完返回来找不到不到哪一个是被修改的营业时间
                Intent intent = new Intent();
                intent.putExtra("is_modify_mode", true);
                intent.putExtra("mess_time", tvShowTime.getText().toString());
                intent2ActivityWidthExtrasAndForResult(intent,EditMessTimeActivity.class, tag.getmRequestCode());
            }
        });
        return tvShowTime;
    }

    /**
     *
     */
    class MyTextViewTag {

        private int mPosition;  //标记当前元素所在列表位置
        private int mRequestCode;  //标记当前元素所在的营业时间段

        public int getmPosition() {
            return mPosition;
        }

        public void updatemPosition(int mPosition) {
            this.mPosition = mPosition;
        }

        public int getmRequestCode() {
            return mRequestCode;
        }

        public void setmRequestCode(int mRequestCode) {
            this.mRequestCode = mRequestCode;
        }
    }
}

