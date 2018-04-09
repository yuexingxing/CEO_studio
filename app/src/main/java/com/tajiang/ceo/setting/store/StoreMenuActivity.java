package com.tajiang.ceo.setting.store;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 档口管理
 */
public class StoreMenuActivity extends BaseActivity {

    @BindView(R.id.store_menu_name)
    TextView tvName;

    @BindView(R.id.store_menu_state)
    TextView tvState;

    private final int STORE_NAME = 0x0011;
    private final int STORE_STATUS = 0x0012;
    private final int STORE_RANGE = 0x0013;
    private final int STORE_TIME = 0x0014;

    private String store_name;
    private String store_id;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("档口");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_store_menu);
    }

    @Override
    protected void initData() {

        store_name = getIntent().getStringExtra("store_name");
        store_id = getIntent().getStringExtra("store_id");
        state = getIntent().getIntExtra("state", 0);

        tvName.setText(store_name);
        initState();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STORE_NAME && resultCode == RESULT_OK) {

            store_name = data.getStringExtra("store_name");
            tvName.setText(store_name);
        }else if(requestCode == STORE_STATUS && resultCode == RESULT_OK){

            state = data.getIntExtra("state", 0);
            initState();
        }

    }

    /**
     * 更新营业状态，单独放在这里是因为回调也要用
     */
    private void initState(){

        tvState.setText(state == 1 ? "营业中" : "停止营业");
    }

    //档口名称
    @OnClick(R.id.store_menu_layout_store_name)
    public void stroeName(){

        Intent intent = new Intent(this, StoreNameActivity.class);
        intent.putExtra("store_name", store_name);
        intent.putExtra("store_id", store_id);
        startActivityForResult(intent, STORE_NAME);
    }

    //营业状态
    @OnClick(R.id.store_menu_layout_status)
    public void status(){

        Intent intent = new Intent(this, StoreStateActivity.class);
        intent.putExtra("store_id", store_id);
        intent.putExtra("state", state);
        startActivityForResult(intent, STORE_STATUS);
    }

    //配送范围
    @OnClick(R.id.store_menu_layout_range)
    public void range(){

        Intent intent = new Intent(this, StoreApartmentActivity.class);
        intent.putExtra("store_id", store_id);
        startActivityForResult(intent, STORE_RANGE);
    }

    //档口时间
    @OnClick(R.id.store_menu_layout_time)
    public void time(){

        Intent intent = new Intent(this, StoreTimeActivity.class);
        intent.putExtra("store_id", store_id);
        startActivityForResult(intent, STORE_TIME);
    }
}
