package com.tajiang.ceo.mess.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SQL on 2016/7/2.
 */
public class LayoutAddMessTime extends LinearLayout implements View.OnClickListener{

    private int mRequestCode;

    private Context mContext;
    private LayoutInflater mInflater;
    private LinearLayout.LayoutParams mLayoutParams;

    private TextView mTextViewShowDate;
    private TextView mTextViewAddMessTime;
    private LinearLayout mRootView;
    private LinearLayout mRootViewShowMessTime; //装载显示时间TextView的RootView
    private List<View> driverHList;

    private onTextViewClickListener mTextListener;

    public interface onTextViewClickListener{
        void onTextClick();
    }

    public LayoutAddMessTime(Context context, int requestCode) {
        this(context, null, requestCode);
    }

    public LayoutAddMessTime(Context context, AttributeSet attrs, int requestCode) {
        this(context, attrs, 0, requestCode);
    }

    public LayoutAddMessTime(Context context, AttributeSet attrs, int defStyleAttr, int requestCode) {
        super(context, attrs, defStyleAttr);

        initData(context, requestCode);
        initView();
    }

    private void initView() {
        this.setLayoutParams(mLayoutParams);
        this.setOrientation(VERTICAL);

        View view = mInflater.inflate(R.layout.layout_add_mess_time, this, false);
        mTextViewShowDate = (TextView) view.findViewById(R.id.tv_show_date);
        mRootViewShowMessTime = (LinearLayout) view.findViewById(R.id.ll_root_show_mess_time);
        mTextViewAddMessTime = (TextView) view.findViewById(R.id.tv_add_mess_time);

        mTextViewAddMessTime.setOnClickListener(this);

        this.addView(view);
    }

    private void initData(Context context, int requestCode) {
        driverHList = new ArrayList<View>();
        this.mRequestCode = requestCode;
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setMessDate(String messDate) {
        mTextViewShowDate.setText(messDate);
    }

    public void setTextListener(onTextViewClickListener mTextListener) {
        this.mTextListener = mTextListener;
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_add_mess_time) {
            mTextListener.onTextClick();
        }
    }

    //添加显示营业时间的TextView
    public void addMessTimeTextView(TextView mTextViewShowMessTime){

        if (mRootViewShowMessTime != null) {
            mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            mRootViewShowMessTime.addView(mTextViewShowMessTime);

            View driverH = new View(mContext);
            driverH.setLayoutParams(mLayoutParams);
            driverH.setMinimumHeight(1);
            driverH.setBackgroundColor(getColor(R.color.driver));
            driverHList.add(driverH);
            mRootViewShowMessTime.addView(driverH);
        }
    }

    //添加显示营业时间的TextView
    public void removeMessTimeTextView(TextView mTextViewShowMessTime, int position){

        if (mRootViewShowMessTime != null) {
            mRootViewShowMessTime.removeView(mTextViewShowMessTime);

            if (driverHList != null) {
                mRootViewShowMessTime.removeView(driverHList.get(position));
                driverHList.remove(position);
            }
        }
    }

    private int getColor(int resId){
        return mContext.getResources().getColor(resId);
    }
}
