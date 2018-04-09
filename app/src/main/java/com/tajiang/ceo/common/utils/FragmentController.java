package com.tajiang.ceo.common.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

public class FragmentController {

    private int containerId;
    private FragmentManager fm;

    private List<Fragment> mFragmentList;

    public FragmentController(FragmentActivity activity, int containerId, List<Fragment> fragments) {
        this.containerId = containerId;
        fm = activity.getSupportFragmentManager();
        mFragmentList = fragments;
        initFragment();
        showFragment(0);
    }

    public void initFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : mFragmentList) {
            ft.add(containerId, fragment);
        }
//        ft.commit();
        ft.commitAllowingStateLoss();
    }

    public void showFragment(int position) {
        hideFragments();
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(mFragmentList.get(position));
//        ft.commit();
        ft.commitAllowingStateLoss();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : mFragmentList) {
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
//        ft.commit();
        ft.commitAllowingStateLoss();
    }

    public Fragment getFragment(int position) {
        return mFragmentList.get(position);
    }

}