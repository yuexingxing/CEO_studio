package com.tajiang.ceo.model;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class Order {

    public static final String RIDER = "0"; //骑士
    public static final String CEO = "1"; //CEO
    public static final String GET_ORDER_STATE_NO_CONFIRM = "0"; //未确认订单
    public static final String GET_ORDER_STATE_OK_CONFIRM = "1"; //已确认订单
    public static final String GET_ORDER_STATE_DANGQIAN= "0"; //未确认订单
    public static final String GET_ORDER_STATE_YUDING = "1"; //已确认订单
    public static final String GET_ORDER_STATE_SONGDA = "2"; // 送达订单
    public static String CURRENT_ORDER_STATE = GET_ORDER_STATE_NO_CONFIRM;//当前订单状态
    public static final int PAGE_SIZE = 10; //每页数量
    public static final int ORDER_UNFINISHED = 0;
    public static final int ORDER_FINISHED = 1;

    public static int CURRENT_ORDER_TYPE = 1;

    /*支付侧：业务类型 */
    public static final int BIZ_TYPE_RECHARGE = 1;//充值
    public static final int BIZ_TYPE_ORDER = 2;//订单
    public static final int BIZ_TYPE_WITHDRAW = 3;//提现

    /*账户侧：支付类型 */
    public static final int PAY_TYPE_RECHARGE = 10;//充值
    public static final int PAY_TYPE_REWARD_ORDER = 11;//订单收入
    public static final int PAY_TYPE_REWARD_ROYALTY = 12;//提成收入
    public static final int PAY_TYPE_REWARD_BONUS = 13;//奖励收入
    public static final int PAY_TYPE_PAY = 22;//余额支付
    public static final int PAY_TYPE_WITHDRAW = 21;//提现
    public static final int PAY_TYPE_REFUND = 30;//退款
    public static final int PAY_TYPE_PUNISH = 40;//扣罚支出

    public static int getOrderUnfinished() {
        return ORDER_UNFINISHED;
    }

    public static int getOrderFinished() {
        return ORDER_FINISHED;
    }

    private String orderId;
    private String schoolId;
    private String shopName;
    private String buyerRemark;
    private String delyStartDate;

    private String delyEndDate;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddr;
    private int orderState;

    private int orderStateExtra;
    private int totalPartQty;
    private int totalMoney;
    private int finalMoney;
    private int totalDelyFee;

    private int totalPackFee;
    private int voucherMoney;
    private int fullActivityMoney;
    private String createDate;
    private String delyId;

    private String delyName;
    private String delyPhone;
    private String takerDate;
    private String delyDate;
    private String arriveDate;

    private String confirmDate;
    private List<OrderGoods> orderItemList;

    private int delyFast;//是否尽快送达

    private boolean hidenGoods;//隐藏菜单栏
    private boolean checked;//是否被选中

    public int getDelyFast() {
        return delyFast;
    }

    public void setDelyFast(int delyFast) {
        this.delyFast = delyFast;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isHidenGoods() {
        return hidenGoods;
    }

    public void setHidenGoods(boolean hidenGoods) {
        this.hidenGoods = hidenGoods;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    private boolean choosed;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    public String getDelyStartDate() {
        return delyStartDate;
    }

    public void setDelyStartDate(String delyStartDate) {
        this.delyStartDate = delyStartDate;
    }

    public String getDelyEndDate() {
        return delyEndDate;
    }

    public void setDelyEndDate(String delyEndDate) {
        this.delyEndDate = delyEndDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getOrderStateExtra() {
        return orderStateExtra;
    }

    public void setOrderStateExtra(int orderStateExtra) {
        this.orderStateExtra = orderStateExtra;
    }

    public int getTotalPartQty() {
        return totalPartQty;
    }

    public void setTotalPartQty(int totalPartQty) {
        this.totalPartQty = totalPartQty;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getFinalMoney() {
        return finalMoney;
    }

    public void setFinalMoney(int finalMoney) {
        this.finalMoney = finalMoney;
    }

    public int getTotalDelyFee() {
        return totalDelyFee;
    }

    public void setTotalDelyFee(int totalDelyFee) {
        this.totalDelyFee = totalDelyFee;
    }

    public int getTotalPackFee() {
        return totalPackFee;
    }

    public void setTotalPackFee(int totalPackFee) {
        this.totalPackFee = totalPackFee;
    }

    public int getVoucherMoney() {
        return voucherMoney;
    }

    public void setVoucherMoney(int voucherMoney) {
        this.voucherMoney = voucherMoney;
    }

    public int getFullActivityMoney() {
        return fullActivityMoney;
    }

    public void setFullActivityMoney(int fullActivityMoney) {
        this.fullActivityMoney = fullActivityMoney;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

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

    public String getDelyPhone() {
        return delyPhone;
    }

    public void setDelyPhone(String delyPhone) {
        this.delyPhone = delyPhone;
    }

    public String getTakerDate() {
        return takerDate;
    }

    public void setTakerDate(String takerDate) {
        this.takerDate = takerDate;
    }

    public String getDelyDate() {
        return delyDate;
    }

    public void setDelyDate(String delyDate) {
        this.delyDate = delyDate;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public List<OrderGoods> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderGoods> orderItemList) {
        this.orderItemList = orderItemList;
    }

}
