package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LogoutLog {
    private String logoutId;

    private String logoutUsername;

    private String logoutIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date logoutTime;
}
