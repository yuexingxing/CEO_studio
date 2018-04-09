package com.tajiang.ceo.mess.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpHandler;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.SoftKeyboardUtil;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.LoadingDialog;
import com.tajiang.ceo.mess.view.LayoutEditBuilding;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.setting.dialog.UpdateApartmentStateDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by work on 2016/7/7.
 */
public class SetApartmentActivity extends BaseActivity implements HttpResponseListener {


    @BindView(R.id.et_zone)
    EditText etZone;
    @BindView(R.id.btn_add_building)
    Button btnAddBuilding;
    @BindView(R.id.ll_root_building)
    LinearLayout llRootBuilding;
//    @BindView(R.id.rl_add_new_building)
//    RelativeLayout rlAddNewBuilding;
//    @BindView(R.id.et_new_building)
//    EditText etNewBuilding;

    private boolean isDeleteMod = false;
    private boolean isDelSuccess = false;
    private boolean notifyOrderFragment = false;

    private LoadingDialog loadingDialog;
    private List<String> IdList = new ArrayList<>();   //存放将删除的楼栋ID

    private ArrayList<Building> mBuildingListBackstage;  //楼栋列表,存储来自后台的数据

    private ArrayList<LayoutEditBuilding> mViewList;  //用于存储显示楼栋mBuildingList的Item布局
    private ArrayList<LayoutEditBuilding> mViewListCreate;  //楼栋列表,来自前台手动添加
    private ArrayList<LayoutEditBuilding> mTempViewBuilding;  //临时存储即将要被删除的楼栋布局View(LayoutEditBuilding)

