package com.tajiang.ceo.common.http;

import com.tajiang.ceo.common.retrofit.RetrofitService;
import com.tajiang.ceo.common.retrofit.TJRetrofit;
import com.tajiang.ceo.common.retrofit.TJSubscriber;
import com.tajiang.ceo.model.Apartment;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.AppInfoResult;
import com.tajiang.ceo.model.Bank;
import com.tajiang.ceo.model.BillDetail;
import com.tajiang.ceo.model.BillRecord;
import com.tajiang.ceo.model.MessTime;
import com.tajiang.ceo.model.Name;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.Pager;
import com.tajiang.ceo.model.SalesStatistics;
import com.tajiang.ceo.model.Store;
import com.tajiang.ceo.model.User;
import com.tajiang.ceo.model.WithdrawMethosDetail;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/20.
 */
public class HttpForRetrofit implements HttpLoad {
    private RetrofitService service;
    private HttpResponseListener listener;

    public HttpForRetrofit(HttpResponseListener listener) {
        service = TJRetrofit.createRetrofitService();
        this.listener = listener;
    }

    /**
     * 执行网络请求
     * @param observable
     * @param <T>
     */
    private <T> void excuteRequest(Observable<BaseResponse<T>> observable) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new TJSubscriber<T>(listener));
    }

    /**
     *用户登录
     * @param userName
     * @param passWord
     */
