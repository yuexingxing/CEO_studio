package com.tajiang.ceo.common.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.Notification;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.HomeActivity;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.utils.AppUtils;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.SharedPreferencesUtils;

import java.lang.reflect.Type;

public class PushTaJiangReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 1;
    private ReceiveNewMessager messager;

    public interface ReceiveNewMessager{
        public void showMessage(String message);
    }

    public void setReceiveNewMessager(ReceiveNewMessager messager) {
        this.messager = messager;
    }
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtils.e("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                LogUtils.d("第三方回执接口调用" + (result ? "成功" : "失败"));
                //处理透传数据
                NotifyUser(context, payload);
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                SharedPreferencesUtils.put(SharedPreferencesUtils.CLIENT_ID, cid);
//                if (GetuiSdkDemoActivity.tView != null) {
//                    GetuiSdkDemoActivity.tView.setText(cid);
//                }
                LogUtils.d(cid);
                break;
            case PushConsts.GET_SDKONLINESTATE:
                boolean online = bundle.getBoolean("onlineState");
                Log.d("GetuiSdkDemo", "online = " + online);
                break;

            case PushConsts.SET_TAG_RESULT:
                String sn = bundle.getString("sn");
                String code = bundle.getString("code");

                String text = "设置标签失败, 未知异常";
                switch (Integer.valueOf(code)) {
                    case PushConsts.SETTAG_SUCCESS:
                        text = "设置标签成功";
                        break;

                    case PushConsts.SETTAG_ERROR_COUNT:
                        text = "设置标签失败, tag数量过大, 最大不能超过200个";
                        break;

                    case PushConsts.SETTAG_ERROR_FREQUENCY:
                        text = "设置标签失败, 频率过快, 两次间隔应大于1s";
                        break;

                    case PushConsts.SETTAG_ERROR_REPEAT:
                        text = "设置标签失败, 标签重复";
                        break;

                    case PushConsts.SETTAG_ERROR_UNBIND:
                        text = "设置标签失败, 服务未初始化成功";
                        break;

                    case PushConsts.SETTAG_ERROR_EXCEPTION:
                        text = "设置标签失败, 未知异常";
                        break;

                    case PushConsts.SETTAG_ERROR_NULL:
                        text = "设置标签失败, tag 为空";
                        break;

                    case PushConsts.SETTAG_NOTONLINE:
                        text = "还未登陆成功";
                        break;

                    case PushConsts.SETTAG_IN_BLACKLIST:
                        text = "该应用已经在黑名单中,请联系售后支持!";
                        break;

                    case PushConsts.SETTAG_NUM_EXCEED:
                        text = "已存 tag 超过限制";
                        break;

                    default:
                        break;
                }

                Log.d("GetuiSdkDemo", "settag result sn = " + sn + ", code = " + code);
                Log.d("GetuiSdkDemo", "settag result sn = " + text);
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }

    private void NotifyUser(Context context, byte[] payload) {
        if (payload != null) {
            String data = new String(payload);
            Log.d("GetuiSdkDemo", "receiver payload : " + data);

            Notify notify = parseJson2Notify(data);

            if (notify != null) {
                notify2User(context, notify);
            }

//            Intent intent = new Intent("new_order_message");
//            intent.putExtra("new_message",data);
//            context.sendBroadcast(intent);
        }
    }

    /**
     * 解析来自推送的订单
     */
    private Notify parseJson2Notify(String json) {
        Notify notify = null;
        Gson gson = new Gson();
        try {
            Type type = new TypeToken<Notify>() {}.getType();
            notify = gson.fromJson(json, type);
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return notify;
    }

    private void notify2User(Context context, Notify notify) {
        Intent intent;
        //判断APP是否存活
        if (AppUtils.isAppAlive(context)) {
            if (SharedPreferencesUtils.contains(SharedPreferencesUtils.USER_LOGIN_INFOR)) {
                intent = new Intent(context, com.tajiang.ceo.common.activity.HomeActivity.class);
            } else {
                intent = new Intent(context, com.tajiang.ceo.login.activity.LoginActivity.class);
            }

        } else {
            PackageManager manager = context.getPackageManager();
            intent = manager.getLaunchIntentForPackage(TJApp.MY_PKG_NAME);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context).setContentTitle(notify.getDescription())
                .setContentText(notify.getContent())
//                .setContentText(data.equals("") ? null : data)
                .setTicker("新消息")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);//执行intent

        notificationBuilder.setAutoCancel(true);//自己维护通知的消失

        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND;
        notification.vibrate = new long[]{500L, 200L, 200L, 500L};
        NotificationManager notificationManager = (NotificationManager) TJApp.getInstance()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
