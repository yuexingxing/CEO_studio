package com.tajiang.ceo.model;

/**
 * 钱包账户信息
 * Created by Administrator on 2017-08-04.
 */
public class AccountInfo {

    private int acctId;//	账户ID	number	@mock=154
    private int activeState;//		是否激活 1-已激活 0-未激活	number	@mock=1
    private int balance;//		可用余额	number	@mock=0
    private int depositAmount;//		小票机押金	number	@mock=15000
    private int frozenBalance;//		冻结余额	number	@mock=2000
    private int userId	;//	用户ID	number	@mock=198

    private String payPassword;//支付密码

    public int getAcctId() {
        return acctId;
    }

    public void setAcctId(int acctId) {
        this.acctId = acctId;
    }

    public int getActiveState() {
        return activeState;
    }

    public void setActiveState(int activeState) {
        this.activeState = activeState;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(int frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }
}
