package com.tajiang.ceo.mess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.model.Store;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 食堂信息
 * Created by Administrator on 2016/5/10.
 */
public class MessInfoActivity extends BaseActivity {

    private static final int SEND_NAME_REQUEST          = 1;    //食堂名字
    private static final int SEND_STATUS_REQUEST        = 2;    //营业状态
    private static final int SEND_BBS_REQUEST           = 3;    //食堂公告
    private static final int SEND_MESS_TIME_REQUEST     = 4;    //食堂时间
    @BindView(R.id.tv_mess_name)
    TextView        tvMessName;
    @BindView(R.id.tv_mess_status)
    TextView        tvMessStatus;
    @BindView(R.id.tv_mess_notice)
    TextView        tvMessNotice;
    @BindView(R.id.rl_mess_name)
    RelativeLayout  rlMessName;
    @BindView(R.id.rl_mess_status)
    RelativeLayout  rlMessStatus;
    @BindView(R.id.rl_mess_notice)
    RelativeLayout  rlMessNotice;

    private boolean isMessInfoChenged = false;    //标记当前食堂信息是否被修改过
    private Store   mStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messinfo);
        ButterKnife.bind(this);

        initMessInfo();
    }

    private void initMessInfo() {
        mStore = getIntent().getParcelableExtra("current_store");
        tvMessName.setText(mStore.getName());
        tvMessNotice.setText(mStore.getNotice());
        if (mStore.getState() == MessStatusActivity.MESS_STATUES_OPENED) {
            tvMessStatus.setText("营业中");
            tvMessStatus.setTextColor(this.getResources().getColor(R.color.green));
        } else {
            tvMessStatus.setText("停止营业");
            tvMessStatus.setTextColor(this.getResources().getColor(R.color.gray_dark));
        }
    }


    @Override
    public void onBackPressed() {
        back2MessSelected();
    }

    @Override
    protected void initTopBar() {
        setTitle("食堂信息");
        enLeftClickListener(new OnLeftClick() {
            @Override
            public void leftClick() {
                back2MessSelected();
            }
        });
    }

    private void back2MessSelected() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_mess_info_changed", isMessInfoChenged);
        MessInfoActivity.this.setResult(RESULT_OK, MessInfoActivity.this.getIntent().putExtras(bundle));
        MessInfoActivity.this.finish();
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {


    }

    @OnClick(R.id.rl_mess_name)
    void nameClick() {
        if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
        currentTime = System.currentTimeMillis();

        Intent intent = new Intent();
        intent.putExtra("store_id", mStore.getId());
        intent.putExtra("mess_name", mStore.getName());
        intent2ActivityWidthExtrasAndForResult(intent, AddMessNameActivity.class, SEND_NAME_REQUEST);
    }

    @OnClick(R.id.rl_mess_status)
    void statusClick() {
        if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
        currentTime = System.currentTimeMillis();

        Intent intent = new Intent();
        intent.putExtra("mess_status", mStore.getState());
        intent.putExtra("store_id", mStore.getId());
        intent2ActivityWidthExtrasAndForResult(intent, MessStatusActivity.class, SEND_STATUS_REQUEST);
    }

    @OnClick(R.id.rl_mess_notice)
    void bbsClick() {
        if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
        currentTime = System.currentTimeMillis();

        Intent intent = new Intent();
        intent.putExtra("store_notice", mStore.getNotice());
        intent.putExtra("store_id", mStore.getId());
        intent2ActivityWidthExtrasAndForResult(intent, AddMessBBSActivity.class, SEND_BBS_REQUEST);
    }

//    @OnClick(R.id.rl_check_mess_time)
//    void onNoticeClick() {
//        if ((System.currentTimeMillis() - currentTime) < TIME_IN_MILLS) return;
//        currentTime = System.currentTimeMillis();
//
//        Intent intent = new Intent();
//        intent.putExtra("store_id", mStore.getId());
//        intent2ActivityWidthExtrasAndForResult(intent, SetMessTimeActivity.class, SEND_MESS_TIME_REQUEST);
//    }

    @OnClick(R.id.rl_deliver_scope)
    public void onClick() {
        Intent intent = new Intent();
        intent.putExtra("store_id", mStore.getId());
        intent2ActivityWithExtras(intent, MessDeliverScopeActivity.class);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();

            if (bundle != null) {
                this.isMessInfoChenged = true;
                switch (requestCode) {
                    case SEND_NAME_REQUEST:   //修改食堂名字
                        tvMessName.setText(bundle.getString("mess_name"));
                        mStore.setName(bundle.getString("mess_name"));
                        break;
                    case SEND_STATUS_REQUEST: //修改食堂营业状态
                        int mMessStatus = bundle.getInt("mess_status");
                        if (mMessStatus == MessStatusActivity.MESS_STATUES_OPENED) {
                            tvMessStatus.setText("营业中");
                            tvMessStatus.setTextColor(this.getResources().getColor(R.color.green));
                        } else {
                            tvMessStatus.setText("停止营业");
                            tvMessStatus.setTextColor(this.getResources().getColor(R.color.text_black_3));
                        }
                        mStore.setState(mMessStatus);
                        break;
                    case SEND_BBS_REQUEST:  //修改食堂公告
                        tvMessNotice.setText(bundle.getString("mess_bbs"));
                        mStore.setNotice(bundle.getString("mess_bbs"));
                        break;
                    case SEND_MESS_TIME_REQUEST: //接收设置好的营业时间段
//                        tvMessOpeningTime.setText(bundle.getBoolean("is_finish_setting")?"已设置":"未设置");
                        break;
                    default:
                        break;
                }
            }
        }

    }


}
