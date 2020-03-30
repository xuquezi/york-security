package com.example.york;

import com.example.york.utils.UUIDUtil;
import org.junit.Test;

public class UUIDTest {
    @Test
    public void test2(){
        System.out.println("Code: "+ UUIDUtil.getUUID());
    }
    @Test
    public void test(){
        String s1 = "1@";
        String s2 = "1@2";
        String[] split1 = s1.split("@");
        String[] split2 = s2.split("@");
        System.out.println(split1.length);
        System.out.println(split2.length);
    }
    @Test
    public void test1(){
        for (int i = 0;i<5;i++){
            System.out.println("UR"+UUIDUtil.getUUID());
        }


    }
}
