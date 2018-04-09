package com.tajiang.ceo.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tajiang.ceo.R;

/**
 * 通用对话框
 *
 * @author yxx
 *
 * @date 2016-5-3 下午10:00:31
 *
 */
public class CommonDialog {

	public static boolean isShow = false;
	static Dialog dialog;

	// 弹窗结果回调函数
	public static abstract class DialogCallback {
		public abstract void callback(int position);
	}

	public CommonDialog(Context context){

	}


	/**
	 * @param context
	 * @param left
	 * @param right
	 * @param message
	 * @param resultCallback
	 */
	public static void showDialog(Context context, String left, String right, String message, final DialogCallback resultCallback){

		if(dialog != null){
			dialog.dismiss();
		}

		dialog = new Dialog(context, R.style.dialog);
		LayoutInflater inflater = dialog.getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_call_phone, null);

		TextView tvMessage = (TextView) layout.findViewById(R.id.call_phone_message);
		TextView tvLeft = (TextView) layout.findViewById(R.id.tv_call_cancel);
		TextView tvRight = (TextView) layout.findViewById(R.id.tv_call_ok);

		tvLeft.setText(left);
		tvRight.setText(right);
		tvMessage.setText(message);

		tvLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();
				resultCallback.callback(0);
			}
		});

		tvRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();
				resultCallback.callback(1);
			}
		});

		dialog.setContentView(layout);

		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		dialog.show();
		setDialogWindowAttr(dialog, context);
		isShow = true;
	}

	/**
	 * 关闭窗口
	 */
	public static void closeDialog(){

		if(dialog != null){
			dialog.dismiss();
		}
	}

	//在dialog.show()之后调用
	public static void setDialogWindowAttr(Dialog dlg,Context ctx){

		WindowManager wm = ((Activity) ctx).getWindowManager();
		Display display = wm.getDefaultDisplay(); // 为获取屏幕宽、高

		Window window = dlg.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = (int) (display.getWidth()/1.5);
		lp.height = (int) (display.getHeight()/5);
		dlg.getWindow().setAttributes(lp);
	}

}
