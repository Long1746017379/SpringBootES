package com.example.demo.test;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/02/05 17:19
 */

@Slf4j
public class TestSimpleDateFormat {

    public static void main(String[] args) {
        String s = gtDateVdvoing();
        log.info("gtDateVdvoing()方法返回值：{}", s);

        String s1 = timeFormat("3.66");
        log.info("timeFormat()方法返回值：{}", s1);

    }

    /**
     * @return  获取时间维度   if 判断当前时间是否在 零点--两点的时间段 ,给的是上一天的时间维度
     * @throws ParseException
     */
    public static String gtDateVdvoing()  {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        Date nowTime = null;
        //设定时间段
        Date amBeginTime = null;
        Date amEndTime = null;

        try {
            nowTime =df.parse(df.format(new Date()));
            amBeginTime = df.parse("00:00");
            amEndTime = df.parse("15:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置时间段
        Calendar amBegin = Calendar.getInstance();
        amBegin.setTime(amBeginTime);
        Calendar pmBegin = Calendar.getInstance();
        pmBegin.setTime(amEndTime);

        // 判断是否在时间段内
        if ((date.after(amBegin) && date.before(pmBegin)) ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -1);
            return dateFormat.format( calendar.getTime());
        } else {
            return dateFormat.format(new Date());
        }
    }

    private static String timeFormat(String time) {
        if (time == null) {
            return time;
        }
        StringBuffer stringBuffer = new StringBuffer();
        Double aDouble = Double.valueOf(time);
        double v = aDouble * 60;
        // 取小数点后一位，四色五入转换成整数
        DecimalFormat df = new DecimalFormat("#0");
        int i = Integer.parseInt(df.format(v));
        int hours = (int) Math.floor(i / 60);
        int minutes = i % 60;
        String average_install_time = stringBuffer.append("" + hours + "小时" + minutes + "分钟").toString();
        return average_install_time;
    }

}
