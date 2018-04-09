package com.tajiang.ceo.common.retrofit;

import android.content.Intent;
import android.text.TextUtils;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.ErrorCode;
import com.tajiang.ceo.common.activity.HomeActivity;
import com.tajiang.ceo.common.http.BaseResponse;
import com.tajiang.ceo.common.http.HttpResponseListener;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.SharedPreferencesUtils;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.http.GlobalErrorCode;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/6/20.
 */
public class TJSubscriber<T> extends Subscriber<BaseResponse<T>> {

    private HttpResponseListener listener;

    public TJSubscriber(HttpResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCompleted() {
        LogUtils.d("onCompleted");
        listener.onFinish();
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.d("onError");
        ToastUtils.showShort(R.string.msg_http_error);
        listener.onFinish();
    }

    @Override
    public void onNext(BaseResponse response) {
        com.tajiang.ceo.common.utils.LogUtils.d("onSuccess");
        if (response.isSuccess()) {
            listener.onSuccess(response);
        } else {
            handleOutLogin(response);
        }
    }

    /**
     * 统一处理未登录
     * @param response
     */
    private void handleOutLogin(BaseResponse response) {
        if(!TextUtils.isEmpty(response.getMoreInfo()) && response.getErrorCode() != GlobalErrorCode.UNAUTHORIZED) {
            com.tajiang.ceo.common.utils.ToastUtils.showShort(response.getMoreInfo());
        }
        if(response.getErrorCode() == GlobalErrorCode.UNAUTHORIZED) {
            if(!TJApp.getInstance().isShowLogin()) {
                TJApp.getInstance().setShowLogin(true);
                Intent intent = new Intent("com.tajiang.ceo.action.LOGIN");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TJApp.getInstance().startActivity(intent);
                /**
                 * 由于HomeActivity为singleInstance,结束HomeActivity对象（若存在）
                 * 清除已过时的登录信息，以免重用旧的实例。同时清空登录信息
                 */
                SharedPreferencesUtils.remove(SharedPreferencesUtils.USER_LOGIN_INFOR);
                TJApp.getInstance().finishActivity(HomeActivity.class);
            }
        } else {
            listener.onFailed(response);
        }
    }

}
