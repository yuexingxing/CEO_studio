package com.tajiang.ceo.mess.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.mess.adapter.BankAdapter;
import com.tajiang.ceo.mess.model.BankInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/20.
 */
public class ChooseBankActivity extends BaseActivity {


    @BindView(R.id.rv_choose_bank)
    RecyclerView rvChooseBank;

    // 配置底部菜单栏内容
    private String[] BankArr = {"交通银行", "农业银行", "中信银行", "民生银行","工商银行", "广发银行",
                                "建设银行", "光大银行", "浦发银行","招商银行"};

    private int[] imgBankResIdArr = {
            R.mipmap.bank_jiao_tong,
            R.mipmap.bank_nong_ye,
            R.mipmap.bank_zhong_xin,
            R.mipmap.bank_ming_sheng,
            R.mipmap.bank_gong_shang,
            R.mipmap.bank_guang_fa,
            R.mipmap.bank_jian_she,
            R.mipmap.bank_guang_da,
            R.mipmap.bank_pu_fa,
            R.mipmap.bank_zhao_shang
    };



    private BankAdapter bankAdapter;

    @Override
    protected void initTopBar() {

    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_choose_bank);
    }

    @Override
    protected void initData() {
        BankInfo bankInfo;
        List<BankInfo> list = new ArrayList<BankInfo>();
        for (int i = 0; i < BankArr.length; i++) {
            bankInfo = new BankInfo();
            bankInfo.setName(BankArr[i]);
            bankInfo.setDescribe("");
            bankInfo.setImageResId(imgBankResIdArr[i]);
            list.add(bankInfo);
        }
        //初始化被选中的银行
        if (getIntent().hasExtra("bank_name")) {
            initRecyclerView(list, getIntent().getStringExtra("bank_name"));
        } else {
            initRecyclerView(list, null);
        }

    }

    private void initRecyclerView(List<BankInfo> list, String bankName) {
        bankAdapter = new BankAdapter(list, bankName);
        bankAdapter.setOnChooseBankListener(new BankAdapter.OnChooseBankListener() {
            @Override
            public void ChooseBankListener(BankInfo bankInfo) {
                Bundle mBundle = new Bundle();
                mBundle.putString("bank_name", bankInfo.getName());
                ChooseBankActivity.this.setResult(RESULT_OK, ChooseBankActivity.this.getIntent().putExtras(mBundle));
                ChooseBankActivity.this.finish();
            }
        });
        rvChooseBank.setLayoutManager(new LinearLayoutManager(ChooseBankActivity.this));
        rvChooseBank.setAdapter(bankAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