//    @Override
//    public void signinCheck(String userName, String passWord) {
//        Observable<BaseResponse<User>> observable = service.signinCheck(userName, passWord);
//        excuteRequest(observable);
//
//    }


    @Override
    public void signinCheck(String userName, String passWord, String clientId, String imei) {
        Observable<BaseResponse<User>> observable = service.signinCheck(userName, passWord, clientId, imei);
        excuteRequest(observable);
    }

    /**
     * 用户账单流水
     */
    @Override
    public void getAmountRecord(int page, int pagesize) {
        Observable<BaseResponse<Pager<BillRecord>>> observable = service.getAmountRecord(page, pagesize);
        excuteRequest(observable);
    }

    /**
     * 首页
     * @param schoolId
     * @param storeId
     * @param apartmentId
     * @param condition
     * @param page
     * @param pagesize
     * @param menu
     */
    @Override
    public void getStoreOrder(String schoolId, String storeId, String apartmentId, String condition, Integer page, Integer pagesize, Integer menu) {
        Observable<BaseResponse<Pager<Order>>> observable = service.getStoreOrder(schoolId, storeId, apartmentId, condition, page, pagesize, menu);
        excuteRequest(observable);
    }

    /**
     * 首页
     * @param stallsId
     * @param apartmentId
     * @param condition
     * @param orderState
     * @param page
     * @param pagesize
     */
    @Override
    public void getStallsOrder(String stallsId, String apartmentId, String condition,
                               Integer orderState, Integer page, Integer pagesize) {
        Observable<BaseResponse<Pager<Order>>> observable = service.getStallsOrder(stallsId, apartmentId, condition, orderState, page, pagesize);
        excuteRequest(observable);
    }

    /**
     * 退单
     * @param orderGoodsId
     */
    @Override
    public void refund(String orderGoodsId) {

    }

    /**
     * 退单记录
     * @param storeId
     * @param schoolId
     * @param page
     * @param pagesize
     * @param buyerPhone
     * @param orderSn
     */
    @Override
    public void singleRecord(String storeId, String schoolId, Integer page, Integer pagesize, String buyerPhone, String orderSn) {

    }

    /**
     * 修改宿舍
     * @param id
     * @param name
     * @param zonesId
     */
    @Override
    public void updateApartment(String id, String name, String zonesId) {

    }

    /**
     * 根据学校ceo用户 和学校id 获取学校宿舍
     * @param schoolId
     */
    @Override
    public void getApartment(String schoolId) {
        Observable<BaseResponse<List<Apartment>>> observable = service.getApartment(schoolId);
        excuteRequest(observable);
    }

    /**
     * 根据学校id 获取学校的分苑
     * @param schoolId
     */
    @Override
    public void getZones(String schoolId) {

    }

    /**
     * 根据用户手机号 获取短信验证码
     * @param mobilePhone
     */
    @Override
    public void getRegCode(String mobilePhone) {
        Observable<BaseResponse<String>> observable = service.getRegCode(mobilePhone);
        excuteRequest(observable);
    }

    /**
     * 根据登录用户的id获取用户的今日有效订单和今日提成 （实时更新）
     */
    @Override
    public void orderToday() {
        Observable<BaseResponse<SalesStatistics>> observable = service.orderToday();
        excuteRequest(observable);
    }

    /**
     * 用户添加银行卡
     * @param cardNo
     * @param userName
     * @param openBank
     * @param prov
     * @param city
     * @return
     */
    @Override
    public void addBank(String cardNo,String userName,String openBank,String prov,String city) {
        Observable<BaseResponse<Boolean>> observable = service.addBank(cardNo, userName, openBank, prov, city);
        excuteRequest(observable);
    }

    /**
     *根据用户id,密码,旧密码 修改密码
     * @param id
     * @param password
     * @param oldPassword
     */
    @Override
    public void updatePassword(String id, String password, String oldPassword) {
        Observable<BaseResponse<User>> observable = service.updatePassword(id, password, oldPassword);
        excuteRequest(observable);
    }

    /**
     * 根据手机号,验证码 验证验证码
     * @param mobilePhone
     * @param regcode
     * @return
     */
    @Override
    public void verifyRegCode(String mobilePhone, String regcode) {
        Observable<BaseResponse<String>> observable = service.verifyRegCode(mobilePhone, regcode);
        excuteRequest(observable);
    }

    /**
     * 根据新密码 修改支付密码
     * @param oldpassword
     * @param password
     * @return
     */
    @Override
    public void modifyPayPwd(String oldpassword, String password) {
        Observable<BaseResponse<Boolean>> observable = service.modifyPayPwd(oldpassword, password);
        excuteRequest(observable);
    }

    /**
     * 找回支付密码
     * @param password
     * @return
     */
    @Override
    public void updatePaymentPsd(String password) {
        Observable<BaseResponse<Boolean>> observable = service.updatePaymentPsd(password);
        excuteRequest(observable);
    }

    /**
     * 用户是否设置密码
     * @param schoolUserId
     * @return
     */
    @Override
    public void selctpsd(String schoolUserId) {
        Observable<BaseResponse<Boolean>> observable = service.selctpsd(schoolUserId);
        excuteRequest(observable);
    }

    /**
     * 食堂管理
     * @param schoolId
     * @return
     */
    public void canteenMnagement(String schoolId) {
        Observable<BaseResponse<Store>> observable = service.canteenMnagement(schoolId);
        excuteRequest(observable);
    }

    /**
     * 判断旧密码是否正确
     * @param oldpassword
     * @return
     */
    @Override
    public void judgePwd(String oldpassword) {
        Observable<BaseResponse<String>> observable = service.judgePwd(oldpassword);
        excuteRequest(observable);
    }



    /**
     * 获取学校食堂列表
     * @param schooldId
     */
    @Override
    public void getStores(String schooldId) {
        Observable<BaseResponse<List<Store>>> observable = service.getStores(schooldId);
        excuteRequest(observable);
    }


    /**
     * 获取食堂和档口列表 (新增， 结构体:一级为食堂，二级为食堂底下的档口)
     * @param schooldId
     */
    @Override
    public void getSchoolStalls(String schooldId) {
        Observable<BaseResponse<List<Store>>> observable = service.getSchoolStalls(schooldId);
        excuteRequest(observable);
    }

    /**
     * 设置提现密码
     * @param password
     * @param schoolUserId
     * @return
     */
    public void setPasswordBank(String password, String schoolUserId) {
        Observable<BaseResponse<Boolean>> observable = service.setPasswordBank(password, schoolUserId);
        excuteRequest(observable);
    }

    /**
     * 更新食堂名字
     * @param storeId
     * @param name
     * @return
     */
    public void updateStoreName(String storeId, String name) {
        Observable<BaseResponse<Boolean>> observable = service.updateStoreName(storeId, name);
        excuteRequest(observable);
    }

    /**
     * 更新食堂公告
     * @param storeId
     * @param notice
     * @return
     */
    public void updateStoreNotice(String storeId, String notice) {
        Observable<BaseResponse<Boolean>> observable = service.updateStoreNotice(storeId, notice);
        excuteRequest(observable);
    }

    /**
     * 修改食堂营业状态
     * @param storeId
     * @param state
     * @return
     */

    public void updateStoreState(String storeId, int state) {
        Observable<BaseResponse<Boolean>> observable = service.updateStoreState(storeId, state);
        excuteRequest(observable);
    }

    /**
     * 根食堂id 获取食堂送餐时间
     * @param storeId
     * @return
     */
    @Override
    public void getStoreTime(String storeId) {
        Observable<BaseResponse<List<MessTime>>> observable = service.getStoreTime(storeId);
        excuteRequest(observable);
    }

    /**
     * 修改食堂送餐时间
     * @param time
     * @param storeId
     * @return
     */
    @Override
    public void updateStoreSuchedule(String time, String storeId) {
        Observable<BaseResponse<Boolean>> observable = service.updateStoreSuchedule(time,storeId);
        excuteRequest(observable);
    }

    /**
     * 可提现金额
     * @return
     */
    @Override
    public void getBalance() {
        Observable<BaseResponse<String>> observable = service.getBalance();
        excuteRequest(observable);
    }
    /**
     * 获取学校宿舍分区及分区下的宿舍列表
     * @return
     */
    @Override
    public void getSchoolZonesAparment() {
        Observable<BaseResponse<List<ApartmentZone>>> observable = service.getSchoolZonesAparment();
        excuteRequest(observable);
    }


    /**
     * 添加寝室
     * @param apartment
     */
    @Override
    public void addApartmentZones(String apartment) {
        Observable<BaseResponse<Boolean>> observable = service.addApartmentZones(apartment);
        excuteRequest(observable);
    }

    /**
     * 修改宿舍名称 (编辑宿舍)
     * @param apartment
     * @return
     */
    @Override
    public void updateApartmentZones(String apartment) {
        Observable<BaseResponse<Boolean>> observable = service.updateApartmentZones(apartment);
        excuteRequest(observable);
    }

    /**
     * 根据账单 获取明细
     * @param amountRecordId
     * @return
     */
    @Override
    public void getMoneyRecord(String amountRecordId) {
        Observable<BaseResponse<List<BillDetail>>> observable = service.getMoneyRecord(amountRecordId);
        excuteRequest(observable);
    }


    /**
     * ceo 获取学校下的分苑及宿舍
     * @return
     */
    @Override
    public void getSchoolZones() {
        Observable<BaseResponse<List<ApartmentZone>>> observable = service.getSchoolZones();
        excuteRequest(observable);
    }

    /**
     * 申请提现
     * @param money
     * @param password
     * @return
     */
    public void addApply(float money, String password) {
        Observable<BaseResponse<Boolean>> observable = service.addApply(money, password);
        excuteRequest(observable);
    }

    /**
     * 获取用户账户信息（银行卡卡号，持卡人）
     * @return
     */
    public void getCeoUserIdBank() {
        Observable<BaseResponse<Bank>> observable = service.getCeoUserIdBank();
        excuteRequest(observable);
    }

    /**
     * 修改定单配送状态
     * @return
     */
    @Override
    public void updateServiceState(String orderId) {
        Observable<BaseResponse<Boolean>> observable = service.updateServiceState(orderId);
        excuteRequest(observable);
    }

    /**
     * 更新银行卡信息
     * @param id
     * @param cardNo
     * @param openBank
     * @param prov
     * @param city
     * @return
     */
    @Override
    public void updateBank(String id, String cardNo, String openBank, String userName, String prov, String city) {
        Observable<BaseResponse<Boolean>> observable = service.updateBank(id, cardNo, openBank, userName, prov, city);
        excuteRequest(observable);
    }

    /**
     * 修改定单配送状态
     * @param bankNo
     * @return
     */
    @Override
    public void bankUtil(String bankNo) {
        Observable<BaseResponse<Name>> observable = service.bankUtil(bankNo);
        excuteRequest(observable);
    }

    @Override
    public void collectClientMsg(String userId, String clientId, int os, String osVersion, String deviceModel, String appVersion) {
        Observable<BaseResponse<Boolean>> observable = service.collectClientMsg(userId, clientId, os, osVersion, deviceModel, appVersion);
        excuteRequest(observable);
    }
    /**
     * 获取APP版本信息
     * @return
     */
    @Override
    public void getAppInfo() {
        Observable<BaseResponse<AppInfoResult>> observable = service.getAppInfo();
        excuteRequest(observable);
    }

    /**
     * 判断今日是否可提现，
     * 获取结算方式
     * @return
     */

    @Override
    public void presentInformation() {
        Observable<BaseResponse<WithdrawMethosDetail>> observable = service.presentInformation();
        excuteRequest(observable);
    }

    /**
     * 删除楼栋，以楼栋号和,分隔开。
     * @param idList
     * @return
     */
    @Override
    public void deleteApartment(String idList) {
        Observable<BaseResponse<Boolean>> observable = service.deleteApartment(idList);
        excuteRequest(observable);
    }


    /**
     * 寝室楼id,搜索条件，加载页，单页加载数量 获取历史订单信息
     * @param apartmentId
     * @param condition
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public void getStoreHistoricalOrder(String storeId, String schoolId, String apartmentId, String condition, Integer page, Integer pagesize) {
        Observable<BaseResponse<Pager<Order>>> observable = service.getStoreHistoricalOrder(storeId, schoolId, apartmentId, condition, page, pagesize);
        excuteRequest(observable);
    }

    /**
     * 根据食堂id获取食堂配送的楼
     * @param storeId
     * @return
     */
    @Override
    public void getStoreAparment(String storeId) {
        Observable<BaseResponse<List<String>>> observable = service.getStoreAparment(storeId);
        excuteRequest(observable);
    }

    /**
     * 编辑食堂配送范围
     * @param aparmentIdList
     * @param storeId
     * @return
     */
    @Override
    public void updateStoreAparment(String storeId, String aparmentIdList) {
        Observable<BaseResponse<Boolean>> observable = service.updateStoreAparment(storeId, aparmentIdList);
        excuteRequest(observable);
    }


    /**
     * 根学校id 获取学校各个食堂送餐时间
     * @param schoolId
     * @return
     */
    @Override
    public void getSchoolTime(String schoolId) {
        Observable<BaseResponse<List<MessTime>>> observable = service.getSchoolTime(schoolId);
        excuteRequest(observable);
    }

    /**
     * 修改学校配送时间
     * @param time
     * @param schoolId
     * @return
     */
    @Override
    public void updateSchoolSuchedule(String time, String schoolId) {
        Observable<BaseResponse<Boolean>> observable = service.updateSchoolSuchedule(time,schoolId);
        excuteRequest(observable);
    }

    /**
     * 订单确认送达
     * @param orderIds 多个订单号的JSON字符串
     * @return
     */
    public void orderConfirmedV2(String orderIds) {
        Observable<BaseResponse<Integer>> observable = service.orderConfirmedV2(orderIds);
        excuteRequest(observable);
    }

    /**
     * ceo确认取餐
     * @param orderIds 多个订单号的JSON字符串
     * @return
     */
    public void takeMealV2(String orderIds) {
        Observable<BaseResponse<Integer>> observable = service.takeMealV2(orderIds);
        excuteRequest(observable);
    }

}
