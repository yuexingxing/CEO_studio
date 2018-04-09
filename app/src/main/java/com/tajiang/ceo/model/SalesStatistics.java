package com.tajiang.ceo.model;

import java.math.BigDecimal;

public  class SalesStatistics{

    private Integer orderCount; //总订单

    private Integer salenumCount;//总份数

    private BigDecimal saleamount;//成本总销售额

    private BigDecimal actualprice;//实际销售总额

    private BigDecimal profit;//收益

    private BigDecimal reward; //奖励

    private BigDecimal todayIncome; //今日收入

    private BigDecimal sumMoney; //累计收入

    public BigDecimal getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(BigDecimal todayIncome) {
        this.todayIncome = todayIncome;
    }

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }


    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getSalenumCount() {
        return salenumCount;
    }

    public void setSalenumCount(Integer salenumCount) {
        this.salenumCount = salenumCount;
    }

    public BigDecimal getSaleamount() {
        return saleamount;
    }

    public void setSaleamount(BigDecimal saleamount) {
        this.saleamount = saleamount;
    }

    public BigDecimal getActualprice() {
        return actualprice;
    }

    public void setActualprice(BigDecimal actualprice) {
        this.actualprice = actualprice;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }


}