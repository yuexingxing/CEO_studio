package com.tajiang.ceo.common.utils;


import android.content.Context;
import android.widget.Toast;

import com.tajiang.ceo.common.application.TJApp;

/** 
 * Toast统一管理类 
 *  
 */ 
public class ToastUtils {
	
	private static Context context = TJApp.getInstance();

    /**
     * 短时间显示Toast 
     *  
     * @param message
     */  
    public static void showShort(CharSequence message)  
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
  
    /** 
     * 短时间显示Toast 
     *  
     * @param message stringRes
     */  
    public static void showShort(int message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
  
    /** 
     * 长时间显示Toast 
     *  
     * @param message
     */  
    public static void showLong(CharSequence message)  
    {  
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }  
  
    /** 
     * 长时间显示Toast 
     *  
     * @param message stringRes
     */  
    public static void showLong(int message)  
    {  
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }  
  
    /** 
     * 自定义显示Toast时间 
     *  
     * @param message
     * @param duration 
     */  
    public static void show(CharSequence message, int duration)  
    {  
        Toast.makeText(context, message, duration).show();
    }  
  
    /** 
     * 自定义显示Toast时间 
     *  
     * @param message
     * @param duration 
     */  
    public static void show( int message, int duration)  
    {  
        Toast.makeText(context, message, duration).show();
    }  

}
