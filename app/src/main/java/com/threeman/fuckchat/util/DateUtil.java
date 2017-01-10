package com.threeman.fuckchat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CGT on 2016/11/22.
 * <p>
 * 日期工具类
 */
public class DateUtil {

    /**
     * 获取月日
     *
     * @return 月日
     */
    public static String getMonthAndDay() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(new Date());
    }


    /**
     * 获取当前日期
     *
     * @param dateformat 日期格式
     * @return 当前日期 String
     */
    public static String getNowDate(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        String date = dateFormat.format(now);
        return date;
    }

    /**
     * 计算相差日期
     *
     * @param date 传入年月日
     * @return 天数 int
     */
    public static int compareToday(String date) {
        long time = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(new Date());  //今天
        try {
            time = df.parse(today).getTime() - df.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int days = (int) (time / (1000 * 60 * 60 * 24));  //计算相差天数
        return days;
    }

    //获取年月日
    public static String getYMD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());

    }
    /**
     * 获取星期
     *
     * @return 星期英文缩写
     */
    public static String getWeeks() {
        Calendar calendar = Calendar.getInstance();
        String wDay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(wDay)) {
            wDay = "星期日";
        } else if ("2".equals(wDay)) {
            wDay = "星期一";
        } else if ("3".equals(wDay)) {
            wDay = "星期二";
        } else if ("4".equals(wDay)) {
            wDay = "星期三";
        } else if ("5".equals(wDay)) {
            wDay = "星期四";
        } else if ("6".equals(wDay)) {
            wDay = "星期五";
        } else if ("7".equals(wDay)) {
            wDay = "星期六";
        }
        return wDay;
    }


    /**
     * 获取手机系统时间
     * */
    public static String getTime(){
        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if (calendar.get(Calendar.MINUTE)<=9){
            minute = "0"+String.valueOf(calendar.get(Calendar.MINUTE));
        }
        return hour+":"+minute;
    }

}
