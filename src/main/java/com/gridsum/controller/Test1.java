package com.gridsum.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author xulei
 * @date 2022/10/31
 * @description: 测试
 */
public class Test1 {
    public static void main(String[] args) throws IOException, ParseException {
        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2022-11-11");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        calendar.add(Calendar.HOUR_OF_DAY,+16);
        Date startTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,+1);
        Date endTime = calendar.getTime();
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(date);*/

        String regex = "^[a-z0-9A-Z-]+$";

        System.out.println("-".matches(regex));

    }
}
