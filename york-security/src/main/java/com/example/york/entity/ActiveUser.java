package com.example.york.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ActiveUser {
    private String username;
    private String avatar;
    private String[] roles;
}
