package com.tajiang.ceo.model;

/**
 * 配送员数据
 * Created by Admins on 2017/3/22.
 */

public class Deliver {

    private String name; //配送员姓名

    private String account; //账号 （电话等等）

    private int orderCount; //单量

    private int copies;  //份数

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

}
