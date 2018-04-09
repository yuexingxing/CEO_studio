package com.tajiang.ceo.common.utils;

import android.content.Context;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.igexin.sdk.PushManager;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.http.Api;
import com.tajiang.ceo.model.AccountInfo;
import com.tajiang.ceo.model.AccountWithDrawInfo;
import com.tajiang.ceo.model.ApartmentZone;
import com.tajiang.ceo.model.Building;
import com.tajiang.ceo.model.MessageInfo;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.OrderGoods;
import com.tajiang.ceo.model.Pager;
import com.tajiang.ceo.model.SettleDetailInfo;
import com.tajiang.ceo.model.SettleInfo;
import com.tajiang.ceo.model.Shop;
import com.tajiang.ceo.model.StoreTime;
import com.tajiang.ceo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-25.
 */

public class PostDataTools {

    public static abstract class DataCallback {
        public abstract void callback(boolean flag, String message, Object object);
    }

    /**
     * 登录
     * */
    public static void login(Context context, String phone, String password, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("phone", phone);
        mParams.put("password", password);
        mParams.put("clientId", PushManager.getInstance().getClientid(context));
        mParams.put("imei", CommonUtils.getMIME(context));

        RequestUtil.postData(context, Api.user_login, mParams, new RequestUtil.Callback() {
            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                TJApp.getInstance().limitTime = jsonObject.optLong("43200");
                TJApp.getInstance().mToken = jsonObject.optString("token");

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 退出APP
     * @param context
     * @param dataCallback
     * 接口名称 用户退出
     * 请求类型 post
     * 负  责  人 吴俊杰
     * 版        本 91
     * 状        态 测试通过
     * 请求  Url  /base/user/logout
     */
    public static void user_logout(Context context, final DataCallback dataCallback){

        RequestUtil.postData(context, Api.user_logout, new HashMap<String, String>(), new RequestUtil.Callback() {
            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 获取个人信息
     * */
    public static void getUserInfo(Context context, final DataCallback dataCallback){

        RequestUtil.getData(context, Api.user_current, null, new RequestUtil.Callback() {
            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                jsonObject = jsonObject.optJSONObject("data");

                User user = new User();
                user.setRealName(jsonObject.optString("realName"));
                user.setSchoolName(jsonObject.optString("schoolName"));
                user.setRoleType(jsonObject.optInt("roleType"));
                user.setCertified(jsonObject.optInt("certified"));

                UserUtils.saveUser(user);

                if(dataCallback != null){
                    dataCallback.callback(flag, remark, jsonObject);
                }
            }
        });
    }

    /**
     * 接口名称 获取商家列表
     请求类型 get
     负  责  人 刘益伟
     版        本 V1.0.0
     状        态 测试通过
     请求  Url  /dely/shop/list
     * */
    public static void shop_list(Context context, final DataCallback dataCallback){

        RequestUtil.getData(context, Api.shop_list, null, new RequestUtil.Callback() {
            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                if (jsonObject == null){
                    return;
                }

                List<Shop> list = new ArrayList<Shop>();

                JSONArray jsonArray = jsonObject.optJSONArray("data");
                int len = jsonArray.length();
                for(int i=0; i<len; i++){

                    jsonObject = jsonArray.optJSONObject(i);

                    Shop shop = new Shop();
                    shop.setShopId(jsonObject.optString("shopId"));
                    shop.setShopName(jsonObject.optString("shopName"));
                    shop.setShopImage(jsonObject.optString("shopImage"));
                    shop.setSchoolId(jsonObject.optString("schoolId"));
                    shop.setPhone(jsonObject.optString("phone"));
                    shop.setBussState(jsonObject.optInt("bussState"));

                    list.add(shop);
                }

                dataCallback.callback(flag, remark, list);
            }
        });
    }

    /**
     * 接口名称 订单查询
     请求类型 post
     负  责  人 刘益伟
     版        本 V1.0.0
     状        态 开发已完成
     请求  Url  /dely/order/list
     * */
    public static void order_list(Context context, String apartmentId, String code, String condition, String orderState, int page, String shopId, final SwipeToLoadLayout swipeToLoadLayout, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();

        mParams.put("apartmentId", apartmentId + "");//宿舍id	string	选填，code=1 有效
        mParams.put("code", code);//查询分类 string	必填，0-查询首页订单；1-查询全部订单
        mParams.put("condition", condition + "");//模糊查询 string	选填，code=1 有效
        mParams.put("orderState", orderState + "");//订单状态	string	必填，code＝0时（0-未确认、1-已确认）；code＝1时（0-当前订单、1－预定订单、2-完成订单）
        mParams.put("page", page + "");//number	必填，1开始
        mParams.put("pageSize", Order.PAGE_SIZE + "");//number	必填
        mParams.put("shopId", "1");//商家id	string	选填，code=1 有效

        RequestUtil.postData(context, Api.order_list, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                //关闭刷新特效
                if (swipeToLoadLayout != null && swipeToLoadLayout.isRefreshing()) {
                    swipeToLoadLayout.setRefreshing(false);
                }
                if (swipeToLoadLayout != null && swipeToLoadLayout.isLoadingMore()) {
                    swipeToLoadLayout.setLoadingMore(false);
                }

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                Pager<Order> pager = new Pager<Order>();
                List<Order> list = new ArrayList<Order>();
                try {

                    pager.setPage(jsonObject.optInt("page"));
                    pager.setPageSize(jsonObject.optInt("pageSize"));
                    pager.setTotalCount(jsonObject.optInt("totalCount"));
                    pager.setTotalCount(jsonObject.optInt("totalPageCount"));

                    JSONArray jsonArray = jsonObject.optJSONArray("list");
                    int len = jsonArray.length();
                    for(int i=0; i<len; i++){

                        jsonObject = jsonArray.optJSONObject(i);

                        Order order = new Order();

                        order.setOrderId(jsonObject.optString("orderId"));
                        order.setSchoolId(jsonObject.optString("schoolId"));
                        order.setShopName(jsonObject.optString("shopName"));
                        order.setBuyerRemark(jsonObject.optString("buyerRemark"));
                        order.setDelyStartDate(jsonObject.optString("delyStartDate"));

                        order.setDelyEndDate(jsonObject.optString("delyEndDate"));
                        order.setReceiverName(jsonObject.optString("receiverName"));
                        order.setReceiverPhone(jsonObject.optString("receiverPhone"));
                        order.setReceiverAddr(jsonObject.optString("receiverAddr"));
                        order.setOrderState(jsonObject.optInt("orderState"));

                        order.setOrderStateExtra(jsonObject.optInt("orderStateExtra"));
                        order.setTotalPartQty(jsonObject.optInt("totalPartQty"));
                        order.setTotalMoney(jsonObject.optInt("totalMoney"));
                        order.setFinalMoney(jsonObject.optInt("finalMoney"));
                        order.setTotalDelyFee(jsonObject.optInt("totalDelyFee"));

                        order.setTotalPackFee(jsonObject.optInt("totalPackFee"));
                        order.setVoucherMoney(jsonObject.optInt("voucherMoney"));
                        order.setFullActivityMoney(jsonObject.optInt("fullActivityMoney"));
                        order.setCreateDate(jsonObject.optString("createDate"));
                        order.setDelyId(jsonObject.optString("delyId"));

                        order.setDelyName(jsonObject.optString("delyName"));
                        order.setDelyPhone(jsonObject.optString("delyPhone"));
                        order.setTakerDate(jsonObject.optString("takerDate"));
                        order.setDelyDate(jsonObject.optString("delyDate"));
                        order.setArriveDate(jsonObject.optString("arriveDate"));

                        order.setConfirmDate(jsonObject.optString("confirmDate"));

                        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
                        JSONArray jsonArray1 = jsonObject.optJSONArray("orderItemList");
                        int len1 = jsonArray1.length();
                        for(int k=0; k<len1; k++){

                            JSONObject jsonObject1 = jsonArray1.optJSONObject(k);

                            OrderGoods orderGoods = new OrderGoods();
                            orderGoods.setGoodsName(jsonObject1.optString("goodsName"));
                            orderGoods.setGoodsQty(jsonObject1.optInt("goodsQty"));
                            orderGoods.setGoodsPrice(jsonObject1.optInt("goodsPrice"));

                            orderGoodsList.add(orderGoods);
                        }

                        order.setOrderItemList(orderGoodsList);

                        list.add(order);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                pager.setList(list);
                dataCallback.callback(flag, remark, pager);
            }
        });
    }

    /**
     * 接口名称 查询学校所有楼栋
     请求类型 get
     负  责  人 刘益伟
     版        本 V1.0.0
     状        态 测试通过
     请求  Url  /dely/apartment/list
     * */
    public static void apartment_list(Context context, final DataCallback dataCallback){

        RequestUtil.getData(context, Api.apartment_list, null, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                JSONArray jsonArray = jsonObject.optJSONArray("data");
                int len = jsonArray.length();

                List<ApartmentZone> list = new ArrayList<ApartmentZone>();
                for(int i=0; i<len; i++){

                    jsonObject = jsonArray.optJSONObject(i);

                    ApartmentZone apartmentZone = new ApartmentZone();
                    apartmentZone.setId(jsonObject.optString("id"));
                    apartmentZone.setName(jsonObject.optString("name"));

                    ArrayList<Building> buildingList = new ArrayList<Building>();
                    JSONArray jsonArray1 = jsonObject.optJSONArray("apartmentList");
                    for(int k=0; k<jsonArray1.length(); k++){

                        JSONObject jsonObject1 = jsonArray1.optJSONObject(k);

                        Building building = new Building();
                        building.setId(jsonObject1.optString("apartmentId"));
                        building.setName(jsonObject1.optString("apartmentName"));
                        building.setState(jsonObject1.optInt("state"));

                        buildingList.add(building);
                    }

                    apartmentZone.setList(buildingList);

                    list.add(apartmentZone);
                }

                dataCallback.callback(flag, remark, list);
            }
        });
    }

    /**
     * 接口名称 修改楼栋配送开关
     * 请求类型 post
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 测试通过
     * 请求  Url  /dely/apartment/state
     * */
    public static void apartment_state(Context context, JSONObject jsonObject, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("apartment", jsonObject.toString());

        RequestUtil.postData(context, Api.apartment_state, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 骑士取件
     * 请求类型 post
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     *  状        态 测试通过
     * 请求  Url  /dely/order/take
     */
    public static void order_take(Context context, String orderId, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("orderId", orderId);

        RequestUtil.postData(context, Api.order_take, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     *接口名称 骑士送达
     请求类型 post
     负  责  人 刘益伟
     版        本 V1.0.0
     状        态 开发已完成
     请求  Url  /dely/order/arrive
     */
    public static void order_arrive(Context context, String orderIds, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("orderIds", orderIds);

        RequestUtil.postData(context, Api.order_arrive, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 获取订单详情
     * @param context
     * @param orderId
     * @param dataCallback
     * 接口名称 订单详情
     * 请求类型 get
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 测试通过
     * 请求  Url  /dely/order/detail
     */
    public static void order_detail(Context context, String orderId, final DataCallback dataCallback){

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestUtil.getData(context, Api.order_detail, jsonObject, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                jsonObject = jsonObject.optJSONObject("data");
                Order order = new Order();

                order.setOrderId(jsonObject.optString("orderId"));
                order.setShopName(jsonObject.optString("shopName"));
                order.setBuyerRemark(jsonObject.optString("buyerRemark"));
                order.setDelyStartDate(jsonObject.optString("delyStartDate"));

                order.setDelyEndDate(jsonObject.optString("delyEndDate"));
                order.setReceiverName(jsonObject.optString("receiverName"));
                order.setReceiverPhone(jsonObject.optString("receiverPhone"));
                order.setReceiverAddr(jsonObject.optString("receiverAddr"));
                order.setOrderState(jsonObject.optInt("orderState"));

                order.setOrderStateExtra(jsonObject.optInt("orderStateExtra"));
                order.setTotalPartQty(jsonObject.optInt("totalPartQty"));
                order.setTotalMoney(jsonObject.optInt("totalMoney"));
                order.setFinalMoney(jsonObject.optInt("finalMoney"));
                order.setTotalDelyFee(jsonObject.optInt("totalDelyFee"));

                order.setTotalPackFee(jsonObject.optInt("totalPackFee"));
                order.setVoucherMoney(jsonObject.optInt("voucherMoney"));
                order.setFullActivityMoney(jsonObject.optInt("fullActivityMoney"));
                order.setCreateDate(jsonObject.optString("createDate"));
                order.setDelyId(jsonObject.optString("delyId"));

                order.setDelyName(jsonObject.optString("delyName"));
                order.setDelyPhone(jsonObject.optString("delyPhone"));
                order.setTakerDate(jsonObject.optString("takerDate"));
                order.setDelyDate(jsonObject.optString("delyDate"));
                order.setArriveDate(jsonObject.optString("arriveDate"));

                order.setConfirmDate(jsonObject.optString("confirmDate"));
                order.setDelyFast(jsonObject.optInt("delyFast"));

                List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
                JSONArray jsonArray1 = jsonObject.optJSONArray("orderItemList");
                int len1 = jsonArray1.length();
                for(int k=0; k<len1; k++){

                    JSONObject jsonObject1 = jsonArray1.optJSONObject(k);

                    OrderGoods orderGoods = new OrderGoods();
                    orderGoods.setGoodsName(jsonObject1.optString("goodsName"));
                    orderGoods.setGoodsQty(jsonObject1.optInt("goodsQty"));
                    orderGoods.setGoodsPrice(jsonObject1.optInt("goodsPrice"));

                    orderGoodsList.add(orderGoods);
                }

                order.setOrderItemList(orderGoodsList);


                dataCallback.callback(flag, remark, order);
            }
        });
    }

    /**
     * 接口名称 修改商家名称
     * 请求类型 post
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 测试通过
     * 请求  Url  /dely/shop/name
     */
    public static void shop_name(Context context, String shopName, String shopId, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();

        mParams.put("shopId", shopId+"");
        mParams.put("shopName", shopName);

        RequestUtil.postData(context, Api.shop_name, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 获取商家配送范围
     * 请求类型 get
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 测试通过
     * 请求  Url  /dely/shop/apartment
     */
    public static void shop_apartment(Context context, String shopId, final DataCallback dataCallback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopId", shopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestUtil.getData(context, Api.shop_apartment, jsonObject, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                JSONArray jsonArray = jsonObject.optJSONArray("data");

                List<ApartmentZone> list = new ArrayList<ApartmentZone>();
                for(int i=0; i<jsonArray.length(); i++){

                    ApartmentZone zone = new ApartmentZone();
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    zone.setId(jsonObject1.optString("id"));
                    zone.setName(jsonObject1.optString("name"));

                    ArrayList<Building> buildingList = new ArrayList<Building>();
                    JSONArray jsonArray1 = jsonObject1.optJSONArray("apartmentList");
                    for(int k=0; k<jsonArray1.length(); k++){

                        JSONObject jsonObject2 = jsonArray1.optJSONObject(k);
                        Building building = new Building();

                        building.setId(jsonObject2.optString("apartmentId"));
                        building.setName(jsonObject2.optString("apartmentName"));
                        building.setState(jsonObject2.optInt("checkStatus"));

                        buildingList.add(building);
                    }

                    zone.setList(buildingList);
                    list.add(zone);
                }

                dataCallback.callback(flag, remark, list);
            }
        });
    }

    /**
     * 接口名称 修改商家配送范围
     * 请求类型 get
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 测试通过
     * 请求  Url  /dely/shop/apartment
     */
    public static void modify_shop_apartment(Context context, String shopId, String apartment, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();

        mParams.put("apartment", apartment);//宿舍楼id，逗号隔开	string	必填，全部取消关联则传入-1
        mParams.put("shopId", shopId);

        RequestUtil.postData(context, Api.shop_apartment, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 修改商家营业状态
     * 请求类型 post
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 测试通过
     * 请求  Url  /dely/shop/bussstate
     */
    public static void shop_bussstate(Context context, String shopId, String bussState, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();

        mParams.put("bussState", bussState);//营业状态 1:开启 0:关闭
        mParams.put("shopId", shopId);

        RequestUtil.postData(context, Api.shop_bussstate, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 获取商家营业时间
     * 请求类型 get
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 测试通过
     * 请求  Url  /dely/shop/busstime
     */
    public static void shop_busstime(Context context, String shopId, final DataCallback dataCallback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopId", shopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestUtil.getData(context, Api.shop_busstime, jsonObject, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                List<StoreTime> list = new ArrayList<StoreTime>();
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                for (int i=0; i<jsonArray.length(); i++){

                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    StoreTime storeTime = new StoreTime();
                    storeTime.setStartTime(jsonObject1.optString("startTime"));
                    storeTime.setEndTime(jsonObject1.optString("endTime"));
                    storeTime.setWeekDay(jsonObject1.optString("weekDay"));

                    list.add(storeTime);
                }

                dataCallback.callback(flag, remark, list);
            }
        });
    }

    /**
     * 接口名称 修改商家营业时间
     * 请求类型 post
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 测试通过
     * 请求  Url  /dely/shop/busstime
     */
    public static void modify_shop_busstime(Context context, String shopId, JSONObject bussTime, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();

        mParams.put("shopId", shopId);
        mParams.put("bussTime", bussTime.toString());

        RequestUtil.postData(context, Api.shop_busstime, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                ToastUtils.showLong(remark);
                if (!flag){
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 获取最新的业绩详情
     * 请求类型 get
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 开发已完成
     * 请求  Url  /dely/settle/list
     * */
    public static void settle_list(Context context, final DataCallback dataCallback){

        RequestUtil.getData(context, Api.settle_list, null, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                jsonObject = jsonObject.optJSONObject("data");
                SettleInfo settleInfo = new SettleInfo();
                settleInfo.setAssignId(jsonObject.optString("assignId"));
                settleInfo.setAssignState(jsonObject.optInt("assignState"));
                settleInfo.setDelayTotalMoney(jsonObject.optInt("delayTotalMoney"));
                settleInfo.setSettleCycle(jsonObject.optString("settleCycle"));
                settleInfo.setSettleCycleStr(jsonObject.optString("settleCycleStr"));
                settleInfo.setValidOrderQty(jsonObject.optInt("validOrderQty"));

                List<SettleDetailInfo> settleDetailInfoList = new ArrayList<SettleDetailInfo>();
                JSONArray jsonArray = jsonObject.optJSONArray("delaySettleList");
                for(int i=0; i<jsonArray.length(); i++){

                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    SettleDetailInfo detailInfo = new SettleDetailInfo();
                    detailInfo.setDelyId(jsonObject1.optString("delyId"));
                    detailInfo.setDelyName(jsonObject1.optString("delyName"));
                    detailInfo.setPayedMoney(jsonObject1.optInt("payedMoney"));
                    detailInfo.setSettleId(jsonObject1.optString("settleId"));
                    detailInfo.setSettleMoney(jsonObject1.optInt("settleMoney"));
                    detailInfo.setTotalOrderQty(jsonObject1.optInt("totalOrderQty"));
                    detailInfo.setTotalPartQty(jsonObject1.optInt("totalPartQty"));

                    settleDetailInfoList.add(detailInfo);
                }

                settleInfo.setDelaySettleList(settleDetailInfoList);

                dataCallback.callback(flag, remark, settleInfo);
            }
        });
    }

    /**
     * 接口名称 上传业绩分配
     * 请求类型 post
     * 负  责  人 刘益伟
     * 版        本 V1.0.0
     * 状        态 开发已完成
     * 请求  Url  /dely/settle/upload
     * */
    public static void settle_upload(Context context, JSONArray jsonArray, String assignId, final DataCallback dataCallback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("delayMoney", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> mParams = new HashMap<>();
        mParams.put("assignId", assignId);
        mParams.put("delayMoney", jsonObject.toString());

        RequestUtil.postData(context, Api.settle_upload, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 获取账户信息
     * 请求类型 get
     * 负  责  人 张向阳
     * 状        态 测试通过
     * 请求  Url  /base/acct/info
     * */
    public static void acct_info(Context context, final DataCallback dataCallback){

        RequestUtil.getData(context, Api.acct_info, null, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                jsonObject = jsonObject.optJSONObject("data");

                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setAcctId(jsonObject.optInt("acctId"));
                accountInfo.setActiveState(jsonObject.optInt("activeState"));
                accountInfo.setBalance(jsonObject.optInt("balance"));
                accountInfo.setDepositAmount(jsonObject.optInt("depositAmount"));
                accountInfo.setFrozenBalance(jsonObject.optInt("frozenBalance"));
                accountInfo.setUserId(jsonObject.optInt("userId"));
                accountInfo.setPayPassword(jsonObject.optString("payPassword"));

                dataCallback.callback(flag, remark, accountInfo);
            }
        });
    }

    /**
     * 接口名称 短信验证码
     * 请求类型 post
     * 负  责  人 吴俊杰
     * 版        本 91
     * 状        态 测试通过
     * 请求  Url  /base/msg/code
     * codeType	验证码类型	string	* REG_CODE（注册验证码） BIND_PHONE_CODE绑定手机号码 RESET_PWD（重置登录密码） PAY_PWD_CODE （重置支付密码验证码）
     * phone	手机号	number	*
     * */
    public static void msg_code(Context context, String codeType, String phone, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("codeType", codeType);
        mParams.put("phone", phone);

        RequestUtil.postData(context, Api.msg_code, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 设置支付密码
     请求类型 post
     负  责  人 张向阳
     状        态 测试通过
     请求  Url  /base/acct/setpwd
     * */
    public static void acct_setpwd(Context context, String payPwd, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("payPwd", payPwd);

        RequestUtil.postData(context, Api.acct_setpwd, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 重置支付密码
     * 请求类型 post
     * 负  责  人 张向阳
     * 状        态 测试通过
     * 请求  Url  /base/acct/resetpwd
     * */
    public static void acct_resetpwd(Context context, String payPwd, String smsCode, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("payPwd", payPwd);
        mParams.put("smsCode", smsCode);

        RequestUtil.postData(context, Api.acct_resetpwd, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 修改支付密码
     * 请求类型 post
     * 负  责  人 张向阳
     * 状        态 测试通过
     * 请求  Url  /base/acct/updatepwd
     * newPwd	新支付密码	number
     * oldPwd	旧支付密码	number
     * */
    public static void acct_updatepwd(Context context, String newPwd, String oldPwd, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("newPwd", newPwd);
        mParams.put("oldPwd", oldPwd);

        RequestUtil.postData(context, Api.acct_updatepwd, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 充值-app-支付宝
     * 请求类型 post
     * 负  责  人 张向阳
     * 状        态 开发已完成
     * 请求  Url  /base/acct/recharge
     * payAmount	充值金额	number
     * thdType	支付方式	number	1-微信 2-支付宝
     * */
    public static void acct_recharge(Context context, int payAmount, int thdType, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("payAmount", payAmount + "");
        mParams.put("thdType", thdType + "");

        RequestUtil.postData(context, Api.acct_recharge, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 用户实名认证
     * 请求类型 post
     * 负  责  人 吴俊杰
     * 版        本 91
     * 状        态 测试通过
     * 请求  Url  /base/user/certify
     * idCard	身份证号码	string	*
     * realName	真实姓名	string	*
     * */
    public static void user_certify(Context context, String realName, String idCard, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("idCard", idCard);
        mParams.put("realName", realName);

        RequestUtil.postData(context, Api.user_certify, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 设置提现账户
     请求类型 post
     负  责  人 张向阳
     状        态 测试通过
     请求  Url  /base/acct/setwithdraw
     * acctName	账户名	string
     acctNo	账户号	string
     bankId	银行id	number	1-微信 2-支付宝
     city	城市	string	银行卡绑定必填
     prov	省份	string	银行卡绑定必填
     * */
    public static void acct_setwithdraw(Context context, String acctName, String acctNo, int bankId, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("acctName", acctName);
        mParams.put("acctNo", acctNo);
        mParams.put("bankId", bankId + "");
        mParams.put("city", "");
        mParams.put("prov", "");

        RequestUtil.postData(context, Api.acct_setwithdraw, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 获取第三方提现账户
     请求类型 get
     负  责  人 张向阳
     状        态 测试通过
     请求  Url  /base/acct/withdrawacct
     *
     * */
    public static void acct_withdrawacct(Context context, final DataCallback dataCallback){

        RequestUtil.getData(context, Api.acct_withdrawacct, null, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray == null){
                    return;
                }

                List<AccountWithDrawInfo> list = new ArrayList<AccountWithDrawInfo>();
                for(int i=0; i<jsonArray.length(); i++){

                    jsonObject = jsonArray.optJSONObject(i);

                    AccountWithDrawInfo accountWithDrawInfo = new AccountWithDrawInfo();
                    accountWithDrawInfo.setAccountId(jsonObject.optInt("accountId"));
                    accountWithDrawInfo.setAccountNo(jsonObject.optString("accountNo"));
                    accountWithDrawInfo.setAccountOwner(jsonObject.optString("accountOwner"));
                    accountWithDrawInfo.setAcctId(jsonObject.optInt("acctId"));
                    accountWithDrawInfo.setBankId(jsonObject.optInt("bankId"));
                    accountWithDrawInfo.setBankType(jsonObject.optInt("bankType"));
                    accountWithDrawInfo.setIsDefault(jsonObject.optInt("isDefault"));

                    list.add(accountWithDrawInfo);
                }

                dataCallback.callback(flag, remark, list);
            }
        });
    }


    /**
     * 接口名称 提现操作
     请求类型 post
     负  责  人 张向阳
     状        态 测试通过
     请求  Url  /base/acct/withdraw
     * accountId	第三方账户id	number
     * payPwd	支付密码	string
     * withdrawAmount	提现金额	number
     * */
    public static void acct_withdraw(Context context, int accountId, String payPwd, int withdrawAmount, final DataCallback dataCallback){

        Map<String, String> mParams = new HashMap<>();
        mParams.put("accountId", accountId + "");
        mParams.put("payPwd", payPwd);
        mParams.put("withdrawAmount", withdrawAmount + "");

        RequestUtil.postData(context, Api.acct_withdraw, mParams, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 获取提现进度
     请求类型 get
     负  责  人 张向阳
     状        态 测试通过
     请求  Url  /base/acct/withdrawstate
     * exchangeId	提现ID	number
     * */
    public static void acct_withdrawstate(Context context, int exchangeId, final DataCallback dataCallback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("exchangeId", exchangeId + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestUtil.getData(context, Api.acct_withdrawstate, jsonObject, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                dataCallback.callback(flag, remark, jsonObject);
            }
        });
    }

    /**
     * 接口名称 获取交易列表
     请求类型 get
     负  责  人 张向阳
     状        态 测试通过
     请求  Url  /base/acct/tradlist
     * page	页码
     * pageSize	每页显示量
     * */
    public static void acct_withdrawlist(Context context, final int page, final SwipeToLoadLayout swipeToLoadLayout, final DataCallback dataCallback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", page+"");
            jsonObject.put("pageSize", Order.PAGE_SIZE + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestUtil.getData(context, Api.acct_withdrawlist, jsonObject, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                //关闭刷新特效
                if (swipeToLoadLayout != null && swipeToLoadLayout.isRefreshing()) {
                    swipeToLoadLayout.setRefreshing(false);
                }
                if (swipeToLoadLayout != null && swipeToLoadLayout.isLoadingMore()) {
                    swipeToLoadLayout.setLoadingMore(false);
                }

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                jsonObject = jsonObject.optJSONObject("data");

                Pager<AccountWithDrawInfo> pager = new Pager<AccountWithDrawInfo>();
                List<AccountWithDrawInfo> list = new ArrayList<AccountWithDrawInfo>();

                pager.setPage(jsonObject.optInt("page"));
                pager.setPageSize(jsonObject.optInt("pageSize"));
                pager.setTotalCount(jsonObject.optInt("totalCount"));
                pager.setTotalCount(jsonObject.optInt("totalPageCount"));

                JSONArray jsonArray = jsonObject.optJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    AccountWithDrawInfo info = new AccountWithDrawInfo();

                    info.setAccountNo(jsonObject1.optString("accountNo"));
                    info.setAccountOwner(jsonObject1.optString("accountOwner"));
                    info.setBalance(jsonObject1.optInt("balance"));
                    info.setBankName(jsonObject1.optString("bankName"));
                    info.setCreateTime(jsonObject1.optString("createTime"));
                    info.setExchangeId(jsonObject1.optInt("exchangeId"));
                    info.setPayTime(jsonObject1.optString("payTime"));
                    info.setState(jsonObject1.optInt("state"));

                    list.add(info);
                }

                pager.setList(list);

                dataCallback.callback(flag, remark, pager);
            }
        });
    }

    /**
     * 接口名称 用户消息列表
     请求类型 get
     负  责  人 吴俊杰
     版        本 91
     状        态 测试通过
     请求  Url  /base/msg/user/list
     * page	页码
     * pageSize	每页显示量
     * */
    public static void msg_user_list(Context context, final int page, final SwipeToLoadLayout swipeToLoadLayout, final DataCallback dataCallback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", page+"");
            jsonObject.put("pageSize", Order.PAGE_SIZE + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestUtil.getData(context, Api.msg_user_list, jsonObject, new RequestUtil.Callback() {

            @Override
            public void callback(boolean flag, String remark, JSONObject jsonObject) {

                //关闭刷新特效
                if (swipeToLoadLayout != null && swipeToLoadLayout.isRefreshing()) {
                    swipeToLoadLayout.setRefreshing(false);
                }
                if (swipeToLoadLayout != null && swipeToLoadLayout.isLoadingMore()) {
                    swipeToLoadLayout.setLoadingMore(false);
                }

                if (!flag){
                    ToastUtils.showLong(remark);
                    return;
                }

                jsonObject = jsonObject.optJSONObject("data");

                Pager<MessageInfo> pager = new Pager<MessageInfo>();
                List<MessageInfo> list = new ArrayList<MessageInfo>();

                pager.setPage(jsonObject.optInt("page"));
                pager.setPageSize(jsonObject.optInt("pageSize"));
                pager.setTotalCount(jsonObject.optInt("totalCount"));
                pager.setTotalCount(jsonObject.optInt("totalPageCount"));

                JSONArray jsonArray = jsonObject.optJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setContent(jsonObject1.optString("content"));
                    messageInfo.setCreateTime(jsonObject1.optString("createTime"));
                    messageInfo.setIsRead(jsonObject1.optInt("isRead"));
                    messageInfo.setMsgId(jsonObject1.optInt("msgId"));

                    list.add(messageInfo);
                }

                pager.setList(list);

                dataCallback.callback(flag, remark, pager);
            }
        });
    }
}
