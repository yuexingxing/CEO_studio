package com.tajiang.ceo.setting.store;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.dialog.CommonDialog;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.Res;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 营业状态修改
 */
public class StoreStateActivity extends BaseActivity {

    @BindView(R.id.store_state_icon)
    ImageView imgIcon;

    @BindView(R.id.store_state_tips)
    TextView tvTips;

    @BindView(R.id.store_state_btn)
    Button btn;

    private String store_id;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("营业状态调整");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_store_state);
    }

    @Override
    protected void initData() {

        store_id = getIntent().getStringExtra("store_id");
        state = getIntent().getIntExtra("state", 0);
        if(state == 1){

            btn.setText("停止营业");
            imgIcon.setImageResource(R.drawable.in_operation);
            tvTips.setText(Res.getString(R.string.store_state_tips_open));
        }else{
            btn.setText("开始营业");
            imgIcon.setImageResource(R.drawable.in_closed);
            tvTips.setText(Res.getString(R.string.store_state_tips_close));
        }
    }

    @OnClick(R.id.store_state_btn)
    public void commit(){

        String messge = "";
        if(state == 0){
            messge = "确定开始营业？";
        }else{
            messge = Res.getString(R.string.store_state_tips_2);
        }

        CommonDialog.showDialog(this, "取消", "确认", messge, new CommonDialog.DialogCallback() {

            @Override
            public void callback(int position) {

                if(position != 1){
                    return;
                }

                String bussState = state == 1 ? "0" : "1";//如果当前是营业，则停止营业，否则开始营业
                PostDataTools.shop_bussstate(StoreStateActivity.this, store_id, bussState, new PostDataTools.DataCallback() {

                    @Override
                    public void callback(boolean flag, String message, Object object) {

                        if (flag){
                            Intent intent = new Intent();
                            intent.putExtra("state", state == 1 ? 0 : 1);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

}
