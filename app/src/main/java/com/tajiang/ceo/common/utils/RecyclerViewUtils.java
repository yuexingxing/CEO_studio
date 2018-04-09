package com.tajiang.ceo.common.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Admins on 2016/11/7.
 */
public class RecyclerViewUtils {

    /**
     * 计算滑动的垂直距离
     * @param recyclerView 滑动对象
     * @return
     */
    public static int getgetScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

}
