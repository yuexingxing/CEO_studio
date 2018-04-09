package com.tajiang.ceo.common.utils;

import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.tajiang.ceo.common.application.TJApp;

/**
 * 跟App相关的辅助类
 *
 */
public class AppUtils {

    private volatile static AppUtils appUtils;

    private static Context context;

    private AppUtils() {}

    public static AppUtils instance(Context context) {

        AppUtils.context = context;

        if (appUtils == null) {
            synchronized (AppUtils.class) {
                if (appUtils == null) {
                    appUtils = new AppUtils();
                }
            }
        }

        return appUtils;

    }

    /**
     * 获取应用程序名称
     */
    public String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public String getVersionName() {
        try {

            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo( context.getPackageName(), 0);

            return packageInfo.versionName;

        } catch (NameNotFoundException e) {

            e.printStackTrace();

        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @return 当前应用的版本名称
     */
    public int getVersionCode() {
        try {

            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo( context.getPackageName(), 0);

            return packageInfo.versionCode;

        } catch (NameNotFoundException e) {

            e.printStackTrace();

        }
        return 0;
    }

    /**
     * 获取本地语言
     *
     * @return
     */
    public  String getLocales() {
        Locale locales = Locale.getDefault();
        // 格式化语言类型
        String format = locales.getLanguage() + "-" + locales.getCountry();
        return format;
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(context , cls);
        context.startActivity(intent);
    }

    /**
     * 判断应用是否已经启动
     * @param context context
     * @return boolean
     */
    public static boolean isAppAlive(Context context){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : processInfos) {
            if(runningAppProcessInfo.processName.equals(TJApp.MY_PKG_NAME)){
                LogUtils.i(String.format("the %s is running, isAppAlive return true", TJApp.MY_PKG_NAME));
                return true;
            }
        }

        LogUtils.i(String.format("the %s is not running, isAppAlive return false", TJApp.MY_PKG_NAME));
        return false;
    }


}