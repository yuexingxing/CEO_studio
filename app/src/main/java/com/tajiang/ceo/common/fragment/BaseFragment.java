package com.tajiang.ceo.common.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/15.
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {

    protected static final long TIME_IN_MILLS = 600;
    protected long    currentTime = 0;      //用户600ms内只能响应用户一次点击事件，此方式仅解决用户手抖造成点开2次activity

    TextView tvTitle;

    private RelativeLayout rootNav;
    private ViewStub leftRectNav;
    private ViewStub titleRectNav;
    private ViewStub rightRectNav;

    private FrameLayout rootContent;

    private OnLeftClick onLeftClick;
    private OnTitleClick onTitleClick;
    private OnRightClick onRightClick;

    public BaseActivity activity;
    private LayoutInflater inflater;
    private LinearLayout rootLayout;

    protected abstract void initTopBar();

    protected abstract void initLayout();

    protected abstract void initData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(getClass().getSimpleName() + "--------->onCreate");

        if (activity instanceof FragmentActivity) {
            activity = (BaseActivity) getActivity();
        }
    }

    @Override
    public void onAttach(Context context) {
        context = activity;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        rootLayout = (LinearLayout) inflater.inflate(R.layout.base_title, null);

        rootNav = (RelativeLayout) rootLayout.findViewById(R.id.common_nav);

        titleRectNav = (ViewStub) rootLayout.findViewById(R.id.common_nav_title);
        leftRectNav = (ViewStub) rootLayout.findViewById(R.id.common_nav_left);
        rightRectNav = (ViewStub) rootLayout.findViewById(R.id.common_nav_right);

        rootContent = (FrameLayout) rootLayout.findViewById(R.id.common_content_root);
        initTopBar();
        initLayout();
        initData();

        return rootLayout;
    }

    public <T extends View> T findv(int id) {
        return (T) rootLayout.findViewById(id);
    }

    protected void setContentView(int resLayoutId) {
        View view = LayoutInflater.from(getActivity()).inflate(resLayoutId, null);
        rootContent.addView(view);
        ButterKnife.bind(this, view);
    }

    public String getResourcesString(int resId) {
        return getActivity().getApplicationContext().getResources().getString(resId);
    }

    public void setTitle(CharSequence title) {
        titleRectNav.setLayoutResource(R.layout.base_title_text);
        tvTitle = (TextView) titleRectNav.inflate();
        tvTitle.setText(title);
        tvTitle.setOnClickListener(this);
    }

    public void updateTitleText(CharSequence title) {
        tvTitle.setText(title);
    }

    public void setTitleRightImg(int resId) {
        Drawable drawable= getResources().getDrawable(resId);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvTitle.setCompoundDrawables(null,null,drawable,null);
    }

    protected void setTitleByLayout(int resId) {
        titleRectNav.setLayoutResource(resId);
        titleRectNav.inflate().setOnClickListener(this);
    }

    protected void enLeftImage() {
        leftRectNav.setLayoutResource(R.layout.base_title_left_img);
        leftRectNav.inflate().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    protected void setLeftByString(String text) {
        leftRectNav.setLayoutResource(R.layout.base_title_left_text);
        TextView txt = (TextView) leftRectNav.inflate();
        txt.setText(text);
        txt.setTextSize(14);
        txt.setOnClickListener(this);
    }

    protected void setLeftByLayout(int resId) {
        leftRectNav.setLayoutResource(resId);
        leftRectNav.inflate().setOnClickListener(this);
    }

    protected void setLeftByImgId(int imgId) {
        leftRectNav.setLayoutResource(R.layout.base_title_left_img);
        ImageView img = (ImageView) leftRectNav.inflate();
        img.setImageResource(imgId);
        img.setOnClickListener(this);
    }

    protected void setRightByImgId(int imgId) {
        rightRectNav.setLayoutResource(R.layout.base_title_right_img);
        ImageView img = (ImageView) rightRectNav.inflate();
        img.setImageResource(imgId);
        img.setOnClickListener(this);
    }

    protected void setRightByString(String text) {
        rightRectNav.setLayoutResource(R.layout.base_title_right_text);
        TextView txt = (TextView) rightRectNav.inflate();
        txt.setText(text);
        txt.setTextSize(14);
        txt.setOnClickListener(this);
    }

    protected void setRightByLayout(int resId) {
        rightRectNav.setLayoutResource(resId);
        rightRectNav.inflate().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_img:
            case R.id.tv_title_left:
                if(onLeftClick != null) {
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

    public void setOnLeftClick(OnLeftClick o) {
        this.onLeftClick = o;
}

    public void setOnTitleClick(OnTitleClick o) {
        this.onTitleClick = o;
    }

    public void setOnRightClick(OnRightClick o) {
        this.onRightClick = o;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    protected void intent2ActivityWidthExtrasAndForResult(Intent intent, Class<?> cls, int requestCode) {
        intent.setClass(getActivity(), cls);
        startActivityForResult(intent, requestCode);
    }

    protected void intent2Activity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    protected void intent2ActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
        startActivityForResult(intent, requestCode);
    }

    protected void intent2ActivityWidthExtras(Intent intent, Class<?> cls) {
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    protected View getRootLayoutView() {
        return rootLayout;
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    public void reFreshCurrentPageData() {

    }

    protected void disableNav() {

        rootNav.setVisibility(View.GONE);
    }
}
