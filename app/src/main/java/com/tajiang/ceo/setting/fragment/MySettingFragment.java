package com.tajiang.ceo.setting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.adapter.CommonAdapter;
import com.tajiang.ceo.common.adapter.ViewHolder;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.utils.UserUtils;
import com.tajiang.ceo.model.SettingData;
import com.tajiang.ceo.model.User;
import com.tajiang.ceo.setting.building.BuildingMenuActivity;
import com.tajiang.ceo.setting.settle.SettleDistributionActivity;
import com.tajiang.ceo.setting.store.StoreListActivity;
import com.tajiang.ceo.setting.wallet.WalletMenuActivity;
import com.tajiang.ceo.setting.wallet.message.MessageMenuActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 */
public class MySettingFragment extends BaseFragment {

    @BindView(R.id.my_setting_icon)
    ImageView icon;

    @BindView(R.id.my_setting_name)
    TextView tvName;

    @BindView(R.id.my_setting_phone)
    TextView tvPhone;

    @BindView(R.id.my_setting_school)
    TextView tvSchool;

    @BindView(R.id.lv_public_setting)
    GridView gridView;

    CommonAdapter<SettingData> mAdapter;
    List<SettingData> dataList = new ArrayList<SettingData>();

    private int[] imgId = {
            R.drawable.set_up_stalls_management, R.drawable.set_up_my_wallet,
            R.drawable.set_up_lou_dong_management, R.drawable.set_up_customer_service,
            R.drawable.set_up_performance_distribution
    };

    private String[] title = {
            "档口管理","我的钱包",
            "楼栋管理","联系客服",
            "业绩分配"
    };

    private Class[] activities = {
            StoreListActivity.class, WalletMenuActivity.class,
            BuildingMenuActivity.class, null,
            SettleDistributionActivity.class
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initTopBar() {

        setTitle("设置");
        setLeftByImgId(R.drawable.mine_news);
        setOnLeftClick(new OnLeftClick() {

            @Override
            public void leftClick() {

                Intent intent = new Intent(getActivity(), MessageMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.fragment_my_setting);
    }

    @Override
    protected void initData() {

        User user = UserUtils.getUser();
        tvName.setText(user.getRealName() + "");
        tvPhone.setText(user.getPhone() + "");
        tvSchool.setText(user.getSchoolName() + "");

        for(int i=0; i<imgId.length; i++){

            SettingData data = new SettingData();
            data.setImgId(imgId[i]);
            data.setTitle(title[i]);
            data.setActivity(activities[i]);

            dataList.add(data);
        }

        gridView.setAdapter(mAdapter = new CommonAdapter<SettingData>(getActivity(), dataList, R.layout.item_layout_setting) {

            @Override
            public void convert(ViewHolder helper, SettingData item) {

                helper.setImageResource(R.id.item_layout_setting_img, item.getImgId());
                helper.setText(R.id.item_layout_setting_title, item.getTitle());
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(activities[position] == null){
                    return;
                }

                Intent intent = new Intent(getActivity(), activities[position]);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.my_setting_finish)
    public void exitAPP(){

        PostDataTools.user_logout(getActivity(), new PostDataTools.DataCallback() {
            @Override
            public void callback(boolean flag, String message, Object object) {
                ToastUtils.showShort(message);
            }
        });

        MobclickAgent.onKillProcess(getActivity());
        TJApp.getInstance().exit();
    }
}
