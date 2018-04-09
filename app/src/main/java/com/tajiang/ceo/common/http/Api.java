package com.tajiang.ceo.common.http;

public class Api {

    // 正式服务器
//    private static final String HOST = "apiwmdev.axpapp.com";//rest.itajiang.com:8888
//    private static final String HOST_TEST = "test.itajiang.com:8888";
    private static  final String CUI_TEST="192.168.3.103:8084";


    private Api() {}

    //====== 用户 ======//

    public static final String user_login = "base/user/login";//用户登录
    public static final String user_logout = "base/user/logout";//用户退出APP
    public static final String user_current = "base/user/current";//获取用户信息
    public static final String shop_list = "dely/shop/list";//获取商家列表
    public static final String order_list = "dely/order/list";//订单查询
    public static final String apartment_list = "dely/apartment/list";//查询学校所有楼栋
    public static final String apartment_state = "dely/apartment/state";//修改楼栋开关
    public static final String order_take = "dely/order/take";//骑士取件
    public static final String order_arrive = "dely/order/arrive";//骑士送达
    public static final String order_detail = "dely/order/detail";//获取订单详情
    public static final String shop_name = "dely/shop/name";//修改档口名称
    public static final String shop_apartment = "dely/shop/apartment";//获取配送范围，修改配送范围，请求方式不同
    public static final String shop_bussstate = "dely/shop/bussstate";//修改营业状态
    public static final String shop_busstime =  "dely/shop/busstime";//获取商家营业时间
    public static final String settle_list =  "dely/settle/list";//获取最新业绩
    public static final String settle_upload =  "dely/settle/upload";//上传业绩

    public static final String acct_info = "base/acct/info";//获取钱包账户相信
    public static final String msg_code = "base/msg/code";//获取验证码
    public static final String acct_setpwd = "base/acct/setpwd";//设置支付密码
    public static final String acct_resetpwd = "base/acct/resetpwd";//重置支付密码
    public static final String acct_updatepwd = "base/acct/updatepwd";//修改支付密码

    public static final String acct_recharge = "base/acct/recharge";//充值-app-支付宝
    public static final String user_certify = "base/user/certify";//实名认证
    public static final String acct_withdraw = "base/acct/withdraw";//提现操作
    public static final String acct_tradlist = "base/acct/tradlist";//获取交易列表
    public static final String acct_setwithdraw = "base/acct/setwithdraw";//设置提现账号
    public static final String acct_withdrawacct = "base/acct/withdrawacct";//获取第三方提现账户信息
    public static final String acct_withdrawlist = "base/acct/withdrawlist";//获取提现记录
    public static final String acct_withdrawstate =  "base/acct/withdrawstate";//提现进度

    public static final String msg_user_list =  "base/msg/user/list";//用户消息列表

    // 用户统计
    public static final String USER_STATISTIC = "/userStatistics";
    // 当前账号信息
    public static final String USER_GET_SCHOOL_USER = "/getSchoolUser";
    // 修改密码
    public static final String USER_UPDATE_SCHOOL_USER = "/updateSchoolUser";
    // 用户登录验证
    public static final String USER_VERIFIC_ATION = "/verification";

    //====== 食堂 ======//

    // 食堂管理
    public static final String MESS_CANTEENMNAGEMENT = "/canteenMnagement";
    // 根据学校获取食堂列表
    public static final String MESS_GETSTORES = "/getStores";
    // 根据食堂id 获取食堂信息
    public static final String MESS_GETSTORE = "/getStore";
    // 根据食堂id修改食堂基本信息
    public static final String MESS_UPDATESTORE = "/updateStore";
    // 根食堂id 获取食堂送餐时间
    public static final String MESS_STORETIME = "/storeTime";

    //====== 宿舍 ======//

    // 修改宿舍
    public static final String APARTMENT_UPDATE_APARTMENT = "/updateApartment";
    // 根据学校ceo用户 和学校id 获取学校宿舍
    public static final String APARTMENT_GET_APARTMENT = "/getApartment";
    // 根据学校id 获取学校的分苑
    public static final String APARTMENT_GET_ZONES = "/getZones";
    // 根据学校 id 修改学校分苑
    public static final String APARTMENT_UPDATE_ZONES = "/updateZones";
    // 添加学校分苑
    public static final String APARTMENT_ADD_ZONES = "/addZones";
    // 添加宿舍楼
    public static final String APARTMENT_ADD_APARTMENT = "/addApartment";

    //====== 订单 ======//

    // 根据学校id,食堂id,寝室楼id,打印状态，手机号，订单号 获取当日订单信息
    public static final String ORDER_GET_STOREORDER = "/getStoreOrder";
    // 退单
    public static final String ORDER_REFUND = "/refund";
    // 退单记录
    public static final String ORDER_SINGLE_RECORD = "/singleRecord";

    //====== 测试 ======//
    public static final String TEST_LOGIN = "signin_check";

    //用户账单流水
    public static  final String AMOUNT_RECORD = "getAmountRecord";
    //根据登录用户的id获取用户的今日有效订单和今日提成 （实时更新）
    public static  final  String OEDER_TODAY = "orderToday";
    //用户银行卡信息
    public static  final String ACCOUNT_INFORMATION = "accountInformation";
    //用户添加银行卡
    public static  final  String ADD_BANK = "addBank";
}
