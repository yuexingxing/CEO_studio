package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by work on 2016/7/5.
 */
public class NoticeActivity extends BaseActivity {

    @BindView(R.id.tv_notice_normal)
    TextView tvNoticeNormal;
    @BindView(R.id.tv_notice_important)
    TextView tvNoticeImportant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("消息通知");
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.tv_notice_normal)
    public void onNormalClick() {
        intent2Activity(ShowNoticeActivity.class);
    }

    @OnClick(R.id.tv_notice_important)
    public void onImportantClick() {

    }
}
