package com.example.york;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeTest extends YorkApplicationTests {
    @Test
    public void test1(){
        Calendar calendar = Calendar.getInstance();
//得到昨天的当前时间
        calendar.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String startTime = yesterday + " 00:00:00";
        String endTime = yesterday + " 23:59:59";
        System.out.println(startTime);
        System.out.println(endTime);

        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH));
    }
}
