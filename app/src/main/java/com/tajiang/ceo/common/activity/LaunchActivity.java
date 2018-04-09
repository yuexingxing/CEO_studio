package com.tajiang.ceo.common.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.AppUtils;
import com.tajiang.ceo.common.utils.NetWorkUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.login.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/29.
 */
public class LaunchActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void initTopBar() {
        disableNav();
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_launch);
        initMyView();
    }

    private void initMyView() {
        tvVersion.setText("版本号" + AppUtils.instance(this).getVersionName());
    }

    @Override
    protected void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //用户已经登录
//                if (!TextUtils.isEmpty((String) SharedPreferencesUtils.get(SharedPreferencesUtils.USER_LOGIN_INFOR, ""))) {
//                    intent2Activity(HomeActivity.class);
//                } else {
                    intent2Activity(LoginActivity.class);
//                intent2Activity(HomeActivity.class);
//                }
                finish();
            }
        }, 1000);
        CheckNetConnect();
    }

    /**
     * 检查网络
     */
    private void CheckNetConnect() {
        if (false == NetWorkUtils.isConnected(getApplicationContext())) {
            ToastUtils.showShort("网络连接连接失败，请检查网络！");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
