package outfox.infra.server.commons;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期时间相关处理
 * author: gongzhanjing
 * create: 2020/12/10
 */
public class DateTimeUtils {

    /**
     * 获取当前时间的开始时间
     *
     * @return 当天开始的毫秒时间戳
     */
    public static long getTodayStartTime() {
        return getDayStartTime(System.currentTimeMillis());
    }

    /**
     * 获取指定时间所在天的开始时间
     *
     * @param timestamp 毫秒时间戳
     * @return 当天开始的毫秒时间戳
     */
    public static long getDayStartTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneOffset.ofHours(8))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toInstant()
                .toEpochMilli();
    }

    /**
     * 获取当前时间的结束时间
     *
     * @return 当天开始的毫秒时间戳
     */
    public static long getTodayEndTime() {
        return getDayEndTime(System.currentTimeMillis());
    }

    /**
     * 获取指定时间所在天的结束时间
     *
     * @param timestamp 毫秒时间戳
     * @return 当天结束的毫秒时间戳
     */
    public static long getDayEndTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneOffset.ofHours(8))
                .plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toInstant()
                .toEpochMilli() - 1;
    }

    /**
     * 获取昨天开始的时间
     */
    public static long getYesterdayStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获取昨天结束的时间
     */
    public static long getYesterdayEndTime() {
        return getTodayStartTime() - 1;
    }

    /**
     * 获取明天开始的时间
     */
    public static long getTomorrowStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getTomorrowStartTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获取指定时间偏移后的时间
     *
     * @param timestamp 毫秒时间戳
     * @param days      天数
     * @param hour      小时
     * @param minute    分钟
     * @return 偏移后的毫秒时间戳
     */
    public static long getScheduleTime(long timestamp, int days, int hour, int minute) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneOffset.ofHours(8))
                .plusDays(days)
                .plusHours(hour)
                .plusMinutes(minute)
                .toInstant()
                .toEpochMilli();
    }

    /**
     * 获取指定时间戳的下一毫秒
     *
     * @param timestamp 当前毫秒时间戳
     * @return timestamp + 1ms
     */
    public static long waitUntilNextTime(final long timestamp) {
        long currentTimeMillis = System.currentTimeMillis();
        while (currentTimeMillis <= timestamp) {
            currentTimeMillis = System.currentTimeMillis();
        }
        return currentTimeMillis;
    }

    /**
     * 获取指定时间所在周的周一开始时间
     */
    public static long getWeekStartTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获取指定时间所在周的周顺序[1,7]
     */
    public static int getDayOfWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    /**
     * 毫秒时间戳转换为日期+时间
     */
    public static String convertToDateTimeStr(long timestamp) {
        return convertTimestampToStr(timestamp, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 毫秒时间戳转化为日期
     */
    public static String convertToDateStr(long timestamp) {
        return convertTimestampToStr(timestamp, "yyyy-MM-dd");
    }

    /**
     * 毫秒时间戳转化字符串
     */
    public static String convertTimestampToStr(long timestamp, String pattern) {
        LocalDateTime now = LocalDateTime.ofInstant(new Date(timestamp).toInstant(), ZoneId.systemDefault());
        return DateTimeFormatter.ofPattern(pattern).format(now);
    }

    /**
     * 日期+时间字符串转化为毫秒时间戳
     */
    public static long convertDateTimeStrToTimestamp(String dateTimeStr) {
        return convertStrToTimestamp(dateTimeStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期转化为毫秒时间戳
     */
    public static long convertDateStrToTimestamp(String dateStr) {
        return convertStrToTimestamp(dateStr, "yyyy-MM-dd");
    }

    /**
     * 字符串转化为毫秒时间戳
     */
    public static long convertStrToTimestamp(String dateTimeStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime parse = LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
        return LocalDateTime.from(parse)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    /**
     * 获取指定时间所在月的开始时间
     */
    public static long getMonthStartTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
