package com.tajiang.ceo.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.application.TJApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SQL on 2016/8/4.
 */
public class GuideWindow extends Dialog {

    private static final int IMAGE_SUM = 5;
    @BindView(R.id.iv_guide_store)
    ImageView ivGuideStore;
    @BindView(R.id.iv_guide_dorm_list)
    ImageView ivGuideDormList;
    @BindView(R.id.iv_guide_search)
    ImageView ivGuideSearch;
    @BindView(R.id.iv_guide_delivered)
    ImageView ivGuideDelivered;
    @BindView(R.id.iv_guide_confirm_order)
    ImageView ivGuideConfirmOrder;
    @BindView(R.id.rl_root_view)
    RelativeLayout rlRootView;

    private List<ImageView> images;

    private int enable[] = {2, 1, 1, 1, 1};

    private int count = 0;

    public GuideWindow(Context context) {
        super(context, R.style.default_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_guide_view_order, null);
        setContentView(view);
        ButterKnife.bind(this);
        initWindow();
        initView();
    }

    private void initWindow() {
        WindowManager m = (WindowManager) TJApp.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        this.setCancelable(false);

        images = new ArrayList<ImageView>();

        images.add(ivGuideStore); //标题栏引导

        ivGuideDormList.setVisibility(View.GONE); //宿舍楼引导
        images.add(ivGuideDormList);

        ivGuideSearch.setVisibility(View.GONE);  //搜索框引导
        images.add(ivGuideSearch);

        ivGuideConfirmOrder.setVisibility(View.GONE);  //确认订单引导
        images.add(ivGuideConfirmOrder);

        ivGuideDelivered.setVisibility(View.GONE); //确认送达页面引导
        images.add(ivGuideDelivered);
    }

    @OnClick(R.id.rl_root_view)
    public void onRootViewClick() {
        for (int i = 0; i < IMAGE_SUM; i++) {
            if (i + 1 == IMAGE_SUM) {
                count = IMAGE_SUM;
                break;
            }
            if (enable[i] == 2) {
                images.get(i).setVisibility(View.GONE);
                if (i + 1 < IMAGE_SUM) {
                    images.get(i + 1).setVisibility(View.VISIBLE);
                    enable[i + 1] = 2;
                }
                enable[i] = 3;
                count++;
                break;
            }
        }
        if (count == IMAGE_SUM) {
            this.dismiss();
        }
    }
}
