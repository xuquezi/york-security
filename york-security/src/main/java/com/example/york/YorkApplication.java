package com.example.york;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
//禁用登录验证,Activiti中自动集成了security的权限验证，当我们访问接口的时候会弹出登录界面，所以我们需要禁用掉登录验证
@SpringBootApplication(exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class})
@EnableScheduling
public class YorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(YorkApplication.class, args);
	}

}
