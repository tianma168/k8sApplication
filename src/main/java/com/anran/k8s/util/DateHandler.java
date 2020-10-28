package com.anran.k8s.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;


public class DateHandler {
    public static final String MASK = "yyyy-MM-dd HH:mm:ss";
    public static final String OnlyDateMASK = "yyyy-MM-dd";
    public static final String OnlyDateMASK_FOR_PARTITION = "yyyyMMdd";
    public final static String[] REPLACE_STRING = new String[]{"GMT+0800", "GMT+08:00"};
    public final static String SPLIT_STRING = "(中国标准时间)";

    public static Date dateFormat(String dateString) {
        try {
            dateString = dateString.split(Pattern.quote(SPLIT_STRING))[0].replace(REPLACE_STRING[0], REPLACE_STRING[1]);
            SimpleDateFormat sf1 = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z", Locale.US);
            Date date = sf1.parse(dateString);
            String de = new SimpleDateFormat(MASK).format(date);
            return parseDate(de);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Date localToUTC(String localTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate = null;
        try {
            localDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long localTimeInMillis = localDate.getTime();
        /** long时间转换成Calendar */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        Date utcDate = new Date(calendar.getTimeInMillis());
        return utcDate;
    }

    public static String getTomorrow() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(MASK);
        return df.parse(dateStr);
    }

    public static String parseToDateStr(String modifyTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            Date date = (Date) sdf.parse(modifyTime);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String res = simpleDateFormat.format(date);
            return res;
        } catch (Exception ex) {
            return null;
        }
    }


    public static String formatOnlyDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(OnlyDateMASK);
        return df.format(date);
    }

    public static String formatDate(Date date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(MASK);
        return df.format(date);
    }

    public static Date getPushIntervalTime(Long intervalTime) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            date.setTime(date.getTime() + intervalTime);
            String temp = df.format(date);
            return date;
        } catch (Exception ex) {
            return new Date();
        }
    }

    public static String getYestoday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date d = cal.getTime();

        SimpleDateFormat sp = new SimpleDateFormat("yyyyMMdd");
        //获取昨天日期
        String yestoday = sp.format(d);
        return yestoday;
    }

    public static String formatDateForPartition(Date date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(OnlyDateMASK_FOR_PARTITION);
        return df.format(date);
    }

    public static String getCurDateTime() {
        return getDateTime(MASK, new Date());
    }

    public static String getCurDateTime(String aMask) {
        return getDateTime(aMask, new Date());
    }

    public static String getDateTime(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(MASK);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate != null) {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static long gap(Date start, Date end, int type) {
        switch (type) {
            case Calendar.SECOND:
                return toSeconds(end.getTime()) - toSeconds(start.getTime());
            case Calendar.MINUTE:
                return toMinutes(end.getTime()) - toMinutes(start.getTime());
            case Calendar.HOUR:
                return toHours(end.getTime()) - toHours(start.getTime());
            default:
                return end.getTime() - start.getTime();
        }
    }

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static int dateCompare(Date date1, Date data2) {
        return date1.compareTo(data2);
    }

    public static String relativeDate(String aMask, Date date) {
        long delta = System.currentTimeMillis() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        return getDateTime(aMask, date);
    }
}
