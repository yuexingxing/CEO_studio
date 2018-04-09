package com.tajiang.ceo.common.constant;

import com.tajiang.ceo.common.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2016/6/6.
 * 系统常量
 */
public class TJCst {

    public static final String CUSTOMER_SERVICES_PHONE = "4009920278";

    public static final boolean IS_DEBUG = true;
    /**
     * 内存监控开关 LeakCanary
     */
    public static final boolean IS_LEAK_CANARY_OPEN = false;
    /**
     *
     * 3e4d24bad69adf07705dee7a8cad7e92
     * 正式服务器
     */
    public static final String HOST = "apiwmdev.axpapp.com";//rest.itajiang.com:8888
    /**
     * 测试服务器
     */
    public static final String TEST_HOST = "192.168.3.4:8080";

    public static final String BIG_BROTHER = "192.168.0.13:8888";

    public static final String LV_TEST = "192.168.0.84:8084";

//    public static final String ZD_TEST = "121.196.217.158:9001";
//    public static final String ZD_TEST = "apipre.axpapp.com";
    public static final String ZD_TEST = "apipre.itajiang.com";

    /**
     * 发布线上版本请使用此URL
     * 根URl地址
     */
//    public static final String BASE_URL = IS_DEBUG ? "http://" + TEST_HOST + "/ceo/" : "http://" + HOST + "/ceo/";
    /**
     * 开发测试请使用此URL
     * 根URl地址
     */
    public static String BASE_URL = "http://" + SharedPreferencesUtils.get(SharedPreferencesUtils.HOST_TEST_ROOT_URL, HOST) + "/";

}
