package com.tajiang.ceo.setting.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.setting.wallet.alipay.AuthResult;
import com.tajiang.ceo.setting.wallet.alipay.PayResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 充值主界面
 */
public class RechargeMenuActivity extends BaseActivity {

    @BindView(R.id.recharge_menu_tip)
    Button btnTips;

    @BindView(R.id.recharge_menu_fee_1)
    EditText edtFee1;

    @BindView(R.id.recharge_menu_fee_2)
    TextView tvFee2;

    @BindView(R.id.recharge_menu_zhifubao)
    ImageView imgAli;

    @BindView(R.id.recharge_menu_weixin)
    ImageView imgWeixin;

    private IWXAPI wxAPI;
    private PayReq req = new PayReq();

    private int payType = 2;//默认1为支付宝支付
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("在线支付");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_recharge_menu);
    }

    @Override
    protected void initData() {

        zhifubao();
        wxAPI = WXAPIFactory.createWXAPI(RechargeMenuActivity.this, PayUtil.APPID);
        wxAPI.registerApp(PayUtil.APPID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0x0011 && resultCode == RESULT_OK) {

            int price = data.getIntExtra("price", 0);
            int reward = data.getIntExtra("reward", 0);
            btnTips.setText(String.format("充%s元送%s元", price, reward));
            edtFee1.setText((price + reward) + "");
            tvFee2.setText(price + "");
        }
    }

    //活动
    @OnClick(R.id.recharge_menu_tip)
    public void tip(){

        Intent intent = new Intent(this, RechargeActivity.class);
        startActivityForResult(intent, 0x0011);
    }

    //选择支付宝
    @OnClick(R.id.recharge_menu_zhifubao_layout)
    public void zhifubao(){

        payType = 2;
        imgAli.setBackgroundResource(R.drawable.order_select);
        imgWeixin.setBackgroundResource(R.drawable.order_unselected);
    }

    //选择微信
    @OnClick(R.id.recharge_menu_weixin_layout)
    public void weixin(){

        payType = 1;
        imgWeixin.setBackgroundResource(R.drawable.order_select);
        imgAli.setBackgroundResource(R.drawable.order_unselected);
    }

    //确认支付
    @OnClick(R.id.recharge_menu_commit)
    public void commit(){

        PostDataTools.acct_recharge(this, 1, payType, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                if(flag){
                    getOrderInfo(payType, (JSONObject) object);
                }
            }
        });

    }

    /**
     * 解析预支付订单信息
     * @param payType 1-微信 2-支付宝
     * @param jsonObject
     */
    private void getOrderInfo(int payType, JSONObject jsonObject){

        if(payType == 2){

            final String orderInfo = jsonObject.optString("payInfo");
            Log.v("result", orderInfo);

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {

                    PayTask alipay = new PayTask(RechargeMenuActivity.this);
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    Log.i("result", result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }else{

            if(!PayUtil.isWeixinAvilible(RechargeMenuActivity.this)){
                ToastUtils.showShort("当前设备没有安装微信客户端!");
                return;
            }

            req.appId = jsonObject.optString("appid");
            req.partnerId = jsonObject.optString("partnerid");
            req.prepayId = jsonObject.optString("prepayid");
            req.packageValue = jsonObject.optString("package");
            req.nonceStr = jsonObject.optString("noncestr");
            req.timeStamp = jsonObject.optString("timestamp");
            req.sign = jsonObject.optString("sign");

//            wxAPI.registerApp(req.appId);
            wxAPI.sendReq(req);
        }
    }

    public Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case SDK_PAY_FLAG: {

                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.v("result", "resultInfo = " + resultInfo + "");
                    Log.v("result", "resultStatus = " + resultStatus + "");
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeMenuActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    //用户取消支付
                    else if(TextUtils.equals(resultStatus, "6001")){
                        Toast.makeText(RechargeMenuActivity.this, "取消支付", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeMenuActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    AuthResult authResult = new AuthResult(
                            (Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000")
                            && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(
                                RechargeMenuActivity.this,
                                "授权成功\n"
                                        + String.format("authCode:%s",
                                        authResult.getAuthCode()),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(
                                RechargeMenuActivity.this,
                                "授权失败"
                                        + String.format("authCode:%s",
                                        authResult.getAuthCode()),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
}
