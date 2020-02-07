package com.example.york;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration//用来声明加载的类是一个WebApplicationContext
public class YorkApplicationTests {

    //BCryptPasswordEncoder加密测试
    @Test
    public void test1() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("123456");
        System.out.println(password);

    }
    @Test
    public void test2(){
        Long time = 1580520477366L;
        Date date = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sf.format(date);
        System.out.println("哈哈"+format);

    }
    @Test
    public void test3(){
        Map map = new HashMap();
        map.put("a",1);
        map.put("a",2);
        map.put("b",1);
        map.put("3",1);
        System.out.println(map.size());

    }
}
