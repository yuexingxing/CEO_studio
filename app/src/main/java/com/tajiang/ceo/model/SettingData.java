package com.tajiang.ceo.model;

/**
 * Created by Administrator on 2017-08-01.
 */

public class SettingData {

    private int imgId;
    private String title;
    private Class mClass;

    public Class getActivity() {
        return mClass;
    }

    public void setActivity(Class mClass) {
        this.mClass = mClass;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
