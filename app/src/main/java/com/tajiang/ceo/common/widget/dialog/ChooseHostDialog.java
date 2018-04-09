package com.tajiang.ceo.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.constant.TJCst;
import com.tajiang.ceo.common.utils.SharedPreferencesUtils;
import com.tajiang.ceo.common.utils.ToastUtils;

/**
 * Created by Admins on 2016/10/24.
 */
public class ChooseHostDialog extends Dialog implements View.OnClickListener{

    private View contentView;
    Button btnHost;
    Button btnHostTest;
    Button btnHosCuit;

    public ChooseHostDialog(Context context ) {
        super(context, R.style.dialog_operate);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        contentView = getLayoutInflater().inflate(R.layout.dialog_choose_host, null);

        btnHost = (Button) contentView.findViewById(R.id.btn_host);
        btnHostTest = (Button)contentView.findViewById(R.id.btn_host_test);
        btnHosCuit = (Button)contentView.findViewById(R.id.btn_host_cui);

        btnHost.setOnClickListener(this);
        btnHostTest.setOnClickListener(this);
        btnHosCuit.setOnClickListener(this);

        setContentView(contentView);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_host:
                SharedPreferencesUtils.put(SharedPreferencesUtils.HOST_TEST_ROOT_URL, TJCst.HOST );
                break;
            case R.id.btn_host_test:
                SharedPreferencesUtils.put(SharedPreferencesUtils.HOST_TEST_ROOT_URL, TJCst.TEST_HOST );
                break;
            case R.id.btn_host_cui:
                SharedPreferencesUtils.put(SharedPreferencesUtils.HOST_TEST_ROOT_URL, TJCst.BIG_BROTHER );
                break;
        }
        /**
         * 清楚用户信息
         */
        SharedPreferencesUtils.remove(SharedPreferencesUtils.USER_LOGIN_INFOR);
        ToastUtils.showShort("重启应用生效");
        dismiss();
    }

}
