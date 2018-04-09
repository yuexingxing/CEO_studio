package com.tajiang.ceo.model;

public class  OrderGoods{

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsQty() {
        return goodsQty;
    }

    public void setGoodsQty(int goodsQty) {
        this.goodsQty = goodsQty;
    }

    public int getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(int goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    private  String goodsName;
    private  int goodsQty;
    private  int goodsPrice;



}