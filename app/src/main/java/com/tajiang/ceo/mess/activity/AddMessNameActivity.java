package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

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
 * Created by work on 2016/6/30.
 */
public class AddMessNameActivity extends BaseActivity implements HttpResponseListener{

    @BindView(R.id.et_mess_name)
    EditText etMessName;
    @BindView(R.id.btn_save)
    Button btnSave;

    private boolean hasStoreId = false;
    private String storeName;
    private String storeId;
    private Bundle mBundle;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mess_name_activity);
        ButterKnife.bind(this);

        if ( hasStoreId = getIntent().hasExtra("store_id") ) {
            storeName = getIntent().getStringExtra("mess_name");
            storeId = getIntent().getStringExtra("store_id");
            etMessName.setText(storeName);
        }
    }

    @Override
    protected void initTopBar() {
        setTitle("食堂名称");
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {
        this.loadingDialog = new LoadingDialog(this);
    }

    @OnClick(R.id.btn_save)
    public void onClick() {
        if (hasStoreId && checkName()) {
            loadingDialog.show();
            new HttpHandler(this).updateStoreName(storeId,etMessName.getText().toString());
        }
    }

    private boolean checkName() {
        if (etMessName.getText() == null ||
                etMessName.getText().toString().equals("")) {
            ToastUtils.showShort("请填写食堂名称");
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if ((Boolean)response.getData() == true){
            ToastUtils.showShort("食堂名称保存成功");
            putBundle2Intent();
        } else {
            ToastUtils.showShort(response.getMoreInfo());
        }
    }

    @Override
    public void onFailed(BaseResponse response) {
        ToastUtils.showShort(response.getMoreInfo());
    }

    @Override
    public void onFinish() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void putBundle2Intent(){
        //实例化 Bundle, 设置需要传递的参数
        mBundle = new Bundle();
        mBundle.putString("mess_name", etMessName.getText().toString());

        AddMessNameActivity.this.setResult(RESULT_OK, AddMessNameActivity.this.getIntent().putExtras(mBundle));
        AddMessNameActivity.this.finish();
    }
}
