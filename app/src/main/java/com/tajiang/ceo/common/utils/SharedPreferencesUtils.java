package com.tajiang.ceo.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tajiang.ceo.common.application.TJApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 对SharedPreference的使用做了建议的封装，对外公布出put，get，remove，clear等等方法；
 * 注意一点，里面所有的commit操作使用了SharedPreferencesCompat
 * .apply进行了替代，目的是尽可能的使用apply代替commit
 * 首先说下为什么，因为commit方法是同步的，并且我们很多时候的commit操作都是UI线程中，毕竟是IO操作，尽可能异步；
 * 所以我们使用apply进行替代，apply异步的进行写入； 但是apply相当于commit来说是new API呢，为了更好的兼容，我们做了适配；
 * SharedPreferencesCompat也可以给大家创建兼容类提供了一定的参考~~
 */
public class SharedPreferencesUtils {

	/** 测试用URL服务器地址 */
	public static final String HOST_TEST_ROOT_URL = "host_test_root_url";

	/** 记录当前CID上传的系统时间 */
	public static final String LAST_CID_UPLOAD_TIME = "last_cid_upload_time";

	/** 是否已成功收集用户ClientID及相关数据 */
	public static final String BOOLEAN_CID_COLLECTED = "boolean_cid_collected";

	/** 保存在手机里面的文件名 */
	public static final String FILE_NAME = "share_data_ceo";

	/** 用户 - 信息 */
	public static final String USER_LOGIN_INFOR = "USER_LOGIN_INFOR";

	/** 用户 - 首次登录引导 */
	public static final String USER_LOGIN_GUIDE = "USER_LOGIN_GUIDE";

	/** 用户 - 用户手机ClientID */
	public static final String CLIENT_ID = "client_id";

	public static Context context = TJApp.getInstance();


	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 *
	 * @param key
	 * @param object
	 */
	public static void put(String key, Object object) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, new Gson().toJson(object));
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param key
	 * @param defaultObject
	 * @return
		*/
		public static Object get(String key, Object defaultObject) {
			SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
					Context.MODE_PRIVATE);

			if (defaultObject instanceof String) {
				return sp.getString(key, (String) defaultObject);
			} else if (defaultObject instanceof Integer) {
				return sp.getInt(key, (Integer) defaultObject);
			} else if (defaultObject instanceof Boolean) {
				return sp.getBoolean(key, (Boolean) defaultObject);
			} else if (defaultObject instanceof Float) {
				return sp.getFloat(key, (Float) defaultObject);
			} else if (defaultObject instanceof Long) {
				return sp.getLong(key, (Long) defaultObject);
			}

			return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param key
	 */
	public static void remove(String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 */
	public static void clear() {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	public static boolean contains(String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @return
	 */
	public static Map<String, ?> getAll() {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		*/
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}

}
