package com.tajiang.ceo.common.http;


/**
 * Created by Administrator on 2016/6/20.
 */
public interface HttpResponseListener {

     void onSuccess(BaseResponse response);

     void onFailed(BaseResponse response);

     void onFinish();
}
