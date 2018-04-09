package com.tajiang.ceo.common.http;

/**
 * Created by Administrator on 2016/6/20.
 */
public interface HttpLoad {
    /**
     * 用户登录
     * @param userName
     * @param passWord
     */
    void signinCheck(String userName, String passWord, String clientId, String imei);

    /**
     * 用户账单
     */
    void getAmountRecord(int page, int pagesize);

    /**
     * 首页订单
     * @param schoolId
     * @param storeId
     * @param apartmentId
     * @param condition
     * @param page
     * @param pagesize
     * @param menu
     */
    void getStoreOrder(String schoolId,String storeId,String apartmentId,String condition,
                       Integer page,Integer pagesize, Integer menu);


    /**
     * 首页订单
     * @param stallsId
     * @param apartmentId
     * @param condition
     * @param orderState
     * @param page
     * @param pagesize
     */
    void getStallsOrder(String stallsId, String apartmentId, String condition,
                        Integer orderState, Integer page, Integer pagesize);

    /**
     * 退单
     * @param orderGoodsId
     */
    void refund(String orderGoodsId);

    /**
     * 退单接口
     * @param storeId
     * @param schoolId
     * @param page
     * @param pagesize
     * @param buyerPhone
     * @param orderSn
     */
    void singleRecord(String storeId,String schoolId,Integer page,Integer pagesize,String buyerPhone,String orderSn);

    /**
     *
     * 修改宿舍
     * @param id
     * @param name
     * @param zonesId
     */
    void updateApartment(String id,String name,String zonesId);

    /**
     * 根据学校ceo用户 和学校id 获取学校宿舍
     * @param schoolId
     */
    void getApartment(String schoolId);

    /**
     * 根据学校id 获取学校的分苑
     * @param schoolId
     */
    void getZones(String schoolId);

    /**
     * 根据用户手机号 获取短信验证码
     * @param mobilePhone
     */
    void getRegCode (String mobilePhone);

    /**
     * 根据登录用户的id获取用户的今日有效订单和今日提成 （实时更新）
     */
    void orderToday();


    /**
     * 用户添加银行卡
     * @param cardNo
     * @param userName
     * @param openBank
     * @param prov
     * @param city
     * @return
     */
    void addBank(String cardNo,String userName,String openBank,String prov,String city);


    /**
     * 根据用户id,密码,旧密码 修改密码
     * @param id
     * @param password
     * @param oldPassword
     */
    void updatePassword (String id, String password, String oldPassword);

    /**
     * 根据手机号,验证码 验证验证码
     * @param mobilePhone
     * @param regcode
     * @return
     */
    void verifyRegCode(String mobilePhone, String regcode);

    /**
     * 根据新密码 修改支付密码
     * @param oldpassword
     * @param password
     * @return
     */
    void modifyPayPwd(String oldpassword, String password);

    /**
     * 找回支付密码
     * @param password
     * @return
     */
    void updatePaymentPsd(String password);

    /**
     * 用户是否设置密码
     * @param schoolUserId
     * @return
     */
    void selctpsd(String schoolUserId);


    /**
     * 食堂管理
     * @param schoolId
     * @return
     */
    void canteenMnagement(String schoolId);

    /**
     * 判断旧密码是否正确
     * @param oldpassword
     * @return
     */
    void judgePwd(String oldpassword);

    /**
     * 获取学校食堂列表
     * @param schooldId
     */
    void getStores(String schooldId);

    /**
     * 获取食堂和档口列表 (新增， 结构体:一级为食堂，二级为食堂底下的档口)
     * @param schooldId
     */
    void getSchoolStalls(String schooldId);

    /**
     * 设置提现密码
     * @param password
     * @param schoolUserId
     * @return
     */
    void setPasswordBank(String password, String schoolUserId);

    /**
     * 修改食堂名字
     * @param storeId
     * @param name
     * @return
     */
    void updateStoreName(String storeId, String name);

