package com.example.york;

import com.example.york.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceExtendsTest extends YorkApplicationTests {
    @Autowired
    private UserService userService;
    @Test
    public void test1(){
        userService.deleteNotActiveUser();
    }
   
}