    private ApartmentZone apartmentZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initMyDorm();
    }

    @Override
    public void onBackPressed() {
        if (isDeleteMod == true) {
            updateEditMode(false);
        } else {
            finishSelfForResult(notifyOrderFragment);
        }

    }

    @Override
    protected void initTopBar() {
        setTitle("编辑寝室");

        enableLeftClick(new OnLeftClick() {
            @Override
            public void leftClick() {
                finishSelfForResult(notifyOrderFragment);
            }
        });


        OnRightClick listener = new OnRightClick() {
            @Override
            public void rightClick() {
                if (isDeleteMod == false) { //进入编辑模式
                    updateEditMode(true);
                } else {                    //退出编辑模式
                    httpDeleteBuilding();
                }
            }
        };

        enableRightText("", listener);
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_edit);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setRightTextAndDrawable("", null, null, drawable, null);

    }


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_edit_building);
        etZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etZone.setCursorVisible(true);
            }
        });
    }

    @Override
    protected void initData() {
        loadingDialog = new LoadingDialog(this);
        mViewList = new ArrayList<LayoutEditBuilding>();
        mViewListCreate = new ArrayList<LayoutEditBuilding>();
        mTempViewBuilding = new ArrayList<LayoutEditBuilding>();

        apartmentZone = new ApartmentZone();

        initMyListener();
    }

    private void initMyListener() {
        //监听键盘的显示和隐藏
        SoftKeyboardUtil.observeSoftKeyboard(this, new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, int screenHeight, boolean visible) {
                if (isDeleteMod == false) {
                    if (visible == true) {
                        btnAddBuilding.setVisibility(View.GONE);
                    } else {
                        btnAddBuilding.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

//        //支持搜索框上弹出软键盘回车搜索
//        etNewBuilding.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    //TODO.....搜索....订单........
////                    etNewBuilding.setCursorVisible(false);
//                    if (!TextUtils.isEmpty(etNewBuilding.getText().toString())) {
//                        addLayoutEditBuilding();
//                    }
//                }
//                return false;
//            }
//        });
//
//        etNewBuilding.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                etNewBuilding.setCursorVisible(true);
//            }
//        });
    }

    private void initMyDorm() {
        Intent intent = getIntent();
        mBuildingListBackstage = intent.getParcelableArrayListExtra("building_list");
        apartmentZone.setName(intent.getStringExtra("dorm_name"));
        apartmentZone.setId(intent.getStringExtra("id"));
        apartmentZone.setSchoolId(intent.getStringExtra("schoolId"));
        if (apartmentZone.getName() != null) {
            etZone.setText(apartmentZone.getName());
        }
        if (mBuildingListBackstage != null) {
            for (int i = 0; i < mBuildingListBackstage.size(); i++) {
                addLayoutShowBuilding(mBuildingListBackstage.get(i));
            }
        }
    }

    /**
     * 保存按钮的状态变更
     * @param enable
     */
    private void SetSaveButtonState(boolean enable) {
        if (btnAddBuilding != null) {
            btnAddBuilding.setEnabled(enable);
            btnAddBuilding.setBackgroundDrawable(getResources().getDrawable(
                    enable == false ? R.drawable.slt_btn_rect_gray : R.drawable.slt_btn_rect_green
            ));
        }
    }

    /**
     * 更新编辑状态
     *
     * @param isEditMode
     */
    private void updateEditMode(boolean isEditMode) {
        SetSaveButtonState(!isEditMode);
        isDeleteMod = isEditMode;
        if (isEditMode == true) {
//            rlAddNewBuilding.setVisibility(View.GONE);
            /// 这一步必须要做,否则不会显示.
            setRightTextAndDrawable("完成", null, null, null, null);
        } else {
//            rlAddNewBuilding.setVisibility(View.VISIBLE);
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_edit);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            setRightTextAndDrawable("", null, null, drawable, null);
        }

        for (LayoutEditBuilding view : mViewList) {
            view.getLlDeleteBuilding().setVisibility(isEditMode == true ? View.VISIBLE : View.GONE);
            view.getSwitchView().setVisibility(isEditMode == true ? View.GONE : View.VISIBLE);
            view.getEditText().setEnabled(!isEditMode);
        }
        for (LayoutEditBuilding view : mViewListCreate) {
            view.getLlDeleteBuilding().setVisibility(isEditMode == true ? View.VISIBLE : View.GONE);
            view.getSwitchView().setVisibility(isEditMode == true ? View.GONE : View.VISIBLE);
            view.getEditText().setEnabled(!isEditMode);
        }

    }

    private void httpDeleteBuilding() {
        String stringIds = "";
        for (LayoutEditBuilding building : mTempViewBuilding) {
            IdList.add(((Building) building.getTag()).getId());
        }

        if (IdList != null && IdList.size() > 0) {
            stringIds += IdList.get(0);
            for (int i = 1; i < IdList.size(); i++) {
                stringIds += "," + IdList.get(i);
            }
        } else {
            updateEditMode(false);
            return;
        }

        LogUtils.e("删除的楼栋ID -- >" + stringIds);
        loadingDialog.show();
        new HttpHandler(new HttpResponseListener() {
            @Override
            public void onSuccess(BaseResponse response) {
                if ((Boolean) response.getData() == true) {
                    ToastUtils.showShort("删除成功");
                    isDelSuccess = true;
                    notifyOrderFragment = true;
                } else {
                    ToastUtils.showShort("删除失败");
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
                updateEditMode(false);
                if (mTempViewBuilding != null && mTempViewBuilding.size() > 0) {
                    for (LayoutEditBuilding building : mTempViewBuilding) {
                        if (isDelSuccess == false) {
                            building.setVisibility(View.VISIBLE);
                        } else {
                            mViewList.remove(building);
                            llRootBuilding.removeView(building);
                        }
                    }
                }
                /**
                 * 还原数据之后，清空临时变量数组
                 */
                IdList.clear();
                mTempViewBuilding.clear();
                isDelSuccess = false;  //还原默认的状态
                /**
                 * 还原数据之后，清空临时变量数组
                 */
                //如果原始后台的楼栋全部删除，就返回上一页
                if (mViewList.size() < 1) {
                    finishSelfForResult(notifyOrderFragment);
                }
            }
        }).deleteApartment(stringIds);

    }

    public String parseJsonFromObject() {
        apartmentZone.setName(etZone.getText().toString());
        ArrayList<Building> list = new ArrayList<Building>();
        //被编辑过状态的楼栋列表
        for (int i = 0; i < mViewList.size(); i++) {
//            Building building = mBuildingListBackstage.get(i);
            Building building = (Building) mViewList.get(i).getTag();

            building.setName(mViewList.get(i).getEditText().getText().toString());  //保存修改过的楼栋名字
            if (mViewList.get(i).getBuildingStatues() == true) {  //保存楼栋打开状态
                building.setState(Building.BUILDING_STATE_OPENED);
            } else {
                building.setState(Building.BUILDING_STATE_CLOSED);
            }
            list.add(building);
        }
        //手动添加的楼栋列表（默认开启状态）
        for (int i = 0; i < mViewListCreate.size(); i++) {
            Building building = new Building();
            building.setName(mViewListCreate.get(i).getEditText().getText().toString());
            if (mViewListCreate.get(i).getBuildingStatues() == true) {  //楼栋打开状态
                building.setState(Building.BUILDING_STATE_OPENED);
            } else {
                building.setState(Building.BUILDING_STATE_CLOSED);
            }
            list.add(building);
        }

        apartmentZone.setList(list);

        return new Gson().toJson(apartmentZone);
    }

    private boolean checkBuilding() {
        for (int i = 0; i < mViewListCreate.size(); i++) {
            if (mViewListCreate.get(i).getEditText().getText().toString().equals("")
                    || mViewListCreate.get(i).getEditText().getText() == null) {
                return false;
            }
        }

        for (int i = 0; i < mViewList.size(); i++) {
            if (mViewList.get(i).getEditText().getText().toString().equals("")
                    || mViewList.get(i).getEditText().getText() == null) {
                return false;
            }
        }

        return true;
    }

    private boolean checkZone() {
        if (etZone.getText().toString().equals("")
                || etZone.getText() == null) {
            return false;
        }
        return true;
    }




    /**
     * 保存所有数据
     */
    @OnClick(R.id.btn_add_building)
    public void onClick() {
        if (checkBuilding()) {   //检查每个楼栋列表是否为空
            if (checkZone()) {   //检查宿舍区是否为空
//            if (true) {
                //TODO..网络请求....
                String json = parseJsonFromObject();
                LogUtils.e(json);
                new HttpHandler(SetApartmentActivity.this).updateApartmentZones(json);
            } else {
                ToastUtils.showShort("宿舍区不能为空！");
            }
        } else {
            ToastUtils.showShort("楼栋名不能为空！");
        }
    }

    private void addLayoutEditBuilding() {

        SoftKeyboardUtil.hideSoftKeyboard(this);

        final LayoutEditBuilding mView = new LayoutEditBuilding(SetApartmentActivity.this, LayoutEditBuilding.MODE_EDIT_APARTMENT);

        mView.setOnDeleteBuildingListener(new LayoutEditBuilding.OnDeleteBuildingListener() {
            @Override
            public void onDelBulding() {
                mViewListCreate.remove(mView);
                llRootBuilding.removeView(mView);

            }
        });

        mView.setButtonClickListener(new LayoutEditBuilding.onButtonClickListener() {
            @Override
            public void onButtonClick() { //点击删除此行楼栋
//                mViewListCreate.remove(mView);
//                llRootBuilding.removeView(mView);

                UpdateApartmentStateDialog dialog =
                        new UpdateApartmentStateDialog(SetApartmentActivity.this, mView);
                dialog.show();
            }
        });

        mView.setBuildingStatues(Building.BUILDING_STATE_OPENED);
//        mView.getEditText().setText(etNewBuilding.getText().toString());
        mView.setEditTextFocusable(true);
        mView.getSwitchView().setVisibility(View.GONE);
//        etNewBuilding.setText("");
        mViewListCreate.add(mView);  //保存到本地添加列表中
        llRootBuilding.addView(mView);



    }

    private void addLayoutShowBuilding(Building building) {
        final LayoutEditBuilding mView = new LayoutEditBuilding(SetApartmentActivity.this, LayoutEditBuilding.MODE_EDIT_APARTMENT);
        mView.setOnDeleteBuildingListener(new LayoutEditBuilding.OnDeleteBuildingListener() {
            @Override
            public void onDelBulding() {
//                Building building1 = (Building) mView.getTag();
//                IdList.add(building1.getId());
                mTempViewBuilding.add(mView); //保存将被删除的楼栋号的View
                mView.setVisibility(View.GONE);
//                mViewList.remove(mView);
//                llRootBuilding.removeView(mView);
            }
        });
        mView.setButtonClickListener(new LayoutEditBuilding.onButtonClickListener() {
            @Override
            public void onButtonClick() { //点击关闭状态
                UpdateApartmentStateDialog dialog =
                        new UpdateApartmentStateDialog(SetApartmentActivity.this, mView);
                dialog.show();
            }
        });
        mView.setBuildingStatues(building.getState());
        mView.getEditText().setText(building.getName());
        mView.setTag(building);
        mViewList.add(mView);
        llRootBuilding.addView(mView);

//        /**
//         * 指定mView显示在 DefaultView 之下
//         */
//        RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//        if (mViewList.size() > 0) {
//            relLayoutParams.addRule(RelativeLayout.ABOVE,mViewList.get(0).getId());
//        }
//
//        mViewList.add(mView);
//        llRootBuilding.addView(mView, relLayoutParams);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if ((Boolean) response.getData() == true) {
            ToastUtils.showShort("保存成功!");
            finishSelfForResult(true);
        }
    }

    private void finishSelfForResult(boolean bool) {
        if (bool == true) {
            //实例化 Bundle
            Bundle mBundle = new Bundle();
            mBundle.putBoolean("is_editing_finished", true);
            SetApartmentActivity.this.setResult(RESULT_OK, SetApartmentActivity.this.getIntent().putExtras(mBundle));
        }
        SetApartmentActivity.this.finish();
    }

    @Override
    public void onFailed(BaseResponse response) {
//        ToastUtils.showShort(response.getMoreInfo());
    }

    @Override
    public void onFinish() {

    }

    @OnClick(R.id.rl_add_building)
    public void onAddBuildingClick() {
//        if (etNewBuilding.getText().toString() == null
//                || etNewBuilding.getText().toString().equals("")) {
//            ToastUtils.showShort("清先填写楼栋名");
//        } else {
//            addLayoutEditBuilding();
//        }

        if (isDeleteMod ==  false) {
            addLayoutEditBuilding();
        }

    }

}
