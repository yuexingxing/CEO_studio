package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;

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
public class AddMessBBSActivity extends BaseActivity implements HttpResponseListener {

    @BindView(R.id.et_mess_bbs)
    EditText etMessBbs;
    @BindView(R.id.tv_bbs_remain_count)
    TextView tvBbsRemainCount;
    @BindView(R.id.btn_save)
    Button btnSave;

    private Bundle mBundle;
    private boolean hasStoreId = false;
    private String storeId;
    private String storeNotice;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mess_bbs_activity);
        ButterKnife.bind(this);

        initTextChangedListener();
        initText();
    }

    private void initText(){
        if ( hasStoreId = getIntent().hasExtra("store_id") ) {
            storeId = getIntent().getStringExtra("store_id");
        }
        storeNotice = getIntent().getStringExtra("store_notice");
        etMessBbs.setText(storeNotice);
    }

    /**
     *
     * 设置公告输入框文字数量监听器
     */
    private void initTextChangedListener() {
        TextWatcher mTextWatcher = new TextWatcher() {
            private CharSequence temp;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                tvBbsRemainCount.setText("" + (25 - temp.length()));
            }
        };
        etMessBbs.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initTopBar() {
        setTitle("食堂公告");
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

        if (hasStoreId) {
            loadingDialog.show();
            new HttpHandler(this).updateStoreNotice(storeId,etMessBbs.getText().toString());
        } else {
            putBundle2Intent();
        }
    }

    private void putBundle2Intent(){
        //实例化 Bundle, 设置需要传递的参数
        mBundle = new Bundle();
        mBundle.putString("mess_bbs", etMessBbs.getText().toString());
        AddMessBBSActivity.this.setResult(RESULT_OK, AddMessBBSActivity.this.getIntent().putExtras(mBundle));
        AddMessBBSActivity.this.finish();
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if ((Boolean)response.getData() == true){
            ToastUtils.showShort("食堂公告保存成功");
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
}
