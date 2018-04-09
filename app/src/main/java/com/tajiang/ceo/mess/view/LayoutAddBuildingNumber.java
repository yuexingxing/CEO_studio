package com.tajiang.ceo.mess.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.DensityUtils;

/**
 * Created by SQL on 2016/7/2.
 */
public class LayoutAddBuildingNumber extends LinearLayout implements View.OnClickListener{

    private Context mContext;
    private LayoutInflater mInflater;
    private LinearLayout.LayoutParams mLayoutParams;

    private EditText mEditTextBuilding;
    private ImageView mImageViewDelete;

    private onImageViewClickListener mImgListener;

    public interface onImageViewClickListener{
        void onImgClick();
    }

    public LayoutAddBuildingNumber(Context context) {
        this(context, null);
    }

    public LayoutAddBuildingNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayoutAddBuildingNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData(context);
        initView();
    }

    public void setBuildingEditTextEnable(boolean editable){
        mEditTextBuilding.setEnabled(editable);
    }

    public void setNameEditTextBuilding(String name){
        mEditTextBuilding.setText(name);
    }

    public void setEditBuildingBackground(int drawable) {
        mEditTextBuilding.setBackgroundResource(drawable);
    }

    private void initView() {

        this.setLayoutParams(mLayoutParams);
        this.setGravity(Gravity.CENTER);

        View view = mInflater.inflate(R.layout.layout_building_number, this, false);

        mEditTextBuilding = (EditText) view.findViewById(R.id.et_building_number);
//        mImageViewDelete = (ImageView) view.findViewById(R.id.iv_delete_img);
//        mImageViewDelete.setOnClickListener(this);

        this.addView(view);
    }

    private void initData(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                ,DensityUtils.dp2px(mContext, 45));
    }

    public void setDeleteImageClickListener(onImageViewClickListener mImgListener) {
        this.mImgListener = mImgListener;
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.iv_delete_img) {
//            mImgListener.onImgClick();
//        }
    }

    private int getColor(int resId){
        return mContext.getResources().getColor(resId);
    }
}
