package com.tajiang.ceo.common.receiver;

/**
 * Created by Administrator on 2016/8/26.
 */
public class Notify {

    public static final int TYPE_RED_PACKAGE = 1;

    /**
     * 推送类型
     *  {"type":10,"description":"全民雷锋","content":"全民雷锋携手滴滴出行为您送上8元无门槛出租车优惠券（限武汉地区），点击领取。"}
     *  1 : 红包优惠券
     */
    private int type;
    /**
     *
     * 推送描述
     */
    private String description;
    /**
     * 推送内容
     */
    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

