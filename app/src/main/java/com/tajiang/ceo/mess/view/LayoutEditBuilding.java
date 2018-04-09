package com.tajiang.ceo.mess.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.widget.SwitchView;
import com.tajiang.ceo.model.Building;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by work on 2016/7/7.
 */
public class LayoutEditBuilding extends LinearLayout implements View.OnClickListener {

    public static final int MODE_ADD_APARTMENT = 1;
    public static final int MODE_EDIT_APARTMENT = 2;

    private int currentMode = MODE_ADD_APARTMENT;

    private Context mContext;



    private SwitchView switchView;
    private Button buttonEditBuilding;
    private EditText editTextEditBuilding;
    private LayoutInflater mInflater;
    private LinearLayout llDeleteBuilding;

    public void setOnDeleteBuildingListener(OnDeleteBuildingListener onDeleteBuildingListener) {
        this.onDeleteBuildingListener = onDeleteBuildingListener;
    }

    private OnDeleteBuildingListener onDeleteBuildingListener;
    private onButtonClickListener mButtonListener;
    private LayoutParams mLayoutParams;

    private boolean opened = true;

    public SwitchView getSwitchView() {
        return switchView;
    }
    public LinearLayout getLlDeleteBuilding() {
        return llDeleteBuilding;
    }

    public interface OnDeleteBuildingListener {
        void onDelBulding();
    }

    public interface onButtonClickListener {
        void onButtonClick();
    }

    public LayoutEditBuilding(Context context, int mode) {
        this(context, null, mode);
    }

    public LayoutEditBuilding(Context context, AttributeSet attrs, int mode) {
        this(context, attrs, 0, mode);
    }

    public LayoutEditBuilding(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr);
        currentMode = mode;
        initData(context);
        initView();
    }

    private void initView() {
        this.setLayoutParams(mLayoutParams);
        this.setOrientation(HORIZONTAL);

        View view = mInflater.inflate(R.layout.layout_edit_building, this, false);

        switchView = (SwitchView) view.findViewById(R.id.switch_view);
        buttonEditBuilding = (Button) view.findViewById(R.id.btn_edit_building);
        editTextEditBuilding = (EditText) view.findViewById(R.id.et_edit_building);
        llDeleteBuilding = (LinearLayout) view.findViewById(R.id.ll_delete_building);

        llDeleteBuilding.setOnClickListener(this);

        switch (currentMode) {
            case MODE_ADD_APARTMENT:
                switchView.setVisibility(GONE);
                buttonEditBuilding.setOnClickListener(this);
                break;
            case MODE_EDIT_APARTMENT:
                buttonEditBuilding.setVisibility(GONE);
                switchView.setOnClickListener(this);
                break;
            default:
                break;
        }


        this.addView(view);

    }

    private void initData(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.WRAP_CONTENT);
    }

    public void setButtonClickListener(onButtonClickListener mButtonListener) {
        this.mButtonListener = mButtonListener;
    }

    public void setBuildingText(String buildingNumber) {
        editTextEditBuilding.setText(buildingNumber);
    }

    public void setBuildingStatues(int state) {
        if (state == Building.BUILDING_STATE_OPENED) {
            opened = true;
            switchView.setOpened(true);
//            buttonEditBuilding.setText("关闭");
//            buttonEditBuilding.setBackgroundResource(R.drawable.slt_btn_rect_green);
        } else {
            opened = false;
            switchView.setOpened(false);
//            buttonEditBuilding.setText("已关闭");
//            buttonEditBuilding.setBackgroundResource(R.drawable.shape_rect_round_gray3);
        }

    }

    public boolean getCurrentButtonState() {
        return opened;
    }

    public void changeButtonStatues() {
        if (opened == true) {
            opened = false;
            switchView.setOpened(false);
//            buttonEditBuilding.setText("已关闭");
//            buttonEditBuilding.setBackgroundResource(R.drawable.shape_rect_round_gray3);

        } else {
            opened = true;
            switchView.setOpened(true);
//            buttonEditBuilding.setText("关闭");
//            buttonEditBuilding.setBackgroundResource(R.drawable.slt_btn_rect_green);
        }
    }

    public void setEditTextFocusable(boolean focusable) {
        if (focusable == true) {
            editTextEditBuilding.requestFocus();
        } else {
            editTextEditBuilding.setFocusable(false);
        }
    }

    public EditText getEditText() {
        return editTextEditBuilding;
    }

    public Button getButton() {
        return buttonEditBuilding;
    }

    public boolean getBuildingStatues() {
        return opened;
    }

    public void setBuildingStatues() {
        opened = true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_delete_building:
                onDeleteBuildingListener.onDelBulding();
                break;
            case R.id.btn_edit_building:
                mButtonListener.onButtonClick();
                break;
            case R.id.switch_view:
                mButtonListener.onButtonClick();
                break;
            default:
                break;
        }
    }
}
