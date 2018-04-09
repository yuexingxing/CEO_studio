package com.tajiang.ceo.model;

import java.math.BigDecimal;

/**
 * Created by work on 2016/7/13.
 */
public class BillDetail {
    private String id;

    private String amountRecordId;

    private String describe;

    private long createdAt;

    private long updatedAt;

    private BigDecimal money;

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmountRecordId() {
        return amountRecordId;
    }

    public void setAmountRecordId(String amountRecordId) {
        this.amountRecordId = amountRecordId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
