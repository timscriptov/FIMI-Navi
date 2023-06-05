package com.fimi.kernel.utils;

import com.fimi.android.app.R;
import com.fimi.kernel.base.BaseApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtil {
    public static final String AM = "AM";
    public static final String PM = "PM";
    public static final String dateFormatHM = "HH:mm";
    public static final String dateFormatHMS = "HH:mm:ss";
    public static final String dateFormatMD = "MM/dd";
    public static final String dateFormatYM = "yyyy-MM";
    public static final String dateFormatYMD = "yyyy-MM-dd";
    public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String dateFormatYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        try {
            Date date = mSimpleDateFormat.parse(strDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringByOffset(String strDate, String format, int calendarField, int offset) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            String mDateTime = mSimpleDateFormat.format(c.getTime());
            return mDateTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringByOffset(Date date, String format, int calendarField, int offset) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            String strDate = mSimpleDateFormat.format(c.getTime());
            return strDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
            if (mSimpleDateFormat.format(date).equals(mSimpleDateFormat.format(new Date()))) {
                return BaseApplication.getContext().getString(R.string.date_today);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static String getStringByFormat(String strDate, String format) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            String mDateTime = mSimpleDateFormat2.format(c.getTime());
            return mDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringByFormat(long milliseconds, String format) {
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            String thisDateTime = mSimpleDateFormat.format(Long.valueOf(milliseconds));
            return thisDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDate(String format) {
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            String curDateTime = mSimpleDateFormat.format(c.getTime());
            return curDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDateByOffset(String format, int calendarField, int offset) {
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            String mDateTime = mSimpleDateFormat.format(c.getTime());
            return mDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getOffectDay(long milliseconds1, long milliseconds2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        int y1 = calendar1.get(1);
        int y2 = calendar2.get(1);
        int d1 = calendar1.get(6);
        int d2 = calendar2.get(6);
        if (y1 - y2 > 0) {
            int maxDays = calendar2.getActualMaximum(6);
            int day = (d1 - d2) + maxDays;
            return day;
        } else if (y1 - y2 < 0) {
            int maxDays2 = calendar1.getActualMaximum(6);
            int day2 = (d1 - d2) - maxDays2;
            return day2;
        } else {
            int day3 = d1 - d2;
            return day3;
        }
    }

    public static int getOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(11);
        int h2 = calendar2.get(11);
        int day = getOffectDay(date1, date2);
        int h = (h1 - h2) + (day * 24);
        return h;
    }

    public static int getOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(12);
        int m2 = calendar2.get(12);
        int h = getOffectHour(date1, date2);
        int m = (m1 - m2) + (h * 60);
        return m;
    }

    public static String getFirstDayOfWeek(String format) {
        return getDayOfWeek(format, 2);
    }

    public static String getLastDayOfWeek(String format) {
        return getDayOfWeek(format, 1);
    }

    private static String getDayOfWeek(String format, int calendarField) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(7);
            if (week == calendarField) {
                String strDate = mSimpleDateFormat.format(c.getTime());
                return strDate;
            }
            int offectDay = calendarField - week;
            if (calendarField == 1) {
                offectDay = 7 - Math.abs(offectDay);
            }
            c.add(5, offectDay);
            String strDate2 = mSimpleDateFormat.format(c.getTime());
            return strDate2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFirstDayOfMonth(String format) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.set(5, 1);
            String strDate = mSimpleDateFormat.format(c.getTime());
            return strDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getLastDayOfMonth(String format) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.set(5, 1);
            c.roll(5, -1);
            String strDate = mSimpleDateFormat.format(c.getTime());
            return strDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getFirstTimeOfDay() {
        try {
            String currentDate = getCurrentDate("yyyy-MM-dd");
            Date date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
            return -1L;
        }
    }

    public static long getLastTimeOfDay() {
        try {
            String currentDate = getCurrentDate("yyyy-MM-dd");
            Date date = getDateByFormat(currentDate + " 24:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
            return -1L;
        }
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 || year % 400 == 0;
    }


    public static String formatDateStr2Desc(String strDate, String outFormat) {
        DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if (d == 0) {
                int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
                if (h > 0) {
                    return "今天" + getStringByFormat(strDate, dateFormatHM);
                } else if (h >= 0 && h == 0) {
                    int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
                    if (m > 0) {
                        return m + "分钟前";
                    }
                    if (m >= 0) {
                        return "刚刚";
                    }
                }
            } else if (d <= 0 ? d >= 0 || d == -1 || d == -2 : d == 1 || d == 2) {
            }
            String out = getStringByFormat(strDate, outFormat);
            if (out.isEmpty()) {
                return out;
            }
        } catch (Exception e) {
        }
        return strDate;
    }

    public static String getWeekNumber(String strDate, String inFormat) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
            int intTemp = calendar.get(7) - 1;
            switch (intTemp) {
                case 0:
                    week = "星期日";
                    break;
                case 1:
                    week = "星期一";
                    break;
                case 2:
                    week = "星期二";
                    break;
                case 3:
                    week = "星期三";
                    break;
                case 4:
                    week = "星期四";
                    break;
                case 5:
                    week = "星期五";
                    break;
                case 6:
                    week = "星期六";
                    break;
            }
            return week;
        } catch (Exception e) {
            return "错误";
        }
    }

    public static String getTimeQuantum(String strDate, String format) {
        Date mDate = getDateByFormat(strDate, format);
        int hour = mDate.getHours();
        return hour >= 12 ? PM : AM;
    }

    public static String getTimeDescription(long milliseconds) {
        if (milliseconds > 1000) {
            if ((milliseconds / 1000) / 60 > 1) {
                long minute = (milliseconds / 1000) / 60;
                long second = (milliseconds / 1000) % 60;
                return minute + "分" + second + "秒";
            }
            return (milliseconds / 1000) + "秒";
        }
        return milliseconds + "毫秒";
    }

    public static boolean isToday(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date).equals(fmt.format(new Date()));
    }

    public static void main(String[] args) {
        new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date(1562680885L);
        System.out.println(date);
    }

    public static String[] getStringByFormat2(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(Long.valueOf(milliseconds));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] ret = thisDateTime.split(" ");
        ret[1] = ret[1].replace("-", " ");
        return ret;
    }

    public static String getStringByFormat3(long milliseconds, String format) {
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            String thisDateTime = mSimpleDateFormat.format(Long.valueOf(milliseconds));
            return thisDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getDateByOffset(Date date, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    public void getUTC(byte[] bytes) {
        Calendar cal = Calendar.getInstance();
        setTimeBytes(bytes, 0, cal.get(13));
        int pos = 4;
        setTimeBytes(bytes, pos, cal.get(12));
        int pos2 = pos + 4;
        setTimeBytes(bytes, pos2, cal.get(11));
        int pos3 = pos2 + 4;
        setTimeBytes(bytes, pos3, cal.get(5));
        int pos4 = pos3 + 4;
        setTimeBytes(bytes, pos4, cal.get(2));
        int pos5 = pos4 + 4;
        setTimeBytes(bytes, pos5, cal.get(1) - 1900);
        int pos6 = pos5 + 4;
        setTimeBytes(bytes, pos6, cal.get(7));
        int pos7 = pos6 + 4;
        setTimeBytes(bytes, pos7, cal.get(6));
        int pos8 = pos7 + 4;
        int zoneOffset = cal.get(15);
        int dstOffset = cal.get(16);
        setTimeBytes(bytes, pos8, dstOffset);
        cal.add(14, -(zoneOffset + dstOffset));
        long utc = cal.getTimeInMillis();
        setUtc(bytes, pos8 + 4, utc);
    }

    private void setTimeBytes(byte[] bytes, int pos, int value) {
        for (int i = 0; i < 4; i++) {
            int v = value >> (i * 8);
            bytes[pos + i] = (byte) (v & 255);
        }
    }

    private void setUtc(byte[] bytes, int pos, long value) {
        for (int i = 0; i < 8; i++) {
            long v = value >> (i * 8);
            bytes[pos + i] = (byte) (255 & v);
        }
    }
}
