package com.tajiang.ceo.common.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;

/**
 * SD卡相关的辅助类
 * 
 * 
 * 
 */
public class SDCardUtils {

	private SDCardUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断SDCard是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardEnable() {		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	/**
	 * 获取SD卡的剩余容量 单位byte
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getSDCardAllSize() {
		if (isSDCardEnable()) {
			StatFs stat = new StatFs(getSDCardPath());
			// 获取空闲的数据块的数量
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
			// 获取单个数据块的大小（byte）
			long freeBlocks = stat.getAvailableBlocks();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}

	/**
	 * 获取指定路径所在空间的剩余可用容量字节数，单位byte
	 * 
	 * @param filePath
	 * @return 容量字节 SDCard可用空间，内部存储可用空间
	 */
	@SuppressWarnings("deprecation")
	public static long getFreeBytes(String filePath) {
		// 如果是sd卡的下的路径，则获取sd卡可用容量
		if (filePath.startsWith(getSDCardPath())) {
			filePath = getSDCardPath();
		} else {// 如果是内部存储的路径，则获取内存存储的可用容量
			filePath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs stat = new StatFs(filePath);
		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
		return stat.getBlockSize() * availableBlocks;
	}

	/**
	 * 获取系统存储路径
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath() {
		return Environment.getRootDirectory().getAbsolutePath();
	}

	/**
	 * Android 6.0及以上版本设备读写操作
	 * 需要在运行时申请SDCard读写权限
	 * @return
	 */
	public static void applySDCardPermission(Activity activity) {
		//获取当前系统的android版本号
		if (android.os.Build.VERSION.SDK_INT < 23) {
			return;
		}
		int REQUEST_EXTERNAL_STORAGE = 1;
		String[] PERMISSIONS_STORAGE = {
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE
		};
		//获取以上权限是否被授权
		int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
			);
		}
	}
}