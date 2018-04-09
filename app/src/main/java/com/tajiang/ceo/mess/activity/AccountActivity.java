package com.tajiang.ceo.mess.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.CardNumberUtil;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.common.widget.WheelView;
import com.tajiang.ceo.mess.data_base_op.DBHelperGetProvinces;
import com.tajiang.ceo.model.Bank;
import com.tajiang.ceo.model.City;
import com.tajiang.ceo.model.Name;
import com.tajiang.ceo.model.Province;
import com.xw.repo.xedittext.XEditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账户信息
 */
public class AccountActivity extends BaseActivity implements HttpResponseListener {

    private static int CARD_NAME_LENGTH = 8;  //限制持卡人名字长度为6个中文字符

    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.tv_bank_address)
    TextView tvBankAddress;
    @BindView(R.id.wv_choose_shen_fen)
    WheelView wvChooseShenFen;
    @BindView(R.id.wv_choose_city)
    WheelView wvChooseCity;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_card_name)
    EditText tvCardName;
    @BindView(R.id.tv_card_number)
    XEditText tvCardNumber;
    @BindView(R.id.rl_choose_bank)
    RelativeLayout rlChooseBank;
    @BindView(R.id.rl_account_address)
    RelativeLayout rlAccountAddress;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.tv_withdraw_date)
    TextView tvWithdrawDate;
    @BindView(R.id.tv_min_money)
    TextView tvMinMoney;

    private boolean isCardBind = false;
    private boolean isUpdateBank = false;
    private boolean isEditBankMode = false;

    private HandlerThread mGetAssetsThread;
    private MyHandler mGetAssetsHandler;

    private LoadingDialog loadingDialog;
    private List<Province> provinceList;

    private Bank bank;

    private DBHelperGetProvinces dbHelperGetProvinces;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        initMyView();
    }

    private void initMyView() {
        this.tvMinMoney.setText("￥" + this.getIntent().getStringExtra("min_withdraw_cash"));
        this.tvWithdrawDate.setText(this.getIntent().getStringExtra("withdraw_date"));

        int pattern[] = {4, 4, 4, 4, 4};
        this.tvCardNumber.setSeparator(" ");
        this.tvCardNumber.setPattern(pattern);
        this.tvCardNumber.setRightMarkerDrawable(null);

        this.tvCardNumber.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strCardNum = CardNumberUtil.ClearBlank(temp.toString());

                if (strCardNum.length() == 16) {
                    /**
                     * 当卡号长度达到16位时候，需要调用银行卡判断接口
                     */
                    new HttpHandler(new HttpResponseListener() {
                        @Override
                        public void onSuccess(BaseResponse response) {
                            Name name = (Name) response.getData();
                            if (name.getName() != null && !name.getName().equals("")) {
                                AccountActivity.this.tvBank.setText(name.getName());
                            }
                        }

                        @Override
                        public void onFailed(BaseResponse response) {

                        }

                        @Override
                        public void onFinish() {

                        }
                    }).bankUtil(strCardNum);
                }
            }
        });

        /**
         * 持卡人文本框 只输入汉字
         */
        this.tvCardName.addTextChangedListener(new TextWatcher() {
            private String LastText = "";

            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
//                LogUtils.e("beforeTextChanged 被执行----> s=" + s+"----start="+ start
//                        + "----after="+after + "----count" +count);
                LastText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LogUtils.e("onTextChanged 被执行---->s=" + s + "----start="+ start
//                        + "----before="+before + "----count" +count);
                /**
                 * 新输入的数据
                 */
//                if (isEditBankMode == false && count > 0) {
//                    LogUtils.e("isEditBankMode---->s=");
//                    String temp = s.toString();
//                    String newString ="";
//
//                    //限制持卡人名字长度为8个中文字符
//                    if (temp.length() > CARD_NAME_LENGTH) {
//                        temp = temp.substring(0, CARD_NAME_LENGTH);
//                        //新输入的数据
//                        newString = temp.substring(start, temp.length());
//                        if (newString.toString().matches("[\u4e00-\u9fa5]+")) {
//                            tvCardName.setText(LastText + newString);
//                        } else {
//                            tvCardName.setText(LastText);
//                        }
//                        tvCardName.setSelection(tvCardName.getText().toString().length());
//                    } else {
//                        //新输入的数据
//                        newString = temp.substring(start, temp.length());
//                        if (!newString.toString().matches("[\u4e00-\u9fa5]+")) {
//                            tvCardName.setText(LastText);
//                            tvCardName.setSelection(tvCardName.getText().toString().length());
//                        }
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setViewVisible(View.GONE);
        wvChooseShenFen.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {

                List<City> cities = dbHelperGetProvinces.getCity(provinceList.get(selectedIndex - 1).getAreaId());
                List<String> cityNames = new ArrayList<String>();
                for (City city : cities) {
                    cityNames.add(city.getAreaName());
                }
                wvChooseCity.setOffset(1);
                wvChooseCity.setItems(cityNames);
                wvChooseCity.setSeletion(0);
            }
        });

        wvChooseCity.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {

            }
        });

    }

    @Override
    protected void initTopBar() {
        setTitle("账户信息");
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initData() {
        this.isCardBind = this.getIntent().getBooleanExtra("is_card_bind", false);
        this.dbHelperGetProvinces = new DBHelperGetProvinces(this);
        this.bank = new Bank();
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        mGetAssetsHandler = new MyHandler();
        //创建启动解析资源的后台线程
        new Thread(new GetAssetsThread()).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO.........
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                tvBank.setText(bundle.getString("bank_name"));
            }
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        this.bank = (Bank) response.getData();
        if (bank != null) {
            if (bank.getCardNo() != null && bank.getUserName() != null) {
                setBankViewsEditable(false);
            }
            setAccountInfo(bank);
        }
    }

    @Override
    public void onFailed(BaseResponse response) {

    }

    @Override
    public void onFinish() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void setBankViewsEditable(boolean editable) {
        if (editable == true) {
            tvCardName.setEnabled(editable);
            tvCardName.setHint(this.getResources().getString(R.string.msg_input_your_card_name));
            tvCardName.setText(bank.getUserName());
            tvCardName.setSelection(bank.getUserName().length());

            tvCardNumber.setEnabled(editable);
            rlChooseBank.setClickable(editable);
            rlAccountAddress.setClickable(editable);
            tvCardNumber.setText("");
            tvCardNumber.setTextColor(this.getResources().getColor(R.color.text_black));
            tvBank.setText("");
            tvBankAddress.setText("");
            btnSave.setText("保存");
            isEditBankMode = false;
        } else {
            tvCardName.setEnabled(editable);
            rlChooseBank.setClickable(editable);
            rlAccountAddress.setClickable(editable);
            tvCardNumber.setEnabled(editable);

            tvBank.setTextColor(this.getResources().getColor(R.color.text_black_3));
            tvCardNumber.setTextColor(this.getResources().getColor(R.color.text_black_3));
            tvBankAddress.setTextColor(this.getResources().getColor(R.color.text_black_3));

            btnSave.setText("修改账户信息");
            isUpdateBank = true;
            isEditBankMode = true;
        }
    }

    public void setAccountInfo(Bank bank) {
        tvCardName.setHint(bank.getUserName() == null ? "" : formatCardName(bank.getUserName()));
        tvCardName.setHintTextColor(getResources().getColor(R.color.text_black_3));

        tvCardNumber.setText(bank.getCardNo() == null ? "" : CardNumberUtil.formatCardNumber(bank.getCardNo()));
        tvBank.setText(bank.getOpenBank() == null ? "" : bank.getOpenBank());

        tvBankAddress.setText(bank.getCity() == null ? "" : bank.getCity());

    }

    /**
     * 格式化持卡人姓名，只显示姓名最后一个字
     *
     * @param CardName
     * @return
     */
    private String formatCardName(String CardName) {
        String name = "";
        if (CardName.length() > 1) {
            for (int i = 0; i < CardName.length() - 1; i++) {
                name += "*";
            }
        }
        name += CardName.charAt(CardName.length() - 1);
        return name;
    }

    private void setViewVisible(int visibility) {
        tvCancel.setVisibility(visibility);
        tvFinish.setVisibility(visibility);
        wvChooseShenFen.setVisibility(visibility);
        wvChooseCity.setVisibility(visibility);
    }

    @OnClick(R.id.tv_cancel)
    public void onCancelClick() {
        setBankAddressRightImg(R.mipmap.icon_jiantou_r);
        setViewVisible(View.GONE);
    }

    @OnClick(R.id.tv_finish)
    public void onFinishClick() {
        setBankAddressRightImg(R.mipmap.icon_jiantou_r);
        setViewVisible(View.GONE);
        tvBankAddress.setText(wvChooseCity.getSeletedItem());
    }

    @OnClick(R.id.rl_account_address)
    public void onAddressClick() {
        setBankAddressRightImg(R.mipmap.icon_jiantou_xia_2);
        setViewVisible(View.VISIBLE);
    }

    @OnClick(R.id.rl_choose_bank)
    public void onClick() {

        if (tvBank.getText() == null || tvBank.getText().toString().equals("")) {
            intent2ActivityForResult(ChooseBankActivity.class, 0);
        } else {
            Intent intent = new Intent();
            intent.putExtra("bank_name", tvBank.getText().toString());
            intent2ActivityWidthExtrasAndForResult(intent, ChooseBankActivity.class, 0);
        }
    }

    @OnClick(R.id.btn_save)
    public void onSaveAccountClick() {
        if (isEditBankMode == true) {
            setBankViewsEditable(true);
            return;
        }

        if (check()) {
            if (isUpdateBank == true) {
                final LoadingDialog loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                new HttpHandler(new HttpResponseListener() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getData().equals(true)) {
                            ToastUtils.showShort("保存完毕");

                            Bundle mBundle = new Bundle();
                            mBundle.putBoolean("is_update_success", true);
                            AccountActivity.this.setResult(RESULT_OK, AccountActivity.this.getIntent().putExtras(mBundle));
                            AccountActivity.this.finish();
                        } else {
                            ToastUtils.showShort("保存失败");
                        }
                    }

                    @Override
                    public void onFailed(BaseResponse response) {

                    }

                    @Override
                    public void onFinish() {
                        loadingDialog.dismiss();
                    }
                }).updateBank(bank.getId(), tvCardNumber.getText().toString().replace(" ", "")
                        , tvBank.getText().toString()
                        , tvCardName.getText().toString()
                        , wvChooseShenFen.getSeletedItem()
                        , wvChooseCity.getSeletedItem());

            } else {
                final LoadingDialog loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                new HttpHandler(new HttpResponseListener() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getData().equals(true)) {
                            ToastUtils.showShort("保存成功");

                            Bundle mBundle = new Bundle();
                            mBundle.putBoolean("is_update_success", true);
                            AccountActivity.this.setResult(RESULT_OK, AccountActivity.this.getIntent().putExtras(mBundle));
                            AccountActivity.this.finish();
                        } else {
                            ToastUtils.showShort("保存失败");
                        }
                    }

                    @Override
                    public void onFailed(BaseResponse response) {

                    }

                    @Override
                    public void onFinish() {
                        loadingDialog.dismiss();
                    }
                }).addBank(tvCardNumber.getText().toString().replace(" ", "")
                        , tvCardName.getText().toString(), tvBank.getText().toString()
                        , wvChooseShenFen.getSeletedItem(), wvChooseCity.getSeletedItem());
            }
        }
    }

    private boolean check() {
        if (tvCardName.getText() == null || tvCardName.getText().toString().equals("")) {
            ToastUtils.showShort("请填写持卡人姓名");
            return false;
        }
        if (tvCardNumber.getText() == null || tvCardNumber.getText().toString().equals("")) {
            ToastUtils.showShort("银行卡号不能为空");
            return false;
        }
        if (tvBank.getText() == null || tvBank.getText().toString().equals("")) {
            ToastUtils.showShort("请选择银行");
            return false;
        }
        if (tvBankAddress.getText() == null || tvBankAddress.getText().toString().equals("")) {
            ToastUtils.showShort("请选择开户所在地区");
            return false;
        }
        return true;
    }

    private void updateWheelView() {
        if (provinceList != null) {
            List<String> provinces = new ArrayList<String>();

            for (Province province : provinceList) {
                provinces.add(province.getAreaName());
            }
            wvChooseShenFen.setOffset(1);
            wvChooseShenFen.setItems(provinces);

            List<City> cities = dbHelperGetProvinces.getCity(provinceList.get(0).getAreaId());
            List<String> cityNames = new ArrayList<String>();
            for (City city : cities) {
                cityNames.add(city.getAreaName());
            }
            wvChooseCity.setOffset(1);
            wvChooseCity.setItems(cityNames);
            wvChooseCity.setSeletion(0);
        }
    }

    class MyHandler extends Handler {
        public MyHandler() {

        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 此处可以更新UI
            Bundle bundle = msg.getData();
            boolean isFInished = bundle.getBoolean("is_finished");
            if (isFInished) {
                if (isCardBind == true) {  //如果绑定了银行卡，获取具体信息
                    new HttpHandler(AccountActivity.this).getCeoUserIdBank();
                } else {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
                updateWheelView();
            }
        }
    }

    class GetAssetsThread implements Runnable {
        public void run() {
            Message msg = new Message();
            Bundle bundle = new Bundle();// 存放数据
            bundle.putBoolean("is_finished", true);
            msg.setData(bundle);
            getFromDataBase();
            AccountActivity.this.mGetAssetsHandler.sendMessage(msg); // 向Handler发送消息，更新UI

        }
    }

    //读取资源文件，初始化省份和城市选择器
    public void getFromDataBase() {
        try {
            this.provinceList = dbHelperGetProvinces.getProvince();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //读取资源文件，初始化省份和城市选择器
    public void getFromAssets() {
        String fileName = "province_city.txt";
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName), "UTF-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String jsonData = "";
            while ((line = bufReader.readLine()) != null)
                jsonData += line;
            Gson gson = new Gson();
            Type type = new TypeToken<List<Province>>() {
            }.getType();

            AccountActivity.this.provinceList = gson.fromJson(jsonData, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBankAddressRightImg(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvBankAddress.setCompoundDrawables(null, null, drawable, null);
    }
}
