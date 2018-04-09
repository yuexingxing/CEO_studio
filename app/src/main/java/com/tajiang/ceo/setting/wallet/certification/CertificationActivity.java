package com.tajiang.ceo.setting.wallet.certification;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 实名认证
 */
public class CertificationActivity extends BaseActivity {

    @BindView(R.id.certafication_name)
    EditText edtName;

    @BindView(R.id.certafication_id)
    EditText edtId;

    @BindView(R.id.certafication_commit)
    Button btnCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void initTopBar() {
        setTitle("实名认证");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_certification);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.certafication_commit)
    public void commit(){

        String name = edtName.getText().toString();
        String id = edtId.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(id)){
            ToastUtils.showShort("姓名或身份证号不能为空");
            return;
        }

        PostDataTools.user_certify(this, name, id, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                if(flag){
                    finish();
                }
            }
        });
    }

}
