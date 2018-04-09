package com.tajiang.ceo.model;

public class Bank{
    private String id;

    private String createdAt;

    private String updatedAt;
    /**
     * 登录用户ID
     */
    private String ceoUserId;
    /**
     * 登录用户银行卡号
     */
    private String cardNo;
    /**
     * 登录用户银行卡持卡人姓名
     */
    private String userName;
    /**
     * 登录用户银行卡开户银行
     */
    private String openBank;
    /**
     * 登录用户开户银行所在省份
     */
    private String prov;
    /**
     * 登录用户开户银行所在城市
     */
    private String city;

    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCeoUserId() {
        return ceoUserId;
    }

    public void setCeoUserId(String ceoUserId) {
        this.ceoUserId = ceoUserId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}