package com.tajiang.ceo.common.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.utils.FragmentController;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.BottomBarView;
import com.tajiang.ceo.order.fragment.MyOrderMenuFragment;
import com.tajiang.ceo.scan.ScanerFragment;
import com.tajiang.ceo.setting.fragment.MySettingFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/10.
 */
public class HomeActivity extends BaseActivity implements BottomBarView.OnBottomBarClickListener {

    private static final int PAGE_COUNT = 4;
    public static final String EXTRA_FROM_LOGIN = "extra_from_login";

    public static final int PAGE_ORDER = 0;
    public static final int PAGE_SEARCH_ORDER = 1;
    public static final int PAGE_MESS = 2;
    public static final int PAGE_SETTING = 3;
    @BindView(R.id.nnvTabBarHome)
    BottomBarView nnvTabBarHome;
    @BindView(R.id.fl_home)
    FrameLayout flHome;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    static HomeActivity activity;

    private View mBottomAttachedView;
    private long mExitTime;

    // 配置底部菜单栏内容
    private String[] labelArr = {"我的订单", "扫码", "设置"};

    private int[] imgNormalResIdArr = {
            R.mipmap.order_my_order_unselected,
            R.mipmap.order_scan_code_unselected,
            R.mipmap.order_set_upunselected
    };

    private int[] imgPressedResIdArr = {
            R.mipmap.order_my_order,
            R.mipmap.order_scan_code,
            R.mipmap.order_set
    };

    private List<Fragment> mFragmentList = new ArrayList<>();

    private FragmentController controller;


    /**
     * 此处根布局用于添加需要从底部向上面弹出的自定义View,(初始位置隐藏于页面下方)
     *
     * @return ViewGroup
     */
    public View addViewIntoBottom(int resourceID) {
        if (mBottomAttachedView != null) llRoot.removeView(mBottomAttachedView);
        mBottomAttachedView = LayoutInflater.from(this).inflate(resourceID, llRoot, false);
        mBottomAttachedView.setClickable(true);
        llRoot.addView(mBottomAttachedView);
        return mBottomAttachedView;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //用户首次使用APP，弹出新手引导页面
//        if (SharedPreferencesUtils.get(SharedPreferencesUtils.USER_LOGIN_GUIDE, new Boolean(true)).equals(new Boolean(true))) {
//            SharedPreferencesUtils.put(SharedPreferencesUtils.USER_LOGIN_GUIDE, new Boolean(false));
//            new GuideWindow(this).show();
//        }
    }

    @Override
    protected void initTopBar() {
        disableNav();
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

        activity = HomeActivity.this;
        nnvTabBarHome.initData(labelArr, imgNormalResIdArr, imgPressedResIdArr);
        nnvTabBarHome.setOnBottomBarClickListener(this);

        mFragmentList.add(new MyOrderMenuFragment());
        mFragmentList.add(new ScanerFragment());
        mFragmentList.add(new MySettingFragment());

        controller = new FragmentController(this, R.id.fl_home, mFragmentList);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBottomBarClick(int position) {
        switch (position) {
            case PAGE_ORDER:
                ((BaseFragment) (mFragmentList.get(position))).reFreshCurrentPageData();
                break;
            case PAGE_SEARCH_ORDER:
                ((BaseFragment) (mFragmentList.get(position))).reFreshCurrentPageData();

//                if (mFragmentList.get(position) instanceof SearchFragment) {
//                    ((SearchFragment) (mFragmentList.get(position))).setStoreId(
//                            ((OrderFragment) (mFragmentList.get(PAGE_ORDER))).getCurrentStallId());
//
//                    ((SearchFragment) (mFragmentList.get(position))).setSchoolId(
//                            ((OrderFragment) (mFragmentList.get(PAGE_ORDER))).getUser().getSchoolId());
//                }

                break;
            case PAGE_MESS:
                ((BaseFragment) (mFragmentList.get(position))).reFreshCurrentPageData();
                break;
            case PAGE_SETTING:

                break;
            default:
                break;
        }
        controller.showFragment(position);
    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mExitTime < 2000) {

            PostDataTools.user_logout(this, new PostDataTools.DataCallback() {
                @Override
                public void callback(boolean flag, String message, Object object) {

                }
            });

            MobclickAgent.onKillProcess(this);
            TJApp.getInstance().exit();
        } else {
            mExitTime = nowTime;
            ToastUtils.showLong("再按一次退出");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        PostDataTools.getUserInfo(this, null);
    }

    public static Activity getActivity(){

        return activity;
    }
}
