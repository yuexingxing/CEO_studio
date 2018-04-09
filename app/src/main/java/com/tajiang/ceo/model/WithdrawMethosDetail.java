package com.tajiang.ceo.model;

/**
 * Created by Administrator on 2016/8/26.
 */
public class WithdrawMethosDetail {

    /**
     * 结算周期
     */
    private String settlementMode;

    /**
     * 最低提现金额
     */
    private double minmoney;

    /**
     * 可提现日期信息
     */
    private String withdrawMode;

    /**
     * 当前是否可提现
     */
    private boolean state;

    /**
     * 现在时间
     */
    private long nowtime;

    public double getMinmoney() {
        return minmoney;
    }

    public void setMinmoney(double minmoney) {
        this.minmoney = minmoney;
    }

    public String getWithdrawMode() {
        return withdrawMode;
    }

    public void setWithdrawMode(String withdrawMode) {
        this.withdrawMode = withdrawMode;
    }

    public String getSettlementMode() {
        return settlementMode;
    }

    public void setSettlementMode(String settlementMode) {
        this.settlementMode = settlementMode;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public long getNowtime() {
        return nowtime;
    }

    public void setNowtime(long nowtime) {
        this.nowtime = nowtime;
    }


}
