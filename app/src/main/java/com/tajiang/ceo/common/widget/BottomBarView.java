package com.tajiang.ceo.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12154 on 2015/11/11.
 */
public class BottomBarView extends LinearLayout implements View.OnClickListener{

    private Context mContext;

    private int colorBg = R.color.white;
    private int colorTextPress = R.color.green;
    private int colorTextNormal = R.color.text_black_3;

    private final int heightImg = 26;

    private OnBottomBarClickListener onBottomBarClickListener;

    private String[] labelArr = {};

    private int[] imgPressedResIdArr = {};
    private int[] imgNormalResIdArr = {};

    public BottomBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(getResources().getColor(colorBg));
    }

    private List<ImageView> imageViewList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();

    private void initLayout() {

        for (int i = 0; i < labelArr.length; i++) {

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout contentLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            contentLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(imgPressedResIdArr[i]);

            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setText(labelArr[i]);
            textView.setTextColor(getResources().getColor(colorTextPress));
            textView.setTextSize(10);

            contentLayout.addView(imageView);
            contentLayout.addView(textView);
            linearLayout.addView(contentLayout);

            imageViewList.add(imageView);
            textViewList.add(textView);
            linearLayout.setOnClickListener(this);

            addView(linearLayout);

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            int childCount = getChildCount();

            for(int i = 0; i < childCount; i++ ) {

                LinearLayout view = (LinearLayout) getChildAt(i);
                LayoutParams layoutParams =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
                view.setLayoutParams(layoutParams);

                LinearLayout childLayout = (LinearLayout) view.getChildAt(0);
                childLayout.setGravity(Gravity.CENTER);

                ImageView imageView = (ImageView) childLayout.getChildAt(0);
                imageView.setLayoutParams(new LayoutParams(DensityUtils.dp2px(mContext,heightImg), DensityUtils.dp2px(mContext,heightImg)));

            }

    }

    public void doPressedItem(int position){

        for(int i = 0; i < imageViewList.size(); i++){
            if(i == position){
                imageViewList.get(i).setImageResource(imgPressedResIdArr[i]);
                textViewList.get(i).setTextColor(getResources().getColor(colorTextPress));
            }else{
                imageViewList.get(i).setImageResource(imgNormalResIdArr[i]);
                textViewList.get(i).setTextColor(getResources().getColor(colorTextNormal));
            }
        }
    }

    @Override
    public void onClick(View v) {
        int childCount = getChildCount();
        for(int i = 0 ; i < childCount; i++ ){
            if(v == getChildAt(i)){
                doPressedItem(i);
                if(onBottomBarClickListener != null){
                    onBottomBarClickListener.onBottomBarClick(i);
                }
            }
        }
    }

    public void initData(String[] labelArr, int[] imgNormalResIdArr , int[] imgPressedResIdArr){
        this.labelArr = labelArr;
        this.imgNormalResIdArr = imgNormalResIdArr;
        this.imgPressedResIdArr = imgPressedResIdArr;
        initLayout();
        doPressedItem(0);
    }



    public interface OnBottomBarClickListener {
        void onBottomBarClick(int position);
    }

    public void setOnBottomBarClickListener(OnBottomBarClickListener onBottomBarClickListener) {
        this.onBottomBarClickListener = onBottomBarClickListener;
    }

    public void setSelectedItem(int position) {
        doPressedItem(position);
    }

}
