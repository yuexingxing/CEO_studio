package com.tajiang.ceo.common.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.constant.TJCst;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;


/**
 * activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public static final long TIME_IN_MILLS = 600;
    protected long    currentTime = 0;      //用户600ms内只能响应用户一次点击事件，此方式仅解决用户手抖造成点开2次activity

    private RelativeLayout rootNav;
    private ViewStub leftRectNav;
    private ViewStub titleRectNav;
    private ViewStub rightRectNav;
    private FrameLayout flContent;

    private TextView tvTitle;

    private TextView baseTitleRightTextView;
    private TextView baseTitleLeftTextView;

    private OnLeftClick onLeftClick;
    private OnTitleClick onTitleClick;
    private OnRightClick onRightClick;

    protected abstract void initTopBar();

    protected abstract void initLayout();

    protected abstract void initData();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_title);
        TJApp.getInstance().addActivity(this);
        /**
         * 检测内存使用泄漏情况
         * 在自己的应用初始Activity中加入如下两行代码
         */
        if (TJCst.IS_LEAK_CANARY_OPEN) {
            RefWatcher refWatcher = TJApp.getInstance().getRefWatcher();
            refWatcher.watch(this);
        }
        initView();
        enLeftImage();
        initTopBar();
        initLayout();
        initData();

    }


    protected void initView() {

        rootNav = (RelativeLayout) findViewById(R.id.common_nav);
        leftRectNav = (ViewStub) findViewById(R.id.common_nav_left);
        titleRectNav = (ViewStub) findViewById(R.id.common_nav_title);
        rightRectNav = (ViewStub) findViewById(R.id.common_nav_right);
        flContent = (FrameLayout) findViewById(R.id.common_content_root);
    }

    @Override
    public void setContentView(View view) {
        flContent.addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        flContent.addView(view);
        ButterKnife.bind(this, view);

    }

    protected void disableNav() {

        rootNav.setVisibility(View.GONE);
    }

    public void setTitle(CharSequence title) {
        setTitle(title, null);
    }

    public void setTitleMsg(String title) {

        tvTitle.setText(title);
    }

    public void setTitle(CharSequence title, OnTitleClick listener) {
        this.onTitleClick = listener;
        titleRectNav.setLayoutResource(R.layout.base_title_text);
        tvTitle = (TextView) titleRectNav.inflate();
        tvTitle.setText(title);
        tvTitle.setOnClickListener(this);
    }

    public void setTitle(int title) {
        setTitle(title, null);
    }

    public void setTitle(int title, OnTitleClick listener) {
        this.onTitleClick = listener;
        titleRectNav.setLayoutResource(R.layout.base_title_text);
        TextView txt = (TextView) titleRectNav.inflate();
        txt.setText(title);
        txt.setOnClickListener(this);
    }

    public String getResourcesString(int resId) {
        return getApplicationContext().getResources().getString(resId);
    }

    /**
     * 左侧为图标,默认为返回按钮
     */
    protected void enLeftImage() {
        leftRectNav.setLayoutResource(R.layout.base_title_left_img);
        leftRectNav.inflate().setOnClickListener(this);
    }

    protected void enLeftClickListener(OnLeftClick listener) {
        this.onLeftClick = listener;
    }

    /**
     * 掩藏左边按钮
     */
    protected void disEnableLeft() {
        leftRectNav.setVisibility(View.GONE);
    }

    protected void enableLeftText(CharSequence text) {
        enableLeftText(text, null);
    }

    protected void enableLeftClick(OnLeftClick listener) {
        this.onLeftClick = listener;
    }

    /**
     * 左侧为字体
     * @param text
     */
    protected void enableLeftText(CharSequence text, OnLeftClick listener) {
        this.onLeftClick = listener;
        leftRectNav.setLayoutResource(R.layout.base_title_left_text);

        baseTitleLeftTextView = initCommonText(leftRectNav, R.layout.base_title_left_text);
        baseTitleLeftTextView.setText(text);
    }

    protected void enableLeftText(int text) {
        enableLeftText(text, null);
    }

    protected void enableLeftText(int text, OnLeftClick listener) {
        this.onLeftClick = listener;
        baseTitleLeftTextView = initCommonText(leftRectNav, R.layout.base_title_left_text);
        baseTitleLeftTextView.setText(text);
    }


    private TextView initCommonText(ViewStub viewStub, int resId) {
        leftRectNav.setLayoutResource(resId);
        TextView textView = (TextView) viewStub.inflate();
        textView.setTextSize(14);
        textView.setPadding(16, 0, 0, 0);
        textView.setOnClickListener(this);
        return textView;
    }

    protected void enableRightImg(int resId) {
        enableRightImg(resId, null);
    }

    protected void enableRightImg(int resId, OnRightClick listener) {
        this.onRightClick = listener;
        rightRectNav.setLayoutResource(R.layout.base_title_right_img);
        ImageView imageView = (ImageView) rightRectNav.inflate();
        imageView.setImageResource(resId);
        imageView.setPadding(0, 0, 16, 0);
        imageView.setOnClickListener(this);
    }

    protected void enableRightText(int text) {
        enableRightText(text, null);
    }

    protected void enableRightText(int text, OnRightClick listener) {
        this.onRightClick = listener;
        baseTitleRightTextView = initCommonText(rightRectNav, R.layout.base_title_right_text);
        baseTitleRightTextView.setText(text);
    }

    protected void enableRightText(CharSequence text) {
        enableRightText(text, null);
    }

    protected void enableRightText(CharSequence text, OnRightClick listener) {
        this.onRightClick = listener;
        rightRectNav.setLayoutResource(R.layout.base_title_right_text);
        baseTitleRightTextView = initCommonText(rightRectNav, R.layout.base_title_right_text);
        baseTitleRightTextView.setPadding(0, 0, 16, 0);
        baseTitleRightTextView.setText(text);
    }

    protected void setLeftTextAndDrawable(String text, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        baseTitleLeftTextView.setText(text);
        baseTitleRightTextView.setCompoundDrawables(left, top, right, bottom);
    }

    protected void setRightTextAndDrawable(String text, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        baseTitleRightTextView.setText(text);
        baseTitleRightTextView.setCompoundDrawables(left, top, right, bottom);
    }

    protected void intent2Activity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void intent2ActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
    }

    protected void intent2ActivityWithExtras(Intent intent, Class<?> cls) {
        intent.setClass(this, cls);
        startActivity(intent);
    }

    protected void intent2ActivityWidthExtrasAndForResult(Intent intent, Class<?> cls, int requestCode) {
        intent.setClass(this, cls);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_img:
            case R.id.tv_title_left:
                if(onLeftClick == null) {
                    finish();
                } else {
                    onLeftClick.leftClick();
                }
                break;
            case R.id.tv_title:
                if(onTitleClick != null) {
                    onTitleClick.titleClick();
                }
                break;
            case R.id.iv_title_right:
            case R.id.tv_title_right:
                if(onRightClick != null) {
                    onRightClick.rightClick();
                }
                break;
            default:
                break;
        }
    }

    public interface OnLeftClick {
        void leftClick();
    }

    public interface OnTitleClick {
        void titleClick();
    }

    public interface OnRightClick {
        void rightClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TJApp.getInstance().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        BaseActivity.this.finish();
    }

}
