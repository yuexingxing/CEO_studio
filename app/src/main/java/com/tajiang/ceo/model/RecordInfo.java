package com.tajiang.ceo.model;

/**
 * Created by Administrator on 2017-08-07.
 */

public class RecordInfo{

    private int billId;//	number	@mock=$order(1222,1223)
    private String createTime;//		string	@mock=$order('2017-10-01 00:00:00','2017-10-01 00:00:00')
    private int optAmount;//		number	@mock=$order(122,122)
    private int payType;//		number	@mock=$order(1,1)

    private int bizType;

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getOptAmount() {
        return optAmount;
    }

    public void setOptAmount(int optAmount) {
        this.optAmount = optAmount;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }
}
