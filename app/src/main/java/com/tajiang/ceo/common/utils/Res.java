package com.tajiang.ceo.common.utils;

import android.graphics.drawable.Drawable;

import com.tajiang.ceo.common.application.TJApp;

public class Res {

	public Res(){
		
	}
	
	/**
	 * 获取value值
	 * @param name id
	 * @return
	 */
	public static String getString(int name){
		
		return TJApp.getInstance().getString(name);
	}
	
	/**
	 * 获取颜色值
	 * @param name id
	 * @return
	 */
	public static int getColor(int name){
		
		return TJApp.getInstance().getResources().getColor(name);
	}
	
	/**
	 * 获取颜色值
	 * @param name id
	 * @return
	 */
	public static Drawable getDrawable(int name){
		
		return TJApp.getInstance().getResources().getDrawable(name);
	}
}
