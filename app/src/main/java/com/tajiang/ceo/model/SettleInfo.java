package com.tajiang.ceo.model;

import java.util.List;

/**
 * 结算
 * Created by Administrator on 2017-08-04.
 */
public class SettleInfo {

    private String assignId;//	结算id，上传时用此属性	string
    private int assignState;//	分配状态 1:未处理 2:提交审核 3:审核通过 4:审核不通过	number	@mock=2
    private List<SettleDetailInfo> delaySettleList;//		array<object>
    private int delayTotalMoney;//	总配送金额	number	@mock=395
    private String settleCycle;//	结算周期到:20170715	string	@mock=20170715
    private String settleCycleStr;//	结算周期字符	string	@mock=2017-07-01 ～ 2017-07-15
    private int validOrderQty;//	有效订单总数	number	@mock=140

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public int getAssignState() {
        return assignState;
    }

    public void setAssignState(int assignState) {
        this.assignState = assignState;
    }

    public List<SettleDetailInfo> getDelaySettleList() {
        return delaySettleList;
    }

    public void setDelaySettleList(List<SettleDetailInfo> delaySettleList) {
        this.delaySettleList = delaySettleList;
    }

    public int getDelayTotalMoney() {
        return delayTotalMoney;
    }

    public void setDelayTotalMoney(int delayTotalMoney) {
        this.delayTotalMoney = delayTotalMoney;
    }

    public String getSettleCycle() {
        return settleCycle;
    }

    public void setSettleCycle(String settleCycle) {
        this.settleCycle = settleCycle;
    }

    public String getSettleCycleStr() {
        return settleCycleStr;
    }

    public void setSettleCycleStr(String settleCycleStr) {
        this.settleCycleStr = settleCycleStr;
    }

    public int getValidOrderQty() {
        return validOrderQty;
    }

    public void setValidOrderQty(int validOrderQty) {
        this.validOrderQty = validOrderQty;
    }

}
