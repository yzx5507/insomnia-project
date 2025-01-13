package com.insomnia.multiCountry.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
@Slf4j
public class DateHelpUtil {
    /**
     * yyyy-MM-dd HH:mm:ss 格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm 格式
     */
    public static final String DEFAULT_DATE_TIME_HHmm_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    /**
     * yyyy-MM-dd 格式
     */
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    /**
     * HH:mm 格式
     */
    public static final String DEFAULT_TIME_HHmm_FORMAT_PATTERN = "HH:mm";

    /**
     * MM-dd 格式
     */
    public static final String DEFAULT_TIME_MMdd_FORMAT_PATTERN = "MM-dd ";


    public static final String YYYY_SPOT_MM_DD_HHMMSS = "yyyy.MM.dd HH:mm:ss";

    /**
     * yyyy/MM/dd HH:mm 格式
     */
    public static final String DEFAULT_DATE_TIME_HHmm_FORMAT_PATTERN_1 = "yyyy/MM/dd HH:mm";

    public static final String YEARMONTHDAY = "yyyy/MM/dd";

    public static final String YEARMONTHDAYTIME = "yyyy/MM/dd HH:mm:ss";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";

    public static final String YYYYMMDDHH = "yyyyMMddHH";

    public static final String YYYYMMDD = "yyyMMdd";

    public static final String YYYYMM = "yyyyMM";

    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");



    /**
     * 将字符串日期转换为日期类型，yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr 字符串日期
     * @return Date:yyyy-MM-dd HH:mm:ss
     */
    public static Date strToDate(String dateStr) {
        Date date = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = longSdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将字符串日期转换为日期类型，yyyyMMdd
     *
     * @param dateStr 字符串日期
     * @return Date
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;
        SimpleDateFormat longSdf = new SimpleDateFormat(format);
        try {
            date = longSdf.parse(dateStr);
        } catch (ParseException e) {
            log.error("dateStr transfer to date format error, exception: {}", e);
        }
        return date;
    }

