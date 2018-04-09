package com.tajiang.ceo.setting.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.constant.TJCst;
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.utils.SharedPreferencesUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.login.activity.LoginActivity;
import com.tajiang.ceo.mess.activity.DormInfoActivity;
import com.tajiang.ceo.mess.activity.MessSelectActivity;
import com.tajiang.ceo.setting.activity.PaySettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.leifeng.ceo.common.activity.

/**
 * 设置
 * Created by Administrator on 2016/5/10.
 */
public class SettingFragment extends BaseFragment {

    @BindView(R.id.rl_setting_admin)
    RelativeLayout rlSettingAdmin;
    @BindView(R.id.rl_setting_pay)
    RelativeLayout rlSettingPay;
    @BindView(R.id.btn_quit)
    Button btnQuit;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_dorm)
    TextView tvDorm;
    @BindView(R.id.tv_canteen)
    TextView tvCanteen;
    @BindView(R.id.ll_content_ceo)
    LinearLayout llContentCeo;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    private LoadingDialog loadingDialog;

    @Override
    protected void initTopBar() {
        setTitle(getResourcesString(R.string.msg_setting));
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.fragment_setting);
        this.tvAppVersion.setText(TJCst.IS_DEBUG ? "测试版" : "");
        //只有CEO才能拥有此功能模块
//        if (UserUtils.getUser().getRoles() != TJApp.RULES_SCHOOL_CEO) {
//            llContentCeo.setVisibility(View.GONE);
//        }
    }


    @Override
    protected void initData() {
        loadingDialog = new LoadingDialog(getActivity());
//        tvUserName.setText(UserUtils.getUser().getRealName()+"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.rl_setting_admin, R.id.tv_dorm, R.id.tv_canteen, R.id.rl_setting_pay, R.id.btn_quit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_setting_admin:

                break;
            case R.id.tv_dorm:
                intent2Activity(DormInfoActivity.class);
                break;
            case R.id.tv_canteen:
                intent2Activity(MessSelectActivity.class);
                break;
            case R.id.rl_setting_pay:
                checkBank();
                break;
            case R.id.btn_quit:
                SharedPreferencesUtils.remove(SharedPreferencesUtils.USER_LOGIN_INFOR);
                SettingFragment.this.getActivity().finish();
                intent2Activity(LoginActivity.class);
                break;
        }
    }

    /**
     * 检查账户是否设置过银行卡，否则不能进入提现设置
     *
     * @return
     */
    private void checkBank() {

        intent2Activity(PaySettingActivity.class);
//        new HttpHandler(new HttpResponseListener() {
//            @Override
//            public void onSuccess(BaseResponse response) {
//                Bank bank = (Bank) response.getData();
//                if (bank.getCardNo() == null || bank.getCardNo().equals("")) {
//                    ToastUtils.showShort("请先绑定银行卡！");
//                } else {
//                    intent2Activity(PaySettingActivity.class);
//                }
//            }
//
//            @Override
//            public void onFailed(BaseResponse response) {
////                if (response.getErrorCode() == ErrorCode.CODE_NO_BANK) {   //没有绑定银行卡的错误码
////                    ToastUtils.showShort(response.getMoreInfo());
////                }
//            }
//
//            @Override
//            public void onFinish() {
//                if (loadingDialog.isShowing()) {
//                    loadingDialog.dismiss();
//                }
//            }
//        }).getCeoUserIdBank();
    }
}
