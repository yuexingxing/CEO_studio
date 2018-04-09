package com.tajiang.ceo.model;

/**
 * 结算详情
 * Created by Administrator on 2017-08-04.
 */
public class SettleDetailInfo {

    private String delyId;//	配送人id	string	@mock=12
    private String delyName;//	配送人姓名
    private int payedMoney	;//实际结算金额	number	@mock=29
    private String settleId;//	骑士结算id，分配后上传参数	string
    private int settleMoney;//	应该结算金额	number	@mock=32
    private int totalOrderQty;//	总订单数	number	@mock=3
    private int totalPartQty;//	总份数	number	@mock=5

    public String getDelyId() {
        return delyId;
    }

    public void setDelyId(String delyId) {
        this.delyId = delyId;
    }

    public String getDelyName() {
        return delyName;
    }

    public void setDelyName(String delyName) {
        this.delyName = delyName;
    }

    public int getPayedMoney() {
        return payedMoney;
    }

    public void setPayedMoney(int payedMoney) {
        this.payedMoney = payedMoney;
    }

    public String getSettleId() {
        return settleId;
    }

    public void setSettleId(String settleId) {
        this.settleId = settleId;
    }

    public int getSettleMoney() {
        return settleMoney;
    }

    public void setSettleMoney(int settleMoney) {
        this.settleMoney = settleMoney;
    }

    public int getTotalOrderQty() {
        return totalOrderQty;
    }

    public void setTotalOrderQty(int totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
    }

    public int getTotalPartQty() {
        return totalPartQty;
    }

    public void setTotalPartQty(int totalPartQty) {
        this.totalPartQty = totalPartQty;
    }

}