    public static Date timestampToDate(Long timeStamp){
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT_PATTERN);//要转换的时间格式
        Date date = null;
        try {
            date = sdf.parse(sdf.format(timeStamp));
        } catch (ParseException e) {
            log.error("timestamp transfer to date format error, exception: {}", e);
        }
        return date;
    }

    /**
     * 根据传入的时间格式 将字符串日期转换为日期类型
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToStr(Date date, String pattern) {
        SimpleDateFormat longSdf = new SimpleDateFormat(pattern);
        return longSdf.format(date);
    }

    /**
     * 取指定天数后的日期，时分秒置为0,一般用来计算若干天后的过期日期
     *
     * @param days
     * @return
     */
    public static final Date getExpiredDay(int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.DATE, days);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 指定日期与当前日期相差的天数
     *
     * @param date
     * @return
     */
    public static final int getBetweenDays(Date date) {
        long milliSeconds = System.currentTimeMillis() - date.getTime();

        return (int) (milliSeconds / 1000 / 24 / 3600);
    }


    /**
     * 获取两个日期的相差天数
     */
    public static int getIntervalDays(Date date1, Date date2) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        //同一年
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                //闰年
                boolean leapYearFlag = (i % 4 == 0 && i % 100 != 0 || i % 400 == 0);
                if (leapYearFlag) {
                    timeDistance += 366;
                } else {  //不是闰年
                    timeDistance += 365;
                }
            }

            return Math.abs(timeDistance + (day2 - day1));
        } else {  //不同年
            return Math.abs(day2 - day1);
        }

    }


    /**
     * 获得本月的开始时间，如2015-06-01 00:00:00
     *
     * @return
     */
    public static final Date getCurrentMonthStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得本月的开始时间，如2015-06-01 00:00:00
     *
     * @return
     */
    public static final Date getLastMonthStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int m = c.get(Calendar.MONTH);
            c.set(Calendar.MONTH, m - 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得上月的结束时间，如2015-06-30 23:59:59
     *
     * @return
     */
    public static final Date getLastMonthEndTime() {
        Calendar c = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int m = c.get(Calendar.MONTH);
            c.set(Calendar.MONTH, m - 1);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            date = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取昨天结束时间
     *
     * @return
     */
    public static final Date getYearterDayEndTime() {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            //c.set(Calendar.DATE,1);
            c.add(Calendar.DATE, -1);
            SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取昨天开始时间
     *
     * @return
     */
    public static final Date getYearterDayStartTime() {
        Calendar c = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //c.set(Calendar.DATE,1);
            c.add(Calendar.DATE, -1);
            date = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取今天开始时间
     *
     * @return
     */
    public static final Date getToDayStartTime() {
        Calendar c = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取今天某个时间
     *
     * @return
     */
    public static final Date getToDayTime(String hourFormat) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = longSdf.parse(shortSdf.format(c.getTime()) + " " + hourFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取某天某个时间
     *
     * @return
     */
    public static final Date getDayHourTime(Date date, String hourFormat) {
        Date newDate = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            newDate = longSdf.parse(shortSdf.format(date) + " " + hourFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * 获取今天结束时间
     *
     * @return
     */
    public static final Date getToDayEndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    //获取指定月份的天数
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static List<String> getDayReport(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始

        List<String> dateList = Lists.newArrayList();
        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);
            dateList.add(df + " 00:00:00");
        }

        return dateList;
    }


    public static List<String> getHoursReportToDay(Date date) {

        String formatDate = getDateToString(date, DEFAULT_DATE_FORMAT_PATTERN);
        List<String> dates = Lists.newArrayList();
        for (int i = 0; i < 23; i++) {
            dates.add(formatDate + " " + i + ":00:00");
        }
        return dates;
    }

    /**
     * 获取N天前的结束时间 23:59:59
     *
     * @param n
     * @return
     */
    public static final Date getNDayEndTime(Integer n) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.add(Calendar.DATE, -n);
            date = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取N天前的开始时间 00：00：00
     *
     * @param n
     * @return
     */
    public static final Date getNDayStartTime(Integer n) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.add(Calendar.DATE, -n);
            date = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取N天前后时间
     *
     * @param n
     * @return
     */
    public static final Date addDay(Integer n) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            c.add(Calendar.DATE, n);
            date = c.getTime();
        } catch (Exception e) {
            log.error("addDay", e);
        }
        return date;
    }

    /**
     * @param date
     * @param n
     * @return
     */
    public static final Date addDay(Date date, Integer n) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        Date newDate = null;
        try {
            gc.add(Calendar.DATE, n);
            newDate = gc.getTime();
        } catch (Exception e) {
            log.error("addDay", e);
        }
        return newDate;
    }


    /**
     * 当前月的结束时间，如2015-06-31 23:59:59
     *
     * @return
     */
    public static final Date getCurrentMonthEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
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
     * data 转换成 字符串
     * 格式为： yyyy-MM-dd HH:mm:ss
     */
    public static String getDateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT_PATTERN);
        return format.format(date);
    }

    /**
     * data 转换成 字符串
     * 格式为： yyyy-MM-dd HH:mm:ss
     */
    public static String getDateToString(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        return formatDate.format(date);
    }

    /**
     * data 转换成 字符串
     * 格式为： yyyy-MM-dd HH:mm:ss
     */
    public static String getDateToString2(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_HHmm_FORMAT_PATTERN);
        return format.format(date);
    }

    /**
     * 格式为yyyy-MM-dd HH:mm:ss字符串转换成date
     */
    public static Date getDateFromString(String datesStr) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT_PATTERN);
        try {
            if (StringUtils.isBlank(datesStr)) {
                throw new IllegalArgumentException("datesStr is null");
            }
            return format.parse(datesStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * yyyy/MM/dd HH:mm格式的字符串转换成yyyy-MM-dd HH:mm格式的date
     */
    public static Date getDateFromStr(String str) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_HHmm_FORMAT_PATTERN_1);
        try {
            Date d = format.parse(str);
            String ss = getDateToString(d);
            return getDateFromString(ss);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static Date addYear(Date date, int num) {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(1, num);
        return gc.getTime();
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            return diffDay == 0;
        }
        return false;
    }

    public static boolean isBeforeCurrent(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.compareTo(Calendar.getInstance());
        return i < 0;
    }


    /**
     * 操作一个给定的时间，可以在此基础上进行年，月，日，时，分，秒的任意相加减，
     */
    public static Date operateDate(Date date, int yearOffset, int monthOffset, int dayOffset, int hourOffset,
                                   int minuteOffset, int secondOffset) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        cal.set(year + yearOffset, month + monthOffset, dayOfMonth + dayOffset, hourOfDay + hourOffset,
                minute + minuteOffset, second + secondOffset);
        Date d = cal.getTime();
        return d;
    }


    /**
     * 返回一个时间处在一个时间段的第几天， 从0开始
     */
    public static int getIndexBeteenDays(Date begin, Date end, Date middle) {

        if (begin == null || end == null || middle == null) {
            throw new RuntimeException("参数为空");
        }
        if (middle.before(begin)) {
            middle = begin;
        }

        if (middle.after(end)) {
            middle = end;
        }
        return getIntervalDays(begin, middle);
    }

    public static Long getSecondsBeteenDates(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0L;
        }
        long mills = Math.abs(date1.getTime() - date2.getTime());
        return mills / 1000;
    }

    public static Date addMinute(Date date, int min) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, min);
        return cal.getTime();
    }

    public static Date addHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    /**
     * 把YYYYMMDDHHMMSS 格式转成 yyyy-MM-dd HH:mm:ss
     */
    public static Date formatYYYYMMDDHHMMSS(String yyyyMMddHHmmss) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = simpleDateFormat.parse(yyyyMMddHHmmss);
            return date;
        } catch (Exception e) {
            log.error("formatYYYYMMDDHHMMSS 发生异常 {}", e);
        }
        return null;
    }

    public static String getDate(final Date date, final String format) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    /**
     * 将LocalDate转为yyyyMM格式
     *
     * @param date 时间
     */
    public static String toMonthString(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(MONTH_FORMATTER);
    }

    public static String toMonthString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMM);
        return sdf.format(date);
    }
    /**
     * 将LocalDate转为yyyyMM格式
     *
     * @param date 时间
     */

    public static Integer toMonthInteger(Date date) {
        if (date == null) {
            return 0;
        }
        return Integer.valueOf(toMonthString(date));
    }

    /**
     * 将LocalDate转为yyyyMM格式
     *
     * @param date 时间
     */

    public static Integer toMonthInteger(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return Integer.valueOf(toMonthString(date));
    }

}
