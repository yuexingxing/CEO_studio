package com.tajiang.ceo.model;

/**
 * 档口
 * Created by Administrator on 2017-07-25.
 */

public class Shop {

    private String shopId;
    private String shopName;
    private String shopImage;
    private String schoolId;
    private String phone;
    private int bussState;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBussState() {
        return bussState;
    }

    public void setBussState(int bussState) {
        this.bussState = bussState;
    }


}
