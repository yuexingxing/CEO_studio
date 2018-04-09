package com.tajiang.ceo.model;

import java.util.List;


/**
 * Created by work on 2016/7/5.
 */
public class MessTime {

    private int weekDay;

    private List<Time> list;


    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public List<Time> getList() {
        return list;
    }

    public void setList(List<Time> list) {
        this.list = list;
    }

    public class Time {

        public Time(){}

        private String startTime;
        private String endTime;

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
    }

}
