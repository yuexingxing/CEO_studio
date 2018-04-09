package com.tajiang.ceo.model;

/**
 * 营业时间
 * Created by Administrator on 2017-08-03.
 */
public class StoreTime {

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    private String startTime;
    private String endTime;
    private String weekDay;

}
