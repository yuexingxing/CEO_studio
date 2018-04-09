package com.tajiang.ceo.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SQL on 2016/9/30.
 */
public class DateUtils {

    /**
     * 获取当前日期是这个月的第几天
     */
    public static int getCurrentDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取相差的天数  2016-09-07
     */
    public static long getDayBetween2DateInMills(long currentTimeMills, long lastTimeMills) {
        long delta = Math.abs(currentTimeMills - lastTimeMills);
        long day = delta / (1000*3600*24);
        return day;
    }

    /**
     * 获取当前年月日  2016-09-07
     */
    public static String getCurrentDayMonthYear() {
        //new日期对象当前时间
        Date date = new Date(System.currentTimeMillis());
        //转换提日期输出格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 获取当前年月日  2016-09-07
     */
    public static String getCurrentDayMonthYearByMills(long timeMills) {
        //new日期对象当前时间
        Date date = new Date(timeMills);
        //转换提日期输出格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
