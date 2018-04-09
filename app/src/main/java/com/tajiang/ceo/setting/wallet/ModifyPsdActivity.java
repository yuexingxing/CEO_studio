package com.tajiang.ceo.setting.wallet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class ModifyPsdActivity extends BaseActivity {

    @BindView(R.id.modify_psd_old)
    EditText edtOld;

    @BindView(R.id.modify_psd_new)
    EditText edtNew;

    @BindView(R.id.modify_psd_new_2)
    EditText edtNew2;

    @BindView(R.id.modify_psd_commit)
    Button btnCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void initTopBar() {
        setTitle("修改密码");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_modify_psd);
    }

    @Override
    protected void initData() {

    }

    /**
     * 对EditText进行录入监听
     * @param edt
     */
    private void setEditableListener(EditText edt){

        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                checkStatus();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
    }

    public void checkStatus(){

        if(edtOld.getText().toString().length() > 5 && edtNew.getText().toString().length() > 5 && edtNew2.getText().toString().length() > 5){
            btnCommit.setBackgroundResource(R.drawable.shape_radius_order_main_color);
        }else{
            btnCommit.setBackgroundResource(R.drawable.shape_radius_order_gray_color);
        }
    }


    @OnClick(R.id.modify_psd_commit)
    public void commit(){

        String strOld = edtOld.getText().toString();
        String strNew = edtNew.getText().toString();
        String strNew2 = edtNew2.getText().toString();

        if(strOld.equals(strNew)){
            ToastUtils.showShort("新密码不能和旧密码一样");
            return;
        }

        if(!strNew2.equals(strNew)){
            ToastUtils.showShort("新密码和确认密码不一致");
            return;
        }

        PostDataTools.acct_updatepwd(this, strOld, strNew, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                if(flag){
                    finish();
                }
            }
        });
    }

}
