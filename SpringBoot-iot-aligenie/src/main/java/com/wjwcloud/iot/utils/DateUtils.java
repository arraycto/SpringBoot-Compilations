package com.wjwcloud.iot.utils;


import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtils {
    private static final String DATE = "yyyy-MM-dd";
    private static final String LOGGERINFO1 = "时间不能为空!";
    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_MINUTE = 60000L;
    public static final long MILLIS_PER_HOUR = 3600000L;
    public static final long MILLIS_PER_DAY = 86400000L;
    public static final Map<String, SimpleDateFormat> dateFormatMap = new HashMap();

    private DateUtils() {
    }

    public static Date buildDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("日期不能为空!");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("时间不能为空!");
        }
    }

    public static int getStartToEndDayInterval(Date start, Date end) {
        if (start != null && end != null) {
            if (start.after(end)) {
                throw new InvalidParameterException("结束日期早于开始日期!");
            } else {
                return (int)((end.getTime() - start.getTime()) / 86400000L);
            }
        } else {
            throw new InvalidParameterException("开始日期和结束日期不能为空!");
        }
    }

    public static String dateToString(Date date, String dateFormatStr) {
        if (date == null) {
            throw new InvalidParameterException("时间不能为空!");
        } else {
            SimpleDateFormat dateFormat = null;
            if (dateFormatStr != null && !"".equals(dateFormatStr)) {
                dateFormat = (SimpleDateFormat)dateFormatMap.get(dateFormatStr);
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(dateFormatStr);
                    dateFormatMap.put(dateFormatStr, dateFormat);
                }
            } else {
                dateFormat = (SimpleDateFormat)dateFormatMap.get("yyyy-MM-dd");
            }

            return dateFormat.format(date);
        }
    }

    public static Date stringToDate(String date, String dateFormatStr) {
        if (date != null && !"".equals(date)) {
            SimpleDateFormat dateFormat = null;
            if (dateFormatStr != null && !"".equals(dateFormatStr)) {
                dateFormat = (SimpleDateFormat)dateFormatMap.get(dateFormatStr);
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(dateFormatStr);
                    dateFormatMap.put(dateFormatStr, dateFormat);
                }
            } else {
                dateFormat = (SimpleDateFormat)dateFormatMap.get("yyyy-MM-dd");
            }

            try {
                return dateFormat.parse(date);
            } catch (ParseException var4) {
                return null;
            }
        } else {
            throw new InvalidParameterException("时间不能为空!");
        }
    }

    public static List<String> getAllDateBetween2Date(String startTime, String endTime) {
        ArrayList dateList = new ArrayList();

        try {
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = df.parse(startTime);
            startCalendar.setTime(startDate);
            Date endDate = df.parse(endTime);
            endCalendar.setTime(endDate);
            dateList.add(startTime);

            while(true) {
                startCalendar.add(5, 1);
                if (startCalendar.getTimeInMillis() >= endCalendar.getTimeInMillis()) {
                    dateList.add(endTime);
                    break;
                }

                dateList.add(df.format(startCalendar.getTime()));
            }
        } catch (ParseException var8) {
            var8.printStackTrace();
        }

        return dateList;
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Date getDate() {
        return new Date();
    }

    static {
        dateFormatMap.put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd"));
        dateFormatMap.put("yyyy-MM-dd hh:mm:ss", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        dateFormatMap.put("yyyy/MM/dd", new SimpleDateFormat("yyyy/MM/dd"));
        dateFormatMap.put("yyyy/MM/dd hh:mm:ss", new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"));
        dateFormatMap.put("yyyyMMdd", new SimpleDateFormat("yyyyMMdd"));
        dateFormatMap.put("yyyyMMddhhmmss", new SimpleDateFormat("yyyyMMddhhmmss"));
    }
}

