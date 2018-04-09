package com.tajiang.ceo.setting.store;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 档口名称
 */
public class StoreNameActivity extends BaseActivity {

    @BindView(R.id.store_name)
    EditText edtName;

    private String store_name;
    private String store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {

    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_store_name);
    }

    @Override
    protected void initData() {

        setTitle("档口名称");
        store_name = getIntent().getStringExtra("store_name");
        store_id = getIntent().getStringExtra("store_id");

        edtName.setText(store_name);
        edtName.setSelection(store_name.length());
    }

    @OnClick(R.id.store_name_save)
    public void save(){

        if(TextUtils.isEmpty(edtName.getText().toString())){
            ToastUtils.showShort("档口名称不能为空");
            return;
        }

        if(store_name.equals(edtName.getText().toString())){
            ToastUtils.showShort("档口名称未做修改");
            return;
        }

        store_name = edtName.getText().toString();
        PostDataTools.shop_name(this, store_name, store_id, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                Intent intent = new Intent();
                intent.putExtra("store_name", store_name);
                intent.putExtra("store_id", store_id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
