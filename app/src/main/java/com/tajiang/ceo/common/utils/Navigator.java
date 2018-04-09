package com.tajiang.ceo.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * Activity页面跳转器，让页面之间解耦合 (利用反射的方法)
 * Created by Qili Shen on 2016/12/20.
 */
public class Navigator {
    public static final int START_ACTIVITY = 1;
    public static final int START_ACTIVITY_RESULT = 2;
    public static final int START_ACTIVITY_RESULT_AND_EXTRA = 3;
    private static final String TAG = "Navigator";

    /**
     * @param activityFrom
     * @param toActivityName
     * @param intent
     * @param bundle   没有可传null
     * @param startType
     * @param requestCode
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void navigatorTo(Activity activityFrom
            , final String toActivityName
            , final Intent intent
            , final Bundle bundle
            , final int startType
            , final int requestCode) {

        if (activityFrom == null || toActivityName == null || intent == null) {
            throw new NullPointerException(TAG + ":activityFrom == null || toActivityName == null || intent == null");
        } else {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(toActivityName);
                if(clazz != null){
                    intent.setClass(activityFrom, clazz);
                    //TODO......添加额外的跳转方式.....
                    switch (startType) {
                        case START_ACTIVITY:
                            activityFrom.startActivity(intent);
                            break;
                        case START_ACTIVITY_RESULT:
                            activityFrom.startActivityForResult(intent, requestCode);
                            break;
                        case START_ACTIVITY_RESULT_AND_EXTRA:
                            activityFrom.startActivityForResult(intent, requestCode, bundle);
                            break;
                        default:
                            break;
                    }
                }
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 存储APP中常用的activity类型
     */
    public static final class ActivityType{
        public static final String ACTIVITY_SECOND = "com.example.administrator.openandroid.SecondActivity";
    }

}
