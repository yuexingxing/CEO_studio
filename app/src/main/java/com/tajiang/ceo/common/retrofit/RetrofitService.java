package com.tajiang.ceo.common.retrofit;

import com.tajiang.ceo.common.http.BaseResponse;
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

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by work on 2016/6/28.
 */
public interface RetrofitService {

    /**
     * 登录接口
     * @param userName
     * @param passWord
     * @return
     */
    @FormUrlEncoded
//    @POST("base/user/login")
    @POST("user/login")
    Observable<BaseResponse<User>> signinCheck(@Field("phone") String userName, @Field("password") String passWord, @Field("clientId") String clientId, @Field("imei") String imei);

    /**
     * 获取学校宿舍
     * @param schoolId
     * @return
     */
    @GET("getApartment")
    Observable<BaseResponse<List<Apartment>>> getApartment(@Query("schoolId") String schoolId);

    /**
     * 根据登录用户id 获取用户的 账单流水
     * @param page
     * @param pagesize
     * @return
     */
    @GET("getAmountRecord")
    Observable<BaseResponse<Pager<BillRecord>>> getAmountRecord(@Query("page") int page, @Query("pagesize") int pagesize);

    /**
     * 根据用户手机号 获取短信验证码 找回密码
     * @param mobilePhone
     * @return
     */
    @GET("regcode")
    Observable<BaseResponse<String>> getRegCode(@Query("mobilePhone") String mobilePhone);


//    /**
//     * 根据学校id,食堂id,寝室楼id,打印状态，手机号，订单号 获取当日订单信息
//     * @param schoolId
//     * @param storeId
//     * @param apartmentId
//     * @param orderPrint
//     * @param buyerPhone
//     * @param orderSn
//     * @param page
//     * @param pagesize
//     * @return
//     */
//    @POST("getStoreOrder")
//    Observable<BaseResponse<Pager<Order>>> getStoreOrder(@Query("schoolId") String schoolId, @Query("storeId") String storeId, @Query("apartmentId") String apartmentId, @Query("orderPrint")Integer orderPrint, @Query("buyerPhone")String buyerPhone,
//                                                         @Query("orderSn")String orderSn, @Query("page")Integer page, @Query("pagesize")Integer pagesize);

    /**
     * 根据学校id,食堂id,寝室楼id,打印状态，手机号，订单号 获取当日订单信息
     * @param schoolId
     * @param storeId
     * @param apartmentId
     * @param condition
     * @param page
     * @param pagesize
     * @param menu 0首页 1今日订单页
     * @return
     */
    @POST("getStoreOrder")
    Observable<BaseResponse<Pager<Order>>> getStoreOrder(@Query("schoolId") String schoolId, @Query("storeId") String storeId, @Query("apartmentId") String apartmentId, @Query("condition")String condition,
                                                         @Query("page")Integer page, @Query("pagesize")Integer pagesize, @Query("menu")Integer menu);


    /**
     * 根据档口id,寝室楼id,模糊查询，手机号，订单状态 获取当日订单信息
     * @param stallsId
     * @param apartmentId
     * @param condition
     * @param orderState 20 （未取餐）,30（配送中）, 40（已送达）
     * @param page
     * @param pagesize
     * @return
     */
    @POST("getStallsOrders")
    Observable<BaseResponse<Pager<Order>>> getStallsOrder(@Query("stallsId") String stallsId,
                                                           @Query("apartmentId") String apartmentId,
                                                           @Query("condition")String condition,
                                                           @Query("orderState")Integer orderState,
                                                           @Query("page")Integer page,
                                                           @Query("pagesize")Integer pagesize);



    /**
     * 根据登录用户的id获取用户的今日有效订单和今日提成 （实时更新）
     * @return
     */
    @GET("orderToday")
    Observable<BaseResponse<SalesStatistics>> orderToday();


    /**
     * 用户添加银行卡
     * @param cardNo
     * @param userName
     * @param openBank
     * @param prov
     * @param city
     * @return
     */
    @POST("addBank")
    Observable<BaseResponse<Boolean>> addBank(@Query("cardNo")String cardNo
            ,@Query("userName")String userName
            ,@Query("openBank")String openBank
            ,@Query("prov")String prov
            ,@Query("city")String city);


    /**
     * 根据用户id,密码,旧密码 修改密码
     * @param id
     * @param password
     * @param oldPassword
     * @return
     */
    @GET("updatePassword")
    Observable<BaseResponse<User>> updatePassword(@Query("id") String id, @Query("password") String password
            , @Query("oldPassword") String oldPassword);

