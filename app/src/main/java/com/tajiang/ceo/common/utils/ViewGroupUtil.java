package com.tajiang.ceo.common.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admins on 2017/3/14.
 */

public class ViewGroupUtil {

    /**
     * 在ViewGroup中根据id进行查找
     * @param vg
     * @param id 如：R.id.tv_name
     * @return
     */
    public static View findViewInViewGroupById(ViewGroup vg, int id) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v.getId() == id) {
                return v;
            } else {
                if (v instanceof ViewGroup) {
                    return findViewInViewGroupById((ViewGroup) v, id);
                }
            }
        }
        return null;
    }

}
