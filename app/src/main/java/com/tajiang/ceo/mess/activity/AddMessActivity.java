package com.tajiang.ceo.mess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加食堂
 * Created by Administrator on 2016/5/10.
 */
public class AddMessActivity extends BaseActivity {

    private static final int SEND_NAME_REQUEST = 1;  //食堂名字
    private static final int SEND_STATUS_REQUEST = 2;    //营业状态
    private static final int SEND_BBS_REQUEST = 3;    //食堂公告
    private static final int SEND_MESS_TIME_REQUEST = 4;    //食堂时间


    private int mMessStatus = 1;  //当前营业状态

    @BindView(R.id.tv_mess_opening_time)
    TextView tvMessOpeningTime;
    @BindView(R.id.rl_mess_name)
    RelativeLayout rlName;
    @BindView(R.id.rl_mess_status)
    RelativeLayout rlStatus;
    @BindView(R.id.rl_mess_bbs)
    RelativeLayout rlBBS;
    @BindView(R.id.rl_mess_time)
    RelativeLayout rlTime;
    @BindView(R.id.tv_mess_name)
    TextView tvMessName;
    @BindView(R.id.tv_mess_bbs)
    TextView tvMessBbs;
    @BindView(R.id.tv_mess_status)
    TextView tvMessStatus;

    @Override
    protected void initTopBar() {
        setTitle("添加食堂");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_addmess);
    }


    @Override
    protected void initData() {

    }


    @OnClick(R.id.rl_mess_name)
    void nameClick() {
        intent2ActivityForResult(AddMessNameActivity.class, SEND_NAME_REQUEST);
    }

    @OnClick(R.id.rl_mess_status)
    void statusClick() {
        Intent intent = new Intent();
        intent.putExtra("mess_status", mMessStatus);
        intent2ActivityWidthExtrasAndForResult(intent, MessStatusActivity.class, SEND_STATUS_REQUEST);
    }

    @OnClick(R.id.rl_mess_bbs)
    void bbsClick() {
        intent2ActivityForResult(AddMessBBSActivity.class, SEND_BBS_REQUEST);
    }

    @OnClick(R.id.rl_mess_time)
    void timeClick() {
        intent2ActivityForResult(MessTimeActivity.class, SEND_MESS_TIME_REQUEST);
//        startActivity(new Intent(AddMessActivity.this, MessTimeActivity.class));
    }

    @OnClick(R.id.btn_add_finished)
    void addFinish() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();

            if (bundle != null) {
                switch (requestCode) {
                    case SEND_NAME_REQUEST:
                        tvMessName.setText(bundle.getString("mess_name"));
                        break;
                    case SEND_STATUS_REQUEST:
                        mMessStatus = bundle.getInt("mess_status");
                        if (mMessStatus == 1) {
                            tvMessStatus.setText("营业中");
                            tvMessStatus.setTextColor(this.getResources().getColor(R.color.green));
                        } else {
                            tvMessStatus.setText("停止营业");
                            tvMessStatus.setTextColor(this.getResources().getColor(R.color.text_black_3));
                        }
                        break;
                    case SEND_BBS_REQUEST:
                        tvMessBbs.setText(bundle.getString("mess_bbs"));
                        break;
                    case SEND_MESS_TIME_REQUEST: //接收设置好的营业时间段
                        tvMessOpeningTime.setText(bundle.getBoolean("is_finish_setting")?"已设置":"未设置");
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
