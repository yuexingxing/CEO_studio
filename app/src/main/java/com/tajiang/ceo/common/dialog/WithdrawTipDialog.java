package com.tajiang.ceo.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tajiang.ceo.R;

/**
 * 通用对话框
 *
 * @author yxx
 *
 * @date 2016-5-3 下午10:00:31
 *
 */
public class WithdrawTipDialog {

	public static boolean isShow = false;
	static Dialog dialog;

	// 弹窗结果回调函数
	public static abstract class DialogCallback {
		public abstract void callback(int position);
	}

	public WithdrawTipDialog(Context context){

	}


	/**
	 * @param context
	 */
	public static void showDialog(Context context){

		if(dialog != null){
			dialog.dismiss();
		}

		dialog = new Dialog(context, R.style.dialog);
		LayoutInflater inflater = dialog.getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_withdraw_tip, null);

		ImageView imgClose = (ImageView) layout.findViewById(R.id.dialog_withdraw_close);
		imgClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeDialog();
			}
		});

		dialog.setContentView(layout);

		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);

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
		lp.width = (int) (display.getWidth()/1.1);
		lp.height = (int) (display.getHeight()/1.3);
		dlg.getWindow().setAttributes(lp);
	}

}
