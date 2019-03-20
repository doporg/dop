package com.clsaa.dop.server.code.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author wsy
 */
public class TimeUtil {

    private final static long YEAR = 1000 * 60 * 60 * 24 * 365L;
    private final static long MONTH = 1000 * 60 * 60 * 24 * 30L;
    private final static long DAY = 1000 * 60 * 60 * 24L;
    private final static long HOUR = 1000 * 60 * 60L;
    private final static long MINUTE = 1000 * 60L;

    /**
     * 2019-02-26T07:57:50.000Z
     *
     * @param lastTime
     * @return
     */
    public static List<String> natureTime(String lastTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = null;
        try {
            date = sdf.parse(lastTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        System.out.println(date);

        //加上8小时的时区差距
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, 8);
        date = c.getTime();

//        System.out.println(date);

        String commit_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        //计算距现在的时长
        Date now = new Date();
        long between = now.getTime() - date.getTime();
        String commit_time;
        if (between > YEAR) {
            commit_time = ((between - YEAR) / YEAR + 1) + "年前";
        }else if (between > MONTH) {
            commit_time = ((between - MONTH) / MONTH + 1) + "月前";
        }else if (between > DAY) {
            commit_time = ((between - DAY) / DAY + 1) + "天前";
        }else if (between > HOUR) {
            commit_time = ((between - HOUR) / HOUR + 1) + "小时前";
        }else if (between > MINUTE) {
            commit_time = ((between - MINUTE) / MINUTE + 1) + "分钟前";
        }else {
            commit_time = "刚刚";
        }

        List<String> res = new ArrayList<>();
        res.add(commit_date);
        res.add(commit_time);

        return res;
    }

    public static void main(String[] args) {
        List<String> res= TimeUtil.natureTime("2019-03-20T13:03:46.000Z");
        System.out.println(res.get(0));
        System.out.println(res.get(1));

    }
}
