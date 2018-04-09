package com.tajiang.ceo.model;

import java.math.BigDecimal;

public class AmountRecord {

    private String schoolUserId; //用户id

    private BigDecimal oldAmount; //当前账户金额

    private Integer changeType; //变更类型  1：为新增  2：为提现

    private BigDecimal changeAmount; //变更金额

    private String changeDescription; //变更说明

    private Long changeDate; //变更日期

    public String getSchoolUserId() {
        return schoolUserId;
    }

    public void setSchoolUserId(String schoolUserId) {
        this.schoolUserId = schoolUserId;
    }

    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }
    public Long getChangeDate() {
        return changeDate;
    }
    public void setChangeDate(int i) {
        this.changeDate = changeDate;
    }


}