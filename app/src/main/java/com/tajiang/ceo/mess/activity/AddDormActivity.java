package com.tajiang.ceo.mess.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.tajiang.ceo.common.utils.SoftKeyboardUtil;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.mess.view.LayoutEditBuilding;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加寝室
 * Created by Administrator on 2016/5/10.
 */
public class AddDormActivity extends BaseActivity implements HttpResponseListener {


    @BindView(R.id.et_zone)
    EditText etZone;
    @BindView(R.id.btn_add_building)
    Button btnAddBuilding;
    @BindView(R.id.ll_root_building)
    LinearLayout llRootBuilding;

    private LayoutInflater mInflater;

    private ArrayList<Building> listBuildingNum;

    private List<LayoutEditBuilding> mViewList; //存储每个列表布局对象

    private boolean EdittingFinished = true;  //判断当前操作是否已经完成，完成以后才能继续添加新的楼栋


    @Override
    protected void initTopBar() {
        setTitle("添加寝室");
//        enableRightText("保存", new OnRightClick() {
//            @Override
//            public void rightClick() {
//                if (checkBuilding()) {
//                    if (checkZone()) {
//                        //TODO..网络请求....
//                        String strJson = parseDataToJson();
//                        new HttpHandler(AddDormActivity.this).addApartmentZones(strJson);
//                    } else {
//                        ToastUtils.showShort("宿舍区不能为空！");
//                    }
//                } else {
//                    ToastUtils.showShort("楼栋名不能为空！");
//                }
//            }
//        });
    }


    private boolean checkBuilding() {
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


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_edit_building);
    }


    @Override
    protected void initData() {
        listBuildingNum = new ArrayList<Building>();
        mViewList = new ArrayList<LayoutEditBuilding>();
        initMyListener();
        //默认添加一个待填的楼栋
        addBuildingNumberView();
    }

    private void initMyListener() {
        //监听键盘的显示和隐藏
        SoftKeyboardUtil.observeSoftKeyboard(this, new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, int screenHeight, boolean visible) {
                if (visible == true) {
                    btnAddBuilding.setVisibility(View.GONE);
                } else {
                    btnAddBuilding.setVisibility(View.VISIBLE);
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
//                        addBuildingNumberView();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        mInflater = getLayoutInflater();
//        addBuildingNumberView();
    }

    /**
     * 点击添加 楼栋
     */
    @OnClick(R.id.btn_add_building)
    public void onClick() {
//        if (EdittingFinished == true) {
//            addBuildingNumberView();
//        } else {
//            ToastUtils.showShort("请完成当前操作以继续添加楼栋。");
//        }

        if (checkBuilding()) {
//            if (true) {
            if (checkZone()) {
                //TODO..网络请求....
                String strJson = parseDataToJson();
                new HttpHandler(AddDormActivity.this).addApartmentZones(strJson);
            } else {
                ToastUtils.showShort("宿舍区不能为空！");
            }
        } else {
            ToastUtils.showShort("楼栋名不能为空！");
        }
    }

    public String parseDataToJson() {
        ApartmentZone apartmentZone = new ApartmentZone();
        listBuildingNum.clear();
        for (int i = 0; i < mViewList.size(); i++) {
            Building buildingNum = new Building();
            buildingNum.setName(mViewList.get(i).getEditText().getText().toString());
            listBuildingNum.add(buildingNum);
        }
        apartmentZone.setName(etZone.getText().toString());
        apartmentZone.setList(listBuildingNum);

        return new Gson().toJson(apartmentZone);
    }

    private void addBuildingNumberView() {

//        SoftKeyboardUtil.hideSoftKeyboard(this);

        final LayoutEditBuilding mView = new LayoutEditBuilding(AddDormActivity.this, LayoutEditBuilding.MODE_ADD_APARTMENT);

        mView.setButtonClickListener(new LayoutEditBuilding.onButtonClickListener() {
            @Override
            public void onButtonClick() { //点击将删除楼栋数据和对应布局
                mViewList.remove(mView);
                llRootBuilding.removeView(mView);
            }
        });
//        mView.getEditText().setText(etNewBuilding.getText().toString());
        mView.setEditTextFocusable(true);
        mView.getButton().setText("删除");
        mView.getButton().setBackgroundResource(R.drawable.shape_rect_round_gray2);
//        etNewBuilding.setText("");
        mViewList.add(mView);
        llRootBuilding.addView(mView);
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

    @Override
    public void onSuccess(BaseResponse response) {

        if ((Boolean) response.getData() == true) {
            ToastUtils.showShort("添加成功!");
            //实例化 Bundle, 设置需要传递的营业时间参数
            Bundle mBundle = new Bundle();
            mBundle.putBoolean("is_editing_finished", true);
            AddDormActivity.this.setResult(RESULT_OK, AddDormActivity.this.getIntent().putExtras(mBundle));
            AddDormActivity.this.finish();
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

    }

    @OnClick(R.id.rl_add_building)
    public void onAddClick() {

        addBuildingNumberView();
//        if (etNewBuilding.getText().toString() == null
//                || etNewBuilding.getText().toString().equals("")) {
//            ToastUtils.showShort("清先填写楼栋名");
//        } else {
//            addBuildingNumberView();
//        }

    }

}
