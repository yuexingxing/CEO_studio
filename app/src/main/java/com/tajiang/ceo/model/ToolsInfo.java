package com.tajiang.ceo.model;

/**
 * Created by Administrator on 2017-08-04.
 */

public class ToolsInfo {

    private int price;
    private int reward;
    private String name;
    private int state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
