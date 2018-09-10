package com.cunyu.fileprocess.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description: 时间通用类
 * Author: panyt
 * Version: 1.0
 * Create Date Time: 2017-09-04 9:39.
 * Update Date Time: 2017-09-04 9:39.
 *
 * @see DateUtils
 */
public class DateUtils {

    /**
     * 字符串转换成时间格式
     *
     * @param pattern 格式
     * @param date    时间
     */
    public static Date formatStringToDate(String pattern, String date) {
        SimpleDateFormat dateFormat;
        if (CommonUtil.isNotEmpty(pattern)) {
            dateFormat = new SimpleDateFormat(pattern);
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            IllegalArgumentException i = new IllegalArgumentException("转换时间错误，请确认格式和字段");
            throw i;
        }
    }

    /**
     * 时间格式化为字符串
     *
     * @param pattern 格式
     * @param date    时间
     */
    public static String formatDateToString(String pattern, Date date) {
        SimpleDateFormat dateFormat;
        if (CommonUtil.isNotEmpty(pattern)) {
            dateFormat = new SimpleDateFormat(pattern);
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return dateFormat.format(date);
    }

    /**
     * 格式化时间为Timestamp
     *
     * @param date 时间
     * @return Timestamp
     */
    public static Timestamp formatDateToTimestamp(Date date) {
        Timestamp ts;
        if (CommonUtil.isNotEmpty(date)) {
            ts = new Timestamp(date.getTime());
        } else {
            ts = new Timestamp(System.currentTimeMillis());
        }
        return ts;
    }

    /**
     * 格式化时间为Timestamp
     *
     * @param date 时间
     * @return Timestamp
     */
    public static Date formatTimestampToDate(Timestamp date) {
        Date ts;
        if (CommonUtil.isNotEmpty(date)) {
            ts = new Date(date.getTime());
        } else {
            ts = new Date(System.currentTimeMillis());
        }
        return ts;
    }


    /**
     * 得到往前或者往后推迟的月数
     *
     * @param month 正整数往后推迟 ，负数往前推迟
     * @return 推迟后的月数
     */
    public static Date getMonth(Integer month) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }

    /**
     * 得到往前或者往后推迟的月数
     *
     * @param month 正整数往后推迟 ，负数往前推迟
     * @return 推迟后的月数
     */
    public static Date getMonth(Integer month, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }

    /**
     * 得到往前或者往后推迟的天数
     *
     * @param day 正整数往后推迟 ，负数往前推迟
     * @return 推迟后的天数
     */
    public static Date getDay(Integer day) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

    /**
     * 得到往前或者往后推迟的天数
     *
     * @param day 正整数往后推迟 ，负数往前推迟
     * @return 推迟后的天数
     */
    public static Date getDay(Integer day, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

    /**
     * 得到往前或者往后推迟的天数
     *
     * @param hour 正整数往后推迟 ，负数往前推迟
     * @return 推迟后的小时
     */
    public static Date getDate(Integer hour) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        return c.getTime();
    }

    /**
     * 得到往前或者往后推迟的天数
     *
     * @param hour 正整数往后推迟 ，负数往前推迟
     * @return 推迟后的小时
     */
    public static Date getDate(Integer hour, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        return c.getTime();
    }

    /**
     * 获得传入天数的开始时间，即2012-01-01 00:00:00
     *
     * @param date 原时间
     * @return 日期开始时间
     */
    public static Date getCurrentDayStartTime(Date date) {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now;
        if (CommonUtil.isNotEmpty(date)) {
            now = date;
        } else {
            now = new Date();
        }
        try {
            now = shortSdf.parse(shortSdf.format(now));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得原日期天的结束时间，即2012-01-01 23:59:59
     *
     * @param date 原时间
     * @return 天的结束时间
     */
    public static Date getCurrentDayEndTime(Date date) {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;
        if (CommonUtil.isNotEmpty(date)) {
            now = date;
        } else {
            now = new Date();
        }
        try {
            now = longSdf.parse(shortSdf.format(now) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得原日期周的第一天，周一
     *
     * @param date 原时间
     * @return 周的第一天
     */
    public static Date getCurrentWeekDayStartTime(Date date) {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;
        if (CommonUtil.isNotEmpty(date)) {
            now = date;
        } else {
            now = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得原日期周的最后一天，周日
     *
     * @param date 原时间
     * @return 周的最后一天
     */
    public static Date getCurrentWeekDayEndTime(Date date) {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date now;
        if (CommonUtil.isNotEmpty(date)) {
            now = date;
        } else {
            now = new Date();
        }
        c.setTime(now);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得原日期月的开始时间
     *
     * @param time 原时间
     * @return 月的开始时间
     */
    public static Date getCurrentMonthStartTime(Date time) {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date nowTime;
        if (CommonUtil.isNotEmpty(time)) {
            nowTime = time;
        } else {
            nowTime = new Date();
        }
        c.setTime(nowTime);
        Date date = null;
        try {
            c.set(Calendar.DATE, 1);
            date = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 原日期月的结束时间
     *
     * @param time 原时间
     * @return 月的结束时间
     */
    public static Date getCurrentMonthEndTime(Date time) {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date nowTime;
        if (CommonUtil.isNotEmpty(time)) {
            nowTime = time;
        } else {
            nowTime = new Date();
        }
        c.setTime(nowTime);
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 原日期季度的开始时间
     *
     * @param time 原时间
     * @return 季度开始时间
     */
    public static Date getCurrentQuarterStartTime(Date time) {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date nowTime;
        if (CommonUtil.isNotEmpty(time)) {
            nowTime = time;
        } else {
            nowTime = new Date();
        }
        c.setTime(nowTime);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 原日期季度的结束时间
     *
     * @param time 原时间
     * @return 季度结束时间
     */
    public static Date getCurrentQuarterEndTime(Date time) {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date nowTime;
        if (CommonUtil.isNotEmpty(time)) {
            nowTime = time;
        } else {
            nowTime = new Date();
        }
        c.setTime(nowTime);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 得到指定月的天数
     * */
    public static int getMonthLastDay(Date date)
    {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        return a.get(Calendar.DATE);
    }

    /**
     * 得到年月日时分秒
     *
     * @param time 时间
     * @param flag 1:年， 2月，3日， 4时，5分，6秒
     * @return
     */
    public static Integer getDate(Date time, Integer flag) {
        Calendar date = Calendar.getInstance();
        date.setTime(time);
        if(flag.equals(1)){
            return date.get(Calendar.YEAR);
        } else if(flag.equals(2)){
            return date.get(Calendar.MONTH )+ 1;
        } else if(flag.equals(3)){
            return date.get(Calendar.DATE);
        } else if(flag.equals(4)){
            return date.get(Calendar.HOUR);
        }
        return null;
    }
}