    /**
     * 根据手机号,验证码 验证验证码
     * @param mobilePhone
     * @param regcode
     * @return
     */
    @POST("verifyregcode")
    Observable<BaseResponse<String>> verifyRegCode(@Query("mobilePhone") String mobilePhone
            , @Query("regcode") String regcode);

    /**
     * 根据旧密码 新密码 修改支付密码
     * @param oldpassword
     * @param password
     * @return
     */
    @POST("modifyPayPwd")
    Observable<BaseResponse<Boolean>> modifyPayPwd(@Query("oldpassword") String oldpassword
            , @Query("password") String password);

    /**
     * 找回支付密码
     * @param password
     * @return
     */
    @POST("UpdatePaymentPsd")
    Observable<BaseResponse<Boolean>> updatePaymentPsd(@Query("password") String password);

    /**
     * 用户是否设置密码
     * @param schoolUserId
     * @return
     */
    @GET("selctpsd")
    Observable<BaseResponse<Boolean>> selctpsd(@Query("schoolUserId") String schoolUserId);


    /**
     * 食堂管理
     * @param schoolId
     * @return
     */
    @GET("canteenMnagement")
    Observable<BaseResponse<Store>> canteenMnagement(@Query("schoolId") String schoolId);

    /**
     * 判断旧密码是否正确
     * @param oldpassword
     * @return
     */
    @GET("judgePwd")
    Observable<BaseResponse<String>> judgePwd(@Query("oldpassword") String oldpassword);

    /**
     * 获取食堂列表
     * @param schoolId
     * @return
     */
    @GET("getStores")
    Observable<BaseResponse<List<Store>>> getStores(@Query("schoolId") String schoolId);

    /**
     * 获取食堂和档口列表 (新增， 结构体:一级为食堂，二级为食堂底下的档口)
     * @param schoolId
     * @return
     */
    @POST("getSchoolStalls")
    Observable<BaseResponse<List<Store>>> getSchoolStalls(@Query("schoolId") String schoolId);

    /**
     * 设置提现密码
     * @param password
     * @param schoolUserId
     * @return
     */
    @POST("setPasswordBank")
    Observable<BaseResponse<Boolean>> setPasswordBank(@Query("password") String password,
                                                     @Query("schoolUserId") String schoolUserId);

    /**
     * 修改食堂名字
     * @param storeId
     * @param name
     * @return
     */
    @GET("updateStoreName")
    Observable<BaseResponse<Boolean>> updateStoreName(@Query("storeId") String storeId,
                                                     @Query("name") String name);

    /**
     * 修改食堂公告
     * @param storeId
     * @param notice
     * @return
     */
    @GET("updateStoreNotice")
    Observable<BaseResponse<Boolean>> updateStoreNotice(@Query("storeId") String storeId,
                                                     @Query("notice") String notice);


    /**
     * 修改食堂营业状态
     * @param storeId
     * @param state
     * @return
     */
    @GET("updateStoreState")
    Observable<BaseResponse<Boolean>> updateStoreState(@Query("storeId") String storeId,
                                                       @Query("state") int state);

     /**
     * 根食堂id 获取食堂送餐时间
     * @param storeId
     * @return
     */
    @GET("storeTime")
    Observable<BaseResponse<List<MessTime>>> getStoreTime(@Query("storeId") String storeId);

    /**
     * 修改食堂送餐时间
     * @param time
     * @param storeId
     * @return
     */
    @POST("updateStoreSuchedule")
    Observable<BaseResponse<Boolean>> updateStoreSuchedule(@Query("time") String time,@Query("storeId") String storeId);


    /**
     * 根学校id 获取学校各个食堂送餐时间
     * @param schoolId
     * @return
     */
    @GET("storeTime")
    Observable<BaseResponse<List<MessTime>>> getSchoolTime(@Query("schoolId") String schoolId);

    /**
     * 修改学校配送时间
     * @param time
     * @param schoolId
     * @return
     */
    @POST("updateStoreSuchedule")
    Observable<BaseResponse<Boolean>> updateSchoolSuchedule(@Query("time") String time,@Query("schoolId") String schoolId);

    /**
     * 可提现金额
     * @return
     */
    @GET("getBalance")
    Observable<BaseResponse<String>> getBalance();

    /**
     * 获取学校宿舍分区及分区下的宿舍列表
     * @return
     */
    @GET("getSchoolZonesAparment")
    Observable<BaseResponse<List<ApartmentZone>>> getSchoolZonesAparment();

