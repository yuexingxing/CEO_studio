package com.tajiang.ceo.common.application;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.constant.TJCst;
import com.tajiang.ceo.common.utils.CommonUtils;
import com.tajiang.ceo.common.utils.SharedPreferencesUtils;
import com.tajiang.ceo.model.User;

import java.util.ArrayList;
import java.util.List;


public class TJApp extends Application {
    /**
     * 应用程序包名
     */
    public static final String MY_PKG_NAME = "com.tajiang.ceo";
    /**
     * 登录用户的权限
     */
    public static final int RULES_ADMIN = 1;
    public static final int RULES_SCHOOL_MANAGER = 2;
    public static final int RULES_SCHOOL_CEO = 3;
    public static final int RULES_LOU_ZHANG = 4;

    private List<Activity> activities = new ArrayList<>();

    private static TJApp app;

    private RefWatcher refWatcher;

    /**
     * 是否已经显示登陆界面
     */
    private volatile boolean isShowLogin;

    public boolean isShowLogin() {
        return isShowLogin;
    }

    public void setShowLogin(boolean showLogin) {
        isShowLogin = showLogin;
    }

    public static RequestQueue mRequestQueue;
    public String sendTime = "35916884898255966"; // 加密time
    public User user = new User();

    public long limitTime;
    public String mToken;

    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    protected static DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initSharedPreferenceData();
//        initPushManager();//初始化个推推送服务
        if (TJCst.IS_LEAK_CANARY_OPEN) {
            initLeakCanary(); //初始化内存泄漏侦测工具
        }

        initImageLoader();

        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.start();
    }

    public static RequestQueue getQueue(){

        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(app);
        }

        return mRequestQueue;
    }

    private void initSharedPreferenceData() {
        //初始化是否已上传用户ClientID及相关数据  false --> 未上传
        if (! SharedPreferencesUtils.contains(SharedPreferencesUtils.BOOLEAN_CID_COLLECTED)) {
            SharedPreferencesUtils.put(SharedPreferencesUtils.BOOLEAN_CID_COLLECTED, false);
        }
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
        // Normal app init code...
    }

    private void initPushManager() {
        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    public static TJApp getInstance() {
        return app;
    }

    /**
     * 返回一个LeakCanary Watcher
     * @return
     */
    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    /**
     * 新建了一个activity
     * @param activity
     */
    public void addActivity(Activity activity) {
        if(activity != null) {
            activities.add(activity);
        }
    }
    /**
     *  结束指定的Activity
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity!=null) {
            this.activities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity(通过类名)
     */
    public void finishActivity(Class clazz) {
        for (Activity activity : activities) {
            if (activity != null && activity.getClass().equals(clazz)) {
                this.activities.remove(activity);
                activity.finish();
            }
        }
    }

    /**
     * 应用退出，结束所有的activity
     */
    public void exit(){
        for (Activity activity : activities) {
            if (activity!=null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /**
     * 初始化ImageLoader
     */
    private void initImageLoader() {

        // 如果上一次删除文件不是今天，则删除文件
//        if (!SharedPreferencesUtils.getParam(Constant.SP_DELETE_FILE_DAY, "")
//                .equals(CommandTools.getChangeDate(-7))) {
//
//            FileUtils.deleteDir(Constant.cacheDir.toString());
//            SharedPreferencesUtils.setParam(Constant.SP_DELETE_FILE_DAY,
//                    CommandTools.getChangeDate(-7));
//        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new UnlimitedDiscCache(CommonUtils.cacheDir))
                // 缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();// 开始构建
        ImageLoader.getInstance().init(config);
    }

    /**
     * 多张图片加载处理
     *
     * @return
     */
    public static ImageLoader getImageLoader() {

        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }

        return imageLoader;
    }

    /**
     * 图片加载
     *
     * @return
     */
    public DisplayImageOptions getOptions() {

        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    // .showStubImage(R.drawable.male)// 设置图片下载期间显示的图片 ，暂时去掉这个属性
                    .showImageForEmptyUri(R.drawable.icon_dingdan) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.icon_dingdan) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory().cacheOnDisc()// 下载的图片缓存在内存卡中
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }

        return options;
    }

    /**
     * 图片加载、加载失败则不显示图片
     *
     * @return
     */
    public DisplayImageOptions getOptionsNoPic() {

        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    // .showStubImage(R.drawable.male)// 设置图片下载期间显示的图片 ，暂时去掉这个属性
                    //			.showImageForEmptyUri(0) // 设置图片Uri为空或是错误的时候显示的图片
                    //			.showImageOnFail(0) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory().cacheOnDisc()// 下载的图片缓存在内存卡中
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }

        return options;
    }


}
