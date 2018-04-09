package com.tajiang.ceo.common.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/20.
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -8742429915783625805L;

    private static final int SUCCESS_CODE = 200;

    // 错误码
    protected int errorCode;
    // 错误消息
    protected String error;
    // 更多信息
    protected String moreInfo;
    // 响应数据
    protected T data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public boolean isSuccess() {
        return errorCode == SUCCESS_CODE;
    }

    @Override
    public String toString() {
        return "BaseResponse [errorCode=" + errorCode + ", error=" + error
                + ", moreInfo=" + moreInfo + ", data=" + data + "]";
    }

}