    /**
     * 修改食堂公告
     * @param storeId
     * @param notice
     * @return
     */
    void updateStoreNotice(String storeId, String notice);

    /**
     * 修改食堂营业状态
     * @param storeId
     * @param state
     * @return
     */
    void updateStoreState(String storeId, int state);

    /**
     * 根食堂id 获取食堂送餐时间
     * @param storeId
     * @return
     */
    void getStoreTime(String storeId);

    /**
     * 修改食堂送餐时间
     * @param time
     * @param storeId
     * @return
     */
    void updateStoreSuchedule(String time,String storeId);

    /**
     * 可提现金额
     * @return
     */
    void getBalance();

    /**
     * 获取学校宿舍分区及分区下的宿舍列表
     * @return
     */
    void getSchoolZonesAparment();

    /**
     * 添加宿舍分区 及 分区下的学校
     * @param apartment
     * @return
     */
    void addApartmentZones(String apartment);

    /**
     * 修改宿舍名称 (编辑宿舍)
     * @param apartment
     * @return
     */
    void updateApartmentZones(String apartment);

    /**
     * 根据账单 获取明细
     * @param amountRecordId
     * @return
     */
    void getMoneyRecord(String amountRecordId);

    /**
     * ceo 获取学校下的分苑及宿舍
     * @return
     */
    void getSchoolZones();
    /**
     * 申请提现
     * @param money
     * @param password
     * @return
     */
    void addApply(float money, String password);

    /**
     * 获取用户账户信息（银行卡卡号，持卡人）
     * @return
     */
    void getCeoUserIdBank();

    /**
     * 修改定单配送状态
     * @param orderId
     * @return
     */
    void updateServiceState(String orderId);

    /**
     * 更新银行卡信息
     * @param id
     * @param cardNo
     * @param openBank
     * @param userName
     * @param prov
     * @param city
     * @return
     */
    void updateBank(String id, String cardNo, String openBank, String userName,  String prov,  String city);

    /**
     * 修改定单配送状态
     * @param bankNo
     * @return
     */
    void bankUtil(String bankNo);

    void collectClientMsg(String userId, String clientId, int os, String osVersion, String deviceModel, String appVersion);


    /**
     * 获取APP版本信息
     * @return
     */
    void getAppInfo();

    /**
     * 判断今日是否可提现，
     * 获取结算方式
     * @return
     */
    void presentInformation();

    /**
     * 删除楼栋，以楼栋号和,分隔开。
     * @param idList
     * @return
     */
    void deleteApartment(String idList);

    /**
     * 寝室楼id,搜索条件，加载页，单页加载数量 获取历史订单信息
     * @param schoolId
     * @param storeId
     * @param apartmentId
     * @param condition
     * @param page
     * @param pagesize
     * @return
     */
    void getStoreHistoricalOrder(String storeId, String schoolId, String apartmentId, String condition, Integer page, Integer pagesize);


    /**
     * 根据食堂id获取食堂配送的楼
     * @param storeId
     * @return
     */
    void getStoreAparment(String storeId);

    /**
     * 编辑食堂配送范围
     * @param aparmentIdList
     * @param storeId
     * @return
     */
    void updateStoreAparment(String storeId, String aparmentIdList);

    /**
     * 根学校id 获取学校各个食堂送餐时间
     * @param schoolId
     * @return
     */
    void getSchoolTime(String schoolId);


    /**
     * 修改学校配送时间
     * @param time
     * @param schoolId
     * @return
     */
    void updateSchoolSuchedule(String time, String schoolId);

    /**
     * 订单确认送达
     * @param orderIds 多个订单号的JSON字符串
     * @return
     */
    void orderConfirmedV2(String orderIds);

    /**
     * ceo确认取餐
     * @param orderIds 多个订单号的JSON字符串
     * @return
     */
    void takeMealV2(String orderIds);
}