    /**
     * 添加宿舍分区 及 分区下的学校
     * @param apartment
     * @return
     */
    @POST("addAparmentZones")
    Observable<BaseResponse<Boolean>> addApartmentZones(@Query("aparment") String apartment);

    /**
     * 修改宿舍名称 (编辑宿舍)
     * @param apartment
     * @return
     */
    @POST("updateAparmentZones")
    Observable<BaseResponse<Boolean>> updateApartmentZones(@Query("aparment") String apartment);

    /**
     * 根据账单 获取明细
     * @param amountRecordId
     * @return
     */
    @GET("getMoneyRecord")
    Observable<BaseResponse<List<BillDetail>>> getMoneyRecord(@Query("amountRecordId") String amountRecordId);

    /**
     * ceo 获取学校下的分苑及宿舍
     * @return
     */
    @GET("getSchoolZones")
    Observable<BaseResponse<List<ApartmentZone>>> getSchoolZones();

    /**
     * 申请提现
     * @param money
     * @param password
     * @return
     */
    @POST("addApply")
    Observable<BaseResponse<Boolean>> addApply(@Query("money") float money, @Query("password") String password);

    /**
     * 获取用户账户信息（银行卡卡号，持卡人）
     * @return
     */
    @GET("getCeoUserIdBank")
    Observable<BaseResponse<Bank>> getCeoUserIdBank();

    /**
     * 修改定单配送状态
     * @param orderId
     * @return
     */
    @GET("updateServiceState")
    Observable<BaseResponse<Boolean>> updateServiceState(@Query("orderId") String orderId);

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
    @POST("updateBank")
    Observable<BaseResponse<Boolean>> updateBank(@Query("id") String id, @Query("cardNo") String cardNo
            , @Query("openBank") String openBank
            ,@Query("userName")String userName
            , @Query("prov") String prov
            , @Query("city") String city);

    /**
     * 修改定单配送状态
     * @param bankNo
     * @return
     */
    @GET("bankUtil")
    Observable<BaseResponse<Name>> bankUtil(@Query("bankNo") String bankNo);

    /**
     * 上传手机信息
     * @param clientId
     * @param os
     * @param osVersion
     * @param deviceModel
     * @param appVersion
     * @return
     */
    @POST("collectClientMsg")
    Observable<BaseResponse<Boolean>> collectClientMsg(@Query("userId") String userId, @Query("clientId") String clientId, @Query("os") int os
            , @Query("osVersion") String osVersion, @Query("deviceModel") String deviceModel, @Query("appVersion") String appVersion);


    /**
     * 获取APP版本信息
     * @return
     */
    @GET("androidVersion")
    Observable<BaseResponse<AppInfoResult>> getAppInfo();

    /**
     * 判断今日是否可提现，
     * 获取结算方式
     * @return
     */
    @GET("presentInformation")
    Observable<BaseResponse<WithdrawMethosDetail>> presentInformation();

    /**
     * 删除楼栋，以楼栋号和,分隔开。
     * @param idList
     * @return
     */
    @GET("deleteApartment")
    Observable<BaseResponse<Boolean>> deleteApartment(@Query("idList") String idList);

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
    @POST("getStoreHistoricalOrder ")
    Observable<BaseResponse<Pager<Order>>> getStoreHistoricalOrder(@Query("storeId") String storeId, @Query("schoolId")String schoolId,
                                                                   @Query("apartmentId") String apartmentId, @Query("condition")String condition,
                                                                      @Query("page")Integer page, @Query("pagesize")Integer pagesize);


    /**
     * 根据食堂id获取食堂配送的楼
     * @param storeId
     * @return
     */
    @GET("getStoreAparment")
    Observable<BaseResponse<List<String>>> getStoreAparment(@Query("storeId") String storeId);

    /**
     * 编辑食堂配送范围
     * @param aparmentIdList
     *  @param storeId
     * @return
     */
    @POST("updateStoreAparment")
    Observable<BaseResponse<Boolean>> updateStoreAparment(@Query("storeId") String storeId, @Query("aparmentIdList") String aparmentIdList);


    /**
     * 订单确认送达
     * @param orderIds 多个订单号的JSON字符串
     * @return
     */
    @POST("confirmedServiceV2")
    Observable<BaseResponse<Integer>> orderConfirmedV2(@Query("orderIds") String orderIds);

    /**
     * ceo确认取餐
     * @param orderIds 多个订单号的JSON字符串
     * @return
     */
    @POST("take_mealV2")
    Observable<BaseResponse<Integer>> takeMealV2(@Query("orderIds") String orderIds);

}
