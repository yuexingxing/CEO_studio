package com.tajiang.ceo.model;

/**
 * Created by Administrator on 2017-08-08.
 */

public class AccountWithDrawInfo {

    private int accountId;//		number	@mock=$order(19,21)
    private String accountNo;//		string	@mock=$order('632663267@qq.com','o6U0tuJA8ewtDsfhmR2rh4-7yDco')
    private String accountOwner;//		string	@mock=$order('张无忌','张向阳')
    private int acctId	;//	number	@mock=$order(154,154)
    private int bankId	;//	number	@mock=$order(2,1)
    private int bankType;//		number	@mock=$order(2,1)
    private int isDefault;//		number	@mock=$order(1,0)

    private int balance;//	提现金额	number	@mock=$order(1000,1000)
    private String bankName;//	提现银行	string	@mock=$order('''支付宝','支付宝''')
    private String createTime;//	创建时间	string	@mock=$order('2017-08-01 14:10:17','2017-07-26 16:57:16')
    private int exchangeId;//	提现ID	number	@mock=$order(38,34)
    private String payTime;//	到账时间	string	@mock=$order('','')
    private int state;//	提现状态	number	@mock=$order(0,1)

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public int getAcctId() {
        return acctId;
    }

    public void setAcctId(int acctId) {
        this.acctId = acctId;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getBankType() {
        return bankType;
    }

    public void setBankType(int bankType) {
        this.bankType = bankType;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }


    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}
