package com.tajiang.ceo.common.utils;

import android.app.Activity;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tajiang.ceo.common.application.TJApp;
import com.zxing.view.CameraManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-25.
 */

public class CommandTools {

    static String strLastTime;

    /**
     * 获取当前时间精确到毫秒
     *
     * @return
     */
    public static String initDataTime() {

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 精确到毫秒
        String suffix = fmt.format(new Date());

        strLastTime = TJApp.getInstance().sendTime;

        boolean flag = strLastTime.equals(suffix);
        while (flag == true) {

            suffix = fmt.format(new Date());
            strLastTime = TJApp.getInstance().sendTime;
            flag = strLastTime.equals(suffix);

            try {
                Thread.sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return suffix;
    }

    /**
     * json数据格式转换为接口需要类型
     * 通过&拼接
     * */
    public static String json2Map(JSONObject jsonObject)
    {
        if(jsonObject == null){
            return "";
        }

        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        Iterator it = jsonObject.keys();
        // 遍历jsonObject数据，添加到Map对象

        String strData = null;
        while (it.hasNext())
        {
            String key = String.valueOf(it.next());
            String value = null;
            try {

                if(TextUtils.isEmpty((String) jsonObject.get(key))){
                    value = "";
                }else{
                    value = (String) jsonObject.get(key);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            data.put(key, value);

            try {
                if(TextUtils.isEmpty(strData)){
                    strData = (key + "=" + URLEncoder.encode(value, "utf-8").toString()) + "&";
                }else{
                    strData = strData + (key + "=" + URLEncoder.encode(value, "utf-8").toString()) + "&";
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return strData;
    }


    public static void test(HashMap<String, String> map){

        String strData;
        int len = map.size();
        for(int i=0; i<len; i++){

        }
    }

    /**
     * 获取当前上下文
     * @return
     */
    public static Activity getGlobalActivity(){

        Class<?> activityThreadClass;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map<?, ?> activities = (Map<?, ?>) activitiesField.get(activityThread);
            for(Object activityRecord:activities.values()){
                Class<? extends Object> activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if(!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static AudioManager getAudioManager(Activity context){
//
//        return (AudioManager)context.getSystemService(Context.LOCATION_SERVICE);
//    }

    /*    *
     * 是否开启了闪光灯
     * @return
     */
    public static boolean isFlashlightOn(Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            String flashMode = parameters.getFlashMode();
            if (flashMode.equals(android.hardware.Camera.Parameters.FLASH_MODE_TORCH)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void openFlash(Camera camera, SurfaceHolder surfaceHolder){

        boolean flag = isFlashlightOn(camera);
        if (flag){
            CameraManager.get().closeDriver();
            try {
                CameraManager.get().openDriver(surfaceHolder);
                CameraManager.get().startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Openshoudian(camera);
        }
    }

    //这是开启方法
    public static void Openshoudian(Camera camera) {
        //异常处理一定要加，否则Camera打开失败的话程序会崩溃
        try {
            Log.d("smile","camera打开");
//            camera = Camera.open();
        } catch (Exception e) {
            Log.d("smile","Camera打开有问题");
            Toast.makeText(TJApp.getInstance(), "Camera被占用，请先关闭", Toast.LENGTH_SHORT).show();
        }

        if(camera != null)
        {
            //打开闪光灯
//            camera.startPreview();
            Camera.Parameters parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
            Log.d("smile","闪光灯打开");
        }
    }

    //这是关闭方法
    public static void Closeshoudian(Camera camera){

        if (camera != null){
            //关闭闪光灯
            Log.d("smile", "closeCamera()");
            camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(camera.getParameters());
//            camera.stopPreview();
//            camera.release();
//            camera = null;
        }
    }

    public  static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}
