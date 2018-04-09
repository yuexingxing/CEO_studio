package com.tajiang.ceo.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.tajiang.ceo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/4.
 */
public class GuideActivity extends AppCompatActivity {

    private static final int IMAGE_SUM = 3;

    @BindView(R.id.iv_guide_store)
    ImageView ivGuideStore;
    @BindView(R.id.iv_guide_dorm_list)
    ImageView ivGuideDormList;
    @BindView(R.id.iv_guide_search)
    ImageView ivGuideSearch;


    private List<ImageView> images;

    private int enable[] = {2, 1, 1};

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.layout_guide_view_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        images = new ArrayList<ImageView>();

        images.add(ivGuideStore);

        ivGuideDormList.setVisibility(View.GONE);
        images.add(ivGuideDormList);

        ivGuideSearch.setVisibility(View.GONE);
        images.add(ivGuideSearch);
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
            GuideActivity.this.finish();
        }
    }
}
