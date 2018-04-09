package com.tajiang.ceo.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tajiang.ceo.common.application.TJApp;

import java.io.File;

/**
 * Created by Administrator on 2016/8/25.
 */
public class CommonUtils {

    public static File cacheDir = StorageUtils.getOwnCacheDirectory(TJApp.getInstance(), "TJ/IconCache/");

    private static TelephonyManager telephonemanage;

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取PDA SN�?
     *
     * @param context
     * @return
     */
    public static String getMIME(Context context) {
        telephonemanage = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String sn = telephonemanage.getDeviceId();
            return sn;
        } catch (Exception e) {
            Log.e("MIME", e.getMessage());
            return "00000000000";
        }
    }
}

