package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 营业状态调整
 * Created by Administrator on 2016/5/10.
 */
public class MessStatusActivity extends BaseActivity implements HttpResponseListener{

    public static final int MESS_STATUES_OPENED = 1;
    public static final int MESS_STATUES_CLOSED = 0;

    @BindView(R.id.tv_msg_mess_state)
    TextView tvMsgMessState;
    @BindView(R.id.tv_mess_status)
    TextView tvMessStatus;
    @BindView(R.id.btn_mess_status)
    Button btnMessStatus;

    private String storeId;
    private int mMessStatus;
    private boolean hasStoreId = false;

    private Bundle mBundle;

    private LoadingDialog loadingDialog;


    @Override
    protected void initTopBar() {
        setTitle("营业状态调整");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_mess_status);

    }

    @Override
    protected void initData() {
        this.loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


        initText();
    }

    private void initText() {

        if (hasStoreId = getIntent().hasExtra("store_id") ) {
            storeId = getIntent().getStringExtra("store_id");
        }

        mMessStatus = getIntent().getIntExtra("mess_status", MESS_STATUES_OPENED);

        if (mMessStatus == MESS_STATUES_OPENED) {
            tvMessStatus.setText("营业中");
            tvMessStatus.setTextColor(this.getResources().getColor(R.color.green));
            tvMsgMessState.setText("当前餐厅处于设置的营业时间内，正常接收订单。");

            btnMessStatus.setText("停止营业");
            btnMessStatus.setTextColor(this.getResources().getColor(R.color.red_orange));

        } else {
            tvMessStatus.setText("停止营业");
            tvMessStatus.setTextColor(this.getResources().getColor(R.color.red_orange));
            tvMsgMessState.setText("当前餐厅处于停止营业状态，不接收订单。");

            btnMessStatus.setText("开启营业");
            btnMessStatus.setTextColor(this.getResources().getColor(R.color.green));
        }
    }

    @OnClick(R.id.btn_mess_status)
    public void onStstusClick() {

        mMessStatus = (mMessStatus == MESS_STATUES_OPENED) ? MESS_STATUES_CLOSED : MESS_STATUES_OPENED;

        if (hasStoreId) {
            loadingDialog.show();
            new HttpHandler(this).updateStoreState(storeId, mMessStatus);
        } else {
            putBundle2Intent();
        }

    }

    private void putBundle2Intent(){
        //实例化 Bundle, 设置需要传递的参数
        mBundle = new Bundle();
        mBundle.putInt("mess_status", mMessStatus);
        MessStatusActivity.this.setResult(RESULT_OK, MessStatusActivity.this.getIntent().putExtras(mBundle));
        MessStatusActivity.this.finish();
    }

    @Override
    public void onSuccess(BaseResponse response) {

        if ((Boolean)response.getData() == true){
            if (mMessStatus == MESS_STATUES_OPENED) {
                ToastUtils.showShort("开启成功");
            } else {
                ToastUtils.showShort("关闭成功");
            }

            putBundle2Intent();
        } else {
            ToastUtils.showShort(response.getMoreInfo());
        }
    }

    @Override
    public void onFailed(BaseResponse response) {
        ToastUtils.showLong(response.getMoreInfo());
    }

    @Override
    public void onFinish() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
