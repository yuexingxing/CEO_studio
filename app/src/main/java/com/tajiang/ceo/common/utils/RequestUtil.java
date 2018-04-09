package com.tajiang.ceo.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.exam.net.VolleyErrorHelper;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.constant.TJCst;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author yxx
 *
 * @date 2015-12-23 涓嬪崍7:48:08
 *
 */
public class RequestUtil{

	private HttpResponseListener listener;

	public static boolean isShow = false;
	private static Dialog dialog;

	/**
	 *
	 */
	public static abstract class Callback {
		public abstract void callback(boolean flag, String message, JSONObject data);
	}

	public RequestUtil(Context context){

	}

	/**
	 * 进行网络请求前的数据处理
	 * @param context
	 * @param strUrl
	 * @param jsonObjectData
	 * @return
	 */
	public static String initDataBeforeHttp(Context context, String strUrl, JSONObject jsonObjectData){

		if(context != null){
			Activity activity = (Activity) context;
			if(!activity.isFinishing()){
				if(dialog == null){
					dialog = new LoadingDialog(context);
				}

				try{
					if(dialog.isShowing()){
						dialog.dismiss();
					}
					dialog.show();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		TJApp.getInstance().sendTime = CommandTools.initDataTime();

		//这里面进行了基础数据的拼接处理，包括中文转码
		String str = CommandTools.json2Map(jsonObjectData);

		StringBuilder sb = new StringBuilder();
		sb
				.append("appid=android.servant")
				.append("&version=1.0")
				.append("&token="  + TJApp.getInstance().mToken)
				.append("&timestamp=" + TJApp.getInstance().sendTime);

		if(!TextUtils.isEmpty(str)){
			sb.append("&" + str);
		}

		strUrl = TJCst.BASE_URL + strUrl + "?" + sb.toString();

		System.out.println(TJCst.BASE_URL + strUrl);
		System.out.println(strUrl);

		return strUrl;
	}

	/**
	 * @param context
	 * @param strUrl
	 * @param jsonObjectData
	 * @param callback
	 */
	public static void postData2(final Context context, String strUrl, JSONObject jsonObjectData, final Callback callback){

		if(context != null){
			Activity activity = (Activity) context;
			if(!activity.isFinishing()){
				if(dialog == null){
					dialog = new LoadingDialog(context);
				}

				try{
					if(dialog.isShowing()){
						dialog.dismiss();
					}
//					dialog.show();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		TJApp.getInstance().sendTime = CommandTools.initDataTime();
		try {
			jsonObjectData.put("appid", "android.servant");
			jsonObjectData.put("version", "1.0");
			jsonObjectData.put("token", TJApp.getInstance().mToken);
			jsonObjectData.put("timestamp", TJApp.getInstance().sendTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, TJCst.BASE_URL + strUrl, jsonObjectData, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonObject) {

				dialog.dismiss();
				Log.v("postData", jsonObject.toString());
				System.out.println(jsonObject.toString());
//				ToastUtils.showShort(jsonObject.toString());

				boolean success = false;
				String message = null;
				try {
					success = jsonObject.getBoolean("success");
					message = jsonObject.getString("message");
					jsonObject = jsonObject.optJSONObject("data");
				} catch (JSONException e) {
					e.printStackTrace();
				}finally{
					callback.callback(success, message, jsonObject);
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

				dialog.dismiss();
				Log.e("file", arg0.toString());
				String str = VolleyErrorHelper.getMessage(arg0, context);
				callback.callback(false, str, null);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				Map<String, String> mParams = new HashMap<String, String>();
				mParams.put("params1", "value1");
				mParams.put("params2", "value2");

				return mParams;
			}} ;

		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		TJApp.getQueue().add(jsonObjectRequest);
	}

	/**
	 * @param context
	 * @param strUrl
	 * @param jsonObjectData
	 * @param callback
	 */
	public static void getData(final Context context, String strUrl, JSONObject jsonObjectData, final Callback callback){

		strUrl = initDataBeforeHttp(context, strUrl, jsonObjectData);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET, strUrl , "", new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonObject) {

				dialog.dismiss();
				Log.v("postData", jsonObject.toString());
				System.out.println(jsonObject.toString());
//				ToastUtils.showShort(jsonObject.toString());

				boolean success = false;
				String message = null;
				try {
					success = jsonObject.getBoolean("success");
					message = jsonObject.getString("message");
				} catch (JSONException e) {
					e.printStackTrace();
				}finally{
					callback.callback(success, message, jsonObject);
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

				dialog.dismiss();
				Log.e("file", arg0.toString());

				String str = VolleyErrorHelper.getMessage(arg0, context);
				callback.callback(false, str, null);
			}
		}) ;

		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		TJApp.getQueue().add(jsonObjectRequest);
	}

	/**
	 * @param context
	 * @param strUrl
	 * @param callback
	 */
	public static void postData(final Context context, String strUrl, final Map<String, String> mParams, final Callback callback){

		if(context != null){
			Activity activity = (Activity) context;
			if(!activity.isFinishing()){
				if(dialog == null){
					dialog = new LoadingDialog(context);
				}

				try{
					if(dialog.isShowing()){
						dialog.dismiss();
					}
					dialog.show();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		strUrl = TJCst.BASE_URL + strUrl;
		TJApp.getInstance().sendTime = CommandTools.initDataTime();

		mParams.put("appid", "android.servant");
		mParams.put("version", "1.0");
		mParams.put("token", TJApp.getInstance().mToken);
		mParams.put("timestamp", TJApp.getInstance().sendTime);

		final String mRequestBody = appendParameter(strUrl,mParams);

		System.out.println(mRequestBody);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, strUrl, mRequestBody, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonObject) {

				dialog.dismiss();
				System.out.println(jsonObject.toString());
//				ToastUtils.showShort(jsonObject.toString());

				boolean success = false;
				String message = null;
				try {
					success = jsonObject.getBoolean("success");
					message = jsonObject.getString("message");
					jsonObject = jsonObject.optJSONObject("data");
				} catch (JSONException e) {
					e.printStackTrace();
				}finally{
					callback.callback(success, message, jsonObject);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				dialog.dismiss();
				Log.e("file", error.toString());
				String str = VolleyErrorHelper.getMessage(error, context);
				callback.callback(false, str, null);
			}
		}){
			@Override
			public String getBodyContentType() {
				return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
			}

			@Override
			public byte[] getBody() {
				try {
					return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
				} catch (Exception uee) {
					return null;
				}
			}
		};

		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		TJApp.getQueue().add(jsonObjectRequest);
	}

	static String appendParameter(String url,Map<String, String> params){

		Uri uri = Uri.parse(url);
		Uri.Builder builder = uri.buildUpon();
		for(Map.Entry<String,String> entry:params.entrySet()){
			builder.appendQueryParameter(entry.getKey(),entry.getValue());
		}
		return builder.build().getQuery();
	}

}
