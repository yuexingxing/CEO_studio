package com.tajiang.ceo.common.http;

/**
 * Created by Administrator on 2016/6/20.
 */
public class HttpHandler implements HttpLoad {

    private HttpLoad httpLoad;

    public HttpHandler(HttpResponseListener listener) {
        httpLoad = new HttpForRetrofit(listener);
    }

    @Override
    public void signinCheck(String userName, String passWord, String clientId, String imei) {
        httpLoad.signinCheck(userName, passWord, clientId, imei);
    }

    @Override
    public void getAmountRecord(int page, int pagesize) {
        httpLoad.getAmountRecord(page, pagesize);
    }

    @Override
    public void getStoreOrder(String schoolId, String storeId, String apartmentId, String condition, Integer page, Integer pagesize, Integer menu) {
        httpLoad.getStoreOrder(schoolId,storeId,apartmentId,condition,page,pagesize, menu);
    }

    @Override
    public void getStallsOrder(String stallsId, String apartmentId, String condition,
                               Integer orderState, Integer page, Integer pagesize) {
        httpLoad.getStallsOrder(stallsId, apartmentId, condition, orderState, page, pagesize);
    }

    @Override
    public void refund(String orderGoodsId) {

    }

    @Override
    public void singleRecord(String storeId, String schoolId, Integer page, Integer pagesize, String buyerPhone, String orderSn) {

    }

    @Override
    public void updateApartment(String id, String name, String zonesId) {

    }

    @Override
    public void getApartment(String schoolId) {
        httpLoad.getApartment(schoolId);
    }

    @Override
    public void getZones(String schoolId) {

    }

    @Override
    public void getRegCode(String mobilePhone) {
        httpLoad.getRegCode(mobilePhone);
    }

    public void orderToday() {
        httpLoad.orderToday();
    }

    public  void addBank(String cardNo,String userName,String openBank,String prov,String city){
        httpLoad.addBank(cardNo, userName, openBank, prov, city);
    }

    @Override
    public void updatePassword(String id, String password, String oldPassword) {
        httpLoad.updatePassword(id, password, oldPassword);
    }

    @Override
    public void verifyRegCode(String mobilePhone, String regcode) {
        httpLoad.verifyRegCode(mobilePhone, regcode);
    }

    @Override
    public void modifyPayPwd(String oldpassword, String password) {
        httpLoad.modifyPayPwd(oldpassword, password);
    }

    @Override
    public void updatePaymentPsd(String password) {
        httpLoad.updatePaymentPsd(password);
    }

    @Override
    public void selctpsd(String schoolUserId) {
        httpLoad.selctpsd(schoolUserId);
    }

    @Override
    public void canteenMnagement(String schoolId) {
        httpLoad.canteenMnagement(schoolId);
    }

    @Override
    public void judgePwd(String oldpassword) {
        httpLoad.judgePwd(oldpassword);
    }

    @Override
    public void getStores(String schooldId) {
        httpLoad.getStores(schooldId);
    }

    @Override
    public void getSchoolStalls(String schooldId) {
        httpLoad.getSchoolStalls(schooldId);
    }

    @Override
    public void setPasswordBank(String password, String schoolUserId) {
        httpLoad.setPasswordBank(password, schoolUserId);
    }

    @Override
    public void updateStoreName(String storeId, String name) {
        httpLoad.updateStoreName(storeId, name);
    }

    @Override
    public void updateStoreNotice(String storeId, String notice) {
        httpLoad.updateStoreNotice(storeId, notice);
    }

    @Override
    public void updateStoreState(String storeId, int state) {
        httpLoad.updateStoreState(storeId, state);
    }

    @Override
    public void getStoreTime(String storeId) {
        httpLoad.getStoreTime(storeId);
    }

    @Override
    public void updateStoreSuchedule(String time, String storeId) {
        httpLoad.updateStoreSuchedule(time, storeId);
    }

    @Override
    public void getBalance() {
        httpLoad.getBalance();
    }

    @Override
    public void getSchoolZonesAparment() {
        httpLoad.getSchoolZonesAparment();
    }

    @Override
    public void addApartmentZones(String apartment) {
        httpLoad.addApartmentZones(apartment);
    }

    @Override
    public void updateApartmentZones(String apartment) {
        httpLoad.updateApartmentZones(apartment);
    }

    @Override
    public void getMoneyRecord(String amountRecordId) {
        httpLoad.getMoneyRecord(amountRecordId);
    }

    @Override
    public void getSchoolZones() {
        httpLoad.getSchoolZones();
    }

    @Override
    public void addApply(float money, String password) {
        httpLoad.addApply(money, password);
    }

    @Override
    public void getCeoUserIdBank() {
        httpLoad.getCeoUserIdBank();
    }

    @Override
    public void updateServiceState(String orderId) {
        httpLoad.updateServiceState(orderId);
    }

    @Override
    public void updateBank(String id, String cardNo, String openBank, String userName,  String prov, String city) {
        httpLoad.updateBank(id, cardNo, openBank, userName, prov, city);
    }

    @Override
    public void bankUtil(String bankNo) {
        httpLoad.bankUtil(bankNo);
    }

    @Override
    public void collectClientMsg(String userId, String clientId, int os, String osVersion, String deviceModel, String appVersion) {
        httpLoad.collectClientMsg(userId, clientId, os, osVersion, deviceModel, appVersion);
    }

    @Override
    public void getAppInfo() {
        httpLoad.getAppInfo();
    }

    @Override
    public void presentInformation() {
        httpLoad.presentInformation();
    }

    @Override
    public void deleteApartment(String idList) {
        httpLoad.deleteApartment(idList);
    }

    @Override
    public void getStoreHistoricalOrder(String storeId, String schoolId, String apartmentId, String condition, Integer page, Integer pagesize) {
        httpLoad.getStoreHistoricalOrder(storeId, schoolId, apartmentId, condition, page, pagesize);
    }

    @Override
    public void getStoreAparment(String storeId) {
        httpLoad.getStoreAparment(storeId);
    }

    @Override
    public void updateStoreAparment(String storeId, String aparmentIdList) {
        httpLoad.updateStoreAparment(storeId, aparmentIdList);
    }

    @Override
    public void getSchoolTime(String schoolId) {
        httpLoad.getSchoolTime(schoolId);
    }

    @Override
    public void updateSchoolSuchedule(String time, String schoolId) {
        httpLoad.updateSchoolSuchedule(time, schoolId);
    }

    /**
     * 订单确认送达
     * @param orderIds 多个订单号的JSON字符串
     * @return
     */
    public void orderConfirmedV2(String orderIds) {
        httpLoad.orderConfirmedV2(orderIds);
    }

    /**
     * ceo确认取餐
     * @param orderIds 多个订单号的JSON字符串
     * @return
     */
    public void takeMealV2(String orderIds) {
        httpLoad.takeMealV2(orderIds);
    }

}
