package com.tajiang.ceo.setting.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.setting.widget.NumericWheelAdapter;
import com.tajiang.ceo.setting.widget.OnWheelChangedListener;
import com.tajiang.ceo.setting.widget.WheelView;

import java.util.Calendar;

public class SelectTwoTime extends PopupWindow implements OnClickListener {

	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private Button btn_submit;
	private String age;
	private DateNumericAdapter startHourAdapter, startMinuteAdapter;
	private DateNumericAdapter endHourAdapter, endMinuteAdapter;
	private WheelView startHour, startMinute;
	private WheelView endHour, endMinute;
	private int mHour = 5, mMinute = 14;
	private String[] dateType = {"时", "分"};

	private TimeCallback timeCallback;

	public static abstract class TimeCallback {
		public abstract void callback(boolean flag, int startHour, int startMinute, int endHour, int endMinute);
	}

	public SelectTwoTime(Activity context, TimeCallback timeCallback) {

		super(context);
		this.timeCallback = timeCallback;
		mContext = context;
		this.age = "10:20";
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.dialog_layout_setting_store_time, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		startHour = (WheelView) mMenuView.findViewById(R.id.hour_start);
		startMinute = (WheelView) mMenuView.findViewById(R.id.minute_start);
		
		endHour = (WheelView) mMenuView.findViewById(R.id.hour_end);
		endMinute = (WheelView) mMenuView.findViewById(R.id.minute_end);
		
		btn_submit = (Button) mMenuView.findViewById(R.id.submit);
		btn_submit.setOnClickListener(this);
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateminutes(startHour, startMinute);
			}
		};

		if (age != null && age.contains(":")) {
			String str[] = age.split(":");
			mHour = Integer.parseInt(str[0]);
			mMinute = Integer.parseInt(str[1]);
		}
		
		startHourAdapter = new DateNumericAdapter(context, 0, 23, 5);
		startHourAdapter.setTextType(dateType[0]);
		startHour.setViewAdapter(startHourAdapter);
		startHour.setCurrentItem(mHour);
		startHour.addChangingListener(listener);
		
		endHourAdapter = new DateNumericAdapter(context, 0, 23, 5);
		endHourAdapter.setTextType(dateType[0]);
		endHour.setViewAdapter(startHourAdapter);
		endHour.setCurrentItem(mHour);
		endHour.addChangingListener(listener);

		// startMinute
		updateminutes(startHour, startMinute);
		startMinute.setCurrentItem(mMinute);
		updateminutes(startHour, startMinute);
		startMinute.addChangingListener(listener);
		
		updateminutes(endHour, endMinute);
		endMinute.setCurrentItem(mMinute);
		updateminutes(endHour, endMinute);
		endMinute.addChangingListener(listener);

		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}


	private void updateminutes(WheelView startHour, WheelView startMinute) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, startHour.getCurrentItem());

		int maxminutes = calendar.getActualMaximum(Calendar.MINUTE);
		startMinuteAdapter = new DateNumericAdapter(mContext, 0, maxminutes, calendar.get(Calendar.MINUTE));
		startMinuteAdapter.setTextType(dateType[1]);
		startMinute.setViewAdapter(startMinuteAdapter);
		int curminute = Math.min(maxminutes, startMinute.getCurrentItem());
		startMinute.setCurrentItem(curminute, true);
		
		endMinuteAdapter = new DateNumericAdapter(mContext, 0, maxminutes, calendar.get(Calendar.MINUTE));
		endMinuteAdapter.setTextType(dateType[1]);
		endMinute.setViewAdapter(endMinuteAdapter);
		int curminute2 = Math.min(maxminutes, endMinute.getCurrentItem());
		endMinute.setCurrentItem(curminute2, true);

		age = startHour.getCurrentItem() + ":" + startMinute.getCurrentItem();
	}

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(20);
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return super.getItemText(index);
		}

	}

	public void onClick(View v) {

		if(startHour.getCurrentItem() > endHour.getCurrentItem()){
			ToastUtils.showShort("开始时间不能大于结束时间");
			return;
		}else if((startHour.getCurrentItem() == endHour.getCurrentItem()) && (startMinute.getCurrentItem() > endMinute.getCurrentItem())){
			ToastUtils.showShort("开始时间不能大于结束时间");
			return;
		}

		timeCallback.callback(true, startHour.getCurrentItem(), startMinute.getCurrentItem(), endHour.getCurrentItem(), endMinute.getCurrentItem());
		this.dismiss();
	}


}
