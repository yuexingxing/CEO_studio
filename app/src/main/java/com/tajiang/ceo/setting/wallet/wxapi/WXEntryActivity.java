package com.tajiang.ceo.setting.wallet.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.setting.wallet.PayUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信
 * 
 * @author
 * 
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	// private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_pay_success);
		IWXAPI api = WXAPIFactory.createWXAPI(this, PayUtil.APPID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		// api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		String string = resp.toString();
		System.out.println("--------微信支付----------" + string);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("11111");
			builder.setMessage(getString(R.string.pay_result_callback_msg,
					resp.errStr + ";code=" + String.valueOf(resp.errCode)));
			builder.show();
		}

		if (resp instanceof SendAuth.Resp) {
			SendAuth.Resp newResp = (SendAuth.Resp) resp;
			
			if(newResp.errCode==0){
				//ERR_OK = 0(用户同意)
				// 获取微信传回的code
				String code = newResp.code;
//				Intent intent = new Intent(this, AccreditBindingActivity.class);
//				intent.putExtra("name", "微信");
//				startActivity(intent);
				finish();
			}else if(newResp.errCode==-4){
				//ERR_AUTH_DENIED = -4（用户拒绝授权）
				//ERR_USER_CANCEL = -2（用户取消）
				// 获取微信传回的code
				ToastUtils.showShort("您已拒绝授权");
				finish();
			}else if(newResp.errCode==-2){
				//ERR_AUTH_DENIED = -4（用户拒绝授权）
				//ERR_USER_CANCEL = -2（用户取消）
				ToastUtils.showShort("您已取消授权");
				finish();
			}
			
			// getWeiXinInfo(code);
		}
	}
}